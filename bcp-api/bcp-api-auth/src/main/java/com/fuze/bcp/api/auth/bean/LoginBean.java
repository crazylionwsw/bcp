package com.fuze.bcp.api.auth.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录.
 */
@Data
public class LoginBean implements Serializable {

    /**
     * 登录名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;



}
