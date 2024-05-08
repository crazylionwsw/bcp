package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.RateType;
import lombok.Data;

/**
 * 客户借款信息定义
 */
@Data
public class LoanSubmissionBean extends APIBaseBean {

    /**
     * 客户
     */
    private String customerId;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 资金来源
     */
    private String cashSourceId;

    /**
     * 车辆实际成交价
     */
    private Double realPrice;

    /**
     * 车辆开票价格 = 贷款额度+首付金额
     */
    private Double receiptPrice;

    /**
     * 预计成交价
     */
    private Double estimatedPrice;

    /**
     * 分期申请车价（开票价，评估价，成交价三者最低为购车价格）= 贷款额度+首付金额
     */
    private Double applyAmount;

    /**
     * 银行批复贷款额度
     */
    private Double approvedCreditAmount;

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

    /**
     * 手续费率
     */
    private RateType rateType = new RateType();


    /**
     * 收费明细列表，用编码标识不同的费用项目
     * 贷款服务费，担保服务费，风险押金 杂费
     * 移到 PurchaseCarOrder 中
     */
//    Map<String, Double> feeItemList = new HashMap<String, Double>();

    /**
     * 垫资金额
     */
    private Double advancedPayment;

    /**
     * 是否贴息
     */
    private Integer compensatoryInterest;

    /**
     * 是否垫资(跟渠道垫资政策同步)
     * 0：否   1：是
     */
    private Integer isNeedPayment;

    /**
     * 贴息金额
     */
    private Double compensatoryAmount = 0.0;

    /**
     * 担保方式
     */
    private String guaranteeWayId;

    /**
     * 银行手续费
     */
    private Double bankFeeAmount = 0.0;

    /**
     * 期数（月份）
     */
    private Integer months = 0;

    /**
     * 总费率
     */
    private Double ratio = 0.0;

    /**
     * 刷卡金额=贷款金额(分期)
     * 刷卡金额=贷款金额-银行手续费(趸交)
     */
    private Double realityBankFeeAmount = 0.0;

    /**
     * 手续费收缴方式, WHOLE/STAGES
     */
    private String chargePaymentWay = "WHOLE";

    private Double swipingAmount = 0.0;


    public void setSwipingAmount(Double swipingAmount) {
        this.swipingAmount = swipingAmount;
    }

    public Double getSwipingAmount() {
        if (this.swipingAmount == null || this.swipingAmount == 0.0) {
            setSwipingAmount(creditAmount);
            return creditAmount;
        } else {
            return this.swipingAmount;
        }
    }

    public void setRealityBankFeeAmount(Double realityBankFeeAmount) {
        this.realityBankFeeAmount = bankFeeAmount - compensatoryAmount;
    }

    /**
     * 贷款服务费  （待确定）
     */
    private Double loanServiceFee;

}
