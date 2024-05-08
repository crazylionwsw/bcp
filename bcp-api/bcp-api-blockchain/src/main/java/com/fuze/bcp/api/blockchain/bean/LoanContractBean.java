package com.fuze.bcp.api.blockchain.bean;

import lombok.Data;

/**
 * Created by Lily on 2018/4/20.
 * 交易上链信息
 */
@Data
public class LoanContractBean {

    /**
     * HASH值
     */
    private String HASH ;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 身份证号
     */
    private String identifyNo;

    /**
     * 贷款金额
     */
    private Double creditAmount;

    /**
     * 放款时间(刷卡时间)
     */
    private String loanDate;

    /**
     * 抵押时间
     */
    private String dmvpledgedata;

}
