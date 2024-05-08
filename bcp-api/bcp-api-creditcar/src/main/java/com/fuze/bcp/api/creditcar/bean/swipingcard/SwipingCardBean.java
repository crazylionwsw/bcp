package com.fuze.bcp.api.creditcar.bean.swipingcard;

import com.fuze.bcp.api.creditcar.bean.APICarBillBean;
import lombok.Data;

/**
 * 刷卡单（包含受托支付和4s店贴息刷卡）
 */
@Data
public class SwipingCardBean extends APICarBillBean {
    /**
     * 渠道刷卡状态：初始状态
     */
    public final static Integer SWIPINGSTATUS_INIT = 0;
    /**
     * 渠道刷卡状态：提交待确认
     */
    public final static Integer SWIPINGSTATUS_SUBMIT = 1;
    /**
     * 渠道刷卡状态：已确认
     */
    public final static Integer SWIPINGSTATUS_CONFIRM = 2;
    /**
     *  渠道刷卡状态
     */
    private Integer status = SWIPINGSTATUS_INIT;

    /**
     * 刷卡时间
     */
    private String payTime;

    /**
     * 刷卡金额
     */
    private Double payAmount;

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 受托支付，贴息4S店刷卡
     * TODO  增加常量定义
     */
    private Integer checkType =  0;

    /**
     * 还款信息
     */
    private String customerRepaymentId = null;

    /**
     * 子类需要定义单据类型信息
     *
     * @return
     */
    public String getBillTypeCode() {
        return "A019";
    }

}