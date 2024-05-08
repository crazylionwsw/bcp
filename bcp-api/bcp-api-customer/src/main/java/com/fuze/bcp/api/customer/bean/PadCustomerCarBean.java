package com.fuze.bcp.api.customer.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 客户会话中车的信息
 */
@Data
public class PadCustomerCarBean extends APIBaseBean {

    /**
     * 客户的ID
     */
    private String customerId = null;

    /**
     * 客户交易
     */
    private String customerTransactionId = null;

    /**
     * 车型
     */
    private String carTypeId;

    /**
     * 是否平行进口车, 0 or 1
     */
    private String parallelImport = "0";

    /**
     * 车辆颜色
     */
    private String carColor;
    /**
     * 车辆颜色
     */
    private String carColorName;

    /**
     * 预计成交价
     */
    private Double estimatedPrice;

    /**
     *  官方指导价（原价）
     */
    private Double guidePrice;

    /**
     * 实际成交价
     */
    private Double realPrice;

    /**
     * 开票价格 = 贷款额度+首付金额
     */
    private Double receiptPrice;

    /**
     * 车牌号码（过户会发生变化）
     */
    private String licenseNumber;

    /**
     * 上牌日期
     */
    private String registryDate = null;

    /**
     * 车辆VIN码（车架号，终身唯一）
     */
    private String vin = null;

    /**
     * 车辆登记证号码（终身唯一）
     */
    private String registryNumber = null;

    /**
     * 发动机号
     */
    private String motorNumber = null;

    //二手车业务里面的字段

    /**
     * 车配置说明
     */
    @JsonProperty("configure")
    private String configures;

    /**
     * 车排量
     */
    @JsonProperty("carEmissions")
    private String ml;

    /**
     * 车过户次数
     */
    private String transferCount;

    /**
     * 车运营状况 1 表示运营， 0 表示非运行
     */
    private String operateStatus;

    /**
     * 评估类型
     */
    @JsonProperty("evaluateSources")
    private String evaluateType;


    /**
     * 评估类型名称
     */
    @JsonProperty("evaluateSourcesName")
    private String evaluateName;

    /**
     * 评估价格
     * CarValuationBean  里的 initialValuationPrice 字段
     */
    private Double evaluatePrice;

    /**
     * 维保里程
     */
    private String maintenanceMileage;

//    /**
//     * 首次购买日期
//     */
//    private String buyDate ;

    /**
     * 首次登记日期
     */
    private String firstRegistryDate = null;


    /**
     * 行驶里程
     */
    private Double mileage ;

    /**
     * 车辆型号
     */
    private String carModelNumber ;

    /**
     * 初始评估价---二手车
     */
    private Double initialValuationPrice;


//    /**
//     * 线上评估来源
//     */
//    private String onlineEvaluateSourceCode;
}