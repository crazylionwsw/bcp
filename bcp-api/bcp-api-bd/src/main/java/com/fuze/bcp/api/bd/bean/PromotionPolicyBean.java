package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;


import java.util.List;
/**
 * 促销政策
 */
@Data
@MongoEntity(entityName = "bd_promotion_policy")
public class PromotionPolicyBean extends APIBaseDataBean {

    /**
     * 规则公式
     */
    private String formula;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 截止日期
     */
    private String endDate;


}
