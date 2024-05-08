package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by ${Liu} on 2018/3/8.
 */
@Data
public class ReceiptAccount implements Serializable{

    /**
     * 开户名称
     */
    private String accountName = null;

    /**
     * 银行名称
     */
    private String bankName = null;

    /**
     * 银行账号
     */
    private String accountNumber = null;
}
