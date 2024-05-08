package com.fuze.bcp.bd.domain;

import com.fuze.bcp.bean.RateType;
import lombok.Data;

/**
 * 贴息费率
 */
@Data
public class CompensatoryRate extends RateType {

    /**
     * 首付比例
     */
    private Double downPaymentRatio;

    /**
     * 贴息费率
     */
    private Double compensatoryRatio;

    /**
     * 借款人承担费率
     */
    private Double creditMasterRatio;

    /**
     * 借款人承担的费率
     */
    private Double customerRatio;

}
