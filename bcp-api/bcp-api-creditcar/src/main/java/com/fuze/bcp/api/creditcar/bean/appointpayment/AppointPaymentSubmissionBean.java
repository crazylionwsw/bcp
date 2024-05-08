package com.fuze.bcp.api.creditcar.bean.appointpayment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.bd.bean.PayAccountBean;
import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.customerfeebill.CustomerFeeBean;
import lombok.Data;

/**
 * Created by Lily on 2017/9/14.
 */
@Data
public class AppointPaymentSubmissionBean extends BillSubmissionBean {
    /**
     * 提车日期
     */
    private String pickupDate = null;

    /**
     * 刷卡时间 ，支付截止日期
     */
    @JsonProperty("swipeDeadlineTime")
    private String appointPayTime = null;

    /**
     * 是否需要提前支付（新车：开票前支付，二手车：转移过户前支付）
     * 1 表示开票前支付，2 表示开票后支付
     */
    @JsonProperty("paymentTimeType")
    private Integer advancedPay = 1;

    /**
     *  支付方式
     *  0   :   差额支付
     *  1   :   全额支付
     */
    @JsonProperty("paymentMethod")
    private Integer chargeParty = 0;

    /**
     * 垫资项(名称)
     * 贴息额：0
     * 贷款额：1
     * 贴息额+贷款额：2
     */
    @JsonProperty("loanAmountType")
    private String paymentType = "1";

    /**
     * 预约垫资金额
     */
    @JsonProperty("loanAmount")
    private Double appointPayAmount = null;

    /**
     * 收款账户
     */
    @JsonProperty("accountInfo")
    private PayAccountBean payAccount = null;

    /**
     * 汇款信息
     */
    private CustomerFeeBean customerFeeBean;

    /**
     * 是否贴息
     * 0. 否
     * 1. 是
     */
    private Integer needCompensatory = 0;

    /**
     * 是否需要垫资
     * 0. 否
     * 1. 是
     */
    private Integer isNeedLoaning  = 0;


    /**
     * 贴息金额
     */
    private Double compensatoryAmount = 0.0;

}
