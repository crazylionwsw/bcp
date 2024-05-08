package com.fuze.bcp.api.creditcar.bean.cartransfer;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import lombok.Data;

/**
 * 转移过户
 * Created by Lily on 2017/9/14.
 */
@Data
public class CarTransferBean extends APICarBillBean {
    /**
     * 过户日期
     */
    private String transferDate = null;

    /**
     * 预计上牌日期
     */
    private String registryDate = null;

    /**
     * 车牌号码（过户会发生变化）
     */
    private String licenseNumber;

    /**
     * 车辆登记证号码（终身唯一）
     */
    private String registryNumber = null;

    /**
     * 车辆VIN码（车架号，终身唯一）
     */
    private String vin = null;

    /**
     * 发动机号
     */
    private String motorNumber = null;

    /**
     * 车辆型号 (国标型号)
     */
    private String carModelNumber = null;

    /**
     * 子类需要定义单据类型信息
     * TODO 确定单据类型
     * @return
     */
    public String getBillTypeCode() {
        return "A023";
    }
}
