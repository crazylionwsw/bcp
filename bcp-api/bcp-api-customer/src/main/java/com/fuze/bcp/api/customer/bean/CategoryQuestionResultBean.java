package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题
 */
@Data
public class CategoryQuestionResultBean extends APIBaseDataBean {

    /**
     * 显示顺序
     */
    private int displayOrder;

    /**
     * 问题列表
     */
    private List<QuestionResultBean> questionsList = new ArrayList<QuestionResultBean>();

}
