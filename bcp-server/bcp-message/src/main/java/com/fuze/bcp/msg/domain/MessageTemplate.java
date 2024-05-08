package com.fuze.bcp.msg.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 消息模板，用于发送消息MQ
 * Created by CJ on 2017/7/19.
 */
@Document(collection = "msg_messagetemplate")
@Data
public class MessageTemplate extends MongoBaseEntity {

    private String name;

    private String templateCode;

    private String sendType;// EMAIL，WX，PHONEMESSAGE

    private String businessType;// 业务类型，eg：买车卖车租车借车

    private String templateContent;

    private String emailObjectId;

    private String pushObjectId;

    private List<String> metaDatas;

}
