package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.List;

/**
 * 顾客调查模板
 */
@Data
@MongoEntity(entityName = "bd_customer_survey_template")
public class CustomerSurveyTemplateBean extends APIBaseDataBean {

    /**
     *      模板编码
     *      对应  银行报批中 客户的分类
     */

    /**
     *      模板名称
     */

    /**
     *       问题编码列表
     */
    private List<String> questionIds;

}
