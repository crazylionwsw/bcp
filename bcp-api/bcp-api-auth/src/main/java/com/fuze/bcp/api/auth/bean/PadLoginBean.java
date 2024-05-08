package com.fuze.bcp.api.auth.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录.
 */
@Data
public class PadLoginBean implements Serializable {

    /**
     * 登录名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 设备信息
     */
    private String identify;
    private String serialNum;
    private String mac;
    private String deviceName;
    private String	deviceType;
    private String OS;
}
