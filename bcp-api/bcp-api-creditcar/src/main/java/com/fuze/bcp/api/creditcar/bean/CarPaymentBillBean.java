package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.bean.PayAccount;
import lombok.Data;

/**
 * 购车垫资付款单
 * Created by sean on 2016/11/27.
 */
@Data
public class CarPaymentBillBean extends APICarBillBean {

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 支付金额
     */
    private Double payAmount;

    /**
     * 收款账户
     */
    private PayAccount receiveAccount;

    /**
     * 付款账户
     */
    private PayAccount payAccount;

    /**
     * 单据类型
     * @return
     */
    public String getBillTypeCode() {
        return "A004";
    }

}