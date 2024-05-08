package com.fuze.bcp.api.creditcar.bean.customerfeebill;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.bean.APIBaseBillBean;
import com.fuze.bcp.bean.PayAccount;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqw on 2017/9/27.
 */
@Data
public class CustomerFeeBillBean extends APIBaseBillBean {

    /**
     * 收费明细列表，用编码标识不同的费用项目
     * 手续费，担保服务费，风险押金
     */
    List<FeeValueBean> feeItemList = new ArrayList<FeeValueBean>();

    /**
     * 总费用/汇款金额
     */
    private Double totalFee = 0.0;

    /**
     * 贷款服务费
     */
    private Double loanServiceFee = 0.0;

    /**
     * 汇款日期
     */
    private String paymentDate;

    /**
     * 交易方式   刷卡，现金，网银转账，微信支付，支付宝支付
     */
    private String tradeWay = null;

    /**
     * 第三方交易ID
     */
    private String thirdTradeId = null;

    /**
     * 收款账户
     */
    private PayAccount receiveAccount= null;

    /**
     * 付款帐户
     */
    private PayAccount payAccount= null;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A025";
    }

}
