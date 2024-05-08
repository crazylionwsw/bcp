package com.fuze.bcp.msg.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Created by CJ on 2017/10/29.
 */
@Document(collection = "msg_notice")
@Data
public class Notice extends MongoBaseEntity {

    // 消息类型 通知、公告、消息
    private String type;
    // 发送类型，短信、微信、pad等
    private String sendType;
    // 标题
    private String title;
    // 内容
    private String content;

    // 1 人，2角色，3组织架构
    private Integer fromGroup;

    private Set<String> loginUserNames;

    private String groupId;

    private String orgId;

    // 发送状态 1 未发送、2发送中、3发送成功、4发送失败
    private Integer status;
    /**
     * 发送结果
     *  1 表示成功
     *  0 表示失败
     */
    private Integer result;

    /*******************消息需要字段**********************/

    private String goActivity;

    private Integer afterOpenAction;

    private String billId;

    private String businessType;

    private String sendTime;

    private String orderId;
}
