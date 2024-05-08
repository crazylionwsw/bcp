package com.fuze.bcp.api.creditcar.bean.bankcardapply;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * app端卡业务列表页面
 * Created by Lily on 2017/12/25.
 */
@Data
public class BankCardListBean extends APIBaseBean {
    /**
     * 客户交易ID
     */
    private String customerTransactionId;

    /**
     *客户姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String identifyNo;

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
     * 卡业务状态
     */
    private int status;
}
