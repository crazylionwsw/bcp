package com.fuze.bcp.workflow.service.impl;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
import com.fuze.bcp.workflow.service.IBusinessFlowService;
import com.mongodb.BasicDBObject;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.NativeExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/8/2.
 */
@Service
public class BusinessFlowServiceImpl implements IBusinessFlowService {

    Logger logger = LoggerFactory.getLogger(BusinessFlowServiceImpl.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;

    @Override
    public List<Execution> findExecutionBySql(String sql, Map params) {
        NativeExecutionQuery nativeExecutionQuery = runtimeService.createNativeExecutionQuery().sql(sql);
        for (Object key : params.keySet()) {
            nativeExecutionQuery.parameter(key.toString(), params.get(key));
        }
        List<Execution> list = nativeExecutionQuery.list();
        return list;
    }

    public void startProcess(String billTypeCode, String businessKey) throws Exception {
        List pi = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey).list();
        if (pi.size() == 0) {
            Integer max = 3;
            while (pi.size() == 0 && max > 0) {
                // 启动流程实例
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(billTypeCode, businessKey);
                pi = runtimeService.createProcessInstanceQuery()
                        .processInstanceBusinessKey(businessKey).list();
                max--;
            }
        }
    }

    @Override
    public void stopProcessInstancekByBusinessKey(String businessKey, String reason, String activitiUserId) throws Exception {

        List<ProcessInstance> pis = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        if (pis.size() > 0) {
            for (int i = 0; i < pis.size(); i++) {
                runtimeService.deleteProcessInstance(pis.get(i).getProcessInstanceId(), reason);
            }
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        while (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                task.delegate(activitiUserId);
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("approveStatus", ApproveStatus.APPROVE_PASSED);
                completeTask(task.getId(), activitiUserId, variables);
            }
            tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        }
    }

    /**
     * 获取用户的任务列表
     *
     * @param activitiUserId
     * @param businessKeyLike
     * @return
     */
    public List<Task> getUserBillTasksLike(String activitiUserId, String businessKeyLike) throws Exception {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(activitiUserId).processInstanceBusinessKeyLikeIgnoreCase(businessKeyLike).list();
        if (tasks == null) tasks = new ArrayList<Task>();
        return tasks;
    }

    /**
     * 获取用户的任务列表
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    public List<Task> getUserBillTasks(String activitiUserId, String businessKey) throws Exception {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(activitiUserId).processInstanceBusinessKey(businessKey).list();
        if (tasks == null) tasks = new ArrayList<Task>();
        return tasks;
    }

    /**
     * 获取用户当前任务
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    public Task getUserBillTask(String activitiUserId, String businessKey) throws Exception {
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(activitiUserId).processInstanceBusinessKey(businessKey);
        if (taskQuery == null) {
            return null;
        }
        Task task = taskQuery.singleResult();
        return task;
    }

    /**
     * 完成一个任务
     *
     * @param taskId
     * @param activitiUserId
     * @param variables
     */
//    public void completeTask(String taskId, String activitiUserId, Integer approveStatus) throws Exception {
//        taskService.claim(taskId, activitiUserId);
//        Map<String, Object> variables = new HashMap<String, Object>();
//        variables.put("approveStatus", approveStatus);
//        taskService.complete(taskId, variables);
//    }
    public void completeTask(String taskId, String activitiUserId, Map<String, Object> variables) throws Exception {
        taskService.claim(taskId, activitiUserId);
        taskService.complete(taskId, variables);
    }

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
    public void completeTaskBranch(String taskId, String activitiUserId, Integer approveStatus, Integer branchId, Integer isCompensatory, Integer advanceDiscountAmount) throws Exception {
        taskService.claim(taskId, activitiUserId);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approveStatus", approveStatus);
        variables.put("branchId", branchId);
        variables.put("isCompensatory", isCompensatory);
        variables.put("advanceDiscountAmount", advanceDiscountAmount);
        taskService.complete(taskId, variables);
    }

    /**
     * 根据用户ID完成一个业务单据
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    public void completeTaskByBusinessKey(String activitiUserId, String businessKey) {
    }

    /**
     * 根据业务单据信息查询是否已经执行完成
     *
     * @param businessKey
     * @return
     */
    public boolean businessBillIsFinish(String businessKey) throws Exception {
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        boolean completeStatus = false;

        /**审核完成*/
        if (tasks.size() == 0) {
            completeStatus = true;//核完成
        }
        return completeStatus;
    }

