package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.BusinessRate;
import com.fuze.bcp.bean.RateType;
import lombok.Data;

import java.util.*;

/**
 * 业务类型定义
 * Created by sean on 2016/10/20.
 */
@Data
@MongoEntity(entityName = "bd_businesstype")
public class BusinessTypeBean extends APIBaseDataBean {
    /**
     * 业务类型常量
     */
    public static final String BUSINESSTYPE_NC = "NC";
    public static final String BUSINESSTYPE_OC = "OC";

    /**
     * 期数及对应的利率表
     * String  资金利率的ID
     *
     */
    private List<BusinessRate> rateTypes;

    private Map<String, List<RateType>> feeItems = new HashMap<String, List<RateType>>();

    /**
     * 指定收费项目
     */
    private List<String> feeItemIds = new ArrayList<String>();


    /**
     * 该业务需要的档案类型
     */
    private List<String>  customerImageTypeCodes = new ArrayList<String>();

    /**
     * 必须快递的资料列表 （编码)
     */
    private List<String> requiredExpImageTypeCodes;

    /**
     * 建议快递的资料列表（编码)
     */
    private List<String> suggestedExpImageTypeCodes;

    /**
     * 业务类型所包含的单据类型
     */
    private List<String> billTypeCodes = new ArrayList<String>();

}
