package com.fuze.bcp.workflow.service;

import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/8/2.
 */
public interface IBusinessFlowService {

    List<Execution> findExecutionBySql(String sql, Map params);

    void startProcess(String billTypeCode, String businessKey) throws Exception;


    /**
     * 中止工作流的全部任务
     * @param businessKey
     * @param reason 原因
     * @return
     */
    void stopProcessInstancekByBusinessKey(String businessKey,String reason,String activitiUserId) throws Exception;

    /**
     * 获取用户的任务列表
     *
     * @param activitiUserId
     * @param businessKeyLike
     * @return
     */
    List<Task> getUserBillTasksLike(String activitiUserId, String businessKeyLike) throws Exception;

    /**
     * 获取用户的任务列表
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    List<Task> getUserBillTasks(String activitiUserId, String businessKey) throws Exception;

    /**
     * 获取用户当前任务
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    Task getUserBillTask(String activitiUserId, String businessKey) throws Exception;

    /**
     * 完成一个任务
     *
     * @param taskId
     * @param activitiUserId
     * @return 0 表示失败  1表示成功
     */

    void completeTask(String taskId, String activitiUserId, Map<String, Object> variables) throws Exception;

    /**
     * 加入分支ID
     *
     * @param taskId
     * @param activitiUserId
     * @param approveStatus
     * @param branchId
     * @param isCompensatory
     * @return
     */
    void completeTaskBranch(String taskId, String activitiUserId, Integer approveStatus, Integer branchId, Integer isCompensatory, Integer advanceDiscountAmount) throws Exception;

    /**
     * 根据用户ID完成一个业务单据
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    void completeTaskByBusinessKey(String activitiUserId, String businessKey);

    /**
     * 根据业务单据信息查询是否已经执行完成
     *
     * @param businessKey
     * @return
     */
    boolean businessBillIsFinish(String businessKey) throws Exception;

    /**
     * 查询历史任务
     *
     * @param businessKey
     * @return
     */
    List<HistoricTaskInstance> findHisTaskList(String businessKey) throws Exception;

    /**
     * 重新部署单据流程
     *
     * @param billTypeCode
     * @return
     */
    void processDeployment(String billTypeCode) throws Exception;

    /**
     * 查询单据对应的id是否存在工作流中
     *
     * @param businessKey
     * @return
     */
    boolean checkBillExists(String businessKey);

    /**
     * 查询指定用户的任务
     *
     * @param userName
     * @return
     */
    List<Task> getUserTasks(String userName) throws Exception;

    /**
     * 查询指定单据的任务
     *
     * @param businessKey
     * @return
     */
    List<Task> getBillTasks(String businessKey) throws Exception;

    List<IdentityLink> getIdentityLinksForTask(String taskId);

    /**
     * 完成任务
     *
     * @param taskId
     * @return
     */
    void endTask(String taskId, Map<String, Object> variables) throws Exception;

    /**
     * 根据手机号查询用户的工作流角色
     */
    List<String> getUserGroups(List<String> activitiIds) throws Exception;

    List<Group> getUserGroups(String username) throws Exception;

    List<Group> getUserGroupsByActivitiUserId(String activitiuserid) throws Exception;

    void assignUserGroups(String userName, List<String> groups) throws Exception;

    List<Group> getAllGroups() throws Exception;

    List<Task> getCurrentGroupAndCheckIsTask(String activitiUserId, String businessKey);

    ResultBean<Boolean> stopWorkFlow(String businessKey);
}
