package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.api.customer.bean.SurveyOption;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 顾客调查结果
 */
@Data
@MongoEntity(entityName = "so_customer_survey_result")
public class CustomerSurveyResultBean extends APIBaseDataBean {

    /**
     *      客户交易ID
     */
    private String customerTransactionId;

    /**
     *  客户问卷调查模板
     */
    private String customerSurveyTemplateId;

    /**
     *      客户模板编码
     *      与 银行报批中的  customerClass 对应
     */

    /**
     *      用户调查结果
     *      例：[
     *              {
     *              "questionId":"",
     *              "answerContents":["","",""]或者"",多选或者单选填空
     *              }
     *          ]
     */
    private List<SurveyOption> result = new ArrayList<SurveyOption>();

}
