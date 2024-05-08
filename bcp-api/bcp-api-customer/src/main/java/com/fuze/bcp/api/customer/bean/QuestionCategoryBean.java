package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APITreeDataBean;
import lombok.Data;

/**
 * 问题分类
 */
@Data
@MongoEntity(entityName = "bd_question_category")
public class QuestionCategoryBean extends APITreeDataBean {

    /**
     *  分类编码
     */

    /**
     *  分类名称
     */

    /**
     * 显示顺序
     */
    private int displayOrder;


}
