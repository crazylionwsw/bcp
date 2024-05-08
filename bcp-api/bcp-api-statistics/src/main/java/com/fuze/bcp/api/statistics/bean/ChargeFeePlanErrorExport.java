package com.fuze.bcp.api.statistics.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by GQR on 2017/10/30.
 */
@Data
public class ChargeFeePlanErrorExport implements Serializable{


    /**
     * 业务类型 (新车/二手车）
     */
    private String businessTypeCode;

    /**
     * 分期经理
     */
    private String username;

    /**
     * 分期手机
     */
    private String  cell;

    /**
     * 贴息/商贷
     */
    private String compensatoryInterest;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 身份证号码
     */
    private String identifyNo;

    /**
     * 客户手机
     */
    private String cells;

    /**
     * 签约日期
     */
    private String transactionTime;

    /**
     * 提车上牌/转移过户
     */
    private String pickUpCar;

    /**
     * 数据错误原因
     */
    private String comments;



}
