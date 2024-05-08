package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import lombok.Data;

/**
 * 答案
 */
@Data
@MongoEntity(entityName = "cus_options")
public class OptionsBean {

    /**
     *      答案内容
     */
    private String content;

    /**
     *      答案编码
     */
    private String code;

    /**
     *       所属问题编码
     */
    private String questionCode;

    /**
     *      显示顺序
     */
    private int displayOrder;
}
