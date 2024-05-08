package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 刷卡单（包含受托支付和4s店贴息刷卡）
 * Created by sean on 2016/11/27.
 */
@Document(collection="so_swiping_card")
@Data
public class SwipingCard extends BaseBillEntity {
    /**
     *  渠道刷卡状态
     */
    private Integer status = 0;

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 刷卡金额
     */
    private Double payAmount;

    /**
     * 刷卡时间
     */
    private String payTime;

    /**
     * 受托支付，贴息4S店刷卡
     * TODO  增加常量定义
     */
    private Integer checkType =  0;


    //其它信息保存到CustomerRepayment

    /**
     * 单据类型
     * @return
     */
    public String getBillTypeCode() {
        return "A019";
    }

}