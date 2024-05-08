package com.fuze.bcp.credithome.domain;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;

/**
 * 家装分期单据对象基类
 * Created by JZ on 2017/02/17.
 */
@Data
public abstract class HomeBillEntity extends MongoBaseEntity implements ApproveStatus {

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
     *  渠道ID
     */
    private String channelId = null;

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

    /**
     * 子类需要制定单据类型编码
     * @return
     */
    public abstract String getBillTypeCode();

}
