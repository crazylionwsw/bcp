package com.fuze.bcp.api.creditcar.bean.carregistry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import lombok.Data;

/**
 * 车辆上牌单（分期经理进行填写，风控审核通过）
 */
@Data
public class CarRegistryBean extends APICarBillBean {

    /**
     * 提车日期
     */
    private String  pickDate;

    /**
     * 上牌日期
     */
    private String registryDate = null;

    /**
     * 车牌号码（过户会发生变化）
     */
    private String licenseNumber;

    /**
     * 车辆VIN码（车架号，终身唯一）
     */
    private String vin = null;

    /**
     * 车辆登记证号码（终身唯一）
     */
//    @JsonProperty("vehicleRegistrationNumber")
    private String registryNumber = null;

    /**
     * 发动机号
     */
//    @JsonProperty("engineNumber")
    private String motorNumber = null;

    /**
     * 车辆型号 (国标型号)
     */
    private String carModelNumber = null;

    /**
     * 车辆类型
     */
    private String vehicleType = null;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A005";
    }
}
