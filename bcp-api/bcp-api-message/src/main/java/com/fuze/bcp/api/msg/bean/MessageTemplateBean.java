package com.fuze.bcp.api.msg.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.List;

/**
 * 消息模板
 * Created by CJ on 2017/7/19.
 */
@Data
@MongoEntity(entityName = "msg_messagetemplate")
public class MessageTemplateBean extends APIBaseBean implements Channels {

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 发送类型 ：Channels.EMAIL
     */
    private String sendType;

    /**
     * 消息业务类型
     */
    private String businessType;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 邮件模板ID
     */
    private String emailObjectId;

    /**
     * 推送模板ID
     */
    private String pushObjectId;

    private List<String> metaDatas;

    /**
     * 该消息对应的接收人条件（通过规则引擎判断接收人）
     */
    private String rules;

}
