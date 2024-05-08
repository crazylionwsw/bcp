package com.fuze.bcp.api.sys.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 终端设备绑定记录
 */
@Data
public class TerminalBindBean extends APIBaseBean {

    /**
     * 登录用户
     */

    private String loginUserId =  null;

    /**
     * 绑定的设备
     */
    private String terminalDeviceId = null;

    /**
     * 绑定时间
     */
    private String bindTime = null;


}
