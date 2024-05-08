package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

/**
 * 提车单
 */
@Data
public class PickupCarBean extends APICarBillBean {

    /**
     * 提车日期
     */
    private String  pickDate;

    /**
     * 车牌号码
     */
    private String  licenseNumber = null;

    /**
     * 登记证编号
     */
    private String  registryNumber = null;

    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A003";
    }
}
