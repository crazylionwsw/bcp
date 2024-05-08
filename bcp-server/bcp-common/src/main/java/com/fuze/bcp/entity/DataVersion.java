package com.fuze.bcp.entity;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 数据版本信息
 */
@Document(collection="sys_dataversion")
public class DataVersion extends MongoBaseEntity {

    /**
     * 版本号
     */
    private String version = null;


    /**
     * 年份
     */
    private String year = null;


    /**
     * 月份
     */
    private String month = null;
}
