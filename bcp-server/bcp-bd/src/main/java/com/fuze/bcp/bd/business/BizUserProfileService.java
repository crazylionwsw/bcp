package com.fuze.bcp.bd.business;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.api.bd.bean.UserProfileBean;
import com.fuze.bcp.api.bd.service.IUserProfileBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bd.domain.Employee;
import com.fuze.bcp.bd.domain.Orginfo;
import com.fuze.bcp.bd.service.IEmployeeService;
import com.fuze.bcp.bd.service.IOrgInfoService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/31.
 */
@Service
public class BizUserProfileService implements IUserProfileBizService {

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IOrgInfoService iOrgInfoService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<UserProfileBean> getProfile(String userId) {
        Employee employee = iEmployeeService.getByLoginUserId(userId);

        if (employee != null) { //存在员工信息
            UserProfileBean userProfile = mappingService.map(employee, UserProfileBean.class);
            if (userProfile != null) {
                Orginfo orginfo = iOrgInfoService.getOne(employee.getOrgInfoId());

                userProfile.setOrgBean(mappingService.map(orginfo, OrgBean.class));
            }
            return ResultBean.getSucceed().setD(userProfile);
        } else { //不存在员工信息
            return ResultBean.getSucceed();
        }
    }

    @Override
    public ResultBean<UserProfileBean> updateProfile(UserProfileBean userProfileBean) {
        //取得旧信息
        Employee old = iEmployeeService.getOne(userProfileBean.getId());
        //LoginUserBean loginUser = iLoginUserService.getOne(old.getLoginUserId()).getD();

        //如果修改了手机号，系统就要修改用户名、自动生成密码，并发送到手机
        if (!userProfileBean.getCell().equals(old.getCell())) {
/*            //把用户名修改为新手机号
            loginUser.setUsername(userProfileBean.getCell());
            //为用户生成新密码
            String password = RandomHelper.getRandNum(6);
            loginUser.setPassword(EncryUtil.MD5(password));

            //把密码和用户名发送到手机
            String message = "您的用户名为:"+loginUser.getUsername()+", 密码是:"+password;
            //iSMSService.sendOne(employee.getCell(), message);

            iLoginUserService.save(loginUser);*/
        } /*else {
            //判断是否修改了密码
            if (userProfileBean.getLoginUser() != null) {
                String newPassword = EncryUtil.MD5(employee.getLoginUser().getPassword());
                if (!loginUser.getPassword().equals(newPassword)) {
                    //设置新密码
                    loginUser.setPassword(newPassword);
                    iLoginUserService.saveLoginUser(loginUser);
                }
            }
        }*/

        //保存员工信息
        Employee employee = iEmployeeService.save(mappingService.map(userProfileBean, Employee.class));
        return ResultBean.getSucceed().setD(mappingService.map(employee, UserProfileBean.class));
    }


    public ResultBean<Map<String,Object>> actGetUserApproval(String userId){
        Map<String,Object> map = new HashMap<String,Object>();

        //参配项中获取审批权限数据
        Map<?, ?> permission = iParamBizService.actGetMap("TEM_APPROVAL_PERMISSION").getD();
        LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(userId).getD();
        if (loginUserBean!= null){
            List<String> activitiUserRoles = loginUserBean.getActivitiUserRoles();
            for (String activitiUserRole : activitiUserRoles){
                Map<String,Object> object = (Map<String, Object>) permission.get(activitiUserRole);
                if (object != null){
                    map = object;
                    map.put("auditRole",activitiUserRole);
                }
            }
            return ResultBean.getSucceed().setD(map);
        }
        return ResultBean.getFailed();
    }

}
