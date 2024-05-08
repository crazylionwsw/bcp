package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.customer.bean.SurveyOption;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 *      调查问卷----    客户调查结果
 * Created by zqw on 2017/8/26.
 */
@Data
@Document(collection = "so_customer_survey_result")
public class CustomerSurveyResult extends BaseDataEntity {

    /**
     *      客户交易ID
     */
    private String customerTransactionId;

    /**
     *      客户问卷调查模板
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
    private List<SurveyOption>  result = new ArrayList<SurveyOption>();
}
