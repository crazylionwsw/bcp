package com.fuze.bcp.api.task.bean;

import com.fuze.bcp.bean.APIBaseBean;

/**
 * 任务管理类
 * Created by sean on 2017/6/8.
 */
public class Task extends APIBaseBean {

    /**
     * 等待执行
     */
    public final static Integer STATUS_WAIT = 0;
    /**
     * 执行中
     */
    public final static Integer STATUS_ONGOING = 1;

    /**
     * 已经执行完成
     */
    public final static Integer STATUS_END = 9;

    /**
     * 已经取消
     */
    public final static Integer STATUS_CANCELED = 99;


    /**
     * 任务执行状态
     */
    private Integer status = STATUS_WAIT;

    /**
     * 工作流的任务ID
     */
    private String activityTaskId = null;


    /**
     * 任务对应的单据类型编码
     */
    private String billTypeCode = null;
    /**
     * 原始单据的ID
     */
    private String sourceBillId = null;


    /**
     * 任务的开始时间
     */
    private String startTime = null;

    /**
     * 任务的实际完成时间
     */
    private String endTime = null;

    /**
     * 任务的超时时间
     */
    private String expireTime = null;
    /**
     * 任务的负责人ID
     */
    private String masterUserId = null;

    /**
     * 用户的执行人ID
     */
    private String userId = null;

    /**
     * 任务的执行代理人ID
     */
    private String agentUserId = null;
}
