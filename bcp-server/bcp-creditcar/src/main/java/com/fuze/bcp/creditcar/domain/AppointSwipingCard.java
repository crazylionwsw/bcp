package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 预约刷卡单
 */
@Document(collection = "so_appoint_swipingcard")
@Data
public class AppointSwipingCard extends BaseBillEntity {
    /**
     *  预约刷卡状态
     */
    private Integer status = 0;

    /**
     * 提车日期
     */
    private String pickupDate = null;

    /**
     * 刷卡时间 ，支付截止日期
     */
    private String appointPayTime = null;

    /**
     * 预约领卡时间
     */
    private String appointTakeTime = null;

    /**
     * 预约刷卡金额
     */
    private Double appointPayAmount = null;

    /**
     * 是否需要垫资
     * 0. 否
     * 1. 是
     */
    private Integer isNeedLoaning  = 0;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A026";
    }
}
