package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.customer.bean.CustomerBean;
import lombok.Data;

/**
 * 客户签约提交单（PAD） 人
 */
@Data
public class OrderSubmissionPeopleBean extends BillSubmissionBean {

    // 人
    // 借款人身份证信息                             1
    // 借款人详细信息  保存到customer 里           1
    // 配偶身份证信息                              1
    // 贷款主体  指标人与本人的关系                  1
    // 指标人身份证信息                             1
    // 指标状态信息
    // 是否需要反担保人                             1
    // 反担保人身份证信息  新增一个 customer 后 ID 保存到 order 里    1
    /**
     * 贷款主体 客户ID
     */
    private String customerId;

    /**
     * 贷款主体信息，同样的客户基本信息
     */
    private CustomerBean creditMaster = null;

    /**
     * 配偶
     */
    private CustomerBean mateCustomer = null;

    /**
     * 抵押人 （指标人）
     */
    private CustomerBean pledgeCustomer = null;

    /**
     * 贷款主体与客户之间关系
     */
    private String relation;

    /**
     * 交易ID
     */
    private String customerTransactionId;

    /**
     * 是否需要反担保人
     */
    private Integer needCounterGuarantor = 0;

    /**
     * 反担保人
     */
    private CustomerBean counterGuarantor = null;

}
