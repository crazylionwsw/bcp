package com.fuze.bcp.api.creditcar.bean.cartransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import lombok.Data;

/**
 * 转移过户
 * Created by Lily on 2017/9/14.
 */
@Data
public class CarTransferSubmissionBean extends BillSubmissionBean {

    /**
     * 过户日期
     */
    private String transferDate = null;

    /**
     * 车辆登记证号码（终身唯一）
     */
    @JsonProperty("registerNumber")
    private String registryNumber = null;

    /**
     * 车辆VIN码（车架号，终身唯一）
     */
    @JsonProperty("vihicleVin")
    private String vin = null;

    /**
     * 车牌号码（过户会发生变化）
     */
    @JsonProperty("carLicenseNumber")
    private String licenseNumber;

    /**
     * 发动机号
     */
    @JsonProperty("engineNumber")
    private String motorNumber = null;

    /**
     * 车辆型号 (国标型号)
     */
    @JsonProperty("chinaModel")
    private String carModelNumber = null;

}
