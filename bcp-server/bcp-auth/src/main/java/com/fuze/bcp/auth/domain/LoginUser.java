package com.fuze.bcp.auth.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录用户定义
 * Created by sean on 16/10/10.
 */
@Document(collection = "au_loginuser")
@Data
public class LoginUser extends MongoBaseEntity {

    /**
     * 登录用户名
     */
    private String username = null;
    /**
     * 登录密码
     */
    private String password = null;

    /**
     * 登录时间
     */
    private String lastLoginTime = null;

    /**
     * 审批流的用户ID
     */
    private String activitiUserId = null;

    /**
     * 审批流的用户角色
     */
    private List<String> activitiUserRoles;

    /**
     * 是否系统用户，如果是true，不能删除，不能作废，不能修改用户信息，只能修改密码
     */
    private Boolean system = false;

    /**
     * 允许登录的设备数量，默认是1，大部分情况下不允许用户同时登录多个设备
     */
    private Integer deviceNum = 1;

    /**
     * 密码修改时间
     */
    private String lastPasswordResetTime = null;

    /**
     * 用户所有的权限信息
     */
    private List<String>  userRoleIds = new ArrayList<String>();

}
