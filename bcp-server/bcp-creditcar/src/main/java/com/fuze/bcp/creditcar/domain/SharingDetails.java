package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 渠道返佣详情
 */
@Document(collection = "so_sharing_details")
@Data
public class SharingDetails extends MongoBaseEntity {

    /**
     * 分期经理ID
     */
    private String employeeId;

    /**
     * 交易
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
     * 渠道分成比例
     */
    private double sharingRatio;

    /**
     * 渠道分成金额
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
     * 主体模式 正常 1 私单 2 直客 3
     */
    private Integer  mainPartType;

    private String chargePaymentWay;

    private Boolean compensatoryFlag = false;

    private String pledgeDateReceiveTime;

}
