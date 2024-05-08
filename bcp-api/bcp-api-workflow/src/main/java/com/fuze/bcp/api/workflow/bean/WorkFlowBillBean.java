package com.fuze.bcp.api.workflow.bean;

import com.fuze.bcp.bean.APIBaseBean;
import com.fuze.bcp.bean.ApproveStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 2017/4/13.
 * 单独的审批流单据处理,外界可见对象
 */
@Data
public final class WorkFlowBillBean<T extends SignInfo> extends APIBaseBean implements ApproveStatus {

    /**
     * 业务ID
     */
    private String transactionId;

    /**
     * 工作流参数
     */
    private Map<String, List<SignCondition>> params;

    /**
     * 单据对应的mongo collectionName
     */
    private String collectionName;

    /**
     * 业务类型
     */
    private String businessTypeCode;

    /**
     * 审批流编码  A021
     */
    private String flowCode = null;

    /**
     * 单据的ID 59a7fbfee722b2b65c4aef15
     */
    private String sourceId = null;

    /**
     * 审批流的对象ID A021.59a7fbfee722b2b65c4aef15
     */
    private String activitiId = null;

    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 完成任务名称
     */
    private String completedTask = null;

    /**
     * 当前待完成任务
     */
    private String currentTask = null;

    /**
     * 待办任务列表
     */
    private List<String> currentTasks = new ArrayList<String>();

    /**
     * 对应的单据签批意见
     */
    private List<T> signInfos = new ArrayList<>();

}
