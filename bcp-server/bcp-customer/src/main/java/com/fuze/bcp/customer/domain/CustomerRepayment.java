package com.fuze.bcp.customer.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 客户还款信息定义
 */
@Document(collection = "cus_repayment")
@Data
public class CustomerRepayment extends MongoBaseEntity {

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     * 总还款额
     */
    private Double totalRepayment = null;

    /**
     * 首期还款额
     */
    private Double firstRepayment;

    /**
     * 首期还款日期
     */
    private String firstRepaymentDate;

    /**
     * 结束还款日期
     */
    private String lastRepaymentDate;

    /**
     * 月还款额
     */
    private Double monthRepayment;

    /**
     * 还款日（每个月的第几天）
     */
    private Integer repayment;

    /**
     * 账单日 默认为25号
     */
    private Integer  billingDate = null;

    /**
     * 还款方式
     */
    private String repaymentWayId;

    /**
     * 手续费分期还款额
     */
    private Double chargeStageAmount;


}
