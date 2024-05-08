package com.fuze.bcp.api.creditcar.bean.dealerrepayment;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import com.fuze.bcp.bean.PayAccount;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/9/15.
 */
@Data
public class DealerRepaymentBean extends APICarBillBean {
    /**
     * 渠道还款状态：初始状态
     */
    public final static Integer DEALERREPAYMENTSTATUS_INIT = 0;
    /**
     * 渠道还款状态：提交待确认
     */
    public final static Integer DEALERREPAYMENTSTATUS_SUBMIT = 1;
    /**
     * 渠道还款状态：已确认
     */
    public final static Integer DEALERREPAYMENTSTATUS_CONFIRM = 2;
    /**
     *  渠道还款状态
     */
    private Integer status = DEALERREPAYMENTSTATUS_INIT;
    /**
     * 还款时间
     */
    private String  repaymentTime = null;

    /**
     * 还款金额
     */
    private Double  amount = null;

    /**
     * 收款账户
     */
    private PayAccount payAccount = null;

    /**
     * 交易方式:刷卡，现金，网银转账，微信支付，支付宝支付
     */
    private String tradeWay = null;

    /**
     * 第三方交易ID
     */
    private String thirdTradeId = null;

    /**
     * 该还款对应的交易ID
     */
    private List<String> customerTransactionIds = new ArrayList<String>();;


    /**
     * 子类需要定义单据类型信息
     * @return
     */
    public String getBillTypeCode() {
        return "A020";
    }
}
