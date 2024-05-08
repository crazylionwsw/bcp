package com.fuze.bcp.api.statistics.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;

/**
 * Created by GQR on 2017/11/7.
 */
@Data
@MongoEntity(entityName = "fi_balanceaccount_detail")
public class BalanceAccountDetailBean extends APIBaseBean {
    /**
     * 计算失败
     */
    public static final int STATUS_FAILED = 0;
    /**
     * 计算成功
     */
    public static final int STATUS_SUCCEED = 1;

    /**
     * 状态
     */
    private int status = STATUS_FAILED;

    /**
     * 汇总ID
     */
    private String balanceAccountId = null;

    /**
     * 客户ID
     */
    private String customerId = null;

    /**
     * 渠道ID
     */
    private String cardealerId = null;

    /**
     * 交易ID
     */
    private String customerTransactionId = null;

    /**
     * 归属代码行
     */
    private String cashSourceId = null;

    /**
     * 刷卡金额
     */
    private Double swingAmount = null;

    /**
     * 刷卡日期
     */
    private String swingCardDate = null;

    /**
     * 业务类型
     */
    private String businessTypeCode = null;

    /**
     * 手续费缴纳方式
     */
    private String chargePaymentWay = null;

    /**
     * 借款金额
     */
    private Double creditAmount = 0.0;

    /**
     * 借款期限
     */
    private Integer creditMonths = 0;

    /**
     * 银行手续费金额
     */
    private Double chargeFee = 0.0;

    /**
     * 银行手续费率
     */
    private Double chargeFeeRatio = 0.0;

    /**
     * 根据业务刷卡当月发生的数据计算返佣比率，以后每个月的结费沿用此结费政策
     */
    private Double chargeCheckRatio = 0.0;

    /**
     * 结算费用金额
     */
    private Double checkFee = 0.0;

    public void updateTs(){
        setTs(DateTimeUtils.getCreateTime());
    }
}
