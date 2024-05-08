package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.EmployeeUserBean;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.wechat.service.IWechatBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class OrgController {

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IWechatBizService iWechatBizService;


    /**
     * 添加组织架构
     * @param orgBean
     * @return
     */
    @RequestMapping(value = "/org", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveOrg(@RequestBody OrgBean orgBean) {
        return iOrgBizService.actSaveOrg(orgBean);
    }

    /**
     * 删除组织架构
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteOrg(@PathVariable("id") String id) {
        return iOrgBizService.actDeleteOrg(id);
    }

    /**
     * 获取指定组织架构
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOrgInfo(@PathVariable("id") String id) {
        return iOrgBizService.actGetOrg(id);
    }

    /**
     * 获取组织架构(仅可用数据)
     *
     * @return
     */
    @RequestMapping(value = "/org/lookups", method = RequestMethod.GET)
    public ResultBean lookupOrgs() {
        return iOrgBizService.actLookupOrgs();
    }

    @RequestMapping(value = "/orgs",method = RequestMethod.GET)
    public ResultBean getOrgs(){
        return  iOrgBizService.actGetAllOrgs();
    }

    /**
     * 获取下级部门
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/{id}/orgs", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetOrgs(@PathVariable("id") String id) {
        return iOrgBizService.actGetOrgs(id);
    }


    /**
     * 保存员工信息
     *
     * @param employeeBean
     * @return
     */
    @RequestMapping(value = "/org/employee/{isLeader}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveEmployee(@RequestBody EmployeeUserBean employeeBean, @PathVariable Boolean isLeader) {
        return iOrgBizService.actSaveEmployee(employeeBean,isLeader);
    }

    /**
     * 模糊查询员工
     *
     * @param currentPage
     * @param employeeBean
     * @return
     */
    @RequestMapping(value = "/org/employee/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchEmployee(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage, @RequestBody EmployeeBean employeeBean) {
        return iOrgBizService.searchEmployee(currentPage, employeeBean);
    }

    /**
     * 删除员工信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/employee/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean actDeleteEmployee(@PathVariable("id") String id) {
        return iOrgBizService.actDeleteEmployee(id);
    }

    /**
     * 查询单条员工记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/employee/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetOneEmployee(@PathVariable("id") String id) {
        return iOrgBizService.actGetEmployee(id);
    }

    /**
     * 获取某部门下所有员工
     *
     * @param currentPage
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/{id}/employees", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actGetEmployees(@RequestParam(value = "currentPage") Integer currentPage, @PathVariable("id") String id) {
        //return iOrgBizService.actGetEmployees(currentPage, id);   //查询某部门下所有的员工
        return iOrgBizService.actGetAllEmployees(currentPage,id);  //查询该部门下及子部门下所有的员工
    }

    /**
     * 获取所有员工(仅可用)
     *
     * @return
     */
    @RequestMapping(value = "/employee/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean actEmployeeLookup() {
        return iOrgBizService.actLookupEmployees();
    }

    /**
     * 批量获取员工(此处为获取分期经理用)
     * @return
     */
    @RequestMapping(value = "/employee/pickup",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actBusinessEmployee(@RequestBody List<String> ids){
        return iOrgBizService.actGetEmployeeByIds(ids);
    }

    /**
     * 根据登录用户ID查询   登录用户的最上级的组织机构信息
     *
     * @param id        登录用户的ID
     * @return
     */
    @RequestMapping(value = "/org/super/{id}/loginuser", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<OrgBean> getSuperOrgByLoginUser(@PathVariable("id") String id) {
        return iOrgBizService.actFindSuperOrgByLoginUserId(id);
    }

    /**
     *      根据  员工ID        查询员工最上级组织信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/super/{id}/employee", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<OrgBean> getSuperOrgByEmployee(@PathVariable("id") String id) {
        return iOrgBizService.actFindSuperOrgByEmployeeId(id);
    }

    /**
     *      根据  组织ID，查询员工最上级组织信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/super/{id}/org", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<OrgBean> getSuperOrgByOrg(@PathVariable("id") String id) {
        return iOrgBizService.actFindSuperOrgByOrgId(id);
    }

    /**
     *      根据  登录用户ID  查询登录用户的员工信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/employee/loginuser/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<EmployeeBean> actGetEmployee(@PathVariable("id") String id) {
        return iOrgBizService.actFindEmployeeByLoginUserId(id);
    }

    /**
     * 根据登录用户ID获取用户姓名
     * @param id
     * @return
     */
    @RequestMapping(value = "/loginuser/{id}/name", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<String> actGetUsername(@PathVariable("id") String id) {
        return iOrgBizService.actGetNameByLoginUserId(id);
    }

    /**
     * 获取渠道经理
     */
    @RequestMapping(value = "/employee/employeemanager",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getEmployeeManager(){
        return iOrgBizService.actGetEmployeeManagers();
    }

    /**
     * 获取分期经理
     * @return
     */
    @RequestMapping(value = "/employee/businessmanager",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getStageManager(){
        return iOrgBizService.actGetBusinessManagers();
    }

    /**
     * 获取所有员工列表
     * @return
     */
    @RequestMapping(value = "/employee/allemployee",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAllManager(){
        return iOrgBizService.actGetEmployees();
    }


    /**
     *
     */
    @RequestMapping(value = "/cardealer/managers/{id}",method = RequestMethod.GET)
    @ResponseBody
    public  ResultBean getEmployeesByOrgInfoId(@PathVariable("id") String id){
        return iOrgBizService.actGetEmployeeManagersByOrginfoId(id);
    }

    @RequestMapping(value = "/employee/sub/{id}",method = RequestMethod.GET)
    @ResponseBody
    public  ResultBean getEmployeesById(@PathVariable("id") String id){
        return iOrgBizService.actGetEmployee(id);
    }

    @RequestMapping(value = "/employee/role/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getEmployeeByRole(@PathVariable("id") String id){
        return iOrgBizService.actGetEmployeeByRole(id);
    }

    //获取多个部门
    @RequestMapping(value = "/orginfo/pickup",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getEmployeeByIds(@RequestBody List<String> ids){
        return iOrgBizService.actGetOrgsByIds(ids);
    }


    //根据loginUserId，获取该员工是哪些部门的部门经理
    @RequestMapping(value = "/orginfos/{id}/loginuser",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean checkLoginUserIsLeader(@PathVariable("id") String id){
        return iOrgBizService.actGetOrgsByLoginUser(id);
    }

    //同步企业微信中的部门
    @RequestMapping(value = "/sync/orginfo",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean syncWechatOrg() throws Exception {
        return iWechatBizService.syncOrg();
    }

    //删除企业微信中的部门
    @RequestMapping(value = "/wechat/delete/orginfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean deleteWechatOrg(String oId) throws Exception {
        return iWechatBizService.deleteOrg(oId);
    }

    //创建企业微信中的员工
    @RequestMapping(value = "/wechat/create/employee",method = RequestMethod.POST)
    @ResponseBody
    public  ResultBean createWechatEmployee() throws Exception {
        return iWechatBizService.createEmployee();
    }

    //更新企业微信中的员工
    @RequestMapping(value="/wechat/update/employee",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean updateWechatEmployee() throws Exception {
        return iWechatBizService.createEmployee();
    }

    //删除企业微信中的员工
    @RequestMapping(value = "/wechat/delete/employee",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean deleteWechatEmployee(String userId) throws Exception {
        return iWechatBizService.deleteEmployee(userId);
    }

    //新增企业微信员工
    @RequestMapping(value = "/wechat/save/employee/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean syncWechatEmployee(@PathVariable("id") String employeeId)throws Exception{
        return iWechatBizService.saveEmployee(employeeId);
    }





}
