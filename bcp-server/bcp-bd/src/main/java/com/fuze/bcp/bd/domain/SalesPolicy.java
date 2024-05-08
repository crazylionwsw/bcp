package com.fuze.bcp.bd.domain;

import com.fuze.bcp.bean.SalesRate;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by admin on 2017/7/31.
 */
@Document(collection="bd_sales_policy")
@Data
public class SalesPolicy extends BaseDataEntity {

    /**
     * 适用地区
     */
    private List<String> provinces;

    /**
     * 利率列表
     */
    private List<SalesRate> salesRates;
}
