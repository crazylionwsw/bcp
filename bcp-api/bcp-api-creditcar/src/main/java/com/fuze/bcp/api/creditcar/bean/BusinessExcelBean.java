package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/1/22.
 */
@Data
public class BusinessExcelBean extends CustomerTransactionBean{


    /**
     * 经销商名称
     */
    private String dealerName;

    /**
     * 借款人姓名
     */
    private String creditName;

    /**
     * 证件号码(借款人)
     */
    private String identifyNo;

    /**
     * (批贷日期)
     */
    private String approveTime;

    /**
     * 分期金额(贷款额度)
     */
    private String creditAmount;

    /**
     * 分期期数
     */
    private Integer month;

    /**
     * 垫资支付日期
     */
    private String payTime;

    /**
     * 刷卡日期
     */
    private String swipingCardTime;

    /**
     * 汽车型号
     */
    private String carModelNumber;

    /**
     * 汽车销售价格(开票价)
     */
    private String receiptPrice;

    /**
     * 贷款服务费
     */
    private String loanServiceFee;

    /**
     * 垫资服务费
     */
    private String appointServiceFee;

    /**
     * 交易卡号(银行卡号)
     */
    private String cardNo;

    /**
     * 渠道经理
     */
    private String employeeName;

    /**
     * 分期经理
     */
    private String businessName;

    /**
     * 业务类型
     */
    private String businessType;


}
