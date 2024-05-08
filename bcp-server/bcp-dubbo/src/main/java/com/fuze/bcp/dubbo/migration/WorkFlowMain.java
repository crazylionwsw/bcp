package com.fuze.bcp.dubbo.migration;

import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.dubbo.migration.activiti.WorkFlowUtils;
import com.fuze.bcp.dubbo.migration.mongo.MongoConnect;
import com.fuze.bcp.dubbo.migration.rabbit.RabbitMqUtils;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.workflow.domain.WorkFlowBill;
import org.activiti.engine.task.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/11/14.
 */
public class WorkFlowMain {

    public static void main(String[] args) throws Exception {
        MongoTemplate target = MongoConnect.getMongoTemplate("172.16.2.5", 27017, "mongolive", "admin", "FuzefenqiPa88word", "bcp_v11");
        Query query = new Query(Criteria.where("status").ne(CustomerTransactionBean.TRANSACTION_FINISH));
        List<CustomerTransaction> customerTransactions = target.find(query, CustomerTransaction.class);
        for (int i = 0; i < customerTransactions.size(); i = i + 150) {
            int index = i;
            Thread thread = new Thread(() -> {
                WorkFlowUtils workFlowUtils = new WorkFlowUtils();
                RabbitMqUtils rabbitMqUtils = null;
                try {
                    rabbitMqUtils = new RabbitMqUtils();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < 150 && index + j < customerTransactions.size(); j++) {
                    CustomerTransaction transaction = customerTransactions.get(index + j);
                    try {
                        System.out.println(index + j + ":transaction 开始，id：" + transaction.getId());
                        String transactionId = transaction.getId();
                /*
                doneTime
                signInfos
                currentTasks
                currentTask
                completedTask
                 */
                        Query queryWorkflow = new Query(Criteria.where("sourceId").is(transactionId));
                        // 查询业务
                        WorkFlowBill transactionWorkflow = target.findOne(queryWorkflow, WorkFlowBill.class);
                        try {
                            Boolean flag = workFlowUtils.businessBillIsFinish(transactionWorkflow.getActivitiId());
                            if (flag) {
                                workFlowUtils.startProcess(transactionWorkflow.getFlowCode(), transactionWorkflow.getActivitiId());
                            }
                        } catch (Exception e) {
                            System.out.println("error transactionId:" + transactionId + "阶段:创建新业务");
                        }
                        Query queryTransaction = new Query(Criteria.where("customerTransactionId").is(transactionId));

                        // 资质
                        CustomerDemand customerDemand = target.findOne(queryTransaction, CustomerDemand.class);
                        if (customerDemand == null) {
                            continue;
                        } else {
                            Map vars = new HashMap<>();
                            vars.put("approveStatus", customerDemand.getApproveStatus());
                            doBill(target, transaction, customerDemand, transactionWorkflow, workFlowUtils, vars);
                        }

                        // 签约
                        PurchaseCarOrder order = target.findOne(queryTransaction, PurchaseCarOrder.class);
                        if (order == null) {
                            if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                    || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                if (customerDemand != null && (customerDemand.getApproveStatus() == ApproveStatus.APPROVE_ONGOING
                                        || customerDemand.getApproveStatus() == ApproveStatus.APPROVE_PASSED
                                        || customerDemand.getApproveStatus() == ApproveStatus.APPROVE_REAPPLY)) { // 单据校验
                                    //初始化二手车评估单
                                    if (customerDemand.getBusinessTypeCode().equals("OC")) {
                                        rabbitMqUtils.sendBusinessMsg(customerDemand.getBusinessTypeCode() + "_A001_CustomerDemand_CarValuation", new Object[]{customerDemand.getId()}, target);
                                    }
                                    //初始化签约
                                    rabbitMqUtils.sendBusinessMsg(customerDemand.getBusinessTypeCode() + "_A001_CustomerDemand_Submit", new Object[]{customerDemand.getId()}, target);
                                }
                            }
                            continue;
                        } else {
                            Map vars = new HashMap<>();
                            vars.put("approveStatus", order.getApproveStatus());
                            doBill(target, transaction, order, transactionWorkflow, workFlowUtils, vars);
                        }

                        Query x = new Query();
                        x.addCriteria(Criteria.where("_id").is(new ObjectId(order.getCustomerLoanId())));
                        CustomerLoan customerLoan = target.findOne(x, CustomerLoan.class);
                        Integer isNeedPayment = customerLoan.getIsNeedPayment(); // 垫资
                        // 银行制卡
                        BankCardApply bankCardApply = target.findOne(queryTransaction, com.fuze.bcp.creditcar.domain.BankCardApply.class);
                        if (bankCardApply == null) {
                            if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                    || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                if (order != null && (order.getApproveStatus() == ApproveStatus.APPROVE_ONGOING
                                        || order.getApproveStatus() == ApproveStatus.APPROVE_PASSED
                                        || order.getApproveStatus() == ApproveStatus.APPROVE_REAPPLY)) { // 单据校验
                                    //初始化卡业务
                                    rabbitMqUtils.sendBusinessMsg(order.getBusinessTypeCode() + "_A002_Order_Submit", new Object[]{order.getId()}, target);
                                }
                            }
                            continue;
                        } else {
                            WorkFlowBill bankCardApplyW = target.findOne(new Query(Criteria.where("sourceId").is(bankCardApply.getId())), WorkFlowBill.class);
                            if (!(bankCardApply.getSwipingShopTime() != null || bankCardApply.getSwipingTrusteeTime() != null || bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_CANCEL)) {
                                String businessKey = bankCardApplyW.getActivitiId();
                                try {
                                    Boolean flag = workFlowUtils.businessBillIsFinish(businessKey);
                                    if (flag) {
                                        workFlowUtils.startProcess(bankCardApplyW.getFlowCode(), businessKey);
                                    }
                                    if (bankCardApply.getStatus() != BankCardApplyBean.BKSTATUS_INIT) {
                                        List<Task> tasklist = workFlowUtils.getBillTasks(businessKey);
                                        for (Task task : tasklist) {
                                            Map variables = new HashMap<>();
                                            variables.put("approveStatus", ApproveStatus.APPROVE_PASSED);
                                            workFlowUtils.endTask(task.getId(), variables);
                                        }
                                        CardActionRecord c = new CardActionRecord();
                                        c.setAction("申请制卡");
                                        c.setActionName("自动创建");
                                        c.setTs(DateTimeUtils.getCreateTime());
                                        c.setDataStatus(2);
                                        List list = new ArrayList<>();
                                        list.add(c);
                                        bankCardApply.setActionRecords(list);
                                        bankCardApply.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
                                        bankCardApply.setStatus(BankCardApplyBean.BKSTATUS_APPLY);
                                    } else if (bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_INIT) {
                                        List list = new ArrayList<>();
                                        bankCardApply.setActionRecords(list);
                                        bankCardApply.setApproveStatus(ApproveStatus.APPROVE_INIT);
                                        bankCardApply.setStatus(BankCardApplyBean.BKSTATUS_INIT);
                                    }
                                    target.save(bankCardApply);
                                } catch (Exception e) {
                                    System.out.println("error:" + bankCardApplyW.getFlowCode() + " transactionId:" + transactionId + "阶段:制卡阶段提交：" + businessKey);
                                    throw new Exception(e);
                                }

                                // 大流程
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", ApproveStatus.APPROVE_PASSED);
                                vars.put("isNeedPayment", isNeedPayment);
                                List<Task> userBillTasks = workFlowUtils.getUserBillTasks("root", transactionWorkflow.getActivitiId());
                                for (Task task : userBillTasks) {
                                    if (task.getTaskDefinitionKey().equals(bankCardApply.getBillTypeCode())) {
                                        workFlowUtils.completeTask(task.getId(), "root", vars);
                                    }
                                }
                                List<Task> list = workFlowUtils.getBillTasks(transactionWorkflow.getActivitiId());
                                if (list.size() > 0) {
                                    transactionWorkflow.setCurrentTask(list.get(0).getTaskDefinitionKey());
                                    transactionWorkflow.setCurrentTasks(getCurrentTaskKeys(workFlowUtils.getBillTasks(transactionWorkflow.getActivitiId())));
                                }
                                transactionWorkflow.setCompletedTask(bankCardApply.getBillTypeCode());
                            } else {
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", ApproveStatus.APPROVE_PASSED);
                                vars.put("isNeedPayment", isNeedPayment);
                                List<Task> userBillTasks = workFlowUtils.getUserBillTasks("root", transactionWorkflow.getActivitiId());
                                for (Task task : userBillTasks) {
                                    if (task.getTaskDefinitionKey().equals(bankCardApply.getBillTypeCode())) {
                                        workFlowUtils.completeTask(task.getId(), "root", vars);
                                    }
                                }
                                List<Task> list = workFlowUtils.getBillTasks(transactionWorkflow.getActivitiId());
                                if (list.size() > 0) {
                                    transactionWorkflow.setCurrentTask(list.get(0).getTaskDefinitionKey());
                                    transactionWorkflow.setCurrentTasks(getCurrentTaskKeys(workFlowUtils.getBillTasks(transactionWorkflow.getActivitiId())));
                                }
                                transactionWorkflow.setCompletedTask(bankCardApply.getBillTypeCode());
                            }
                            target.save(transactionWorkflow);
                        }
                        Integer isNeedLoaning = 0;

                        if (isNeedPayment == 0) { // 不垫资
                            // 预约刷卡
                            AppointSwipingCard appointSwipingCard = target.findOne(queryTransaction, AppointSwipingCard.class);
                            if (appointSwipingCard == null) {
                                if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                        || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                    if (order != null && order.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                        //初始化预约刷卡
                                        rabbitMqUtils.sendBusinessMsg(order.getBusinessTypeCode() + "_A002_Order_Complete", new Object[]{order.getId()}, target);
                                    }
                                }
                                continue;
                            } else {
                                isNeedLoaning = appointSwipingCard.getIsNeedLoaning();
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", appointSwipingCard.getApproveStatus());
                                vars.put("isNeedLoaning", isNeedLoaning);
                                doBill(target, transaction, appointSwipingCard, transactionWorkflow, workFlowUtils, vars);
                            }
                            if (isNeedLoaning == 1) {
                                AppointPayment appointPayment = target.findOne(queryTransaction, AppointPayment.class);
                                if (appointPayment == null) {
                                    if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                            || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                        if (order != null && order.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                            //初始化预约垫资
                                            rabbitMqUtils.sendBusinessMsg(order.getBusinessTypeCode() + "_A002_Order_Complete", new Object[]{order.getId()}, target);
                                        }
                                    }
                                    continue;
                                } else {
                                    isNeedLoaning = appointPayment.getIsNeedLoaning();
                                    Map vars = new HashMap<>();
                                    vars.put("approveStatus", appointPayment.getApproveStatus());
                                    vars.put("isNeedLoaning", isNeedLoaning);
                                    doBill(target, transaction, appointPayment, transactionWorkflow, workFlowUtils, vars);
                                }
                            }
                            // 渠道刷卡
                            SwipingCard swipingCard = target.findOne(queryTransaction, SwipingCard.class);
                            if (swipingCard == null) {
                                if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                        || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                    if (appointSwipingCard != null && appointSwipingCard.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                        //初始化渠道刷卡
                                        rabbitMqUtils.sendBusinessMsg(appointSwipingCard.getBusinessTypeCode() + "_A026_AppointSwipingCard_Confirm", new Object[]{appointSwipingCard.getId()}, target);
                                    }
                                }
                                continue;
                            } else {
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", swipingCard.getApproveStatus());
                                doBill(target, transaction, swipingCard, transactionWorkflow, workFlowUtils, vars);
                            }

                        } else if (isNeedPayment == 1) { // 垫资
                            // 预约垫资
                            AppointPayment appointPayment = target.findOne(queryTransaction, AppointPayment.class);
                            if (appointPayment == null) {
                                if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                        || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                    if (order != null && order.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                        //初始化预约垫资
                                        rabbitMqUtils.sendBusinessMsg(order.getBusinessTypeCode() + "_A002_Order_Complete", new Object[]{order.getId()}, target);
                                    }
                                }
                                continue;
                            } else {
                                isNeedLoaning = appointPayment.getIsNeedLoaning();
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", appointPayment.getApproveStatus());
                                vars.put("isNeedLoaning", isNeedLoaning);
                                doBill(target, transaction, appointPayment, transactionWorkflow, workFlowUtils, vars);
                            }
                        }
                        if ("NC".equals(transaction.getBusinessTypeCode())) { // 车辆上牌
                            // 车辆上牌
                            CarRegistry carRegistry = target.findOne(queryTransaction, CarRegistry.class);
                            SwipingCard swipingCard = target.findOne(queryTransaction, SwipingCard.class);
                            AppointPayment appointPayment = target.findOne(queryTransaction, AppointPayment.class);
                            if (carRegistry == null) {
                                if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                        || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                    if (appointPayment != null && appointPayment.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                        //垫资初始化车辆上牌
                                        rabbitMqUtils.sendBusinessMsg(customerDemand.getBusinessTypeCode() + "_A004_AppointPayment_Pay", new Object[]{appointPayment.getId()}, target);
                                    }
                                    if (swipingCard != null && swipingCard.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                        //刷卡初始化车辆上牌
                                        rabbitMqUtils.sendBusinessMsg(swipingCard.getBusinessTypeCode() + "_A019_SwipingCard_Confirm", new Object[]{swipingCard.getId()}, target);
                                    }
                                }
                                continue;
                            } else {
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", carRegistry.getApproveStatus());
                                doBill(target, transaction, carRegistry, transactionWorkflow, workFlowUtils, vars);
                            }
                        } else if ("OC".equals(transaction.getBusinessTypeCode())) { // 转移过户
                            // 转移过户
                            CarTransfer carTransfer = target.findOne(queryTransaction, CarTransfer.class);
                            if (carTransfer == null) {
                                if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                        || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                    if (order != null && order.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                        //初始化转移过户
                                        rabbitMqUtils.sendBusinessMsg(order.getBusinessTypeCode() + "_A002_Order_Complete", new Object[]{order.getId()}, target);
                                    }
                                }
                                continue;
                            } else {
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", carTransfer.getApproveStatus());
                                doBill(target, transaction, carTransfer, transactionWorkflow, workFlowUtils, vars);
                            }
                        }
                        // 抵押登记
                        DMVPledge dmvPledge = target.findOne(queryTransaction, DMVPledge.class);
                        CarRegistry carRegistry = target.findOne(queryTransaction, CarRegistry.class);
                        CarTransfer carTransfer = target.findOne(queryTransaction, CarTransfer.class);
                        if (dmvPledge == null) {
                            if (transaction != null && (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING
                                    || transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING)) {//业务校验
                                if (carRegistry != null && carRegistry.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                    //初始化新车抵押登记
                                    rabbitMqUtils.sendBusinessMsg(carRegistry.getBusinessTypeCode() + "_A005_CarRegistry_Approval", new Object[]{carRegistry.getId()}, target);
                                }
                                if (carTransfer != null && carTransfer.getApproveStatus() == ApproveStatus.APPROVE_PASSED) { // 单据校验
                                    //初始化二手车抵押登记
                                    rabbitMqUtils.sendBusinessMsg(carTransfer.getBusinessTypeCode() + "_A005_CarRegistry_Approval", new Object[]{carTransfer.getId()}, target);
                                }
                            }
                            continue;
                        } else {
                            if (dmvPledge.getPledgeEndTime() != null && !"".equals(dmvPledge.getPledgeEndTime())) { // 抵押时间ok，流程结束
                                Map vars = new HashMap<>();
                                vars.put("approveStatus", ApproveStatus.APPROVE_PASSED);
                                List<Task> userBillTasks = workFlowUtils.getUserBillTasks("root", transactionWorkflow.getActivitiId());
                                for (Task task : userBillTasks) {
                                    if (task.getTaskDefinitionKey().equals(dmvPledge.getBillTypeCode())) {
                                        System.out.println("************完成流程：" + dmvPledge.getBillTypeCode());
                                        workFlowUtils.completeTask(task.getId(), "root", vars);
                                    }
                                }
                                transactionWorkflow.setCurrentTask(null);
                                transactionWorkflow.setCurrentTasks(new ArrayList<>());
                                transactionWorkflow.setCompletedTask(dmvPledge.getBillTypeCode());
                                transactionWorkflow.setApproveStatus(ApproveStatus.APPROVE_PASSED);
                                target.save(transactionWorkflow);
                                transaction.setStatus(CustomerTransactionBean.TRANSACTION_FINISH);
                                target.save(transaction);
                            } else { // 启动小流程
                                System.out.println("******工作流，billId：" + dmvPledge.getId() + "，当前阶段:" + dmvPledge.getBillTypeCode());
                                WorkFlowBill dmvPledgeW = target.findOne(new Query(Criteria.where("sourceId").is(dmvPledge.getId())), WorkFlowBill.class);
                                String businessKey = dmvPledgeW.getActivitiId();
                                try {
                                    Boolean flag = workFlowUtils.businessBillIsFinish(businessKey);
                                    if (flag) {
                                        workFlowUtils.startProcess(dmvPledgeW.getFlowCode(), businessKey);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error Id:" + dmvPledgeW.getActivitiId() + "阶段:" + dmvPledgeW.getFlowCode());
                                }
                                System.out.println("************设置当前业务当前阶段:" + dmvPledge.getBillTypeCode());
                                transactionWorkflow.setCurrentTask(dmvPledgeW.getFlowCode());
                                List list = new ArrayList<>();
                                list.add(dmvPledgeW.getFlowCode());
                                transactionWorkflow.setCurrentTasks(list);
                                if (dmvPledge.getApproveStatus() != ApproveStatus.APPROVE_REAPPLY && dmvPledge.getApproveStatus() != ApproveStatus.APPROVE_REJECT) {
                                    dmvPledge.setApproveStatus(ApproveStatus.APPROVE_INIT);
                                    dmvPledgeW.setSignInfos(new ArrayList<>());
                                }
                                target.save(dmvPledgeW);
                                target.save(dmvPledge);
                                target.save(transactionWorkflow);
                            }
                        }
                        try {
                            Criteria criteria = Criteria.where("dataStatus").is(1);
                            criteria.orOperator(Criteria.where("orderId").is(order.getId()),Criteria.where("orderId").is(customerDemand.getId()));
                            Query queryCancel = new Query(criteria);
                            CancelOrder cancelOrder = target.findOne(queryCancel, CancelOrder.class);
                            if (cancelOrder != null) {
                                String businessKey = cancelOrder.getBillTypeCode() + "." +  cancelOrder.getId();
                                if (cancelOrder.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
                                    Boolean flag = workFlowUtils.businessBillIsFinish(businessKey);
                                    if (flag) {
                                        workFlowUtils.startProcess(cancelOrder.getBillTypeCode(), businessKey);
                                    }
                                    List<Task> userBillTasks = workFlowUtils.getUserBillTasks(cancelOrder.getLoginUserId(), businessKey);
                                    for (Task task : userBillTasks) {
                                        if (task.getTaskDefinitionKey().equals(cancelOrder.getBillTypeCode())) {
                                            Map map = new HashMap<>();
                                            map.put("approveStatus", ApproveStatus.APPROVE_PASSED);
                                            System.out.println("************执行流程：" + cancelOrder.getBillTypeCode());
                                            workFlowUtils.completeTask(task.getId(), cancelOrder.getLoginUserId(), map);
                                        }
                                    }
                                    cancelOrder.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
                                    target.save(cancelOrder);
                                }
                            }
                        }catch (Exception e){
                            System.out.println("error cancelOrder:cardemandId:"+customerDemand.getId()+"orderId"+order.getId());
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        File file = new File(transaction.getId());
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        OutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file, true);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                        try {
                            writer.write("业务:" + transaction.getId());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            writer.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }


                    }
                }
            });
            thread.start();
        }
    }

    private static void doBill(MongoTemplate target, CustomerTransaction transaction, BaseBillEntity baseBillEntity, WorkFlowBill transactionFlow, WorkFlowUtils workFlowUtils, Map vars) {
        System.out.println("******工作流，billId：" + baseBillEntity.getId() + "，当前阶段:" + baseBillEntity.getBillTypeCode());
        WorkFlowBill billFlow = target.findOne(new Query(Criteria.where("sourceId").is(baseBillEntity.getId())), WorkFlowBill.class);
        if (billFlow == null) {
            billFlow = new WorkFlowBill();
            billFlow.setFlowCode(baseBillEntity.getBillTypeCode());
            billFlow.setSourceId(baseBillEntity.getId());
            billFlow.setTransactionId(transaction.getId());
            billFlow.setActivitiId(billFlow.getFlowCode() + "." + billFlow.getSourceId());
            Document document = baseBillEntity.getClass().getAnnotation(Document.class);
            billFlow.setCollectionName(document.collection());
            billFlow.setBusinessTypeCode(baseBillEntity.getBusinessTypeCode());
            billFlow.setApproveStatus(baseBillEntity.getApproveStatus());
            billFlow.setDataStatus(1);
            billFlow.setTs(baseBillEntity.getTs());
            target.save(billFlow);
        }
        if (baseBillEntity.getApproveStatus() == ApproveStatus.APPROVE_ONGOING || baseBillEntity.getApproveStatus() == ApproveStatus.APPROVE_REAPPLY) {
            System.out.println("******工作流阶段" + baseBillEntity.getBillTypeCode() + "未完成，执行doCurrentBill");
            doCurrentBill(target, baseBillEntity, transactionFlow, billFlow, workFlowUtils);
        } else if (baseBillEntity.getApproveStatus() == ApproveStatus.APPROVE_REJECT || baseBillEntity.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
            try {
                System.out.println("******工作流阶段" + baseBillEntity.getBillTypeCode() + "已经完成，执行doFinishBill");
                doFinishBill(target, transaction, baseBillEntity, transactionFlow, billFlow, vars, workFlowUtils);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void doCurrentBill(MongoTemplate target, BaseBillEntity baseBillEntity, WorkFlowBill transactionFlow, WorkFlowBill billFlow, WorkFlowUtils workFlowUtils) {
        System.out.println("******工作流，billId：" + baseBillEntity.getId() + "，当前阶段:" + baseBillEntity.getBillTypeCode());
        String businessKey = billFlow.getActivitiId();
        try {
            Boolean flag = workFlowUtils.businessBillIsFinish(businessKey);
            if (flag) {
                workFlowUtils.startProcess(billFlow.getFlowCode(), businessKey);
            }
        } catch (Exception e) {
            System.out.println("error Id:" + billFlow.getActivitiId() + "阶段:" + billFlow.getFlowCode());
        }
        System.out.println("************设置当前业务当前阶段:" + baseBillEntity.getBillTypeCode());
        transactionFlow.setCurrentTask(billFlow.getFlowCode());

        List list = new ArrayList<>();
        list.add(billFlow.getFlowCode());
        transactionFlow.setCurrentTasks(list);
        if (baseBillEntity.getApproveStatus() != ApproveStatus.APPROVE_REAPPLY && baseBillEntity.getApproveStatus() != ApproveStatus.APPROVE_REJECT) {
            baseBillEntity.setApproveStatus(ApproveStatus.APPROVE_INIT);
            billFlow.setSignInfos(new ArrayList<>());
        }
        target.save(billFlow);
        target.save(baseBillEntity);
        target.save(transactionFlow);
    }

    private static void doFinishBill(MongoTemplate target, CustomerTransaction transaction, BaseBillEntity baseBillEntity, WorkFlowBill transactionFlow, WorkFlowBill billFlow, Map vars, WorkFlowUtils workFlowUtils) throws Exception {
        transactionFlow.getSignInfos().addAll(billFlow.getSignInfos());
        if (baseBillEntity.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            System.out.println("************任务被拒绝，流程结束");
            transaction.setStatus(CustomerTransactionBean.TRANSACTION_STOP);
            target.save(transaction);
        }
        List<Task> userBillTasks = workFlowUtils.getUserBillTasks("root", transactionFlow.getActivitiId());
        for (Task task : userBillTasks) {
            if (task.getTaskDefinitionKey().equals(baseBillEntity.getBillTypeCode())) {
                System.out.println("************完成流程：" + baseBillEntity.getBillTypeCode());
                workFlowUtils.completeTask(task.getId(), "root", vars);
            }
        }
        transactionFlow.setCompletedTask(billFlow.getFlowCode());
        List<Task> tasks = workFlowUtils.getBillTasks(transactionFlow.getActivitiId());
        if (tasks.size() > 0) {
            System.out.println("************待执行流程：" + getCurrentTaskKeys(tasks));
            transactionFlow.setCurrentTask(tasks.get(0).getTaskDefinitionKey());
            transactionFlow.setCurrentTasks(getCurrentTaskKeys(tasks));
        }
        target.save(transactionFlow);
    }

    private static List<String> getCurrentTaskKeys(List<Task> tasks) {

        List<String> taskKeys = new ArrayList<String>();
        tasks.forEach(task -> {
            taskKeys.add(task.getTaskDefinitionKey());
        });

        return taskKeys;
    }

}