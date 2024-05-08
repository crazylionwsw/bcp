package com.fuze.bcp.api.ocr.bean;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.fuze.bcp.bean.APIBaseBean;

/**
 * 银行卡信息
 * Created by sean on 2017/6/8.
 */
public class BankCardInfo extends APIBaseBean {

    /**
     * VISA卡
     */
    public final static int CARDTYPE_VISA = 1;

    /**
     * 万事达卡
     */
    public final static int CARDTYPE_MASTERCARD = 2;

    /**
     * 银联卡
     */
    public final static int CARDTYPE_UNIONPAY = 3;


    /**
     * 未知卡类型
     */
    public final static int CARDTYPE_UNKNOWN = 0;


    /**
     * 卡号
     */
    private String cardNo = null;

    /**
     *  三位的CVV号码
     */
    private String cvv = null;

    /**
     * 有效期
     */
    private String yyyyMM = null;

    /**
     * 发卡银行
     */
    private String bankname = null;

    /**
     * 用户名拼音
     */
    private String userNameSpell = null;


}
