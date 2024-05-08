package com.fuze.bcp.api.creditcar.bean.decompressBill;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import com.fuze.bcp.bean.ReceiptAccount;
import lombok.Data;

/**
 * Created by ${Liu} on 2018/3/7.
 * 解押管理
 */
@Data
public class DecompressBillBean extends APICarBillBean{

    /**
     * 解押状态：初始状态
     */
    public final static Integer DECOMPRESSSTATUS_INIT = 0;
    /**
     * 解押状态：提交待确认
     */
    public final static Integer DECOMPRESSSTATUS_SUBMIT = 1;
    /**
     * 解押状态：已确认
     */
    public final static Integer DECOMPRESSSTATUS_CONFIRM = 2;
    /**
     *  解押状态
     */
    private Integer status = DECOMPRESSSTATUS_INIT;

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

    @Override
    public String getBillTypeCode() {
        return "A031";
    }
}
