package com.fuze.bcp.api.creditcar.bean.carregistry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import lombok.Data;

/**
 * Created by Lily on 2017/9/14.
 */
@Data
public class CarRegistrySubmissionBean extends BillSubmissionBean {

    /**
     * 提车日期
     */
    @JsonProperty("pickupDate")
    private String  pickDate;

    /**
     * 上牌日期
     */
    @JsonProperty("vehicleRegistrationTime")
    private String registryDate = null;

    /**
     * 车牌号码（过户会发生变化）
     */
    @JsonProperty("carLicenseNumber")
    private String licenseNumber;

    /**
     * 车辆VIN码（车架号，终身唯一）
     */
    @JsonProperty("vihicleVin")
    private String vin = null;

    /**
     * 车辆型号 (国标型号)
     */
    @JsonProperty("chinaModel")
    private String carModelNumber = null;


    /**
     * 车辆登记证号码（终身唯一）  车辆铭牌
     */
    @JsonProperty("vehicleRegistrationNumber")
    private String registryNumber = null;


    /**
     * 发动机号
     */
    @JsonProperty("engineNumber")
    private String motorNumber = null;

    /**
     * 车辆类型
     */
    private String vehicleType = null;
}
