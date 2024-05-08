package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题
 */
@Data
public class QuestionResultBean extends APIBaseDataBean {

    /**
     * 问题描述
     */
    private String content;

    /**
     * 问题类型
     * 单选  多选  填空
     */
    private String questionType;

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
    private int required = 0;

    /**
     * 用户调查结果
     *      例：{
     *          ["questionId":""],
     *          ["answerIds":{"","",""}或者""],多选或者单选填空
     *          }
     */
    private Object result;

}
