package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/31.
 */
@Data
public class SalesRate implements Serializable {

    /**
     * 业务类型编码
     */
    private String businessTypeCode;

    /**
     * 利率表
     */
    private List<BusinessRate> rateTypeList = new ArrayList<BusinessRate>();
}
