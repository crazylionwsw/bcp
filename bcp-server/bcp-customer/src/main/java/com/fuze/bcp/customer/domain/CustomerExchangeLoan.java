package com.fuze.bcp.customer.domain;

import com.fuze.bcp.bean.RateType;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Document(collection = "cus_loan_exchange")
@Data
public class CustomerExchangeLoan extends MongoBaseEntity{

    /**
     * 客户
     */
    private String customerId = null;

    /**
     * 客户交易ID
     */
    private String customerTransactionId = null;

    /**
     * 资金来源
     */
    private String cashSourceId = null;

    /**
     *  在资质审查页面 为 预计成交价
     *  在客户签约中   为 车辆实际成交价
     */
    private Double realPrice;

    /**
     * 车辆开票价格
     */
    private Double receiptPrice;

    /**
     * 预计成交价
     */
    //private Double estimatedPrice;

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
    private Double downPayment = null;

    /**
     * 首付比例
     */
    private Double downPaymentRatio = null;

    /**
     * 贷款额度
     */
    private Double creditAmount = null;

    /**
     * 贷款的比例
     */
    private Double creditRatio = null;

    /**
     * 手续费率
     */
    private RateType rateType = null;

    /**
     * 收费明细列表，用编码标识不同的费用项目      FeeItem
     * 贷款服务费，担保服务费，风险押金 杂费
     * 移到  PurchaseCarOrder 中
     */
//    Map<String, Double> feeItemList = new HashMap<String, Double>();

    /**
     * 垫资金额
     */
    private Double advancedPayment;

    /**
     * 是否贴息
     * 0 false
     * 1 true
     */
    private Integer compensatoryInterest = 0;

    /**
     * 是否垫资(跟渠道垫资政策同步)
     * 0：否   1：是
     */
    private Integer isNeedPayment;

    /**
     * 贴息方案
     */
    private String compensatoryWay ;

    /**
     * 贴息期限
     */
    private Integer compensatoryMonth;

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
     * 实际手续费
     * 银行手续费-【
     */
    private Double realityBankFeeAmount;

    /**
     * 刷卡金额
     */
    private Double swipingAmount = 0.0;

    /**
     * 手续费收缴方式, WHOLE/STAGES
     */
    private String  chargePaymentWay = "WHOLE";

    /**
     * 贷款服务费  （待确定 需要判断，不得低于手续费率）
     *
     */
    private Double loanServiceFee = 0.00;

}
