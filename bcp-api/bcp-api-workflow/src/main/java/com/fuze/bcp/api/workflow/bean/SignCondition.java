package com.fuze.bcp.api.workflow.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by CJ on 2017/9/15.
 */
@Data
public class SignCondition implements Serializable {

    public static final String MONGOSORT = "mongoSort";


    public SignCondition(String field, String value, String collection, String label, String defaultValue) {
        if (field == null || value == null || collection == null || label == null) {
            throw new IllegalArgumentException("error create SignCondition bean");
        }
        this.field = field;
        this.value = value;
        this.collection = collection;
        this.label = label;
        this.defaultValue = defaultValue;
    }

    public SignCondition(String field, String value, Boolean realValue) {
        if (field == null || value == null || realValue == null) {
            throw new IllegalArgumentException("error create SignCondition bean");
        }
        this.field = field;
        this.value = value;
        this.realValue = realValue;
    }

    public SignCondition(Map<String, Object> condition, String collection, String label, String defaultValue) {
        if (condition == null || collection == null || label == null) {
            throw new IllegalArgumentException("error create SignCondition bean");
        }
        this.condition = condition;
        this.collection = collection;
        this.label = label;
        this.defaultValue = defaultValue;
    }

    public SignCondition() {
    }

    /**
     * 多条件<名称, 值>
     */
    private Map<String, Object> condition;

    /**
     * 查询条件名称
     */
    private String field;

    /**
     * 查询条件值
     */
    private String value;

    /**
     * 条件值所在表名称
     */
    private String collection;

    /**
     * 工作流条件的label
     */
    private String label;


    private String defaultValue;

    private Boolean realValue = false;

}
