package com.fuze.bcp.api.creditcar.bean.dealerrepayment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import lombok.Data;

/**
 * Created by Lily on 2017/9/15.
 */
@Data
public class DealerRepaymentSubmissionBean extends BillSubmissionBean {
    /**
     * 贴息金额
     */
    private Double compensatoryAmount = 0.0;

    /**
     * 垫资金额
     */
    private Double advancedPayment;

    /**
     * 垫资完成时间
     */
    @JsonProperty("advancedPaymentTime")
    private String payTime = null;

    /**
     * 还款时间
     */
    @JsonProperty("repaymentTimeMonthly")
    private String  repaymentTime = null;

    /**
     * 还款金额
     */
    @JsonProperty("repaymentAmountMonthly")
    private Double  amount = null;

    /**
     * 交易方式:刷卡，现金，网银转账，微信支付，支付宝支付
     */
    @JsonProperty("repaymentMethod")
    private String tradeWay = null;
}
