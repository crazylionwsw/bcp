package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by fengyincai on 2017/2/27.
 */
@Document(collection = "sys_versioninfo")
public class APKUpgradeLog extends MongoBaseEntity {

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

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getTerminalBindId() {
        return terminalBindId;
    }

    public void setTerminalBindId(String terminalBindId) {
        this.terminalBindId = terminalBindId;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
