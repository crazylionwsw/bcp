package com.fuze.bcp.api.auth.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录用户
 * Created by sean on 2017/5/19.
 */
@Data
@MongoEntity(entityName = "au_loginuser")
public class LoginUserBean extends APIBaseBean {

    /**
     * 登录名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;


    /**
     * 验证码
     */
    private String cellCode;

    /**
     * 登录时间
     */
    private String lastLoginTime = null;

    /**
     * 审批流的用户ID
     */
    private String activitiUserId = null;

    /**
     * 是否系统用户，如果是true，不能删除，不能作废，不能修改用户信息，只能修改密码
     */
    private Boolean system = false;

    /**
     * 允许登录的设备数量，默认是1，大部分情况下不允许用户同时登录多个设备
     */
    private Integer deviceNum = 1;

    /**
     * 用户角色
     */
    private List<String>  userRoleIds = new ArrayList<String>();

    /**
     * 审批流的用户角色
     */
    private List<String> activitiUserRoles = new ArrayList<String>();

    /**
     * 最后修改密码的时间
     */
    private String lastPasswordResetTime;

    /**
     * 用户角色
     */
    private List<SysRoleBean> sysRoleList = new ArrayList<SysRoleBean>();


}
