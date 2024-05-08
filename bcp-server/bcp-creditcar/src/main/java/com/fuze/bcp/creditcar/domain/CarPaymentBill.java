package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.bean.PayAccount;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 购车垫资付款单
 * Created by sean on 2016/11/27.
 */
@Document(collection="fi_car_payment")
@Data
public class CarPaymentBill extends BaseBillEntity {

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
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A004";
    }

}