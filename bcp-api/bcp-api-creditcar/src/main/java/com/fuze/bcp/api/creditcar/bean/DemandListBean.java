package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

/**
 * 购车资质列表
 */
@Data
public class DemandListBean extends APIBillListBean {

    /**
     * 定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A001";
    }
}
