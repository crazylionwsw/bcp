package com.fuze.bcp.bd.domain;

import com.fuze.bcp.bean.BusinessRate;
import com.fuze.bcp.bean.RateType;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务类型定义
 * Created by sean on 2016/10/20.
 */
@Document(collection="bd_businesstype")
@Data
public class BusinessType extends BaseDataEntity {

    /**
     * 期数及对应的利率表
     * String  资金利率ID
     *
     */
    private List<BusinessRate> rateTypes;

    private Map<String, List<RateType>> feeItems = new HashMap<String, List<RateType>>();

    /**
     * 指定收费项目
     */
    private List<String> feeItemIds = new ArrayList<String>();


    /**
     * 该业务需要的档案类型 (编码)
     */
    private List<String>  customerImageTypeCodes = new ArrayList<String>();

    /**
     * 必须快递的资料列表 （编码)
     */
    private List<String> requiredExpImageTypeCodes = new ArrayList<String>();


    /**
     * 建议快递的资料列表（编码)
     */
    private List<String> suggestedExpImageTypeCodes = new ArrayList<String>();

    /**
     * 业务类型所包含的单据类型(编码)
     */
    private List<String>  billTypeCodes = new ArrayList<String>();

}
