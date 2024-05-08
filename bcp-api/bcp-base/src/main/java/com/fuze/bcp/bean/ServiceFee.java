package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/31.
 */
@Data
public class ServiceFee implements Serializable {

    /**
     * 业务类型编码
     */
    private String businessType;

    /**
     * 利率表
     */
    private List<RateType> rateTypeList = new ArrayList<RateType>();
}
