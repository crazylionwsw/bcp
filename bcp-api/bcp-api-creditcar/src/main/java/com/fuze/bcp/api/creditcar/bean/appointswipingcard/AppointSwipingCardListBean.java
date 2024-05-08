package com.fuze.bcp.api.creditcar.bean.appointswipingcard;

import com.fuze.bcp.api.creditcar.bean.APIBillListBean;
import lombok.Data;

/**
 * 预约刷卡单列表
 */
@Data
public class AppointSwipingCardListBean extends APIBillListBean {

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


}
