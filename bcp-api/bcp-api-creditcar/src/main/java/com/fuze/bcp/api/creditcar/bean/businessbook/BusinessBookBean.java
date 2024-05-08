package com.fuze.bcp.api.creditcar.bean.businessbook;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import lombok.Data;

/**
 * Created by Lily on 2017/12/7.
 */
@Data
public class BusinessBookBean extends CustomerTransactionBean {

    /**
     * 序号
     */
    private int indexNumber;

    /**
     * 客户姓名
     */
    private String customerName = null;

    /**
     * 身份证号码
     */
    private String identifyNo = null;

    /**
     * 联系方式
     */
    private String cells = null;

    /**
     * 拟购车型(（品牌名+车系名+车型名）)
     */
    private String carTypeFullName = null;

    /**
     * 车价(分期申请车价)
     */
    private String applyAmount = null;

    /**
     * 4S店
     */
    private String cadelarName = null;

    /**
     * 首付比例
     */
    private String downPaymentRatio = null;

    /**
     * 新/二手车
     */
    private String businessType = null;

    /**
     * 征信查询时间
     */
    private String creditQueryTime = null;

    /**
     * 客户不做原因(取消业务原因)
     */
    private String cancelReason = null;

    /**
     * 报审时间
     */
    private String reportTime = null;

    /**
     * 批复时间
     */
    private String replyTime = null;

    /**
     * 申请金额(贷款额度)
     */
    private String creditAmount = null;

    /**
     * 批复金额
     */
    private String approvedCreditAmount = null;

    /**
     * 实际贷款金额(刷卡金额)
     */
    private String  swipingAmount = null;

    /**
     * 贷款期限
     */
    private String  months = null;

    /**
     * 手续费分期(是或否)
     */
    private String chargePaymentWay = null;

    /**
     * 首期还款日
     */
    private String firstRepaymentDate = null;

    /**
     * 是否贴息
     * 0：否   1：是
     */
    private String compensatoryInterest = null;

    /**
     * 手续费(银行手续费)
     */
    private Double bankFeeAmount = null;

    /**
     * 领卡时间
     */
    private String receiveCardTime = null;

    /**
     * 卡号
     */
    private String cardNo = null;

    /**
     * 垫资时间
     */
    private String payTime = null;

    /**
     * 垫资金额
     */
    private Double appointPayAmount = null;

    /**
     * 刷卡时间
     */
    private String swipingCardTime = null;

    /**
     * 刷卡金额
     */
    private Double payAmount = null;

    /**
     * 刷卡地点
     */
    private String swipingAddress = null;

    /**
     * 抵押登记时间
     */
    private String pledgeEndTime = null;

    /**
     * 抵押人
     */
    private String pledgeCustomerName = null;

    /**
     * 与申请人的关系(贷款主体与客户之间关系)
     */
    private String relation = null;

    /**
     * 车架号（车辆VIN码，终身唯一）
     */
    private String vin = null;

    /**
     * 发动机号
     */
    private String motorNumber = null;

    /**
     * 车牌号码（过户会发生变化）
     */
    private String licenseNumber = null;

    /**
     * 车辆登记证编号
     */
    private String registryNumber = null;

    /**
     * 银行归档时间
     */
    private String bankCompleteTime = null;

    /**
     * 客户交接时间
     */
    private String customerHandoverTime = null;

    /**
     * 渠道经理
     */
    private String employeeName = null;

    /**
     * 分期经理
     */
    private  String businessManName = null;

    /**
     * 审查人
     */
    private  String reviewName = null;



    private String commentInfo = null;




}
