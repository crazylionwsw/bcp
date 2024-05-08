package com.fuze.bcp.api.sys.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * PAD端版本管理
 */

@Data
@MongoEntity(entityName = "sys_apkversion")
public class APKReleaseBean extends APIBaseBean {

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

    /**
     * 发布声明
     */
    private String releaseNotes;

    /**
     * 更新说明
     */
    private String updateTips;

}
