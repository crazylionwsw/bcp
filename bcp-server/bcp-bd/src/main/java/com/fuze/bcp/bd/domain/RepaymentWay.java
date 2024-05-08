package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * 还款方式定义
 * Created by sean on 2016/10/20.
 */
@Document(collection="bd_repaymentway")
@Data
public class RepaymentWay extends BaseDataEntity {

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



    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Map<Integer, Double> getRatioes() {
        return ratioes;
    }

    public void setRatioes(Map<Integer, Double> ratioes) {
        this.ratioes = ratioes;
    }
}
