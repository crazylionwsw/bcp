package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.MongoBaseEntity;

/**
 * 首付比例
 */
public class DownPaymentRate extends MongoBaseEntity {

    /**
     * 业务类型
     */
    private String businessTypeId;

    /**
     * 动力类型
     */
    private String motiveTypeId;

    /**
     * 首付比例 (下限)
     */
    private Double minDownPaymentRatio;

    /**
     * 首付比例 (上限)
     */
    private Double maxDownPaymentRatio;

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getMotiveTypeId() {
        return motiveTypeId;
    }

    public void setMotiveTypeId(String motiveTypeId) {
        this.motiveTypeId = motiveTypeId;
    }

    public Double getMinDownPaymentRatio() {
        return minDownPaymentRatio;
    }

    public void setMinDownPaymentRatio(Double minDownPaymentRatio) {
        this.minDownPaymentRatio = minDownPaymentRatio;
    }

    public Double getMaxDownPaymentRatio() {
        return maxDownPaymentRatio;
    }

    public void setMaxDownPaymentRatio(Double maxDownPaymentRatio) {
        this.maxDownPaymentRatio = maxDownPaymentRatio;
    }
}
