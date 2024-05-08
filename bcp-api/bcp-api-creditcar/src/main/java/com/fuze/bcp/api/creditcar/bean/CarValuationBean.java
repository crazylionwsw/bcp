package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * 二手车评估单
 * Created by Lily on 2017/8/14.
 */
@Data
public class CarValuationBean extends APICarBillBean {

    /**
     * 车辆类型
     */
    private String carTypeId = null;

    /**
     * VIN信息
     */
    private String vin = null;

    /**
     * 车牌号码
     */
    private String licenceNumber = null;

    /**
     * 车辆型号
     */
    private String carModelNumber = null;

    /**
     * 首次登记日期
     */
    private String firstRegistryDate = null;


    /**
     * 行驶里程
     */
    private Double mileage = null;

    /**
     * 颜色
     */
    private String color = null;

    /**
     * 所在城市
     */
    private String pvovinceId = null;

    /**
     * 维保查询日期
     */
    private String maintenanceDate = null;

    /**
     * 维保URL
     */
    private String maintenanceUrl = null;


    /**
     * 维保里程
     */
    private String maintenanceMileage;

    /**
     * 已签约或者未签约
     */
    private Boolean finishOrder = null;

    /**
     * 是否需要查询维保记录
     */
    private Integer queryMaintenance = 1;

    /**
     * 初始评估价
     */
    private Double initialValuationPrice;

    /**
     * 在线评估结果（包括评估日期、评估价格、评估报告的URL
     */
    private ValuationInfo  valuationInfo = null;

    /**
     * 现场评估价
     */
    private Double price = null;

    /**
     * 签约单的ID
     */
    private String orderId = null;

    /**
     * 客户交易
     */
    private String customerTransactionId = null;

    /**
     * 车辆类型名称（品牌名 + 车系名 + 车型名）
     */
    private String carType = null;

    /**
     * 档案资料（评估报告等）
     */
    private List<String> customerImageIds  = new ArrayList<>();

    /**
     * 线上评估来源
     */
    private String onlineEvaluateSourceCode;

    /**
     * 线下评估来源
     */
    private String offlineEvaluateSourceCode;

    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A021";
    }

}
