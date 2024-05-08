package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 还款方式定义
 */
@Data
@MongoEntity(entityName = "bd_repaymentway")
public class RepaymentWayBean extends APIBaseDataBean {

    /**
     * 期数(期）
     */
    private Integer months = 12;

    /**
     * 还款次数（月)
     */
    private Integer count = 12;

    /**
     * 还款比例列表
     */
    private Map<Integer, Double> ratioes = new HashMap<Integer, Double>();
}
