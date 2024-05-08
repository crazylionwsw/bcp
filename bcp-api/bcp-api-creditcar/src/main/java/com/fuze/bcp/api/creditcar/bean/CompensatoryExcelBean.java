package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/1/19.
 */
@Data
public class CompensatoryExcelBean extends CustomerTransactionBean {

    /**
     * 经销商名称
     */
    private String carDealerName = null;

    /**
     * 序号
     */
    private int indexNumber;

    /**
     * 姓名(指标人)
     */
    private String pledgeCustomerName;

    /**
     * 申请人
     */
    private String applyName;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号码(指标人身份证号码)
     */
    private String identifyNo;

    /**
     * 交易卡号(银行卡号)
     */
    private String cardNo;


    /**
     * 交易检索号
     */
    private String transactionAccessionNo;

    /**
     * 产品名称
     */
    private String creditProductName;

    /**
     * 车架号
     */
    private String vin;

    /**
     * DLR代码
     */
    private String dlrCode;

    /**
     * 申请日期(批贷日期)
     */
    private String approveTime;

    /**
     * 汽车型号
     */
    private String carModelNumber;

    /**
     * 汽车销售价格(开票价)
     */
    private String receiptPrice;

    /**
     * 首付金额
     */
    private String downPayment;

    /**
     * 分期金额(贷款额度)
     */
    private String creditAmount;

    /**
     * 分期期数
     */
    private Integer month;

    /**
     * 备注
     */
    private String commentInfo;








}
