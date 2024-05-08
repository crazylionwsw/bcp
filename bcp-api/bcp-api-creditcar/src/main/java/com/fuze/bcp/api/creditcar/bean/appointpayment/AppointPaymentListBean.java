package com.fuze.bcp.api.creditcar.bean.appointpayment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import lombok.Data;

/**
 * 预约垫资
 */
@Data
public class AppointPaymentListBean extends APIBillListBean {

    /**
     * 提车日期
     */
    private String pickupDate = null;

    /**
     * 刷卡时间 ，支付截止日期
     */
    private String appointPayTime = null;

    /**
     * 是否需要提前支付（新车：开票前支付，二手车：转移过户前支付）
     * 1 是
     * 0 否
     */
    @JsonProperty("paymentTimeType")
    private Integer advancedPay = 1;

    /**
     * 收款账户名称
     */
    private String accountName;

}
