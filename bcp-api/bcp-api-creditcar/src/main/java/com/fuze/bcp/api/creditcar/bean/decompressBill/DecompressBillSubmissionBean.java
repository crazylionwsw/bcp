package com.fuze.bcp.api.creditcar.bean.decompressBill;

import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import com.fuze.bcp.bean.ReceiptAccount;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/3/7.
 * 解押管理
 */
@Data
public class DecompressBillSubmissionBean extends BillSubmissionBean{

    /**
     * 解押总额
     */
    private Double decompressAmount = 0.0;

    /**
     * 解押时间
     */
    private String decompressTime;

    /**
     * 解押类型
     * NORMALPAYMENT:正常解押
     * BEFOREPAYMENT:提前还款
     */
    private String decompressType;

    /**
     * 收款账户
     */
    private ReceiptAccount receiptAccount ;

    /**
     * 支付账户
     */
    private String paymentAccount ;

    /**
     * 支付方式
     */
    private String paymentType;



}
