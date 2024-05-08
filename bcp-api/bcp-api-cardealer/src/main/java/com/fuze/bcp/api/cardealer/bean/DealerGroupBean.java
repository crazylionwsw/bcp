package com.fuze.bcp.api.cardealer.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.Map;

/**
 * 渠道分组（集团）
 */
@Data
@MongoEntity(entityName = "bd_cardealer_group")
public class DealerGroupBean extends APIBaseDataBean {

    /**
     * 分成比例
     */
    private Map<Integer, Double> sharingRatio = null;


}