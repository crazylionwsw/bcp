package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题
 */
@Data
@MongoEntity(entityName = "bd_questions")
public class QuestionsBean extends APIBaseDataBean {

    /**
     *      问题编码
     */

    /**
     *  问题内容
     */
    private String content;

    /**
     *  问题类型
     *  单选 radio 多选 checklist 填空 text
     */
    private String questionType;

    /**
     *  问题分类编码
     */
    private String questionCategoryId;

    /**
     * 答案选项
     */
    private List<Options> questionOptions = new ArrayList<Options>();

    /**
     *  可选答案数量
     */
    private int optionalAnswersQuantity;

    /**
     *      显示顺序
     */
    private int displayOrder;

    /**
     * 是否必填字段
     */
    private int required = 1;

}
