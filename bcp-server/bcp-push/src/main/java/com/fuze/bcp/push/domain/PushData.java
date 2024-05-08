package com.fuze.bcp.push.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by CJ on 2017/9/28.
 */
@Data
@Document(collection = "msg_pushdata")
public class PushData extends MongoBaseEntity {

    private String ticker;

    private String title;

    private String text;

    private Integer afterOpenAction;

    private String url;

    private String activity;

    private String customer;

    /**
     * 自定义参数
     */
    private Map<String, String> extraFields;

}
