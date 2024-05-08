package com.fuze.bcp.api.creditcar.bean.appointpayment;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/2/5.
 */
@Data
public class AppointPaymentExcelBean extends CustomerTransactionBean{

    /**
     * 序号
     */
    private Integer indexNumber;

    /**
     * 报单支行
     */
    private String cashSource;

    /**
     * 渠道支行
     */
    private String cooperationCashSource;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 客户姓名[身份证号码]
     */
    private String customerInfo;

    /**
     * 分期经理[手机号码]
     */
    private String businessManInfo;

    /**
     * 还款账号
     */
    private String cardNo;

    /**
     * 垫资金额
     */
    private String appointPayAmount;

    /**
     * 垫资时间
     */
    private String payTime;

    /**
     * 贷款金额
     */
    private String creditAmount;

    /**
     * 期限
     */
    private Integer month;

    /**
     * 点位(银行手续费率)
     */
    private String rateType;

    /**
     * 是否分期
     */
    private String chargePaymentWay;

    /**
     * 应收基准银行手续费(银行手续费)
     */
    private String bankFeeAmount;

    /**
     * 应收超基准银行手续费（二手车含1、1.5、2年）
     * 贷款服务费
     */
    private String loanServiceFee;

    /**
     * 实收手续费（含代收）--[银行手续费]
     */
    private Double realityBankFeeAmount;

    /**
     * 实收贷款服务费
     */
    private Double reallyLoanServiceFee;

    /**
     * 分期贷款服务费
     */
    private String stagesLoanServiceFee;

    /**
     * 刷卡金额
     */
    private String swipingMoney;

    /**
     * 刷卡点位
     */
    private String swipingRate;

    /**
     * 刷卡时间
     */
    private String swipingTime;

    /**
     * 刷卡期数
     */
    private Integer swipingMonth;

    /**
     * 应还银行手续费
     */
    private String repayBankFeeMoney;

    /**
     * 客户首次还款额
     */
    private String repaymentAmountFirstMonth;

    /**
     * 客户每期还款额
     */
    private String repaymentAmountMonthly;

    /**
     * 客户每期还款额(保留整数)
     */
    private String repaymentAmountMonthlyRound;


    /**
     * 应还银行手续费-分期
     */
    private Double repayStagesBankFee;

    /**
     * 客户首次还款额
     */
    private String repaymentFeeFirstMonth;

    /**
     * 客户每期还款额
     */
    private String repaymentFeeMonthly;

    /**
     * 客户每期还款额(保留整数)
     */
    private String repaymentFeeMonthlyRound;

    /**
     * 代客户存手续费时间
     */
    private String replaceCustomerSaveFeeTime;

    /**
     * 代付手续费金额（刷卡
     */
    private String replaceCustomerAppointPayFeeMoney;




}
