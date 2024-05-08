package com.fuze.bcp.workflow.domain;

import com.fuze.bcp.api.workflow.bean.SignCondition;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 2017/4/13.
 * 单独的审批流单据处理
 */
@Document(collection = "wf_workflowbill")
@Data
public class WorkFlowBill<T extends SignInfo> extends BaseDataEntity implements ApproveStatus {

    public WorkFlowBill() {
    }

    public WorkFlowBill(String businessTypeCode, String flowCode, String sourceId, String collectionName, Map<String, List<SignCondition>> params, String transactionId) {
        this.businessTypeCode = businessTypeCode;
        this.flowCode = flowCode;
        this.sourceId = sourceId;
        this.activitiId = flowCode + "." + sourceId;
        this.params = params;
        this.collectionName = collectionName;
        this.approveStatus = ApproveStatus.APPROVE_ONGOING;
        this.transactionId = transactionId;
    }

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
     * 审批流编码 A021
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
     * 刚完成的任务
     */
    private String completedTask = null;

    /**
     * 当前任务
     */
    private String currentTask = null;

    /**
     * 待办任务列表
     */
    private List<String> currentTasks = new ArrayList<String>();

    /**
     * 审核状态
     */
    private Integer approveStatus = APPROVE_INIT;

    /**
     * 工作流完成时间
     */
    private String doneTime;

    /**
     * 对应的单据签批意见
     */
    private List<T> signInfos = new ArrayList<>();

}
