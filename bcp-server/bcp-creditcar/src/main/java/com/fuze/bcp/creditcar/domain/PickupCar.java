package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 提车单
 */
@Document(collection="so_pickupcar")
@Data
@Deprecated
public class PickupCar extends BaseBillEntity {

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
