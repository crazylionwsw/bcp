package com.fuze.bcp.msg.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by CJ on 2017/7/27.
 */
@Document(collection = "msg_log")
@Data
public class MessageLog extends MongoBaseEntity {

    /**
     * 接收人
     */
    private String to;

    /**
     * 生成后的消息发布内容
     */
    private Map content;

    private Integer result;

}
