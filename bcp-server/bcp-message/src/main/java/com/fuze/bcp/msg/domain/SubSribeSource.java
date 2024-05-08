package com.fuze.bcp.msg.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 订阅源，包含各种目标信息，目标电话，目标邮箱等
 * Created by CJ on 2017/9/14.
 */
@Data
@Document(collection = "msg_source")
public class SubSribeSource extends MongoBaseEntity {

    private String name;

    private String groupName;

    private String phoneNo;

    private String email;

    private String vcharNo;

    private String userType;

    private String loginUserName;
}
