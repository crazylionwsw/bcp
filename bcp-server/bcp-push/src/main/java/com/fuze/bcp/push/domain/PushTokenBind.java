package com.fuze.bcp.push.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 友盟push消息loginUserId、token绑定
 * Created by CJ on 2017/10/11.
 */

@Document(collection = "msg_push_bind")
@Data
public class PushTokenBind extends MongoBaseEntity {

    private String loginUserName;

    private String userToken;

}
