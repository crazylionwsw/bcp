package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.PadSalesRate;
import lombok.Data;

import java.util.List;

/**
 * Created by admin on 2017/7/31.
 */
@Data
public class PadSalesPolicyBean extends APIBaseDataBean {

    /**
     * 新车
     */
    public static final Integer DEF_FLAG_NWE_CAR = 0;
    /**
     * 旧车
     */
    public static final Integer DEF_FLAG_OLD_CAR = 1;
    /**
     * 全部
     */
    public static final Integer DEF_FLAG_ALL_CAR = 2;
    /**
     * 包含那种销售方式
     */
    private Integer flag = DEF_FLAG_NWE_CAR;


    /**
     * 适用地区
     */
    private List<String> provinces;


    /**
     * 利率列表
     */
    private List<PadSalesRate> salesRates;
}
