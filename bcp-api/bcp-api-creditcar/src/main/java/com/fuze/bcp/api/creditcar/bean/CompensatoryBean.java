package com.fuze.bcp.api.creditcar.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by admin on 2017/9/5.
 */
@Data
public class CompensatoryBean implements Serializable {

    /**
     * 车型
     */
    private String carTypeId;

    /**
     * 期数
     */
    private Integer month;

    /**
     * 利率
     */
    private Double ratio;

    /**
     * 首付金额
     */
    private Double downPayment;

    /**
     * 首付比例
     */
    private Double downPaymentRatio;

    /**
     * 贷款额度
     */
    private Double creditAmount;

    /**
     * 贷款的比例
     */
    private Double creditRatio;
}
