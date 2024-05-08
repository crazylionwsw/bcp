package com.fuze.bcp.api.auth.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/3/19.
 * 登录管理
 */
@Data
public class LogManagementBean extends APIBaseBean {

    /**
     * 登录人
     */
    private String userName;

    /**
     * 登录账号
     */
    private String logName;

    /**
     * 登录类型
     */
    private String logType;

    /**
     * 登陆时间
     */
    private String logTime;



}
