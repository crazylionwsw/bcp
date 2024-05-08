package com.fuze.bcp.statistics.domain;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.utils.DateTimeUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by GQR on 2017/11/7.
 */
@Data
@Document(collection = "fi_balanceaccount_detail")
public class BalanceAccountDetail extends MongoBaseEntity {

    /**
     * 计算失败
     */
    public static final int STATUS_FAILED = 0;
    /**
     * 计算成功
     */
    public static final int STATUS_SUCCEED = 1;

    private int status = STATUS_FAILED;


    /**
     * 汇总ID
     */
    private String balanceAccountId = null;


    /**
     * 渠道ID
     */
    private String cardealerId = null;

    /**
     * 订单ID
     */
    private String orderId = null;

    /**
     * 刷卡金额
     */
    private Double swingAmount = null;

    /**
     * 刷卡日期
     */
    private String swingCardDate = null;

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

    public BalanceAccountDetail() {
        super();
        setDataStatus(DataStatus.SAVE);
    }
    public void updateTs(){
        setTs(DateTimeUtils.getCreateTime());
    }


    /**
     * 单据开始时间
     */
    private String startDate;
    /**
     * 审核状态
     */
    private Integer approveStatus = ApproveStatus.APPROVE_INIT;

    /**
     * 最后处理日期
     */
    private String approveDate = null;

    /**
     * 处理用户ID
     */
    private String approveUserId = null;

    /**
     * 业务类型
     */
    private String businessTypeCode = null;

    /**
     * 客户ID
     */
    private String customerId = null;

    /**
     * 客户交易ID
     */
    private String customerTransactionId = null;

    /**
     * 提交人的用户ID （提交人可能是业务员或后台操作人员）
     */
    private String loginUserId = null;

    /**
     * 提交人的员工ID
     */
    private String employeeId = null;

    /**
     * 业务来源，即4S店
     */
    private String  carDealerId = null;

    /**
     * 提交人所在的部门
     */
    private String orginfoId = null;

    /**
     *  报单行ID
     */
    private String cashSourceId = null;

    /**
     *  是否取消业务
     */
    private Integer cancelled = 0;




}
