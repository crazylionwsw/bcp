package com.fuze.bcp.api.creditcar.bean.carregistry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import lombok.Data;

/**
 * Created by Lily on 2017/9/14.
 */
@Data
public class CarRegistryListBean extends APIBillListBean {
    /**
     * 车牌号码（过户会发生变化）
     */
    private String licenseNumber;

    /**
     * 车辆VIN码（车架号，终身唯一）
     */
    private String vin = null;

    /**
     * 车辆型号 (国标型号)
     */
    private String carModelNumber = null;

    /**
     * 车辆登记证号码（终身唯一）
     */
    private String registryNumber = null;

    /**
     * 上牌日期
     */
    private String registryDate = null;

    /**
     * 车辆类型
     */
    private String vehicleType = null;

}
