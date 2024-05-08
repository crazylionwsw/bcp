package com.fuze.bcp.api.creditcar.bean.customerfeebill;

import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.bean.APIBaseBillBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
@Data
public class CustomerFeeBean extends APIBaseBillBean {
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
     * 收款银行
     */
    private String collectionAgency;

    /**
     * 收款帐户
     */
    private String receiptAccount;

    /**
     * 收款人
     */
    private String receiver;

    /**
     * 付款帐户
     */
    private String paymentAccount;

    /**
     * 付款人
     */
    private String drawee;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A025";
    }

}
