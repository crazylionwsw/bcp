package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentBean;
import com.fuze.bcp.bean.PayAccount;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 经销商还款单（一笔可以对应多个用户还款）
 * Created by sean on 2016/11/27.
 */
@Document(collection="so_dealer_repayment")
@Data
public class DealerRepayment extends BaseBillEntity {
    /**
     *  渠道还款状态
     */
    private Integer status = DealerRepaymentBean.DEALERREPAYMENTSTATUS_INIT;
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
