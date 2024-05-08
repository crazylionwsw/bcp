package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;


/**
 * 重新签约
 */
@Data
public class ResetOrderBean extends APICarBillBean {

    /**
     * 原始订单的ID
     */
    private String purchaseOrderId;

    public String getBillTypeCode() {
        return "A014";
    }
}
