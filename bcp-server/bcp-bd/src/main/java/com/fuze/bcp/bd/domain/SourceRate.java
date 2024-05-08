package com.fuze.bcp.bd.domain;

import com.fuze.bcp.bean.RateType;
import com.fuze.bcp.domain.BaseDataEntity;
import com.fuze.bcp.bean.PayAccount;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 资金利率
 * Created by sean on 2016/10/20.
 */
@Document(collection="bd_sourcerate")
public class SourceRate extends BaseDataEntity {

    /**
     * 资金来源信息
     */
    private String cashSourceId = null;

    /**
     * 所属的银行账户
     */
    private PayAccount payAccount = null;

    /**
     * 生效日期
     */
    private String startDate = null;

    /**
     * 截止日期，可以为空，如果为空就说明一直生效
     */
    private String endDate = null;

    /**
     * 期数及对应的利率表
     */
    private List<RateType> rateTypes;

    /**
     * 资金总金额
     */
    private Double amount = 0.0;

    /**
     * 允许的还款方式列表
     */
    private List<String> repaymentWayIds = new ArrayList<String>();

    /**
     * 使用条件 (存储条件表达式)
     */
    private List<String> limits = new ArrayList<String>();

    public PayAccount getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(PayAccount payAccount) {
        this.payAccount = payAccount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<String> getLimits() {
        return limits;
    }

    public void setLimits(List<String> limits) {
        this.limits = limits;
    }

    public String getCashSourceId() {
        return cashSourceId;
    }

    public void setCashSourceId(String cashSourceId) {
        this.cashSourceId = cashSourceId;
    }

    public List<String> getRepaymentWayIds() {
        return repaymentWayIds;
    }

    public void setRepaymentWayIds(List<String> repaymentWayIds) {
        this.repaymentWayIds = repaymentWayIds;
    }

    public List<RateType> getRateTypes() {
        return rateTypes;
    }

    public void setRateTypes(List<RateType> rateTypes) {
        this.rateTypes = rateTypes;
    }
}
