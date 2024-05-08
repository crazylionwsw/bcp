package com.fuze.bcp.api.creditcar.bean.appointswipingcard;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by Lily on 2018/1/4.
 */
@Data
public class AppAppointSwipingCardBean extends APIBaseBean {
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
}
