package com.fuze.bcp.api.creditcar.bean.appointpayment;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import com.fuze.bcp.bean.PayAccount;
import lombok.Data;

/**
 * 预约垫资
 */
@Data
public class AppointPaymentBean extends APICarBillBean {
    /**
     * 垫资状态：初始状态
     */
    public final static Integer APPOINTPAYMENTSTATUS_INIT = 0;
    /**
     * 垫资状态：已提交
     */
    public final static Integer APPOINTPAYMENTSTATUS_SUBMIT = 1;
    /**
     * 垫资状态：部门已审批
     */
    public final static Integer APPOINTPAYMENTSTATUS_DEPARTMENT = 2;
    /**
     * 垫资状态：财务已核对
     */
    public final static Integer APPOINTPAYMENTSTATUS_FINANCECHECK = 3;
    /**
     * 垫资状态：风控已审批
     */
    public final static Integer APPOINTPAYMENTSTATUS_Approval = 4;
    /**
     * 垫资状态：财务已支付
     */
    public final static Integer APPOINTPAYMENTSTATUS_Pay = 5;
    /**
     *  垫资状态
     */
    private Integer status = APPOINTPAYMENTSTATUS_INIT;

    /**
     * 贴息项目
     */
    public final static String PAYMENT_DISCOUNTITEMS = "0";

    /**
     * 垫资项目
     */
    public final static String PAYMENT_LOANINGITEMS = "1";

    /**
     * 贴息垫资项
     */
    public final static String PAYMENT_DISCOUNTANDLOANINGITEMS = "2";

    /**
     * 提车日期
     */
    String pickupDate = null;

    /**
     * 刷卡时间 ，支付截止日期
     */
    private String appointPayTime = null;

    /**
     * 垫资完成时间
     */
    private String payTime = null;

    /**
     * 预计上牌日期
     */
    private String registryDate = null;

    /**
     * 垫资支付时间类型新车(1 开发票前垫资，2 开发票后垫资)
     * 二手车（1 表示出交易票前垫资，2 表示出交易票后垫资）
     */
    /**
     * 是否需要提前支付（新车：开票前支付，二手车：转移过户前支付）
     */
    private Integer advancedPay = 2;

    /**
     * 预约垫资金额
     */
    private Double appointPayAmount = 0.0;

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
     * 贴息额：0
     * 贷款额：1
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
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A004";
    }
}
