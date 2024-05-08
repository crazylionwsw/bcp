package com.fuze.bcp.msg.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/7/21.
 */
@Document(collection = "msg_subscribe")
@Data
public class MessageSubScribe extends MongoBaseEntity {

    /**
     * 业务类型
     */
    private String businessType = null;

    /**
     * 订阅人ID
     */
    private List<String> subScribeSourceIds;

    /**
     * 订阅类型
     */
    private String scribeType;


    /**
     * 发送渠道
     */
    private List<String> userChannel = new ArrayList<>();

}
