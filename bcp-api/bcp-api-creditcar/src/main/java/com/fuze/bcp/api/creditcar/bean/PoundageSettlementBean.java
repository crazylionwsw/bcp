package com.fuze.bcp.api.creditcar.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * Created by zqw on 2017/8/29.
 * 手续费支行结算
 */
@Data
public class PoundageSettlementBean extends APIBaseBean{

    /*
     * 业务初始状态
     */
    public final static int STATUS_INIT = 0;

    /**
     * 业务数据正常状态
     */
    public final static int STATUS_NORMAL = 1;

    /**
     * 业务错误状态
     */
    public final static int STATUS_ERROR = 9;

    /**
     *      未生效
     */
    public final static Integer EFFECT_NONE = 0;

    /**
     *      生效
     */
    public final static Integer EFFECT_DOING = 1;

    private int status = STATUS_INIT;

    /**
     *  交易ID
     */
    private String customerTransactionId;

    /**
     * 客户id
     */
    private String customerId;

    /**
     *      贷款额
     */
    private Double limitAmount = 0.0;

    /**
     *      贷款月数
     */
    private Integer creditMonths;

    /**
     * 手续费
     */
    private Double poundage = 0.0;

    /**
     * 手续费缴纳方式      --      默认从客户签约数据中获取    chargePaymentWay
     */
    private String chargePaymentWayCode;

    /**
     *      代码（结算）支行
     */
    private String settlementCashSourceId ;

    /**
     *      报单支行
     */
    private String declarationCashSourceId ;

    /**
     *      渠道支行
     */
    private String channelCashSourceId ;

    /**
     *  生效状态-----指结算费用的生效状态
     *  默认为：    非生效状态
     *      只有当  刷卡 完成  修改  生效状态
     */
    private Integer effectStatus = EFFECT_NONE;

    /**
     *      订单生成日期
     *      默认  从  订单的 ts  获取
     */
    private String orderTime;

    /**
     *      营销代码
     */
    private String marketingCode;

}
