package com.fuze.bcp.api.msg.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sean on 2017/5/23.
 * 消息发布来源，从外部模块接收到的消息对象
 */
public class MessageSourceBean {

    /**
     * 消息类型编码
     */
    private String msgTempCode = null;


    /**
     * 消息内容需要的键值数据
     */
    private Map<String,Object> contextData = new HashMap<String,Object>();
}
