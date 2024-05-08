package com.fuze.bcp.api.auth.bean;

/**
 * Created by zxp on 2017/8/10.
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 注册
 */
@Data
public class RegistBean implements Serializable {

    /**
     * 注册名称
     */
    private String username;
    /**
     * 注册密码
     */
    private String password;

}
