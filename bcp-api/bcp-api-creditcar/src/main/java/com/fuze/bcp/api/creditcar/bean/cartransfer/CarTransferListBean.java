package com.fuze.bcp.api.creditcar.bean.cartransfer;

import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import lombok.Data;

/**
 * 转移过户
 * Created by Lily on 2017/9/14.
 */
@Data
public class CarTransferListBean extends APIBillListBean {
    /**
     * 过户日期
     */
    private String transferDate = null;

    /**
     * 车辆登记证号码（终身唯一）
     */
    private String registryNumber = null;

    /**
     * 车辆VIN码（车架号，终身唯一）
     */
    private String vin = null;

    /**
     * 车牌号码（过户会发生变化）
     */
    private String licenseNumber;
    /**
     * 车辆型号  （国标型号）
     */
    private String carModelNumber = null;
    /**
     * 车牌号码（过户会发生变化）
     */
    private String GBModel;
}
