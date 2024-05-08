package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 顾客调查模板
 */
@Data
public class CustomerSurveyBean extends APIBaseBean {

    /**
     *      客户交易ID
     */
    private String customerTransactionId;

    /**
     * 问题列表
     */
    private List<CategoryQuestionResultBean> categoryQuestions = new ArrayList<CategoryQuestionResultBean>();

    /**
     * 问题列表
     */
    private List<QuestionResultBean> questionsList;

}
