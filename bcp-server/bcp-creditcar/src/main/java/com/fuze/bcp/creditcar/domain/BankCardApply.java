package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行制卡登记
 * Created by sean on 2016/11/27.
 */
@Document(collection = "so_bankcard_apply")
@Data
public class BankCardApply extends BaseBillEntity {

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
    //TODO 暂时没用到
    private String getTime = null;

    /**
     * 领卡人(分期经理的员工ID)
     */
    //TODO 暂时没用到
    private String getManId = null;

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
     * 调额时间
     */
    private String changeAmountTime = null;

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

    private Integer status = BankCardApplyBean.BKSTATUS_INIT;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A011";
    }

}
