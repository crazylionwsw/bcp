package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * PAD app版本管理.
 */
@Document(collection = "sys_apkversion")
public class APKRelease extends MongoBaseEntity {

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
     * apk 远程服务器地址
     */
    private String apkUrl = null;

    /**
     * 是否需要强制升级
     */
    private Boolean forceUpgrade = false;

    private String releaseNotes;

    /**
     * 更新说明
     */
    private String updateTips;

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

    public Boolean getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(Boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getUpdateTips() {
        return updateTips;
    }

    public void setUpdateTips(String updateTips) {
        this.updateTips = updateTips;
    }
}
