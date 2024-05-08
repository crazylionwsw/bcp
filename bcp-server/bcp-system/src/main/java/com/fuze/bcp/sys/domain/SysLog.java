package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by JZ on 2017/3/16.
 * 系统日志
 */
@Document(collection = "sys_log")
@Data
public class SysLog extends MongoBaseEntity {

    /**
     * 操作用户ID
     */
    private String loginUserId;

    /**
     * 执行的操作
     */
    @DBRef
    private ActionItem action;

    private Boolean success;
}
