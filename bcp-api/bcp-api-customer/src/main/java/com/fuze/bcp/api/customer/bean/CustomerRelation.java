package com.fuze.bcp.api.customer.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zqw on 2017/12/18.
 */
@Data
public class CustomerRelation implements Serializable{

    //  贷款人
    public static String RELATION_SELF = "0";

    //  配偶
    public static String RELATION_MATE = "1";

    //  指标人
    public static String RELATION_PLEDGE = "2";

    private String customerId;

    private String relation;
}
