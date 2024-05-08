package com.fuze.bcp.api.sys.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 系统登录日志
 * Created by sean on 16/10/10.
 */
@Data
public class LoginLogBean extends APIBaseBean {

    /**
     * 绑定的设备信息
     */
    private TerminalDeviceBean terminalDeviceBean = null;

    /**
     * 登录的时间
     */
    private String loginTime = null;

    /**
     * 登录状态
     */
    private Integer status = 0;

    /**
     * 系统账号
     */
    private String loginUserId = null;

}
