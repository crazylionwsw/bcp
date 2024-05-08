package com.fuze.bcp.api.msg.bean;

/**
 * 消息通道类型定义
 * Created by sean on 2017/5/26.
 */
public interface Channels {

    /**
     * 短信
     */
    String SMS = "sms";

    /**
     * 邮箱
     */
    String EMAIL = "email";

    /**
     * WEB页面消息
     */
    String WEBSOCKET = "websocket";

    /**
     * 微信
     */
    String WECHAT = "wechat";

    /**
     * PAD 推送
     */
    String PAD = "pad";

    /**
     * 移动APP
     */
    String MOBILE = "mobile";

    /**
     * 系统内部通知消息
     */
    String NOTIFY = "notify";
}
