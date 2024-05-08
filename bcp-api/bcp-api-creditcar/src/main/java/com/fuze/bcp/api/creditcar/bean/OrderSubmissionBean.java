package com.fuze.bcp.api.creditcar.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.LoanSubmissionBean;
import com.fuze.bcp.api.customer.bean.PadCustomerCarBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户签约提交单（PAD）
 */
@Data
public class OrderSubmissionBean extends BillSubmissionBean {

    /**
     * 是否需要反担保人
     */
    private Integer needCounterGuarantor = 1;

    /**
     * 反担保人
     */
    private CustomerBean counterGuarantor = null;

    /**
     * 贷款主体信息，同样的客户基本信息
     */
    private CustomerBean creditMaster = null;

    /**
     * 贷款主体与客户之间关系
     */
    private String relation;

    /**
     * 抵押人 （指标人）
     */
    private CustomerBean pledgeCustomer = null;

    /**
     * 配偶
     */
    private CustomerBean mateCustomer = null;

    /**
     * 购车信息
     */
    private PadCustomerCarBean customerCar = new PadCustomerCarBean();

    /**
     * 指标状态
     */
    @JsonProperty("indexStatus")
    private Integer indicatorStatus = 1;

    /**
     * 外迁日期
     */
    @JsonProperty("indicatorsStateDate")
    private String moveOutDate = null;

    /**
     * 预计指标获取日期
     */
    private String retrieveDate = null;

    /**
     * 情况说明
     */
    private String indicatorComment = null;

    /**
     * 借款需求
     */
    private LoanSubmissionBean customerLoan = new LoanSubmissionBean();

    /**
     * 家庭总车辆数
     */
    private Integer carNumber;

    /**
     * 贷款购车次数
     */
    private Integer buyCarNumber;

    /**
     * 是否要求代启卡
     */
    private Integer replaceStartCard = 1;

    /**
     * 是否需要打印收入证明
     */
    private Integer printNeedEarningProof = 0;

    /**
     * 申请的分期产品
     */
    private List<String> applyCreditProductIds = null;

    /**
     * 预约提车日期
     */
    private String pickCarDate = null;

    /**
     * 每月还款金额
     */
    private String repaymentAmountMonthly;

    /**
     * 首月还款金额
     */
    private String repaymentAmountFirstMonth;

    /**
     * 总还款金额
     */
    private String repaymentAmountSum;

    /**
     *  销售人员ID
     */
    private String dealerEmployeeId;

    /**
     * 评估单id
     */
    private String vehicleEvaluateInfoId = null;

    /**
     * 是否为直客模式
     * 1：是  0：否
     */
    private int isStraight = 0;

    public CustomerBean getCounterGuarantor() {
        return counterGuarantor;
    }

    public void setCounterGuarantor(CustomerBean counterGuarantor) {
        this.counterGuarantor = counterGuarantor;
    }

    public PadCustomerCarBean getCustomerCar() {
        return customerCar;
    }

    public void setCustomerCar(PadCustomerCarBean customerCar) {
        this.customerCar = customerCar;
    }

    public LoanSubmissionBean getCustomerLoan() {
        return customerLoan;
    }

    public void setCustomerLoan(LoanSubmissionBean customerLoan) {
        this.customerLoan = customerLoan;
    }

    public Integer getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(Integer carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getBuyCarNumber() {
        return buyCarNumber;
    }

    public void setBuyCarNumber(Integer buyCarNumber) {
        this.buyCarNumber = buyCarNumber;
    }

    public Integer getReplaceStartCard() {
        return replaceStartCard;
    }

    public List<String> getApplyCreditProductIds() {
        return applyCreditProductIds;
    }

    public void setApplyCreditProductIds(List<String> applyCreditProductIds) {
        this.applyCreditProductIds = applyCreditProductIds;
    }

    public String getPickCarDate() {
        return pickCarDate;
    }

    public void setPickCarDate(String pickCarDate) {
        this.pickCarDate = pickCarDate;
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

    public CustomerBean getCreditMaster() {
        return creditMaster;
    }

    public void setCreditMaster(CustomerBean creditMaster) {
        this.creditMaster = creditMaster;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public CustomerBean getPledgeCustomer() {
        return pledgeCustomer;
    }

    public void setPledgeCustomer(CustomerBean pledgeCustomer) {
        this.pledgeCustomer = pledgeCustomer;
    }

    public CustomerBean getMateCustomer() {
        return mateCustomer;
    }

    public void setMateCustomer(CustomerBean mateCustomer) {
        this.mateCustomer = mateCustomer;
    }

    public Integer getIndicatorStatus() {
        return indicatorStatus;
    }

    public void setIndicatorStatus(Integer indicatorStatus) {
        this.indicatorStatus = indicatorStatus;
    }

    public String getMoveOutDate() {
        return moveOutDate;
    }

    public void setMoveOutDate(String moveOutDate) {
        this.moveOutDate = moveOutDate;
    }

    public String getRetrieveDate() {
        return retrieveDate;
    }

    public void setRetrieveDate(String retrieveDate) {
        this.retrieveDate = retrieveDate;
    }

    public String getIndicatorComment() {
        return indicatorComment;
    }

    public void setIndicatorComment(String indicatorComment) {
        this.indicatorComment = indicatorComment;
    }

    public Integer getNeedCounterGuarantor() {
        return needCounterGuarantor;
    }

    public void setNeedCounterGuarantor(Integer needCounterGuarantor) {
        this.needCounterGuarantor = needCounterGuarantor;
    }

    public void setReplaceStartCard(Integer replaceStartCard) {
        this.replaceStartCard = replaceStartCard;
    }

    public Integer getPrintNeedEarningProof() {
        return printNeedEarningProof;
    }

    public void setPrintNeedEarningProof(Integer printNeedEarningProof) {
        this.printNeedEarningProof = printNeedEarningProof;
    }

    public String getRepaymentAmountFirstMonth() {
        return repaymentAmountFirstMonth;
    }

    public void setRepaymentAmountFirstMonth(String repaymentAmountFirstMonth) {
        this.repaymentAmountFirstMonth = repaymentAmountFirstMonth;
    }

    public String getDealerEmployeeId() {
        return dealerEmployeeId;
    }

    public void setDealerEmployeeId(String dealerEmployeeId) {
        this.dealerEmployeeId = dealerEmployeeId;
    }

    /**
     * 收费明细列表，用编码标识不同的费用项目
     * 贷款服务费，担保服务费，风险押金 杂费
     */
    List<FeeValueBean> feeItemList = new ArrayList<FeeValueBean>();

    public List<FeeValueBean> getFeeItemList() {
        return feeItemList;
    }

    public void setFeeItemList(List<FeeValueBean> feeItemList) {
        this.feeItemList = feeItemList;
    }
}
