package com.fuze.bcp.bean;

/**
 * 审批状态
 */
public interface ApproveStatus {
    /**
     *初始化状态，未进入审核流程
     * 待审核
     */
    Integer APPROVE_INIT = 0;

    /**
     *审核中，已经进入审核流程
     */
    Integer APPROVE_ONGOING = 1;

    /**
     * 审核通过
     */
    Integer APPROVE_PASSED = 2;

    /**
     * 驳回待提交
     */
    Integer APPROVE_REAPPLY = 8;

    /**
     * 拒绝
     */
    Integer APPROVE_REJECT = 9;

}
