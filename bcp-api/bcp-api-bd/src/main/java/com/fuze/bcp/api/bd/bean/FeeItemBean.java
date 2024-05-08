package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;
import lombok.Data;

/**
 * 收费明细项
 */
@Data
@MongoEntity(entityName = "bd_feeitem")
public class FeeItemBean extends APIBaseDataBean {

    /**
     * 代收
     */
    public static final Integer OWNER_OUTER = 1;

    /**
     * 自收
     */
    public static final Integer OWNER_SELF = 0;

    /**
     * 费用计算公式
     */
    private String formula = null;

    /**
     * 费用
     */
    private Double fee = 0.0;

    /**
     * 小数点位数
     */
    private Integer decimalPoint = 0;

    /**
     * 收费方
     */
    private Integer feeOwner = OWNER_SELF;
}
