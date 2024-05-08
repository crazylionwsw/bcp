package com.fuze.bcp.domain;

/**
 * 基础数据基类
 * Created by sean on 2016/10/20.
 */
public class BaseDataEntity extends MongoBaseEntity {


    /**
     * 编码
     */
    private String code = null;

    /**
     * 名称
     */
    private String name = null;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
