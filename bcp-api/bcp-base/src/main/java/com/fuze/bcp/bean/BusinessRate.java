package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/31.
 */
@Data
public class BusinessRate implements Serializable {

    public final static String DEF_SOURCE_RATE_ID = "58ac71bc2d36325a48db6fa3";

    public final static String DEF_SOURCE_RATE_ID_2 = "599526d5e4b032acaa7209c9";
    /**
     * 资金来源
     * 目前仅有一渠道，给默认值
     */
    private String sourceRateId;

    /**
     * 利率表
     */
    private List<RateType> rateTypeList;
}