    /**
     * 查询历史任务
     *
     * @param businessKey
     * @return
     */
    public List<HistoricTaskInstance> findHisTaskList(String businessKey) throws Exception {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).finished().orderByHistoricTaskInstanceEndTime().asc().list();
        if (tasks == null) tasks = new ArrayList<HistoricTaskInstance>();
        return tasks;
    }

    /**
     * 重新部署单据流程
     *
     * @param billTypeCode
     * @return
     */
    public void processDeployment(String billTypeCode) throws Exception {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().addClasspathResource(billTypeCode + ".bpmn").addClasspathResource(billTypeCode + ".png");
        Deployment deploy = deploymentBuilder.deploy();
        if (deploy.getId() != null) {
            logger.info(billTypeCode + "：部署已完成");
        } else {
            logger.error(billTypeCode + "：部署失败");
        }
    }

    /**
     * 查询单据对应的id是否存在工作流中
     *
     * @param businessKey
     * @return
     */
    public boolean checkBillExists(String businessKey) {
        //查询任务中是否存在
        List<ProcessInstance> runtimelist = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        //查询历史任务中是否存在
        List<HistoricTaskInstance> historylist = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).list();
        if (runtimelist.size() > 0 || historylist.size() > 0) {
            logger.info(businessKey + "：存在工作流中");
            return true;
        } else {
            logger.info(businessKey + "：不存在工作流中");
            return false;
        }
    }

    /**
     * 查询指定用户的任务
     *
     * @param userName
     * @return
     */
    public List<Task> getUserTasks(String userName) throws Exception {
        List<Task> list = taskService.createTaskQuery().taskCandidateUser(userName).list();
        return list;
    }

    /**
     * 查询指定单据的任务
     *
     * @param businessKey
     * @return
     */
    public List<Task> getBillTasks(String businessKey) throws Exception {
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        if (tasks == null) tasks = new ArrayList<Task>();
        return tasks;
    }

    public List<IdentityLink> getIdentityLinksForTask(String taskId){
        return taskService.getIdentityLinksForTask(taskId);
    }

    /**
     * 完成任务
     *
     * @param taskId
     * @return
     */

    public void endTask(String taskId, Map<String, Object> variables) throws Exception {
        taskService.complete(taskId, variables);
    }

    /**
     * 根据手机号查询用户的工作流角色
     */
    public List<String> getUserGroups(List<String> activitiIds) throws Exception {
        List<String> groupNamesList = new ArrayList<String>();
        if (activitiIds != null && activitiIds.size() > 0) {
            for (String id : activitiIds) {
                Group group = identityService.createGroupQuery().groupId(id).singleResult();
                if (group != null || !"".equals(group)) {
                    groupNamesList.add(group.getName());
                }
            }
        }
        return groupNamesList;
    }

    public List<Group> getUserGroups(String username) throws Exception {
        List<Group> groups = identityService.createGroupQuery().groupMember(username).list();
        if (groups.size() == 0) groups = new ArrayList<Group>();
        return groups;
    }

    @Override
    public List<Group> getUserGroupsByActivitiUserId(String activitiuserid) throws Exception {
        List<Group> groups = identityService.createGroupQuery().groupMember(activitiuserid).list();
        if (groups.size() == 0) groups = new ArrayList<Group>();
        return groups;
    }

    public void assignUserGroups(String userName, List<String> groups) throws Exception {
        //检查用户是否存在
        List<User> users = identityService.createUserQuery().userId(userName).list();
        if (users.size() == 0) {
            try {
                User user = identityService.newUser(userName);
                identityService.saveUser(user);
            } catch (Exception ex) {
                logger.error("Error:修改用户出错！", ex);
                throw ex;
            }
        }
        try {
            List<Group> list = identityService.createGroupQuery().list();
            for (int i = 0; i < list.size(); i++) {
                identityService.deleteMembership(userName, list.get(i).getId());
            }
            for (int i = 0; i < groups.size(); i++) {
                identityService.createMembership(userName, groups.get(i));
            }
        } catch (Exception e) {
            logger.error("Error:创建用户组关系失败", e);
            throw e;
        }
    }

    public List<Group> getAllGroups() throws Exception {
        List<Group> groups = identityService.createGroupQuery().list();
        if (groups == null) groups = new ArrayList<Group>();
        return groups;
    }

    public List<Task> getCurrentGroupAndCheckIsTask(String activitiUserId, String businessKey) {
        List<Task> list;
        if (businessKey != null && businessKey != "") {
            list = taskService.createTaskQuery().taskCandidateOrAssigned(activitiUserId).processInstanceBusinessKey(businessKey).list();
        } else {
            list = taskService.createTaskQuery().taskCandidateOrAssigned(activitiUserId).list();
        }
        return list;
    }

    @Override
    public ResultBean<Boolean> stopWorkFlow(String businessKey) {
       /* List<ProcessDefinition> pdList=repositoryService // 获取service
                .createProcessDefinitionQuery() // 创建流程定义查询
                .processDefinitionKey(businessKey) // 根据key查询
                .list();  // 返回一个集合
        for(ProcessDefinition pd:pdList){
            repositoryService.deleteDeployment(pd.getDeploymentId(),true);
        }
        List<ProcessDefinition> list=repositoryService // 获取service
                .createProcessDefinitionQuery() // 创建流程定义查询
                .processDefinitionKey(businessKey) // 根据key查询
                .list();  // 返回一个集合
        if(list.isEmpty()){
            logger.info(businessKey+"流程终止！");
            return ResultBean.getSucceed().setD(true);
        }
        logger.error(businessKey+"流程终止出错！");
        return ResultBean.getSucceed().setD(false);*/
        return null;
    }

    /*private ResultBean getTaskListByBusinesskey(){
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("BankCard_ReceiveTrustee").list();
        for (Task task:list) {
            System.out.print(task.getName());
        }
        return null;
    }*/

}
