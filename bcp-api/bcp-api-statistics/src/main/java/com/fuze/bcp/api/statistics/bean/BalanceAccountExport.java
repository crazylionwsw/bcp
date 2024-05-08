package com.fuze.bcp.api.statistics.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by GQR on 2017/11/10.
 */
@Data
public class BalanceAccountExport implements Serializable{

   /* private String yearAndMonth;


    *//**
     * 银行放款合计
     *//*
    private Double totalCredit = 0.0;

    /**
     * 代码行
     */
    private String cashSourceName = null;

    /**
     * 渠道
     */
    private String cardealerName = null;

    /**
     * 客户名称
     */
    private String customerName = null;

    //身份证号
    private String identifyNo = null;

    /**
     * 业务类型
     */
    private String businessTypeCode = null;

    /**
     * 贷款金额
     */
    private Double creditAmount = 0.0;

    /**
     * 贷款期限
     */
    private String creditMonths;

    /**
     * 手续费缴纳方式
     */
    private String chargePaymentWay = null;

    /**
     * 银行手续费率
     */
    private String chargeFeeRatio;

    /**
     * 银行手续费金额
     */
    private String chargeFee;
    /**
     * 刷卡日期
     */
    private String swingCardDate = null;
    /**
     * 本月结费金额
     */
    private Double checkFee = null;

    /**
     * 是否贴息
     */
    private String compensatoryInterest = null;






}
