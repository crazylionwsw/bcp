package com.fuze.bcp.api.creditcar.bean.appointswipingcard;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import lombok.Data;

/**
 * 预约刷卡单
 */
@Data
public class AppointSwipingCardBean extends APICarBillBean {
    /**
     * 预约刷卡状态：初始状态
     */
    public final static Integer APPOINTSWIPINGSTATUS_INIT = 0;
    /**
     * 预约刷卡状态：提交待确认
     */
    public final static Integer APPOINTSWIPINGSTATUS_SUBMIT = 1;
    /**
     * 预约刷卡状态：已确认
     */
    public final static Integer APPOINTSWIPINGSTATUS_CONFIRM = 2;
    /**
     *  预约刷卡状态
     */
    private Integer status = APPOINTSWIPINGSTATUS_INIT;

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
