package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 转移过户
 */
@Document(collection = "so_cartransfer")
@Data
public class CarTransfer extends BaseBillEntity {

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

/*    *//**
     * 是否需要提前支付（新车：开票前支付，二手车：转移过户前支付）
     *//*
    private Boolean needAdvancedPay = false;

    *//**
     * 收款账户
     *//*
    private PayAccount payAccount = null;

    *//**
     * 要求付款时间
     *//*
    private String requiredPayTime;

    *//**
     * 垫资项(名称,金额)
     *//*
    private Map<String,Double> paymentItems = new HashMap<String,Double>();*/

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
