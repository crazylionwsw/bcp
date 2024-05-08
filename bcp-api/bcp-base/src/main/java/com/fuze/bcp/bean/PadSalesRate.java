package com.fuze.bcp.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/31.
 */
@Data
public class PadSalesRate implements Serializable {

    /**
     * 业务类型ID
     */
    private String businessTypeId;


    /**
     * 业务类型ID
     */
    private String businessType;


    /**
     * 业务类型ID
     */
    private String businessTypeName;


    /**
     * 利率表
     */
    private List<RateType> rateTypeList;
}
