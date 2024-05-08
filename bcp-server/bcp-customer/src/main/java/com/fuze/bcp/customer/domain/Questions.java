package com.fuze.bcp.customer.domain;

import com.fuze.bcp.api.customer.bean.Options;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 *      调查问卷----    问题
 * Created by zqw on 2017/8/26.
 */
@Document(collection = "bd_questions")
@Data
public class Questions extends BaseDataEntity {

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
     *  问题分类ID
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
