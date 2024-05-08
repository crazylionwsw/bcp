package com.fuze.bcp.bd.business;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysRoleBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.bean.EmployeeUserBean;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.bd.domain.Employee;
import com.fuze.bcp.bd.domain.Orginfo;
import com.fuze.bcp.bd.service.IEmployeeService;
import com.fuze.bcp.bd.service.IOrgInfoService;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class BizOrgService implements IOrgBizService {

    @Autowired
    IOrgInfoService iOrgInfoService;

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResultBean<List<EmployeeBean>> actGetEmployees() {
        List<Employee> employees = iEmployeeService.getAllByDataStatus(DataStatus.SAVE);
        return ResultBean.getSucceed().setD(mappingService.map(employees, EmployeeBean.class));
    }

    @Override
    public ResultBean<EmployeeUserBean> actSaveEmployee(EmployeeUserBean employeeBean, Boolean isLeader) {
        Employee employee = mappingService.map(employeeBean, Employee.class);
        if (employee.getDataStatus() == DataStatus.SAVE) {
            //判断是否需要添加用户,参考1.0代码
            LoginUserBean loginUser = employeeBean.getLoginUser();
            if (loginUser == null) { //不需要创建用户
                String oldUserId = employeeBean.getLoginUserId();
                if (oldUserId != null) { //之前有用户
                    LoginUserBean oldUser = iAuthenticationBizService.actGetLoginUser(oldUserId).getD();
                    if (oldUser != null) { //如果该用户存在
                        loginUser = oldUser;
                        loginUser.setPassword(null);
                        //判断手机号是否改变
                        if (!oldUser.getUsername().equals(employeeBean.getCell())) { //如果手机号改变，修改用户名
                            loginUser.setUsername(employeeBean.getCell());

                            loginUser = iAuthenticationBizService.actSaveLoginUser(loginUser).getD();
                        }
                    } else { //如果该用户不存在
                        employee.setLoginUserId(null);
                    }
                }
            } else { //需要创建用户
                String oldUserId = employeeBean.getLoginUserId();
                if (oldUserId != null) { //之前有用户
                    LoginUserBean oldUser = iAuthenticationBizService.actGetLoginUser(oldUserId).getD();
                    if (oldUser != null) { //如果该用户存在
                        loginUser = oldUser;
                        loginUser.setPassword(null);
                        //判断手机号是否改变
                        if (!oldUser.getUsername().equals(employeeBean.getCell())) { //如果手机号改变，修改用户名
                            loginUser.setUsername(employeeBean.getCell());

                            loginUser = iAuthenticationBizService.actSaveLoginUser(loginUser).getD();
                        }
                    } else { //如果该用户不存在
                        employee.setLoginUserId(null);
                    }
                }

                //通过手机号查找用户
                LoginUserBean oldLoginUser = iAuthenticationBizService.actFindByUsername(employeeBean.getCell()).getD();
                if (oldLoginUser != null) {
                    loginUser.setId(oldLoginUser.getId());
                }

                if (loginUser.getId() == null) {
                    //生成六位密码
                    String password = FormatUtils.getStringRandom(6);
                    loginUser.setPassword(password);

                    loginUser = iAuthenticationBizService.actSaveLoginUser(loginUser).getD();

                    /**
                     * TODO：发送MQ消息
                     */
                }

                employee.setLoginUserId(loginUser.getId());
            }
        }
        employee = iEmployeeService.save(employee);

        employee = iEmployeeService.getOne(employee.getId());
        if (employee != null) {
            String orgId = employee.getOrgInfoId();
            Orginfo orginfo = iOrgInfoService.getOne(orgId);
            if (orginfo != null) {
                if (isLeader) {
                    orginfo.setLeaderId(employee.getId());
                } else {
                    if (orginfo.getLeaderId() != null) {                        //判断员工列表里是否已经存在部门领导
                        if (orginfo.getLeaderId().equals(employee.getId())) {    //判断当前这条是否为部门领导
                            orginfo.setLeaderId(null);
                        }
                    } else {
                        orginfo.setLeaderId(null);
                    }
                }

                orginfo = iOrgInfoService.save(orginfo);
            }
        }

        if (employee.getDataStatus() == DataStatus.DISCARD) {
            String loginUserId = employee.getLoginUserId();
            if (loginUserId != null) {
                iAuthenticationBizService.actDiscardLoginUser(loginUserId);
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeUserBean.class));
    }

    public ResultBean<EmployeeBean> actSaveEmployee(EmployeeBean employeeBean) {
        Employee employee = iEmployeeService.save(mappingService.map(employeeBean,Employee.class));
        if(employee==null){
            return ResultBean.getFailed().setM("保存失败");
        }else{
            return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeUserBean.class)).setM("保存成功！");
        }
    }

    @Override
    public ResultBean<EmployeeBean> actDeleteEmployee(String id) {
        Employee employee = iEmployeeService.getOne(id);
        if (employee != null) {
            Orginfo orginfo = iOrgInfoService.getOne(employee.getOrgInfoId());
            if (orginfo.getLeaderId() != null) {
                if (employee.getDataStatus() == DataStatus.DISCARD) {
                    if (orginfo.getLeaderId().equals(employee.getId())) {
                        orginfo.setLeaderId(null);
                        iOrgInfoService.save(orginfo);
                    }
                }
            }
            employee = iEmployeeService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<EmployeeBean> actGetEmployee(String id) {
        Employee employee = iEmployeeService.getOne(id);
        if (employee == null) {
            return ResultBean.getSucceed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeBean.class));
    }

    @Override
    public ResultBean<EmployeeBean> actGetEmployeeByWcUserId(String wcUserId) {
        Employee employee = iEmployeeService.getOneByWxUserId(wcUserId);
        if (employee != null) {
            return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeBean.class));
        } else {
            return ResultBean.getFailed();
        }
    }

    @Override
    public ResultBean<List<EmployeeBean>> actGetEmployeeManagersByOrginfoId(String id) {
        List<Employee> employeeManagers = new ArrayList<Employee>();
        List<Employee> employees = iEmployeeService.getEmployeeManagersByOrginfoId(id);
        for (Employee em : employees) {
            if (em.getLoginUserId() != null) {
                ResultBean<LoginUserBean> loginUserBeanResultBean = iAuthenticationBizService.actGetLoginUser(em.getLoginUserId());
                if (loginUserBeanResultBean.getD() != null) {
                    for (int i = 0; i < loginUserBeanResultBean.getD().getUserRoleIds().size(); i++) {
                        String roleId = loginUserBeanResultBean.getD().getUserRoleIds().get(i);
                        if (roleId != null) {
                            ResultBean<SysRoleBean> sysRoleBeanResultBean = iAuthenticationBizService.actGetSysRole(roleId);
                            if (sysRoleBeanResultBean.getD().getName().equals("渠道经理")) {
                                employeeManagers.add(em);
                            }
                        }
                    }
                }
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(employeeManagers, EmployeeBean.class));
    }

    @Override
    public ResultBean<List<EmployeeLookupBean>> actLookupEmployees() {
        List<Employee> employees = iEmployeeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(employees, EmployeeLookupBean.class));
    }

    @Override
    public ResultBean<DataPageBean<EmployeeBean>> searchEmployee(Integer currentPage, EmployeeBean employeeBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").gt(DataStatus.TEMPSAVE));
        if (!StringUtils.isEmpty(employeeBean.getOrgInfoId()))
            query.addCriteria(Criteria.where("orgInfoId").is(employeeBean.getOrgInfoId()));
        if (!StringUtils.isEmpty(employeeBean.getUsername()))
            query.addCriteria(Criteria.where("username").regex(Pattern.compile("^.*"+ employeeBean.getUsername() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(employeeBean.getIdentifyNo()))
            query.addCriteria(Criteria.where("identifyNo").regex(Pattern.compile("^.*"+ employeeBean.getIdentifyNo() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(employeeBean.getCell()))
            query.addCriteria(Criteria.where("cell").is(employeeBean.getCell()));

        Pageable pageable = new PageRequest(currentPage,20);
        query.with(pageable);
        List list = mongoTemplate.find(query,Employee.class);
        Page<Employee> page  = new PageImpl(list,pageable, mongoTemplate.count(query,Employee.class));
        return ResultBean.getSucceed().setD(mappingService.map(page, EmployeeBean.class));
    }

    @Override
    public ResultBean<DataPageBean<EmployeeBean>> actGetEmployees(Integer currentPage) {
        Page<Employee> employees = iEmployeeService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(employees, EmployeeBean.class));
    }

    /**
     * 根据员工ID获取所有的员工，包含自己以及所有下级的员工
     *
     * @param employeeId
     * @return
     */
    public ResultBean<List<EmployeeBean>> actGetChildEmployees(String employeeId) {

        List<Orginfo> orginfos = iOrgInfoService.getOrgByLeaderId(employeeId);
        List<Employee> result = new ArrayList<Employee>();
        if (orginfos != null && orginfos.size() > 0) {
            for (Orginfo orginfo : orginfos) {
                result.addAll(this.getEmployeesByOrgId(orginfo.getId()));
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(result, EmployeeBean.class));
    }

    public ResultBean<List<EmployeeBean>> actGetOrgEmployees(String orgId) {
        List<Employee> employees = this.getEmployeesByOrgId(orgId);
        return ResultBean.getSucceed().setD(mappingService.map(employees, EmployeeBean.class));
    }

    /**
     * 递归获取该部门及子部门下所有员工
     *
     * @param orgId
     * @return
     */
    private List<Employee> getEmployeesByOrgId(String orgId) {
        List<Employee> employees = iEmployeeService.lookupByOrgInfoId(orgId);
        List<Orginfo> childOrgs = iOrgInfoService.getAvailableChildren(orgId);
        if (childOrgs != null && childOrgs.size() > 0) {
            for (Orginfo childOrg : childOrgs) {
                employees.addAll(this.getEmployeesByOrgId(childOrg.getId()));
            }
        }

        return employees;
    }
    public ResultBean<List<String>> actGetEmployees(String orginfoId){
        List<Employee> employeesByOrgId = getEmployeesByOrgId(orginfoId);
        ArrayList<String> ids = new ArrayList<>();
        for (Employee emp:employeesByOrgId) {
            ids.add(emp.getId());
        }
        return ResultBean.getSucceed().setD(ids);
    }

    public ResultBean<List<Employee>> actGetEmployeesByOrgId(String orgId){
        List<Employee> employeesByOrgId = getEmployeesByOrgId(orgId);
        return ResultBean.getSucceed().setD(employeesByOrgId);
    }


    @Override
    public ResultBean<DataPageBean<EmployeeBean>> actGetEmployees(Integer currentPage, String orgId) {
        Page<Employee> employees = null;
        orgId.toString();
        if (orgId == "0") {
            employees = iEmployeeService.getAll(currentPage);
        } else {
            employees = iEmployeeService.findByOrgInfoId(currentPage, orgId);
        }

//        //根据orgId取出该条组织结构信息为获取子级节点用
//        Orginfo orginfo = iOrgInfoService.getOne(orgId);
//        //取出所有组织结构信息
//        List<Orginfo> orginfos =  iOrgInfoService.getAll();
//        //存放子级部门信息
//        List<Orginfo> childs = new ArrayList<Orginfo>();
//        //递归逐条获取
//        childs(orginfo,orginfos,childs);
//
//        List<Employee> employees = new ArrayList<Employee>();
//
//        childs.add(orginfo);
//
//        for(int i = 0;i<childs.size();i++){
//            String orgEid = childs.get(i).getId();
//            if(orgEid != null){
//                List<Employee> employee = iEmployeeService.findByOrgInfoId(orgEid);
//                for(int j = 0;j<employee.size();j++){
//                    employees.add(employee.get(j));
//                }
//            }
//        }

        return ResultBean.getSucceed().setD(mappingService.map(employees, EmployeeBean.class));
    }

//    查询出所有的员工,包括子部门
    @Override
    public ResultBean<DataPageBean<EmployeeBean>> actGetAllEmployees(Integer currentPage , String orgId){
        Page<Employee> employees = null;
        if(orgId != null){
            List<String> orgs = new ArrayList<String>();
            if(orgId.equals("0")){
                List<Orginfo> all = iOrgInfoService.getAll();
                for (Orginfo oId:all) {
                    orgs.add(oId.getId());
                }
            }
            orgs.add(orgId);
            List<Orginfo> children = iOrgInfoService.getAvailableChildren(orgId);
            if(children != null && children.size()>0){
                for (Orginfo org : children){
                    orgs.add(org.getId());
                }
            }
            employees = iEmployeeService.findByOrgInfoIds(currentPage,orgs);
        }
        return ResultBean.getSucceed().setD(mappingService.map(employees,EmployeeBean.class));
    }

    @Override
    public ResultBean<List<EmployeeLookupBean>> actGetEmployeesList(String userId) {
        Employee bean = iEmployeeService.getByLoginUserId(userId);
        if (bean == null) {
            return ResultBean.getFailed().setM("该用户不是员工，请重试！");
        }
        Orginfo orginfo = null;
        Employee employee = null;
        List<Employee> employeesList = new ArrayList<>();
        HashMap<String, Employee> employeeHashMap = new HashMap<>();
        //把自己加进去
        employeeHashMap.put(bean.getId(), bean);
        List<Orginfo> orginfoList = iOrgInfoService.getOrgByLeaderId(bean.getId());
        Iterator<Orginfo> itr = orginfoList.iterator();
        while (itr.hasNext()) {
            orginfo = itr.next();
            if (orginfo != null) {
                List<Employee> tmpList = iEmployeeService.lookupByOrgInfoId(orginfo.getId());
                Iterator<Employee> empItr = tmpList.iterator();
                while (empItr.hasNext()) {
                    employee = empItr.next();
                    if (employee != null) {
                        employeeHashMap.put(employee.getId(), employee);
                    }
                }
            }
        }
        Iterator mppItr = employeeHashMap.entrySet().iterator();
        while (mppItr.hasNext()) {
            HashMap.Entry<String, Employee> entry = (HashMap.Entry<String, Employee>) mppItr.next();
            if (entry != null) {
                employeesList.add(entry.getValue());
            }
        }

        List<String> role = this.getRoleByLoginUserId(userId);
        List<Employee> manList = new ArrayList<>();
        if(role.contains("CHANNEL_MANAGER")){
            manList.addAll(employeesList);
        }else if(role.contains("SALES_MANAGER")&&!role.contains("CHANNEL_MANAGER")){
            for (Employee emm:employeesList) {
                if(emm.getLoginUserId().equals(userId)){
                    manList.add(emm);
                }
            }
        }
        if (manList == null && manList.size() == 0) {
            return ResultBean.getFailed().setM("没有下属员工");
        } else {
            return ResultBean.getSucceed().setD(mappingService.map(manList, EmployeeLookupBean.class));
        }
    }

    private List<String> getRoleByLoginUserId(String loginUserId){
        List<String> rCode = new ArrayList<String>();
        ResultBean<LoginUserBean> loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId);
        String role = null;
        if(loginUserBean.getD() != null){
            for (int i=0;i<loginUserBean.getD().getUserRoleIds().size();i++){
                String roleId = loginUserBean.getD().getUserRoleIds().get(i);
                ResultBean<SysRoleBean> sysRoleBean = iAuthenticationBizService.actGetAvailableSysRole(roleId);
                if(sysRoleBean.getD() != null){
                    role = sysRoleBean.getD().getCode();
                    rCode.add(role);
                }
            }
        }
        return rCode;
    }

    @Override
    public ResultBean<EmployeeLookupBean> actGetEmployeeByLogin(String userId) {
        Employee employee = iEmployeeService.getByLoginUserId(userId);
        if (employee == null)
            return ResultBean.getSucceed();
        else
            return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeLookupBean.class));
    }

    @Override
    public ResultBean<List<EmployeeBean>> actGetEmployeeByIds(List<String> ids) {
        List employees = iEmployeeService.getAvaliableList(ids);
        return ResultBean.getSucceed().setD(mappingService.map(employees, EmployeeBean.class));
    }


    @Override
    public ResultBean<OrgBean> actSaveOrg(OrgBean orgBean) {
        Orginfo orginfo = mappingService.map(orgBean, Orginfo.class);
        orginfo = iOrgInfoService.save(orginfo);
        return ResultBean.getSucceed().setD(mappingService.map(orginfo, OrgBean.class));
    }

    @Override
    public ResultBean<OrgBean> actDeleteOrg(String id) {
        Orginfo orginfo = iOrgInfoService.getOne(id);
        if (orginfo != null) {
            orginfo = iOrgInfoService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(orginfo, OrgBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<OrgBean> actGetOrg(String id) {
        Orginfo orginfo = iOrgInfoService.getOne(id);
        if (orginfo == null){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORG_NULL_ID"),id));
        }
        return ResultBean.getSucceed().setD(mappingService.map(orginfo, OrgBean.class));
    }

    @Override
    public ResultBean<List<APITreeLookupBean>> actLookupOrgs() {
        List<Orginfo> orginfos = iOrgInfoService.getLookups(null, 0);
        return ResultBean.getSucceed().setD(mappingService.map(orginfos, APITreeLookupBean.class));
    }

    @Override
    public ResultBean<DataPageBean<OrgBean>> actGetOrgs(Integer currentPage) {
        Page<Orginfo> orginfos = iOrgInfoService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(orginfos, OrgBean.class));
    }

    @Override
    public ResultBean<List<OrgBean>> actGetAllOrgs() {
        List<Orginfo> orginfos = iOrgInfoService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(orginfos, OrgBean.class));
    }

    @Override
    public ResultBean<List<OrgBean>> actGetOrgs(String parentId) {

        List<Orginfo> orginfos = iOrgInfoService.getChildren(parentId);

        return ResultBean.getSucceed().setD(mappingService.map(orginfos, OrgBean.class));
    }

    @Override
    public ResultBean<EmployeeBean> actFindEmployeeByLoginUserId(String loginUerId) {
        Employee employee = iEmployeeService.getByLoginUserId(loginUerId);
        if (employee != null) {
            return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<String> actGetNameByLoginUserId(String loginUerId) {
        Employee employee = iEmployeeService.getByLoginUserId(loginUerId);
        if (employee != null) {
            return ResultBean.getSucceed().setD(employee.getUsername());
        } else {
            LoginUserBean user = iAuthenticationBizService.actGetLoginUser(loginUerId).getD();
            if (user != null)
                return ResultBean.getSucceed().setD(user.getUsername());
        }

        return ResultBean.getFailed();
    }

    /**
     * 通过登录用户ID查询该用户最上级的部门信息
     *
     * @param loginUerId
     * @return
     */
    @Override
    public ResultBean<OrgBean> actFindSuperOrgByLoginUserId(String loginUerId) {
        Employee employee = iEmployeeService.getByLoginUserId(loginUerId);
        if (employee != null) {
            return actFindSuperOrgByEmployee(employee);
        }
        return ResultBean.getFailed().setM("对不起，该员工不存在！");
    }

    /**
     * 通过员工ID查询该用户最上级的部门信息
     *
     * @param employeeId
     * @return
     */
    @Override
    public ResultBean<OrgBean> actFindSuperOrgByEmployeeId(String employeeId) {
        Employee employee = iEmployeeService.getOne(employeeId);
        if (employee != null) {
            return actFindSuperOrgByEmployee(employee);
        }
        return ResultBean.getFailed().setM("对不起，该员工不存在！");
    }

    /*获取渠道经理*/
    @Override
    public ResultBean<List<EmployeeBean>> actGetEmployeeManagers() {
        List<Employee> employeeManager = new ArrayList<Employee>();
        //取出所有可用员工
        List<Employee> employee = iEmployeeService.getAllByDataStatus(DataStatus.SAVE);
        for (Employee em : employee) {
            if(em.getOrgInfoId() != null){
                //取出员工的部门
               Orginfo orginfo =  iOrgInfoService.getOne(em.getOrgInfoId());
                if(orginfo != null){
                    //判断该员工是否是该部门的领导
                    if(orginfo.getLeaderId() != null){
                        if(orginfo.getLeaderId().equals(em.getId())){
                            if(em.getLoginUserId() != null){
                                ResultBean<LoginUserBean> loginUserBean = iAuthenticationBizService.actGetLoginUser(em.getLoginUserId());
                                if (loginUserBean.getD() != null) {
                                    for (int i = 0; i < loginUserBean.getD().getUserRoleIds().size(); i++) {
                                        String roleId = loginUserBean.getD().getUserRoleIds().get(i);
                                        if (roleId != null) {
                                            ResultBean<SysRoleBean> sysRoleBeanResultBean = iAuthenticationBizService.actGetAvailableSysRole(roleId);
                                            if (sysRoleBeanResultBean.getD() != null && sysRoleBeanResultBean.getD().getName().equals("渠道经理")) {
                                                employeeManager.add(em);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(employeeManager, EmployeeBean.class));
    }

    /**
     * 获取分期经理
     * 要求:首先必须是员工,并且有登录账号,在登录用户的角色为分期经理
     *
     */
    @Override
    public ResultBean<List<EmployeeBean>> actGetBusinessManagers(){
        List<Employee> businessManager = new ArrayList<Employee>();
        List<Employee> employees = iEmployeeService.getAllByDataStatus(DataStatus.SAVE);
        for (Employee em:employees) {
            if(em.getLoginUserId() != null){
                ResultBean<LoginUserBean> loginUser = iAuthenticationBizService.actGetLoginUser(em.getLoginUserId());
                if(loginUser.getD() != null){
                    for (int i = 0;i<loginUser.getD().getUserRoleIds().size();i++) {
                        String roleId = loginUser.getD().getUserRoleIds().get(i);
                        if(roleId != null){
                            ResultBean<SysRoleBean> sysRole = iAuthenticationBizService.actGetAvailableSysRole(roleId);
                            if(sysRole.getD() != null && sysRole.getD().getName().equals("分期经理")){
                                businessManager.add(em);
                            }
                        }
                    }
                }
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(businessManager,EmployeeBean.class));
    }


    //根据部门ID，查询该部门及其所有的子部门的ID集合
    @Override
    public ResultBean<List<String>> getAllChildOrginfoIds(String id) {
        return ResultBean.getSucceed().setD(iOrgInfoService.getOrgInfoIdsByOrgInfoId(id));
    }

    @Override
    public ResultBean<List<EmployeeBean>> actGetEmployeeByRole(String roleId) {
        List<Employee> employeess = new ArrayList<Employee>();
        List<Employee> employees = iEmployeeService.getAllByDataStatus(DataStatus.SAVE);
        if(employees == null){
            return ResultBean.getFailed();
        }
        for (Employee em:employees) {
            if(em.getLoginUserId() != null){
                ResultBean<LoginUserBean> loginUserBeanResultBean = iAuthenticationBizService.actGetLoginUser(em.getLoginUserId());
                if(loginUserBeanResultBean.getD() != null){
                    if(loginUserBeanResultBean.getD().getUserRoleIds().contains(roleId)){
                        employeess.add(em);
                    }
                }
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(employeess,EmployeeBean.class));
    }

    /**
     * 根据  员工信息     查询 最上级 组织信息
     *
     * @param employee
     * @return
     */
    public ResultBean<OrgBean> actFindSuperOrgByEmployee(Employee employee) {
        if (employee.getOrgInfoId() == null && "".equals(employee.getOrgInfoId())) {
            return ResultBean.getFailed().setM("对不起，该员工没有组织机构信息！");
        }
        Orginfo orginfo = iOrgInfoService.getOne(employee.getOrgInfoId());
        if (orginfo != null) {
            Orginfo superOrg = iOrgInfoService.findSuperOrg(orginfo);
            return ResultBean.getSucceed().setD(mappingService.map(superOrg, OrgBean.class));
        }
        return ResultBean.getFailed().setM("对不起，该员工的组织机构信息不存在！");
    }

    public ResultBean<OrgBean> actFindSuperOrgByOrgId(String orgId) {

        Orginfo orginfo = iOrgInfoService.getOne(orgId);
        if (orginfo != null) {
            Orginfo superOrg = iOrgInfoService.findSuperOrg(orginfo);
            return ResultBean.getSucceed().setD(mappingService.map(superOrg, OrgBean.class));
        }
        return ResultBean.getFailed().setM("对不起，该员工的组织机构信息不存在！");
    }

    @Override
    public ResultBean<List<OrgBean>> actGetOrgsByIds(List<String> ids) {
        List<Orginfo> orgs = iOrgInfoService.getAvaliableList(ids);
        return ResultBean.getSucceed().setD(mappingService.map(orgs,OrgBean.class));
    }

    @Override
    public ResultBean<Set<EmployeeBean>> actGetEmployeeByIds(Set<String> ids) {
        Set<Employee> emps = iEmployeeService.getAvaliableList(ids);
        return ResultBean.getSucceed().setD(mappingService.map(emps,EmployeeBean.class));
    }


    /**
     * 根据登录用户的ID，获取用户所管理部门的ID，包含下级部门
     * @param loginUserId
     * @param includeChild
     * @return
     */
    public ResultBean<List<String>>   actGetAllOrginfoIdByLoginUserId(String loginUserId,Boolean includeChild){
        Employee employee =iEmployeeService.getByLoginUserId(loginUserId);
        if(employee == null){
            return ResultBean.getFailed().setM(String.format("根据登录用户ID【%s】没有查询到员工信息！",loginUserId));
        }
        return ResultBean.getSucceed().setD(iOrgInfoService.getOrgInfoIdsByEmployeeId(employee.getId(),includeChild));
    }

    // 根据loginUserId,获取该员工是哪些部门的部门领导
    public ResultBean<List<OrgBean>> actGetOrgsByLoginUser(String loginUserId){
        Employee employee = iEmployeeService.getByLoginUserId(loginUserId);
        if (employee != null){
            List<Orginfo> orginfos = iOrgInfoService.getOrgByLeaderId(employee.getId());
            return ResultBean.getSucceed().setD(mappingService.map(orginfos,OrgBean.class));
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_EMPLOYEE_NULL_LOGINUSERID"),loginUserId));
    }

    public ResultBean<List<EmployeeBean>> actGetEmployeeManager(String orgId){
        List<Employee> employees = this.actGetEmployeesByOrgId(orgId).getD();
        List<Employee> emManager = new ArrayList<Employee>();
        if(employees != null){
            for (Employee em:employees) {
                if(em.getOrgInfoId() != null){
                    Orginfo orginfo = iOrgInfoService.getAvailableOne(em.getOrgInfoId());
                    if(orginfo.getLeaderId().equals(em.getId())){
                        if(em.getLoginUserId() != null){
                            ResultBean<LoginUserBean> loginUserBean = iAuthenticationBizService.actGetLoginUser(em.getLoginUserId());
                            if (loginUserBean.getD() != null) {
                                for (int i = 0; i < loginUserBean.getD().getUserRoleIds().size(); i++) {
                                    String roleId = loginUserBean.getD().getUserRoleIds().get(i);
                                    if (roleId != null) {
                                        ResultBean<SysRoleBean> sysRoleBeanResultBean = iAuthenticationBizService.actGetAvailableSysRole(roleId);
                                        if (sysRoleBeanResultBean.getD() != null && sysRoleBeanResultBean.getD().getCode().equals("CHANNEL_MANAGER")) {
                                            emManager.add(em);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(emManager,EmployeeBean.class));
    }


    @Override
    public ResultBean<EmployeeBean> actGetEmployeeByWXOpenId(String wxOpenId) {
        Employee employee = iEmployeeService.getEmployeeByWxOpenId(wxOpenId);
        if(employee != null){
            return ResultBean.getSucceed().setD(mappingService.map(employee,EmployeeBean.class));
        }
        return ResultBean.getFailed();
    }

    public ResultBean<Boolean> actCheckEmployeeCell(String cell){
        cell = StringUtils.trimAllWhitespace(cell);
        List<Employee> employees = iEmployeeService.findByCell(cell);
        if (employees.size() > 0){
            return ResultBean.getSucceed().setD(true);
        }
        return ResultBean.getSucceed().setD(false);
    }

    public ResultBean<EmployeeBean> actBindEmployeeManager(String cell, String employeeOpenId) {
        cell = StringUtils.trimAllWhitespace(cell);
        List<Employee> employees = iEmployeeService.findByCell(cell);
        if (employees.size() > 0){
            Employee employee = employees.get(0);
            employee.setWxOpenid(employeeOpenId);
            employee = iEmployeeService.save(employee);
            return ResultBean.getSucceed().setD(mappingService.map(employee, EmployeeBean.class));
        }
        return ResultBean.getFailed().setM("系统中未查到相应的用户信息");
    }
}
