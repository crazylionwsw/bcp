package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

/**
 * 签约列表
 */
@Data
public class OrderListBean extends APIBillListBean {



    /**
     * 定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A002";
    }
}
