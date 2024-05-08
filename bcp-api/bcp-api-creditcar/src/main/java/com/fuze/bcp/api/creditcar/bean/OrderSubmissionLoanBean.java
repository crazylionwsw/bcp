package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 客户签约提交单（PAD） 钱
 */
@Data
public class OrderSubmissionLoanBean implements Serializable {

    // 钱
    // 借款信息                 1
    // 还款信息                 总还款金额  每月还款金额

    /**
     * 借款需求
     */
    private CustomerLoanBean customerLoan = new CustomerLoanBean();

    /**
     * 每月还款金额
     */
    private String repaymentAmountMonthly;

    /**
     * 总还款金额
     */
    private String repaymentAmountSum;


    public CustomerLoanBean getCustomerLoan() {
        return customerLoan;
    }

    public void setCustomerLoan(CustomerLoanBean customerLoan) {
        this.customerLoan = customerLoan;
    }

    public String getRepaymentAmountMonthly() {
        return repaymentAmountMonthly;
    }

    public void setRepaymentAmountMonthly(String repaymentAmountMonthly) {
        this.repaymentAmountMonthly = repaymentAmountMonthly;
    }

    public String getRepaymentAmountSum() {
        return repaymentAmountSum;
    }

    public void setRepaymentAmountSum(String repaymentAmountSum) {
        this.repaymentAmountSum = repaymentAmountSum;
    }
}
