package com.fuze.bcp.workflow.business;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.push.bean.PushDataBean;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.workflow.bean.*;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.APIBillBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.MetaDataService;
import com.fuze.bcp.service.TemplateService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
import com.fuze.bcp.workflow.service.IBusinessFlowService;
import com.fuze.bcp.workflow.service.IWorkFlowBillService;
import com.mongodb.BasicDBObject;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by admin on 2017/6/14.
 */
@Service
public class BizWorkflowService implements IWorkflowBizService {

    private final Logger logger = LoggerFactory.getLogger(BizWorkflowService.class);

    @Autowired
    private IWorkFlowBillService iWorkFlowBillService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    IBusinessFlowService iBusinessFlowService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    TemplateService templateService;

    @Autowired
    MetaDataService metaDataService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private MappingService mappingService;

    @Autowired
    MessageService messageService;

    public ResultBean<List<String>> actGetMyBillByLoginUserId(String businessType, String loginUserId) {
        LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId).getD();
        if (loginUserBean == null) {
            return ResultBean.getFailed();
        }
        List<String> roles = loginUserBean.getActivitiUserRoles();
        List<String> businessKeies = this.actGetBkByRoles(roles, businessType).getD();
        List<String> billIds = new ArrayList<>();
        if (businessKeies != null) {
            for (String businessKey : businessKeies) {
                billIds.add(businessKey.substring(businessKey.indexOf(".") + 1));
            }
        }
        return ResultBean.getSucceed().setD(billIds);
    }

    public ResultBean<List<String>> actGetBkByRoles(List<String> roles, String businessType) {
        if (roles == null || roles.size() == 0) {
            return ResultBean.getFailed();
        }
        Map map = new HashMap<>();
        String roleStr = "#{" + roles.get(0) + "}";
        for (int i = 1; i < roles.size() && roles.size() > 1; i++) {
            roleStr = roleStr + "," + "#{" + roles.get(i) + "}";
        }
        for (String role : roles) {
            map.put(role, role);
        }
        map.put("BUSINESS_KEY_", businessType + "%");
        List<Execution> ex = iBusinessFlowService.findExecutionBySql("SELECT * FROM `ACT_RU_IDENTITYLINK` a, ACT_RU_TASK  b, ACT_RU_EXECUTION c " +
                "where a.GROUP_ID_ in (" + roleStr + ") AND a.TASK_ID_ = b.ID_ AND b.EXECUTION_ID_ = c.ID_" + (businessType != null ? " AND c.BUSINESS_KEY_ like #{BUSINESS_KEY_}" : "") + ";", map);
        List<String> bKey = new ArrayList<>();
        for (Execution e : ex) {
            ExecutionEntity entity = (ExecutionEntity) e;
            bKey.add(entity.getBusinessKey());
        }
        return ResultBean.getSucceed().setD(bKey);
    }


    /**
     * 获取用户的任务列表
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    public ResultBean<List<TaskBean>> actGetUserBillTasks(String activitiUserId, String businessKey) {
        try {
            List<Task> tasks = iBusinessFlowService.getUserBillTasks(activitiUserId, businessKey);
            return ResultBean.getSucceed().setD(mappingService.map(tasks, TaskBean.class));
        } catch (Exception e) {
            logger.error("获取用户的任务列表失败", e);
            return ResultBean.getFailed().setM("获取用户的任务列表失败");
        }
    }

    /**
     * 获取用户当前任务
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    public ResultBean<TaskBean> actGetUserBillTask(String activitiUserId, String businessKey) {
        try {
            Task task = iBusinessFlowService.getUserBillTask(activitiUserId, businessKey);
            if (task == null)
                return ResultBean.getFailed().setM("获取任务失败");

            return ResultBean.getSucceed().setD(mappingService.map(task, TaskBean.class));
        } catch (Exception e) {
            logger.error("获取用户的任务失败", e);
            return ResultBean.getFailed().setM("获取任务失败");
        }
    }

    /**
     * 完成一个任务
     *
     * @param taskId
     * @param activitiUserId
     * @param variables
     */

    public ResultBean actCompleteTask(String taskId, String activitiUserId, Map<String, Object> variables) {
        try {
            iBusinessFlowService.completeTask(taskId, activitiUserId, variables);
            return ResultBean.getSucceed();
        } catch (Exception e) {
            logger.error("完成一个任务失败", e);
            return ResultBean.getFailed().setM("任务失败");
        }
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
    public ResultBean actCompleteTaskBranch(String taskId, String activitiUserId, Integer approveStatus, Integer branchId, Integer isCompensatory, Integer advanceDiscountAmount) {
        try {
            iBusinessFlowService.completeTaskBranch(taskId, activitiUserId, approveStatus, branchId, isCompensatory, advanceDiscountAmount);
        } catch (Exception e) {
            logger.error("加入分支ID失败", e);
            return ResultBean.getFailed().setM("任务失败");
        }
        return ResultBean.getSucceed();
    }

    /**
     * 根据用户ID完成一个业务单据
     *
     * @param activitiUserId
     * @param businessKey
     * @return
     */
    public ResultBean actCompleteTaskByBusinessKey(String activitiUserId, String businessKey) {
        return null;
    }

    /**
     * 根据业务单据信息查询是否已经执行完成
     *
     * @param businessKey
     * @return
     */
    public ResultBean<Boolean> actBusinessBillIsFinish(String businessKey) {
        boolean boo = false;
        try {
            boo = iBusinessFlowService.businessBillIsFinish(businessKey);
        } catch (Exception e) {
            logger.error("", e);
        }
        return ResultBean.getSucceed().setD(boo);
    }

    /**
     * 查询历史任务
     *
     * @param businessKey
     * @return
     */
    public ResultBean<List<TaskBean>> actFindHisTaskList(String businessKey) {
        List<HistoricTaskInstance> tasks = new ArrayList<HistoricTaskInstance>();
        try {
            tasks = iBusinessFlowService.findHisTaskList(businessKey);
        } catch (Exception e) {
            logger.error("查询历史任务失败", e);
            ResultBean.getFailed().setM("查询历史任务失败");
        }
        return ResultBean.getSucceed().setD(mappingService.map(tasks, TaskBean.class));
    }

    /**
     * 重新部署单据流程
     *
     * @param billTypeCode
     * @return
     */
    public ResultBean actProcessDeployment(String billTypeCode) {
        try {
            iBusinessFlowService.processDeployment(billTypeCode);
        } catch (Exception e) {
            logger.error(billTypeCode + "：部署失败");
            return ResultBean.getFailed().setM("部署失败");
        }
        return ResultBean.getSucceed();
    }

    /**
     * 查询单据对应的id是否存在工作流中
     *
     * @param businessKey
     * @return
     */
    public ResultBean<Boolean> actCheckBillExists(String businessKey) {
        return ResultBean.getSucceed().setD(iBusinessFlowService.checkBillExists(businessKey));
    }

    /**
     * 查询指定用户的任务
     *
     * @param userName
     * @return
     */
    public ResultBean<List<TaskBean>> actGetUserTasks(String userName) {
        try {
            List<Task> list = iBusinessFlowService.getUserTasks(userName);
            return ResultBean.getSucceed().setD(list);
        } catch (Exception e) {
            logger.error("Error:查询指定用户的任务失败", e);
            return ResultBean.getFailed().setM("Error:查询指定用户的任务失败");
        }
    }

    /**
     * 查询指定单据的任务
     *
     * @param businessKey
     * @return
     */
    public ResultBean<List<TaskBean>> actGetBillTasks(String businessKey) {
        List<Task> tasks = new ArrayList<Task>();
        try {
            tasks = iBusinessFlowService.getBillTasks(businessKey);
        } catch (Exception e) {
            logger.error("查询指定单据的任务失败", e);
            ResultBean.getFailed().setM("查询指定单据的任务失败");
        }
        return ResultBean.getSucceed().setD(mappingService.map(tasks, TaskBean.class));
    }

    /**
     * 完成任务
     *
     * @param taskId
     * @return
     */
    public ResultBean actEndTask(String taskId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approveStatus", "9");
        return actEndTask(taskId, variables);
    }

    public ResultBean actEndTask(String taskId, Map<String, Object> variables) {
        try {
            iBusinessFlowService.endTask(taskId, variables);
            return ResultBean.getSucceed();
        } catch (Exception e) {
            logger.error("Error:结束正在进行的流程实例失败", e);
            return ResultBean.getFailed().setM("Error:结束正在进行的流程实例失败");
        }
    }

    /**
     * 根据手机号查询用户的工作流角色
     */
    public ResultBean<List<String>> actGetUserGroups(List<String> activitiIds) {
        try {
            return ResultBean.getSucceed().setD(iBusinessFlowService.getUserGroups(activitiIds));
        } catch (Exception e) {
            logger.error("查询用户的工作流角色失败", e);
            return ResultBean.getFailed();
        }
    }

    public ResultBean<List<GroupBean>> actGetUserGroups(String username) {
        List<Group> groups = new ArrayList<Group>();
        try {
            groups = iBusinessFlowService.getUserGroups(username);
        } catch (Exception e) {
            logger.error("查询用户的工作流角色失败", e);
            return ResultBean.getFailed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(groups, GroupBean.class));
    }

    @Override
    public ResultBean<List<GroupBean>> actGetUserGroupsByActivitiUserId(String activitiuserid) {
        List<Group> groups = new ArrayList<Group>();
        try {
            groups = iBusinessFlowService.getUserGroupsByActivitiUserId(activitiuserid);
        } catch (Exception e) {
            logger.error("查询用户的工作流角色失败", e);
            return ResultBean.getFailed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(groups, GroupBean.class));
    }

    public ResultBean actAssignUserGroups(String userName, List<String> groups) {
        try {
            iBusinessFlowService.assignUserGroups(userName, groups);
            return ResultBean.getSucceed();
        } catch (Exception e) {
            logger.error("Error:创建用户组关系失败", e);
            return ResultBean.getFailed().setM("Error:创建用户组关系失败");
        }
    }

    public ResultBean<List<GroupBean>> actGetAllGroups() {
        try {
            List<Group> groups = iBusinessFlowService.getAllGroups();
            if (groups == null) groups = new ArrayList<Group>();
            return ResultBean.getSucceed().setD(mappingService.map(groups, GroupBean.class));
        } catch (Exception e) {
            logger.error("Error:获取工作流组信息失败", e);
            return ResultBean.getFailed().setM("Error:获取工作流组信息失败");
        }
    }

    public ResultBean<WorkFlowBillBean> actGetBillWorkflowTEM(String billId) {
        WorkFlowBill workFlowBill = iWorkFlowBillService.getWorkflowBillByBillIdAndFlowCode(billId);
        WorkFlowBillBean workFlowBillBean = mappingService.map(workFlowBill, WorkFlowBillBean.class);
        workFlowBillBean.setSignInfos(workFlowBill.getSignInfos());
        return ResultBean.getSucceed().setD(workFlowBillBean);
    }

    public ResultBean<WorkFlowBillBean> actGetBillWorkflow(String billId) {

        WorkFlowBill workFlowBill = iWorkFlowBillService.getOneByBillId(billId);
        if (workFlowBill == null) {
            return ResultBean.getSucceed();
        } else {
            WorkFlowBillBean workFlowBillBean = mappingService.map(workFlowBill, WorkFlowBillBean.class);
            workFlowBillBean.setParams(workFlowBill.getParams());
            return ResultBean.getSucceed().setD(workFlowBillBean);
        }
    }

    public ResultBean<WorkFlowBillBean> actSaveWorkflowBill(String billId, SignInfo signInfo) {
        return this.actSaveWorkflowBill(billId, signInfo, 0);
    }

    public ResultBean<WorkFlowBillBean> actSaveWorkflowBill(String billId, SignInfo signInfo, Integer fromType) {
        WorkFlowBill workFlowBill = iWorkFlowBillService.getOneByBillId(billId);
        if (workFlowBill != null) {
            workFlowBill.getSignInfos().add(signInfo);
            workFlowBill = iWorkFlowBillService.save(workFlowBill);

            Map templateData = new HashMap<>();
            Map sendMap = new HashMap<>();
            List<String> toList = new ArrayList<>();
            Map transactionMap = templateService.getTransactionMap(workFlowBill.getTransactionId());
            if (transactionMap.containsKey("employeeId") && fromType == 0) {
                toList.add(transactionMap.get("employeeId").toString());
            }
            templateData.put("businessType", workFlowBill.getFlowCode());
            sendMap.put("bd_employee", toList);
            MsgRecordBean msgRecordBean = new MsgRecordBean("WORKFLOW_COMMENT", workFlowBill.getTransactionId(), templateData, null, sendMap);
            Map extraFields = new HashMap<>();
            extraFields.put("billId", workFlowBill.getSourceId());
            extraFields.put("businessType", workFlowBill.getFlowCode());
            Map ctrlMap = new HashMap<>();
            ctrlMap.put("afterOpenAction", "3");
            List<Map<String, String>> pad_push_types = (List<Map<String, String>>) iParamBizService.actGetList("PAD_PUSH_TYPE").getD();
            for (Map<String, String> map : pad_push_types) {
                if ("comment".equals(map.get("code"))) {
                    ctrlMap.put("go_activity", map.get("activity"));
                    break;
                }
            }
            ctrlMap.put("extraFields", extraFields);
            msgRecordBean.setPushCtrlMap(ctrlMap);
            iAmqpBizService.actSendMq("WORKFLOW_COMMENT", new Object[]{billId}, msgRecordBean);

            WorkFlowBillBean workFlowBillBean = mappingService.map(workFlowBill, WorkFlowBillBean.class);
            workFlowBillBean.setParams(workFlowBill.getParams());
            return ResultBean.getSucceed().setD(workFlowBillBean).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWBILL_NULL"));
    }

    public ResultBean<WorkFlowBillBean> actSaveWorkflowBillBySourceIdAndFlowCode(String FlowCode, String SourceId, SignInfo signInfo) {
        WorkFlowBill workFlowBill = iWorkFlowBillService.getOneByBillIdAndBillTypeCode(SourceId, FlowCode);
        if (workFlowBill != null) {
            return actSaveWorkflowBill(workFlowBill.getId(), signInfo);
        } else {
            return ResultBean.getFailed();
        }
    }

    public ResultBean<WorkFlowBillBean> actCreateWorkflow(String businessTypeCode, String billId, String billTypeCode, String collectionName, Map<String, List<SignCondition>> params, String transactionId) {
        WorkFlowBill<SignInfo> workFlowBill = iWorkFlowBillService.getWorkflowBillByBillIdAndFlowCode(billId);
        if (workFlowBill == null) {
            workFlowBill = iWorkFlowBillService.save(new WorkFlowBill(businessTypeCode, billTypeCode, billId, collectionName, params, transactionId));
        } else { //如果工作流对象存在时，应该允许更新数据
            workFlowBill.setCollectionName(collectionName);
            workFlowBill.setParams(params);
            iWorkFlowBillService.save(workFlowBill);
        }
        String businessKey = billTypeCode + "." + billId;
        try {
            Boolean flag = iBusinessFlowService.businessBillIsFinish(businessKey);
            if (flag) {
                iBusinessFlowService.startProcess(billTypeCode, businessKey);
            }
        } catch (Exception e) {
            logger.error("新流程启动工作流错误", e);
            return ResultBean.getFailed().setM("新流程启动工作流错误，businessKey:" + billId + "." + billTypeCode);
        }
        WorkFlowBillBean workFlowBillBean = mappingService.map(workFlowBill, WorkFlowBillBean.class);
        workFlowBillBean.setParams(workFlowBill.getParams());
        return ResultBean.getSucceed().setD(workFlowBillBean);
    }

    /**
     * 签批
     *
     * @param billId
     * @param signInfo
     * @return
     * @throws Exception
     */
    public ResultBean<WorkFlowBillBean> actSignBill(String billId, SignInfo signInfo) throws Exception {
        return this.actSignBill(billId, signInfo, false);
    }

    /**
     * 签批
     *
     * @param billId
     * @param signInfo
     * @return
     * @throws Exception
     */
    // TODO: 2017/9/23 添加参数用于表明执行哪步操作，用于多分支时的判断条件
    public ResultBean<WorkFlowBillBean> actSignBill(String billId, SignInfo signInfo, Boolean isSpecial) throws Exception {
        WorkFlowBill<SignInfo> workFlowBill = iWorkFlowBillService.getWorkflowBillByBillIdAndFlowCode(billId);
        if (workFlowBill == null) {
            throw new Exception("未找到工作流对象workFlowBill, id:" + billId);
        }
        String entityName = workFlowBill.getCollectionName();
        String businessKey = workFlowBill.getActivitiId();
        Map vars = new HashMap<>();
        vars.put("approveStatus", signInfo.getResult());
        if (isSpecial) {
            TEMSignInfo s = (TEMSignInfo) signInfo;
            vars.putAll(s.getApproveVars());
        }
        LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(signInfo.getUserId()).getD();
        String activitiUserId = loginUserBean.getActivitiUserId();
        Task userTask = iBusinessFlowService.getUserBillTask(activitiUserId, businessKey);
        if (userTask == null) {
            throw new Exception("获取用户当前任务失败，activitiUserId:" + activitiUserId + ",businessKey：" + businessKey);
        }
        if (workFlowBill.getParams() != null && workFlowBill.getParams().containsKey(userTask.getTaskDefinitionKey())) {
            List<SignCondition> signConditions = workFlowBill.getParams().get(userTask.getTaskDefinitionKey());
            signConditions.forEach(signCondition -> {
                vars.putAll(getVarsMap(signCondition));
            });
        }
        List<Task> tasks = null; // 后续任务列表
        try {
            iBusinessFlowService.completeTask(userTask.getId(), activitiUserId, vars);
            tasks = iBusinessFlowService.getBillTasks(businessKey);
            if (tasks != null && tasks.size() > 0) {
                workFlowBill.setCurrentTask(tasks.get(0).getTaskDefinitionKey());
            } else {
                workFlowBill.setCurrentTask(null);
            }
            workFlowBill.setCurrentTasks(this.getCurrentTaskKeys(tasks)); //保存当前工作流中待办业务的键值
            workFlowBill.setCompletedTask(userTask.getTaskDefinitionKey());
            signInfo.setTaskInfo(userTask.getTaskDefinitionKey());
            workFlowBill.setApproveStatus(signInfo.getResult());
        } catch (Exception e) {
            throw new Exception("完成当前任务失败，businessKey：" + businessKey, e);
        }
        Boolean billIsFinish = iBusinessFlowService.businessBillIsFinish(businessKey);
        if (billIsFinish) { //记录工作流结束的时间
            workFlowBill.setDoneTime(DateTimeUtils.getCreateTime());
        }
        if (workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_PASSED || workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            if (!billIsFinish) {
                workFlowBill.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
            }
        }
        workFlowBill.getSignInfos().add(signInfo);
        workFlowBill = iWorkFlowBillService.save(workFlowBill);
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(billId)));
        Update update = Update.update("approveStatus", workFlowBill.getApproveStatus()).set("dataStatus", DataStatus.SAVE);
        // 记录最后处理日期与处理人
        update.set("approveDate", DateTimeUtils.getCreateTime()).set("approveUserId", signInfo.getUserId());
        if ("A022".equals(workFlowBill.getFlowCode())) { // 渠道信息单独处理
            if (workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { //审核通过
                update.set("status", 1).set("startDate", DateTimeUtils.getCreateTime());
            } else if (workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_REJECT) { //审核拒绝
                update.set("status", 9);
            }
        }
        mongoTemplate.updateMulti(query, update, entityName);
        BasicDBObject obj = mongoTemplate.findOne(query, BasicDBObject.class, entityName);
        if (obj != null && obj.containsField("customerTransactionId")) { // 此处内部为业务单据操作
            if (billIsFinish) {
                try {
                    this.actSignForTransaction(obj.get("customerTransactionId").toString(), workFlowBill.getBusinessTypeCode(), workFlowBill.getFlowCode(), signInfo);
                } catch (Exception e) {
                    throw new Exception("完成transaction任务失败，ID:" + obj.get("customerTransactionId").toString(), e);
                }
            }
            try {
                MsgRecordBean msgRecordBean = this.createMsgRecordBean(signInfo, tasks, workFlowBill, obj.get("customerTransactionId").toString(), loginUserBean.getId(), billIsFinish);
                String eventType = workFlowBill.getBusinessTypeCode() + "_" + workFlowBill.getFlowCode() + "_" + workFlowBill.getCompletedTask();
                if (isSpecial) {
                    TEMSignInfo s = (TEMSignInfo) signInfo;
                    if (s.getApproveVars() != null && s.getApproveVars().containsKey("final")) {
                        Integer isFinal = (Integer) s.getApproveVars().get("final");
                        if (!(signInfo.getResult() == SignInfo.SIGN_PASS && isFinal == 0)) {
                            iAmqpBizService.actSendMq(eventType, new Object[]{billId}, msgRecordBean);
                        }
                    }
                } else {
                    iAmqpBizService.actSendMq(eventType, new Object[]{billId}, msgRecordBean);
                }
            } catch (Exception e) {
                logger.error("发送Mq消息异常, businessEventType:" + workFlowBill.getBusinessTypeCode() + "_" + workFlowBill.getFlowCode() + "_" + workFlowBill.getCompletedTask(), e);
            }
        }
        WorkFlowBillBean workFlowBillBean = mappingService.map(workFlowBill, WorkFlowBillBean.class);
        workFlowBillBean.setParams(workFlowBill.getParams());
        return ResultBean.getSucceed().setD(workFlowBillBean);
    }

    public MsgRecordBean createMsgRecordBean(SignInfo signInfo, List<Task> tasks, WorkFlowBill workFlowBill, String customerTransactionId, String loginUserId, Boolean billIsFinish) {
        Map transactionMap = templateService.getTransactionMap(customerTransactionId);
        String eventType = workFlowBill.getBusinessTypeCode() + "_" + workFlowBill.getFlowCode() + "_" + workFlowBill.getCompletedTask();
        Map sendMap = new HashMap<>();
        Set<String> toSet = new HashSet<>();
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) { // 有后续任务，把任务相关人员找到
                List<IdentityLink> list = iBusinessFlowService.getIdentityLinksForTask(task.getId());
                if (list != null) {
                    for (IdentityLink il : list) {
                        if (!"G_SUBMIT".equals(il.getGroupId())) {
                            if ("G_MANAGER".equals(il.getGroupId())) { // 部门经理
                                List<LoginUserBean> userBeans = iAuthenticationBizService.actGetLoginUserByGroupId(il.getGroupId()).getD();
                                for (LoginUserBean userBean : userBeans) {
                                    EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(userBean.getId()).getD(); // 下一步审批人
                                    if (employeeBean != null) {
                                        EmployeeBean employee = iOrgBizService.actGetEmployee((String) transactionMap.get("employeeId")).getD(); // 分期经理
                                        if (employee != null) { // 当前分期经理
                                            OrgBean orgBean = iOrgBizService.actGetOrg(employee.getOrgInfoId()).getD();
                                            String orgId;
                                            if (orgBean != null) {
                                                if (orgBean.getVirtual()) {
                                                    OrgBean base = iOrgBizService.actGetOrg(orgBean.getParentId()).getD();
                                                    orgId = base.getId();
                                                    if (orgId.equals(employeeBean.getOrgInfoId()) && base.getLeaderId().equals(employeeBean.getId())) {
                                                        toSet.add(employeeBean.getId()); // 下一步流程操作人
                                                    }
                                                } else {
                                                    orgId = orgBean.getId();
                                                    if (orgId.equals(employeeBean.getOrgInfoId()) && orgBean.getLeaderId().equals(employeeBean.getId())) {
                                                        toSet.add(employeeBean.getId()); // 下一步流程操作人
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                List<LoginUserBean> userBeans = iAuthenticationBizService.actGetLoginUserByGroupId(il.getGroupId()).getD();
                                for (LoginUserBean userBean : userBeans) {
                                    EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(userBean.getId()).getD();
                                    if (employeeBean != null) {
                                        toSet.add(employeeBean.getId()); // 下一步流程操作人
                                    }
                                }
                            }
                        } else { // 分期经理
                            if (transactionMap.containsKey("employeeId")) {
                                toSet.add(transactionMap.get("employeeId").toString()); // 当前业务分期经理
                            }
                        }
                    }
                }
            }
        } else { // 任务结束通知分期经理
            if (transactionMap.containsKey("employeeId")) {
                toSet.add(transactionMap.get("employeeId").toString()); // 当前业务分期经理
            }
        }
//        EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(loginUserId).getD();
//        if (employeeBean != null) {
//            toSet.add(employeeBean.getId()); // 当前流程操作人
//        }
        List toList = new ArrayList<>();
        toList.addAll(toSet);
        sendMap.put("bd_employee", toList);
        // 这里的map是为了给pad Push数据时告诉pad跳转时使用
        Map ctrlMap = new HashMap<>();
        ctrlMap.put("afterOpenAction", PushDataBean.go_activity); // go activity
        // TODO: 2017/10/16 根据系统参配项获取activity
        String packageStr = getPushSendValue(billIsFinish, workFlowBill.getTransactionId(), workFlowBill.getFlowCode());

        ctrlMap.put("go_activity", packageStr);
        Map<String, String> extraFields = new HashMap<>();
        extraFields.put("businessType", workFlowBill.getBusinessTypeCode()); // 这个没用
        extraFields.put("billId", workFlowBill.getTransactionId());
        if ("com.fzfq.fuzecredit.task.contracte.activity.CustomerContractedActivity".equals(packageStr)) {
            BasicDBObject obj = mongoTemplate.findOne(new Query(Criteria.where("customerTransactionId").is(customerTransactionId)), BasicDBObject.class, "so_purchasecar");
            extraFields.put("id", obj.getString("_id"));
        }
        extraFields.put("messageType", "type_3");
        ctrlMap.put("extraFields", extraFields);
        Map<String, Integer> templateData = new HashMap<>();
        templateData.put("approveStatus", signInfo.getResult());
        MsgRecordBean msgRecordBean = new MsgRecordBean(eventType, customerTransactionId, templateData, null, sendMap);
        msgRecordBean.setPushCtrlMap(ctrlMap);
        return msgRecordBean;
    }

    public String getPushSendValue(Boolean finishThis, String transactionId, String flowCode) {
        WorkFlowBill<SignInfo> transactionBill = iWorkFlowBillService.getOneByBillId(transactionId);
        String completedTask = transactionBill.getCompletedTask();
        if (flowCode.equals(completedTask) || finishThis) { // 当前小流程已经完成，跳转到下个流程 银行制卡特例
            for (String currentTask : transactionBill.getCurrentTasks()) {
                flowCode = currentTask;
                break;
            }
        }
        logger.info("PushSendValue：跳转流程" + flowCode);
        List<Map<String, String>> pad_push_types = (List<Map<String, String>>) iParamBizService.actGetList("PAD_PUSH_TYPE").getD();
        if (pad_push_types == null) {
            logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"), "PAD_PUSH_TYPE"));
            return "";
        }
        for (Map<String, String> pad_push_type : pad_push_types) {
            if (!"A001".equals(flowCode)) {
                if (pad_push_type.get("code").equals(flowCode)) {
                    return pad_push_type.get("activity");
                }
            } else {
                if (pad_push_type.get("code").equals(flowCode + "_" + transactionBill.getBusinessTypeCode())) {
                    return pad_push_type.get("activity");
                }
            }
        }
        return "";

    }

    @Override
    public ResultBean<List<ExecutionBean>> actFindExecutionBySql(String sql, Map params) {
        List<Execution> list = iBusinessFlowService.findExecutionBySql(sql, params);
        return ResultBean.getSucceed().setD(mappingService.map(list, ExecutionBean.class));
    }

    private Map getVarsMap(SignCondition signCondition) {
        Map vars = new HashMap<>();
        if (signCondition.getField() != null && signCondition.getValue() != null) {
            if (signCondition.getRealValue()) {
                vars.put(signCondition.getField(), signCondition.getValue());
            } else {
                BasicDBObject bobj = metaDataService.getObjMap(signCondition.getField(), signCondition.getValue(), signCondition.getCollection());
                if (bobj != null && bobj.containsField(signCondition.getLabel())) {
                    vars.put(signCondition.getLabel(), bobj.get(signCondition.getLabel()));
                } else {
                    vars.put(signCondition.getLabel(), signCondition.getDefaultValue());
                }
            }
        } else if (signCondition.getCondition() != null) {
            Query query = new Query();
            signCondition.getCondition().forEach((field, value) -> {
                if ("_id".equals(field)) { // 特殊情况 ID
                    query.addCriteria(Criteria.where(field).is(new ObjectId(String.valueOf(value))));
                } else if (SignCondition.MONGOSORT.equals(field)) { // 特殊情况 排序
                    query.with(new Sort(Sort.Direction.DESC, String.valueOf(value)));
                } else {
                    query.addCriteria(Criteria.where(field).is(value));
                }
            });
            BasicDBObject bobj = mongoTemplate.find(query, BasicDBObject.class, signCondition.getCollection()).get(0);
            if (bobj != null && bobj.containsField(signCondition.getLabel())) {
                vars.put(signCondition.getLabel(), bobj.get(signCondition.getLabel()));
            } else {
                vars.put(signCondition.getLabel(), signCondition.getDefaultValue());
            }
        }
        return vars;
    }

    private List<String> getCurrentTaskKeys(List<Task> tasks) {

        List<String> taskKeys = new ArrayList<String>();
        tasks.forEach(task -> {
            taskKeys.add(task.getTaskDefinitionKey());
        });

        return taskKeys;
    }

    @Override
    public ResultBean<WorkFlowBillBean> actSignForTransaction(String billId, String businessTypeCode, String billTypeCode, SignInfo signInfo) throws Exception {
        WorkFlowBill<SignInfo> workFlowBill = iWorkFlowBillService.getOneByBillIdAndBillTypeCode(billId, businessTypeCode);
        String businessKey = workFlowBill.getFlowCode() + "." + workFlowBill.getSourceId();
        String activitiUserId = "root";
        List<Task> userBillTasks = iBusinessFlowService.getUserBillTasks(activitiUserId, businessKey);
        for (Task task : userBillTasks) {
            if (task.getTaskDefinitionKey().equals(billTypeCode)) {
                try {
                    Map vars = new HashMap<>();
                    vars.put("approveStatus", signInfo.getResult());
                    if (workFlowBill.getParams() != null && workFlowBill.getParams().containsKey(task.getTaskDefinitionKey())) {
                        List<SignCondition> signConditions = workFlowBill.getParams().get(task.getTaskDefinitionKey());
                        signConditions.forEach(signCondition -> {
                            vars.putAll(getVarsMap(signCondition));
                        });
                    }
                    iBusinessFlowService.completeTask(task.getId(), activitiUserId, vars);
                    List<Task> billTasks = iBusinessFlowService.getBillTasks(businessKey);
                    workFlowBill.setCurrentTask(null);
                    billTasks.forEach(billTask -> {
                        if (billTask.getTaskDefinitionKey().equals(billTypeCode)) {
                            workFlowBill.setCurrentTask(billTasks.get(0).getTaskDefinitionKey());
                        }
                    });
                    workFlowBill.setCompletedTask(task.getTaskDefinitionKey());
                    Boolean billIsFinish = iBusinessFlowService.businessBillIsFinish(businessKey);
                    if (billIsFinish) {
                        workFlowBill.setApproveStatus(signInfo.getResult());
                        String transactionId = workFlowBill.getSourceId();
                        Query query = Query.query(Criteria.where("_id").is(new ObjectId(transactionId)));
                        Update update;
                        if (signInfo.getResult() == ApproveStatus.APPROVE_REJECT) {
                            update = Update.update("status", 18); // 流程结束，交易被拒绝
                        } else { //工作流完成时不更新当前任务列表
                            update = Update.update("status", 8);
                        }
                        mongoTemplate.updateMulti(query, update, workFlowBill.getCollectionName());
                    }
                    if (signInfo.getResult() != ApproveStatus.APPROVE_REJECT) {
                        workFlowBill.setCurrentTasks(this.getCurrentTaskKeys(billTasks)); //保存当前工作流中待办业务的键值
                    }
                } catch (Exception e) {
                    logger.error("完成当前任务失败，businessKey：" + businessKey, e);
                    return ResultBean.getFailed().setM("完成当前任务失败，businessKey：" + businessKey);
                }
                workFlowBill.getSignInfos().add(signInfo);
                iWorkFlowBillService.save(workFlowBill);
            }
        }

        return ResultBean.getSucceed().setD(mappingService.map(workFlowBill, WorkFlowBillBean.class));
    }

    /**
     * 启动工作流并完成第一步操作
     *
     * @param billId
     * @param billTypeCode
     * @param signInfo
     * @param params
     * @return
     */
    @Override
    public ResultBean<WorkFlowBillBean> actSubmit(String businessTypeCode, String billId, String billTypeCode, SignInfo signInfo, String collectionName, Map<String, List<SignCondition>> params, String transactionId) {
        ResultBean r = actCreateWorkflow(businessTypeCode, billId, billTypeCode, collectionName, params, transactionId);
        if (r.failed()) {
            return r;
        }
        try {
            WorkFlowBillBean workFlowBill = this.actSignBill(billId, signInfo).getD();
            return ResultBean.getSucceed().setD(workFlowBill);
        } catch (Exception e) {
            logger.error("提交单据错误，ID:" + billId + ",TYPECODE:" + businessTypeCode + billTypeCode, e);
            return ResultBean.getFailed();
        }

    }

    public ResultBean<List<TaskBean>> actGetCurrentGroupAndCheckIsTask(String activitiUserId, String businessKey) {
        List<Task> list = iBusinessFlowService.getCurrentGroupAndCheckIsTask(activitiUserId, businessKey);
        List<TaskBean> taskInfos = new ArrayList<TaskBean>();
        for (Task task : list) {
            TaskBean taskInfo = new TaskBean();
            taskInfo.setName(task.getName());
            taskInfo.setId(task.getId());
            taskInfos.add(taskInfo);
        }
        return ResultBean.getSucceed().setD(taskInfos);
    }

    public ResultBean<List<SignInfo>> actGetSingInfosByFlowCodeAndSourceId(String flowCode, String sourceId) {
        List<SignInfo> list = new ArrayList<>();
        WorkFlowBill workFlowBill = this.iWorkFlowBillService.getOneByBillIdAndBillTypeCode(sourceId, flowCode);
        if (workFlowBill != null) {
            list = workFlowBill.getSignInfos();
        }
        return ResultBean.getSucceed().setD(list);
    }

    public ResultBean<List<SignInfo>> actGetSingInfosByFlowCodeAndTransactionId(String flowCode, String transactionId) {
        List<SignInfo> list = new ArrayList<>();
        WorkFlowBill workFlowBill = this.iWorkFlowBillService.getOneByTransactionIdAndBillTypeCode(transactionId, flowCode);
        if (workFlowBill != null) {
            list = workFlowBill.getSignInfos();
        }
        return ResultBean.getSucceed().setD(list);
    }

    public ResultBean actStopTransaction(String tid, String reason, String activitiUserId) {

        List<WorkFlowBill> list = iWorkFlowBillService.getAllTransactionFlow(tid);
        try {
            for (WorkFlowBill workflowBill : list) {
                iBusinessFlowService.stopProcessInstancekByBusinessKey(workflowBill.getActivitiId(), reason, activitiUserId);
                iWorkFlowBillService.stophBill(workflowBill, activitiUserId);
            }
            return ResultBean.getSucceed();
        } catch (Exception ex) {
            logger.error("终止业务失败，transactionId:" + tid, ex);
            return ResultBean.getFailed();
        }
    }

    public ResultBean<List<BillSignInfo>> actGetSingInfosByTransaction(String tid) {
        List<BillSignInfo> list = new ArrayList<BillSignInfo>();
        List<WorkFlowBill> workflowList = iWorkFlowBillService.getAllTransactionFlow(tid);
        for (WorkFlowBill workflowBill : workflowList) {
            if (!workflowBill.getFlowCode().equals("NC") && !workflowBill.getFlowCode().equals("OC")) {
                BillSignInfo billSignInfo = new BillSignInfo();
                billSignInfo.setBillTypeCode(workflowBill.getFlowCode());

                List<SignInfo> signInfos = workflowBill.getSignInfos();
                List<SignInfoBean> signInfoBeans = new ArrayList<SignInfoBean>();
                for (SignInfo signInfo : signInfos) {
                    SignInfoBean signInfoBean = mappingService.map(signInfo, SignInfoBean.class);
                    if (signInfo.getEmployeeId() != null) {
                        if (!"-1".equals(signInfo.getEmployeeId())) {
                            EmployeeBean employee = iOrgBizService.actGetEmployee(signInfo.getEmployeeId()).getD();
                            signInfoBean.setEmployeeName(employee.getUsername());
                            signInfoBean.setEmployeeAvatar(employee.getAvatarFileId());
                        } else {
                            signInfoBean.setEmployeeName("管理员");
                            signInfoBean.setEmployeeAvatar(null);
                        }
                    }
                    signInfoBeans.add(signInfoBean);
                }
                billSignInfo.setSignInfos(signInfoBeans);

                list.add(billSignInfo);
            }
        }
        if (list == null || list.size() == 0) {
            return ResultBean.getFailed().setM("该用户当前任务为空!");
        }

        return ResultBean.getSucceed().setD(list);
    }

    @Override
    public ResultBean<WorkFlowBillBean> actSaveWorkFlowBill(WorkFlowBillBean workFlowBillBean) {
        WorkFlowBill workFlowBill = mappingService.map(workFlowBillBean, WorkFlowBill.class);
        workFlowBill = iWorkFlowBillService.save(workFlowBill);
        return ResultBean.getSucceed().setD(mappingService.map(workFlowBill, WorkFlowBillBean.class));
    }

    public ResultBean<WorkFlowBillBean> actReset(APIBillBean bill) {

        WorkFlowBill workFlowBill = iWorkFlowBillService.getOneByBillIdAndBillTypeCode(bill.getId(), bill.getBillTypeCode());
        if (workFlowBill != null) {
            LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(bill.getLoginUserId()).getD();
            String activitiUserId = loginUserBean.getActivitiUserId();

            try {
                iBusinessFlowService.stopProcessInstancekByBusinessKey(bill.getBusinessTypeCode(), "重新审批!", activitiUserId);

                ResultBean result = this.actCreateWorkflow(bill.getBusinessTypeCode(), bill.getId(), bill.getBillTypeCode(), workFlowBill.getCollectionName(), workFlowBill.getParams(), bill.getCustomerTransactionId());
                if (result.failed()) return result;

                SignInfo signInfo = new SignInfo();
                signInfo.setUserId(bill.getLoginUserId());
                signInfo.setEmployeeId(bill.getEmployeeId());
                signInfo.setFromSalesman(false);
                signInfo.setResult(SignInfo.SIGN_PASS);
                signInfo.setFlag(SignInfo.FLAG_AGAIN_COMMIT);
                signInfo.setComment("重新审批!");

                WorkFlowBillBean bean = (WorkFlowBillBean) result.getD();
                bean.getSignInfos().add(signInfo);

                workFlowBill = mappingService.map(bean, WorkFlowBill.class);
                workFlowBill.setParams(bean.getParams());
                workFlowBill = iWorkFlowBillService.save(workFlowBill);

                Query query = Query.query(Criteria.where("_id").is(new ObjectId(bill.getId())));
                Update update = Update.update("approveStatus", ApproveStatus.APPROVE_INIT);
                mongoTemplate.updateMulti(query, update, workFlowBill.getCollectionName());

                return ResultBean.getSucceed().setD(mappingService.map(workFlowBill, WorkFlowBillBean.class));
            } catch (Exception ex) {
                return ResultBean.getFailed().setM("");
            }
        } else {
            return ResultBean.getFailed().setM("");
        }
    }

    @Override
    public ResultBean actDiscardBillWorkFlow(String billId, String activitiUserId) {
        WorkFlowBill workFlowBill = iWorkFlowBillService.getOneByBillId(billId);
        if (workFlowBill != null) {
            iWorkFlowBillService.discard(workFlowBill.getId());

            try {
                iBusinessFlowService.stopProcessInstancekByBusinessKey(workFlowBill.getActivitiId(), "", activitiUserId);
            } catch (Exception ex) {
                return ResultBean.getSucceed();
            }
        }
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<Boolean> actStopWorkFlow(String businessKey) {
        return iBusinessFlowService.stopWorkFlow(businessKey);
    }


}
