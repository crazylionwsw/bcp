package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 *      调查问卷---- 客户调查模板
 * Created by zqw on 2017/8/26.
 */
@Data
@Document(collection = "bd_customer_survey_template")
public class CustomerSurveyTemplate extends BaseDataEntity {

    /**
     *      模板编码
     *      对应  银行报批中 客户的分类
     */


    /**
     *      模板名称
     */


    /**
     *       问题ID列表
     */
    private List<String> questionIds;
}
