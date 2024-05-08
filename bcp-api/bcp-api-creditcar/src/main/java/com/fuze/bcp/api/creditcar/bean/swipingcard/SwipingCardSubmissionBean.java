package com.fuze.bcp.api.creditcar.bean.swipingcard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import com.fuze.bcp.api.customer.bean.CustomerRepaymentBean;
import lombok.Data;

/**
 * Created by Lily on 2017/9/14.
 */
@Data
public class SwipingCardSubmissionBean extends BillSubmissionBean {
    /**
     * 刷卡时间
     */
    @JsonProperty("swipeDate")
    private String payTime;

    /**
     * 刷卡金额
     */
    @JsonProperty("swipeSum")
    private Double payAmount;

    /**
     * 卡号
     */
    private String cardNumber;


    /**
     * 还款信息
     */
    private CustomerRepaymentBean customerRepaymentBean = new CustomerRepaymentBean();

    /************************************************pad 专用字段********************************************************/

    /**
     * 首期还款额
     */
    @JsonProperty("firstPeriodSum")
    private Double firstRepayment = 0.0;

    /**
     * 首期还款日期
     */
    @JsonProperty("firstPeriodDate")
    private String firstRepaymentDate;

    /**
     * 月还款额
     */
    @JsonProperty("monthlyPaymentSum")
    private Double monthRepayment = 0.0;

    /**
     * 还款日（每个月的第几天）
     */
    @JsonProperty("monthlyPaymentDate")
    private Integer repayment = 1;

    /**
     * 账单日 默认为25号
     */
    private Integer  billingDate = null;

    /**
     * 实际手续费
     * 银行手续费-贴息金额
     */
    @JsonProperty("procedureFee")
    private Double realityBankFeeAmount = 0.0;

    /**
     * 手续费收缴方式, WHOLE/STAGES
     */
    private String chargePaymentWay = null;


}
