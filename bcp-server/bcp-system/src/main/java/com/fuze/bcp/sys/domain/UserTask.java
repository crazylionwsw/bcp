package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by admin on 2017/7/7.
 */
@Data
@Document(collection = "user_task")
public class UserTask extends MongoBaseEntity{
    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务人
     */
    private String loginUserId;

    /**
     * 任务角色
     */
    private String roleId;
}
