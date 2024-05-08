package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.bd.bean.FeeBillBean;
import com.fuze.bcp.bean.ReceiptAccount;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 缴费单
 */
@Document(collection = "so_payment_bill")
@Data
public class PaymentBill extends BaseBillEntity{


    /**
     * 缴费总额
     */
    private Double paymentAmount;

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
     * 分为:购车缴费(CARPAY) 解押缴费(PLEDGEPAY)
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
