package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.PoundageSettlementBean;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zqw on 2017/8/29.
 * 手续费支行结算
 */
@Data
@Document(collection="so_poundagesettlement")
public class PoundageSettlement extends BaseDataEntity {

    private int status = PoundageSettlementBean.STATUS_INIT;

    /**
     *  交易ID
     */
    private String customerTransactionId ;

    /**
     * 客户id
     */
    private String customerId ;

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
     *      结算支行--代码行
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
    private Integer effectStatus = PoundageSettlementBean.EFFECT_NONE;

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
