package com.fuze.bcp.dubbo.migration.activiti;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/11/14.
 */
public class WorkFlowUtils {

    RepositoryService repositoryService;

    RuntimeService runtimeService;

    TaskService taskService;

    HistoryService historyService;

    IdentityService identityService;

    ManagementService managementService;

    public WorkFlowUtils(){
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        this.setHistoryService(processEngine.getHistoryService());
        this.setTaskService(processEngine.getTaskService());
        this.setRuntimeService(processEngine.getRuntimeService());
        this.setRepositoryService(processEngine.getRepositoryService());
        this.setIdentityService(processEngine.getIdentityService());
        this.setManagementService(processEngine.getManagementService());
    }

    public ManagementService getManagementService() {
        return managementService;
    }

    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }

    public synchronized void startProcess(String billTypeCode, String businessKey) throws Exception {
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

    public void actEndTask(String taskId, Map<String, Object> variables) {
        try {
            this.endTask(taskId, variables);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public synchronized List<Task> getUserBillTasks(String activitiUserId, String businessKey) throws Exception {
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

    public synchronized void completeTask(String taskId, String activitiUserId, Map<String, Object> variables) throws Exception {
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
    public synchronized boolean businessBillIsFinish(String businessKey) throws Exception {
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
            System.out.println(billTypeCode + "：部署已完成");
        } else {
            System.out.println(billTypeCode + "：部署失败");
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
            System.out.println(businessKey + "：存在工作流中");
            return true;
        } else {
            System.out.println(businessKey + "：不存在工作流中");
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

    public void assignUserGroups(String userName, List<String> groups) throws Exception {
        //检查用户是否存在
        List<User> users = identityService.createUserQuery().userId(userName).list();
        if (users.size() == 0) {
            try {
                User user = identityService.newUser(userName);
                identityService.saveUser(user);
            } catch (Exception ex) {
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

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public IdentityService getIdentityService() {
        return identityService;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }
}
