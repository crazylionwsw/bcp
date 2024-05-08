package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

import java.util.List;

/**
 * 担保方式定义
 * Created by sean on 2016/10/20.
 */
@Data
@MongoEntity(entityName = "bd_guaranteeway")
public class GuaranteeWayBean extends APIBaseDataBean {

    /**
     * 是否需要担保函
     */
    private Boolean requiredGuaranteeLetter = false;

    /**
     * 是否需要抵押资料
     */
    private Boolean requiredPledgeFile = false;

    /**
     * 抵押资料列表
     */
    private List<String>  customerImageTypeIds;

}
