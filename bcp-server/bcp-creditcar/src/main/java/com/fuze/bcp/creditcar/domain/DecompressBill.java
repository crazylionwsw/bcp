package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.bd.bean.FeeBillBean;
import com.fuze.bcp.bean.ReceiptAccount;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/7.
 * 解押管理
 */
@Document(collection = "so_decompress_bill")
@Data
public class DecompressBill extends BaseBillEntity{

    /**
     *  解押状态
     */
    private Integer status = 0;

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
