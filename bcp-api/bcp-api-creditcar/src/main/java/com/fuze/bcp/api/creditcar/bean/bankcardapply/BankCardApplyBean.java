package com.fuze.bcp.api.creditcar.bean.bankcardapply;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 制卡单
 */
@Data
public class BankCardApplyBean extends APICarBillBean {
    /**
     * 卡业务状态：初始状态
     */
    public final static Integer BKSTATUS_INIT = 0;
    /**
     * 卡业务状态：已制卡
     */
    public final static Integer BKSTATUS_APPLY = 1;
    /**
     * 卡业务状态：已取卡
     */
    public final static Integer BKSTATUS_TAKE = 2;
    /**
     * 卡业务状态：已启卡/已代启卡
     */
    public final static Integer BKSTATUS_ACTIVATE = 3;
    /**
     * 卡业务状态：代启卡失败
     */
    public final static Integer BKSTATUS_ACTIVATEFAILED = 99;
    /**
     * 卡业务状态：已调额
     */
    public final static Integer BKSTATUS_CHANGEAMOUN = 4;
    /**
     * 卡业务状态：4S店已刷卡/代刷卡
     */
    public final static Integer BKSTATUS_SWIPING = 5;
    /**
     * 卡业务状态：领卡
     */
    public final static Integer BKSTATUS_GET = 6;
    /**
     * 卡业务状态：销卡
     */
    public final static Integer BKSTATUS_CANCEL = 9;

    /**
     * 银行申请制卡时间
     */
    private String applyTime = null;

    /**
     * 分行取卡时间
     */
    private String takeTime = null;

    /**
     * 启卡时间
     */
    private String activateTime = null;

    /**
     * 代启卡时间
     */
    private String replaceActivateTime = null;

    /**
     * 代启卡人
     */
    private String replaceActivateName = null;

    /**
     * 刷卡金额
     */
    private Double swipingMoney = null;

    /**
     * 刷卡期数
     */
    private Integer swipingPeriods = null;

    /**
     * 领卡时间(分期经理)
     */
    private String getTime = null;

    /**
     * 领卡人(分期经理的员工ID)
     */
    private String getManId = null;

    /**
     * 调额时间
     */
    private String changeAmountTime = null;

    /**
     * 渠道领卡时间
     */
    private String receiveDiscountTime = null;
    /**
     * 渠道刷卡时间
     */
    private String swipingShopTime = null;
    /**
     * 代刷卡时间
     */
    private String swipingTrusteeTime = null;
    /**
     * 领卡时间
     */
    private String receiveTrusteeTime = null;

    /**
     * 销卡时间
     */
    private String cancelCardTime = null;

    /**
     * 刷卡人
     */
    private String swipingName = null;

    /**
     * 领卡人
     */
    private String receiveCardName = null;

    /**
     * 首次还款日(YYYY-MM-DD)
     */
    private String firstReimbursement = null;

    /**
     * 还款日 默认为25号
     */
    private Integer  defaultReimbursement = null;

    /**
     * 账单日 默认为25号
     */
    private Integer  billingDate = null;

    /**
     * 存储完成的动作
     */
    private List<CardActionRecord> actionRecords = new ArrayList<CardActionRecord>();

    /**
     * 卡业务状态
     */
    private Integer status = BKSTATUS_INIT;

    /**
     * 客户借款信息
     */
    private CustomerLoanBean customerLoanBean;

    /**
     * 客户卡的信息
     */
    private CustomerCardBean customerCard;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A011";
    }

}
