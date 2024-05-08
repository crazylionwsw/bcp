package com.fuze.bcp.api.customer.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lily on 2017/8/3.
 */
@Data
public class CustomerCheckBean {
    /**
     * 客户id
     */
    private String customerId;
    /**
     * 个人对外投资
     */
    private PersonalInvestmentBean personalInvestmentBean;
    /**
     * 个人诉讼
     */
    private PersonalActionBean personalActionBean;
    /**
     * 企业诉讼
     */
    private CorporateActionBean corporateActionBean;
    /**
     * 工商企业查询
     */
    private BusinessDataBean businessDataBean;

    /**
     * 扩展数据
     */
    private Map extData = new HashMap();

}
