package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行账户信息
 */
@Data
public class PayAccount implements Serializable {

    /**
     * 个人账户
     */
    public static final Integer ACCOUNTTYPE_PERSONAL = 0;

    /**
     * 公司账户
     */
    public static final Integer ACCOUNTTYPE_PUBLIC = 1;

    /**
     * 收付款账户
     */
    public static final Integer ACCOUTNWAY_ALL = 0;

    /**
     * 付款账户
     */
    public static final Integer ACCOUNTWAY_PAYMENT = 1;

    /**
     * 收款账户
     */
    public static final Integer ACCOUNTWAY_RECIEVED = 2;

    /**
     * 默认账户0
     */
    public static final Integer ACCOUTCHECK_ALL = 0;

    /**
     * 非默认账户1
     */
    public static final Integer ACCOUTCHECK_NALL = 1;

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

    /**
     * 帐号类型
     */
    private Integer accountType = PayAccount.ACCOUNTTYPE_PUBLIC;

    /**
     * 账号用途
     */
    private Integer accountWay = PayAccount.ACCOUTNWAY_ALL;

    /**
     * 默认账户?
     */
    private Integer defaultAccount = PayAccount.ACCOUTCHECK_ALL;

}
