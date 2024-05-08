package com.fuze.bcp.statistics.domain;

import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GQR on 2017/11/7.
 */
@Data
@Document(collection = "fi_balanceaccount")
public class BalanceAccount extends MongoBaseEntity {

    /**
     * 单据类型
     */
    private final String billTypeCode = "A024";


    /**
     * 全部状态
     */
    public static final int CHECKSTATUS_ALL = -1;

    /**
     * 已计算待核对
     */
    public static final int CHECKSTATUS_COMPUTED = 0;

    /**
     * 已核对待付款
     */
    public static final int CHECKSTATUS_CHECKED = 1;

    /**
     * 付款中待确认
     */
    public static final int CHECKSTATUS_PAY = 2;

    /**
     * 已付款，结算结束
     */
    public static final int CHECKSTATUS_FINISH = 9;

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 银行放款合计
     */
    private Double totalCredit = 0.0;

    /**
     * 客户数量
     */
    private Integer totalCount = 0;

    /**
     * 结算费用合计（包含本月趸交和分期的手续费）
     */
    private Double totalPaymentAmount = 0.0;

    /**
     * 结算状态
     */
    private Integer checkStatus = CHECKSTATUS_COMPUTED;


    /**
     * 核对人
     */
    private String checkUserId = null;


    /**
     * 核对日期
     */
    private String checkDate = null;

    /**
     * 计算人
     */
    private String settleUserId = null;

    /**
     * 付款开始日期
     */
    private String paymentApplyDate = null;

    /**
     * 付款到账日期
     */
    private String paymentFinishDate = null;

    /**
     * 附件ID
     */
    private List<String> customerImageIds = new ArrayList<String>();


    public BalanceAccount() {
        super();
        setDataStatus(DataStatus.SAVE);
    }

    /**
     * 审核信息
     */
    private List<SignInfo> signInfos = new ArrayList<SignInfo>();

    public void addSignInfo(SignInfo signInfo) {
        this.signInfos.add(signInfo);
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
    private String carDealerId = null;

    /**
     * 提交人所在的部门
     */
    private String orginfoId = null;

    /**
     * 报单行ID
     */
    private String cashSourceId = null;

    /**
     * 是否取消业务
     */
    private Integer cancelled = 0;


}
