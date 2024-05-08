package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 收费明细项
 * Created by sean on 2017/4/12.
 */
@Document(collection = "bd_feeitem")
@Data
public class FeeItem extends BaseDataEntity {

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
