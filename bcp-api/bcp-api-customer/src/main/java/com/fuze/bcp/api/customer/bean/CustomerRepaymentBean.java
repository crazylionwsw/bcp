package com.fuze.bcp.api.customer.bean;

import com.fuze.bcp.api.bd.bean.RepaymentWayBean;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;


/**
 * 客户还款信息定义
 */
@Data
public class CustomerRepaymentBean extends APIBaseBean  {

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
    private RepaymentWayBean repaymentWay;

}
