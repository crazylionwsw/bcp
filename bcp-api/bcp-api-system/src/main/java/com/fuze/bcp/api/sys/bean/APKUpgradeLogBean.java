package com.fuze.bcp.api.sys.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * PAD更新日志
 */
@Data
public class APKUpgradeLogBean extends APIBaseBean {

    /**
     * app 名称
     */
    private String appName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * apk 编码
     */
    private Integer versionCode;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 登录用户
     */
    private String loginUserId =  null;

    /**
     * 绑定设备信息
     */
    private String terminalBindId = null;

    /**
     * 是否升级成功
     */
    private Boolean isSuccess = true;

}
