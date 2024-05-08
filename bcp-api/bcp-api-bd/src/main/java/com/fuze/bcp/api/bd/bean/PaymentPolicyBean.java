package com.fuze.bcp.api.bd.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Lily on 2017/10/26.
 */
@Data
public class PaymentPolicyBean implements Serializable {
    /**
     * 贴息不垫资 ：0
     * 贴息垫资 ：1
     */
    private Integer discount = 0;

    /**
     * 商贷不垫资 ：0
     * 商贷垫资 ：1
     */
    private Integer business = 1;
}
