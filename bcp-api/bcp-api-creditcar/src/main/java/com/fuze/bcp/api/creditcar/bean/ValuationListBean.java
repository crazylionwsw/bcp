package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.bean.APIBaseBillBean;
import lombok.Data;


/**
 * 评估单列表
 * Created by Lily on 2017/8/14.
 */
@Data
public class ValuationListBean extends APIBaseBillBean {

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
     * 行驶里程
     */
    private Double mileage = null;

    /**
     * 颜色
     */
    private String color = null;

    /**
     * 初始评估价
     */
    private Double initialValuationPrice;

    /**
     * 线上评估价
     */
    private Double onlineValuationPrice;

    /**
     * 现场评估价
     */
    private Double price = null;

    /**
     * 官方指导价（需要从车型表里查询）
     */
    private Double guidePrice = null;

    /**
     * 车辆类型名称（品牌名 + 车系名 + 车型名）
     */
    private String carType = null;

    /**
     * 车辆类型
     */
    private String carTypeId = null;

    /**
     * 当前任务 (从工作流获取）
     */
    private String currentTask = null;

    /**
     * 维保里程
     */
    private String maintenanceMileage;

    /**
     * 维保URL
     */
    private String maintenanceUrl;

    /**
     * 在线评估URL
     */
    private String valuationUrl;

    /**
     * 首次登记日期
     */
    private String firstRegistryDate = null;

    public String getBillTypeCode(){
        return "A021";
    };

}
