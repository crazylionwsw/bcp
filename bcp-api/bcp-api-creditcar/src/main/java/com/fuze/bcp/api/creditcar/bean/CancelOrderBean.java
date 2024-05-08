package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;


/**
 * 取消业务申请单
 */
@Data
public class CancelOrderBean extends APICarBillBean {

    //取消原因
    private String reason;

    public String getBillTypeCode() {
        return "A012";
    }
}
