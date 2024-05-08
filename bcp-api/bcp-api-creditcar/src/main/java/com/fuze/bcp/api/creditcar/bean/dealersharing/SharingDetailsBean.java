package com.fuze.bcp.api.creditcar.bean.dealersharing;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 渠道返佣详情
 */
@Data
public class SharingDetailsBean extends APIBaseBean {

    public static final Integer GROUPDETAIL = 1; // 集团分成

    public static final Integer DEALERDETAIL = 2; // 渠道分成

    public SharingDetailsBean() {
    }

    public SharingDetailsBean(String pledgeDateReceiveTime, Boolean compensatoryFlag, String chargePaymentWay, String employeeId, String transactionId, String customerId, double creditAmount, Double bankFeeAmount, Double loanServiceFee, Integer months, double sharingRatio, double sharingAmount, Integer sharingType, Integer status, Integer mainPartType) {
        this.employeeId = employeeId;
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.creditAmount = creditAmount;
        this.bankFeeAmount = bankFeeAmount;
        this.loanServiceFee = loanServiceFee;
        this.months = months;
        this.sharingRatio = sharingRatio;
        this.sharingAmount = sharingAmount;
        this.sharingType = sharingType;
        this.status = status;
        this.mainPartType = mainPartType;
        this.compensatoryFlag = compensatoryFlag;
        this.chargePaymentWay = chargePaymentWay;
        this.pledgeDateReceiveTime = pledgeDateReceiveTime;
    }

    /**
     * 分期经理ID
     */
    private String employeeId;

    /**
     * 订单
     */
    private String transactionId;

    /**
     * 客户
     */
    private String customerId;

    /**
     * 货款金额
     */
    private double creditAmount;

    /**
     * 银行手续费
     */
    private Double bankFeeAmount;

    /**
     * 贷款服务费
     */
    private Double loanServiceFee;

    /**
     * 贷款期数
     */
    private Integer months;

    /**
     * 分成比例
     */
    private double sharingRatio;

    /**
     * 分成金额
     */
    private double sharingAmount;

    /**
     * 分成类型 1 集团分成 2 渠道分成
     */
    private Integer sharingType;

    /**
     * 状态 0 初始化 1 修正 2 核对 3 复核 4 结算 所有单据都结算达到状态才算到达状态
     */
    private Integer status;

    /**
     * 主体模式 正常 私单 直客
     */
    private Integer  mainPartType;

    /**
     * 缴费方式
     */
    private String chargePaymentWay;

    /**
     * 是否贴息
     */
    private Boolean compensatoryFlag = false;

    private String pledgeDateReceiveTime;

}
