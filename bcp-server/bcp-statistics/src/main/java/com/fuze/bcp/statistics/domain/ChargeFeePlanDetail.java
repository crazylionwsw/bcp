package com.fuze.bcp.statistics.domain;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Changed By wangshaowei 2017-12-28
 */
@Document(collection = "so_chargefeeplan_detail")
@Data
public class ChargeFeePlanDetail extends MongoBaseEntity implements ApproveStatus {

    /**
     * 收款计划ID
     */
    private String chargeFeePlanId = null;


    /**
     * 年月
     */
    private Integer index = 1;

    /**
     * 年月
     */
    private String year = null;
    private String month = null;


    /**
     * 手续费月还款
     */
    private Double chargeAmount = 0.0;

    public ChargeFeePlanDetail(String chargeFeePlanId) {
        this.chargeFeePlanId = chargeFeePlanId;
        setDataStatus(DataStatus.SAVE);
    }

    public ChargeFeePlanDetail(){}

    /**
     * 单据开始时间
     */
    private String startDate;
    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

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

    public String getBillTypeCode() {
        return "A029";
    }
}
