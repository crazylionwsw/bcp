package com.fuze.bcp.api.workflow.service;

import com.fuze.bcp.api.workflow.bean.*;
import com.fuze.bcp.bean.APIBillBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;
import java.util.Map;

/**
 * 客户服务接口类
 * Created by sean on 2017/5/19.
 */
public interface IWorkflowBizService {

    /**
     * 获取用户的任务列表
     *
     * @param activitiUserId
     * @return
     */
    ResultBean<List<TaskBean>> actGetUserBillTasks(String activitiUserId, String businessKey);

    /**
     * 获取用户当前任务
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    ResultBean<TaskBean> actGetUserBillTask(String activitiUserId, String businessKey);

    /**
     * 完成任务
     *
     * @param taskId
     * @param activitiUserId
     * @param variables
     * @return
     */
    ResultBean actCompleteTask(String taskId, String activitiUserId, Map<String, Object> variables);

    /**
     * 加入分支ID
     *
     * @param taskId
     * @param activitiUserId
     * @param approveStatus
     * @param branchId
     * @return
     */
    ResultBean actCompleteTaskBranch(String taskId, String activitiUserId, Integer approveStatus, Integer branchId, Integer isCompensatory, Integer advanceDiscountAmount);

    /**
     * 根据用户ID完成一个业务单据
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    ResultBean actCompleteTaskByBusinessKey(String activitiUserId, String businessKey);

    /**
     * 根据业务单据信息查询是否已经执行完成
     *
     * @param businessKey
     * @return
     */
    ResultBean<Boolean> actBusinessBillIsFinish(String businessKey);

    /**
     * 查询历史任务
     *
     * @param businessKey
     * @return
     */
    ResultBean<List<TaskBean>> actFindHisTaskList(String businessKey);

    /**
     * 重新部署单据流程
     *
     * @param billTypeCode
     * @return
     */
    ResultBean actProcessDeployment(String billTypeCode);

    /**
     * 查询单据对应的id是否存在工作流中
     *
     * @param businessKey
     * @return
     */
    ResultBean<Boolean> actCheckBillExists(String businessKey);


    /**
     * 工作台任务查询
     *
     * @return
     */
    ResultBean<List<TaskBean>> actGetUserTasks(String userName);

    /**
     * 根据businessKey查询任务
     */
    ResultBean<List<TaskBean>> actGetBillTasks(String businessKey);

    /**
     * 改变节点变量，结束正在进行的流程实例
     */
    ResultBean actEndTask(String taskId);

    /**
     * 改变节点变量，结束正在进行的流程实例
     */
    ResultBean actEndTask(String taskId, Map<String, Object> variables);

    /**
     * 根据手机号查询用户的工作流角色
     */
    ResultBean<List<String>> actGetUserGroups(List<String> activitiId);

    /**
     * 根据手机号查询用户的工作流角色
     *
     * @param username
     * @return
     */
    ResultBean<List<GroupBean>> actGetUserGroups(String username);

    /**
     * 根据工作流Id查询工作流角色
     */
    ResultBean<List<GroupBean>> actGetUserGroupsByActivitiUserId(String activitiuserid);


    /**
     * 关联用户与组
     *
     * @return
     */
    ResultBean actAssignUserGroups(String userName, List<String> roles);

    /**
     * 获取审批组名称
     *
     * @return
     */
    ResultBean<List<GroupBean>> actGetAllGroups();

    /**
     * 通过单据ID获取工作流对象
     *
     * @param billId
     * @return
     */
    ResultBean<WorkFlowBillBean> actGetBillWorkflow(String billId);

    /**
     * 根据单据ID，保存简单的签批信息
     *
     * @param billId
     * @param signInfo
     * @param fromType 0 发推送，1 不发推送
     * @return
     */
    ResultBean<WorkFlowBillBean> actSaveWorkflowBill(String billId, SignInfo signInfo, Integer fromType);
    ResultBean<WorkFlowBillBean> actSaveWorkflowBill(String billId, SignInfo signInfo);

    /**
     * 根据　业务类型　对象id　保存简单的签批信息
     *
     * @param FlowCode 业务类型
     * @param SourceId 对象id
     * @param signInfo
     * @return
     */
    ResultBean<WorkFlowBillBean> actSaveWorkflowBillBySourceIdAndFlowCode(String FlowCode, String SourceId, SignInfo signInfo);

    ResultBean<WorkFlowBillBean> actCreateWorkflow(String businessTypeCode, String billId, String billTypeCode, String collectionName, Map<String, List<SignCondition>> params, String transactionId);

    ResultBean<WorkFlowBillBean> actSignBill(String billId, SignInfo signInfo) throws Exception;

    ResultBean<WorkFlowBillBean> actSignBill(String billId, SignInfo signInfo, Boolean isSpecial) throws Exception;

    ResultBean<WorkFlowBillBean> actSubmit(String businessTypeCode, String billId, String billTypeCode, SignInfo signInfo, String collectionName, Map<String, List<SignCondition>> params, String transactionId);

    ResultBean<WorkFlowBillBean> actSignForTransaction(String billId, String businessTypeCode, String billTypeCode, SignInfo signInfo) throws Exception;

    /**
     * 获取当前任务组，并检查该任务是否属于当前用户
     *
     * @param activitiUserId,businessKey
     * @return
     */
    ResultBean<List<TaskBean>> actGetCurrentGroupAndCheckIsTask(String activitiUserId, String businessKey);

    /**
     * 获取对象的签批信息列表
     *
     * @param flowCode billtypecode
     * @param sourceId 对象的id
     * @return
     */
    ResultBean<List<SignInfo>> actGetSingInfosByFlowCodeAndSourceId(String flowCode, String sourceId);

    /**
     * 获取某笔交易的某单据的签批信息列表
     *
     * @param flowCode（billtypecode）单据编码
     * @param transactionId              交易的id
     * @return
     */
    ResultBean<List<SignInfo>> actGetSingInfosByFlowCodeAndTransactionId(String flowCode, String transactionId);

    /**
     * 取消/终止某业务的工作流
     *
     * @param tid
     * @param reason
     * @param activitiUserId
     * @return
     */
    ResultBean actStopTransaction(String tid, String reason, String activitiUserId);

    /**
     * 获取某交易的所有签批信息
     *
     * @param tid
     * @return
     */
    ResultBean<List<BillSignInfo>> actGetSingInfosByTransaction(String tid);

    /**
     * 保存操作记录信息
     *
     * @param workFlowBillBean
     * @return
     */
    ResultBean<WorkFlowBillBean> actSaveWorkFlowBill(WorkFlowBillBean workFlowBillBean);

    /**
     * 重审
     *
     * @param bill
     * @return
     */
    ResultBean<WorkFlowBillBean> actReset(APIBillBean bill);

    /**
     * 废弃
     *
     * @param billId
     * @return
     */
    ResultBean actDiscardBillWorkFlow(String billId, String activitiUserId);

    /**
     * 终止工作流程
     *
     * @param businessKey
     * @return
     */
    ResultBean<Boolean> actStopWorkFlow(String businessKey);

    String getPushSendValue(Boolean finishThis, String transactionId, String flowCode);

    ResultBean<List<ExecutionBean>> actFindExecutionBySql(String sql, Map params);

    ResultBean<List<String>> actGetBkByRoles(List<String> roles, String businessType);

    ResultBean<List<String>> actGetMyBillByLoginUserId(String businessType, String loginUserId);

    ResultBean<WorkFlowBillBean> actGetBillWorkflowTEM(String billId);


}
