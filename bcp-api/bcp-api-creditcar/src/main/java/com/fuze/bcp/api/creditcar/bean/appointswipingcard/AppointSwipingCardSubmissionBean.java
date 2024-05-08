package com.fuze.bcp.api.creditcar.bean.appointswipingcard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuze.bcp.api.creditcar.bean.BillSubmissionBean;
import lombok.Data;

/**
 * 提交预约刷卡单
 * Created by Lily on 2017/9/13.
 */
@Data
public class AppointSwipingCardSubmissionBean extends BillSubmissionBean {
    /**
     * 提车日期
     */
    private String pickupDate = null;

    /**
     * 刷卡时间 ，支付截止日期
     */
    @JsonProperty("swipingDeadline")
    private String appointPayTime = null;

    /**
     * 预约领卡时间
     */
    @JsonProperty("takeCardDate")
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

}
