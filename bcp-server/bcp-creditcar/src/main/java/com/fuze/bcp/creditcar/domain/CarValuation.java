package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.ValuationInfo;
import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 二手车查询信息单
 * Created by Lily on 2017/8/14.
 */
@Document(collection="so_car_valuation")
@Data
public class CarValuation extends BaseBillEntity {

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
     * 车辆型号  （国标型号）
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
     * 线上评估结果
     */
    private ValuationInfo valuationInfo = null;

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
    private String customerTransactionid = null;

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
     * 经销商地址
     */
    private String carDealerAddress = null;

    /**
     * 车辆类型名称（品牌名 + 车系名 + 车型名）
     */
    private String carType = null;


    public void updateTs(){
        setTs(DateTimeUtils.getCreateTime());
    }

    public String getBillTypeCode() {
        return "A021";
    }
}
