package com.fuze.bcp.api.creditcar.bean.paymentBill;

import com.fuze.bcp.api.bd.bean.FeeBillBean;
import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import com.fuze.bcp.bean.ReceiptAccount;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2018/3/5.
 */
@Data
public class PaymentBillBean extends APICarBillBean{


    /**
     * 缴费总额
     */
    private Double paymentAmount = 0.0;

    /**
     * 缴费时间
     */
    private String paymentTime;

    /**
     * 支付方式
     */
    private String paymentType;

    /**
     * 收款账户
     */
    private ReceiptAccount receiptAccount ;

    /**
     * 支付账户
     */
    private String paymentAccount ;

    /**
     * 手续费（商贷）
     */
    private Double chargeFee;

    /**
     * 不选中为0   选中为1
     * 默认不选中(只在手续费商贷使用)
     */
    private Integer chargeStatus = 0;

    /**
     * 缴费类型
     * 分为:购车缴费 解押缴费(CARPAY/PLEDGEPAY)
     */
    private String payContentType = "CARPAY";

    /**
     * 贷款服务费
     *
     */
    private Double loanServiceFee = 0.00;

    /**
     * 不选中为0   选中为1
     * 默认不选中(只在手续费商贷使用)
     */
    private Integer loanServiceStatus = 0;

    /**
     * 收费明细列表，用编码标识不同的费用项目
     * 贷款服务费，担保服务费，风险押金 杂费
     */
    List<FeeBillBean> feeItemList = new ArrayList<FeeBillBean>();

    @Override
    public String getBillTypeCode() {
        return "A025";
    }
}
