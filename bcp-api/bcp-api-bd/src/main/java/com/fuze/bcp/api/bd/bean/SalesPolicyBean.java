package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.SalesRate;
import lombok.Data;

import java.util.List;

/**
 * Created by admin on 2017/7/31.
 */
@Data
@MongoEntity(entityName = "bd_sales_policy")
public class SalesPolicyBean extends APIBaseDataBean {

    /**
     * 适用地区
     */
    private List<String> provinces;


    /**
     * 利率列表
     */
    private List<SalesRate> salesRates;
}
