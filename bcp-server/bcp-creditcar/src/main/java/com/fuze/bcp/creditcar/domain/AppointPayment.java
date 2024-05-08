package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.bean.PayAccount;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 垫资申请单
 */
@Document(collection = "so_appoint_payment")
@Data
public class AppointPayment extends BaseBillEntity {

    /**
     *  垫资状态
     */
    private Integer status = 0;

    /**
     * 提车日期
     */
    private String pickupDate = null;

    /**
     * 刷卡时间 ，支付截止日期
     */
    private String appointPayTime = null;

    /**
     * 预计上牌日期
     */
    private String registryDate = null;

    /**
     * * 垫资支付时间类型 新车(1 开发票前垫资，2 开发票后垫资)
     * 二手车（1 表示出交易票前垫资，2 表示出交易票后垫资）
     */
    private Integer advancedPay = 2;

    /**
     * 预约垫资金额
     */
    private Double appointPayAmount = 0.0;

    /**
     * 垫资完成时间
     */
    private String payTime = null;

    /**
     *  支付方式
     *  0   :   差额支付
     *  1   :   全额支付
     */
    private Integer chargeParty = 0;

    /**
     * 收款账户
     */
    private PayAccount payAccount = null;

    /**
     * 垫资项(名称)
     * 贴息金额：0
     * 贷款金额：1
     * 贴息额+贷款额：2
     */
    private String paymentType = "1";

    /**
     * 是否贴息
     * 0. 否
     * 1. 是
     */
    private Integer needCompensatory = 0;

    /**
     * 是否需要垫资
     * 0. 否
     * 1. 是
     */
    private Integer isNeedLoaning  = 0;

    /**
     * 贴息金额
     */
    private Double compensatoryAmount = 0.0;

    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A004";
    }
}
