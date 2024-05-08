package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyListBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardListBean;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.bean.CustomerRepaymentBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.workflow.bean.ExecutionBean;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TaskBean;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.EncryUtil;
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Lily on 2017/8/21.
 */
@Service
public class BizBankCardApplyService implements IBankCardApplyBizService {

    private final Logger logger = LoggerFactory.getLogger(BizBankCardApplyService.class);

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ISwipingCardService iSwipingCardService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IFileBizService iFileBizService;

    @Override
    public ResultBean<List<BankCardApplyBean>> actGetBankCardApplyByCardStep(String step) {
        Map map = new HashMap<>();
        map.put("ACT_ID_", step);
        List<ExecutionBean> list = iWorkflowBizService.actFindExecutionBySql("select * from ACT_RU_EXECUTION E where E.ACT_ID_ = #{ACT_ID_} and BUSINESS_KEY_ like 'A011%' ", map).getD();
        List<BankCardApply> bankCardApplies = new ArrayList<>();
        for (ExecutionBean executionBean : list) {
            String key = executionBean.getBusinessKey();
            key = key.substring(key.indexOf('.') + 1);
            BankCardApply bankCardApply = iBankCardApplyService.getAvailableOne(key);
            if (bankCardApply != null) {
                bankCardApplies.add(bankCardApply);
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(bankCardApplies, BankCardApplyBean.class));
    }


    @Override
    public ResultBean<BankCardApplyBean> actCreateBankCardApply(String orderId) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.getOne(orderId);
        if (purchaseCarOrder == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(purchaseCarOrder.getCustomerTransactionId()).getD();
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
        if (bankCardApply != null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_ID_EXIST"));
        }
        bankCardApply = new BankCardApply();
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        if (customerLoanBean != null) {//垫资的时候初始化一些数据，不垫资的时候直接从渠道刷卡录入
            //从系统参数获取账单日
            List data = (List) iParamBizService.actGetList("DEALER_REPAYMENT_DAY").getD();
            if (customerLoanBean != null) {
                Map<String, String> mapData = (Map<String, String>) data.get(0);
                int billDate = Integer.parseInt(mapData.get("name"));
                bankCardApply.setSwipingMoney(customerLoanBean.getSwipingAmount());
                bankCardApply.setDefaultReimbursement(5);
                bankCardApply.setBillingDate(billDate);
                bankCardApply.setSwipingPeriods(customerLoanBean.getRateType().getMonths());
            }
        }

        bankCardApply.setBusinessTypeCode(purchaseCarOrder.getBusinessTypeCode());
        bankCardApply.setLoginUserId(purchaseCarOrder.getLoginUserId());
        bankCardApply.setEmployeeId(purchaseCarOrder.getEmployeeId());
        bankCardApply.setCustomerId(purchaseCarOrder.getCustomerId());
        bankCardApply.setCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
        bankCardApply.setCashSourceId(customerTransaction.getCashSourceId());
        return this.actStartBankCardApply(mappingService.map(bankCardApply, BankCardApplyBean.class));
    }

    @Override
    public ResultBean<BankCardApplyBean> actSaveBankCardApply(BankCardApplyBean bankCardApplyBean) {
        BankCardApply bankCardApply = iBankCardApplyService.save(mappingService.map(bankCardApplyBean, BankCardApply.class));
        return ResultBean.getSucceed().setD(mappingService.map(bankCardApply, BankCardApplyBean.class));
    }

    @Override
    public ResultBean<BankCardApplyBean> actFindBankCardApplyById(String id) {
        BankCardApply bankCardApply = iBankCardApplyService.getOne(id);
        if (bankCardApply != null) {
            String code = bankCardApply.getBillTypeCode();
            //通过编码获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            BankCardApplyBean bankCardApplyBean = mappingService.map(bankCardApply, BankCardApplyBean.class);
            bankCardApplyBean.setBillType(billType);
            //获取卡信息
            CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
            if (customerCardBean != null) {
                bankCardApplyBean.setCustomerCard(customerCardBean);
            }
            //获取借款信息
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(bankCardApply.getCustomerTransactionId());
            if (purchaseCarOrder != null) {
                CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
                if (customerLoanBean != null) {
                    bankCardApplyBean.setCustomerLoanBean(customerLoanBean);
                }
            }
            return ResultBean.getSucceed().setD(bankCardApplyBean);
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    @Override
    public ResultBean<BankCardApplyBean> actFindBankCardApplyByTransactionId(String transactionId) {
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(transactionId);
        if (bankCardApply != null) {
            return ResultBean.getSucceed().setD(mappingService.map(bankCardApply, BankCardApplyBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    @Override
    public ResultBean<BankCardApplyBean> actSearchBankCardApply(SearchBean searchBean) {
        Page<BankCardApply> bankCardApply = iBankCardApplyService.findAllBySearchBean(BankCardApply.class, searchBean, SearchBean.STAGE_ORDER,null);
        return ResultBean.getSucceed().setD(mappingService.map(bankCardApply, BankCardApplyBean.class));
    }

    @Override
    public ResultBean<BankCardApplyBean> actFindBankCardApplyByPurchaseCarOrderId(String purchaseCarOrderId) {
        return null;
    }

    @Override
    public ResultBean<BankCardApplyBean> actStartBankCardApply(BankCardApplyBean bankCardApplyBean) {
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(bankCardApplyBean.getCustomerTransactionId());
        if (bankCardApply != null && bankCardApply.getStatus() != BankCardApplyBean.BKSTATUS_INIT) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_ID_EXIST"));
        } else {
            //设置卡业务的初始状态
            bankCardApplyBean.setApproveStatus(ApproveStatus.APPROVE_INIT);
            bankCardApply = iBankCardApplyService.save(mappingService.map(bankCardApplyBean, BankCardApply.class));
        }

        //如果需要的话，启动工作流。
        Document document = BankCardApply.class.getAnnotation(Document.class);
        String collectionName = document.collection();
        SignInfo signInfo = new SignInfo(bankCardApply.getLoginUserId(), bankCardApply.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, bankCardApply.getComment());
        iWorkflowBizService.actCreateWorkflow(bankCardApply.getBusinessTypeCode(), bankCardApply.getId(), bankCardApply.getBillTypeCode(), collectionName, null, bankCardApply.getCustomerTransactionId());
        bankCardApplyBean = mappingService.map(bankCardApply, BankCardApplyBean.class);
        //获取卡信息
        CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
        if (customerCardBean != null) {
            bankCardApplyBean.setCustomerCard(customerCardBean);
        }
        return ResultBean.getSucceed().setD(bankCardApplyBean);
    }

    @Override
    public ResultBean<List<String>> actFindCurrentTask(String id) {
        BankCardApply bankCardApply = new BankCardApply();
        List<TaskBean> taskBeen = iWorkflowBizService.actGetBillTasks(bankCardApply.getBillTypeCode() + "." + id).getD();
        List<String> taskList = new ArrayList<String>();
        for (int i = 0; i < taskBeen.size(); i++) {
            taskList.add(taskBeen.get(i).getName());
        }
        return ResultBean.getSucceed().setD(taskList);
    }

    @Override
    public ResultBean<List> actFindHistoryTask(String id) {
        BankCardApply bankCardApply = new BankCardApply();
        List<TaskBean> taskList = iWorkflowBizService.actFindHisTaskList(bankCardApply.getBillTypeCode() + "." + id).getD();
        return ResultBean.getSucceed().setD(taskList);
    }

    @Override
    public ResultBean<BankCardApplyBean> actApprovedBankCardApply(BankCardApplyBean bankCardApplyBean, Integer approveStatus, Integer start, String loginUserId) {
        CardActionRecord action = new CardActionRecord();
        //默认不贴息，走受托支付
        Integer payment = BooleanDataBean.NUM_SWITH_FALSE;
        //默认为代启卡
        Integer replaceStartCard = BooleanDataBean.NUM_SWITH_FALSE;
        if (bankCardApplyBean == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BANKCARDAPPLY_NOTEXIST"), bankCardApplyBean.getId()));
        }
        //查询操作的员工信息
        EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
        //查询签约是否需要代启卡
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(bankCardApplyBean.getCustomerTransactionId());
        if (purchaseCarOrder != null) {
            replaceStartCard = purchaseCarOrder.getReplaceStartCard();
        }
        //查询借款信息是否需要垫资
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        if (customerLoanBean != null) {
            payment = customerLoanBean.getIsNeedPayment();
        }
        try {
            //获取当前任务
            List<TaskBean> tasklist = iWorkflowBizService.actGetBillTasks(bankCardApplyBean.getBillTypeCode() + "." + bankCardApplyBean.getId()).getD();
            //将完成的任务存到列表中
            List<CardActionRecord> actionRecords = bankCardApplyBean.getActionRecords();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CardActionRecord card = new CardActionRecord();
            TaskBean taskBean = null;
            for (TaskBean task : tasklist) {
                String taskId = task.getId();
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("approveStatus", approveStatus);
                variables.put("start", start);
                variables.put("payment", payment);
                variables.put("replaceStartCard", replaceStartCard);
                if (task.getTaskDefinitionKey().equals("BankCard_Apply")) {
                    //完成任务
                    iWorkflowBizService.actEndTask(taskId, variables);
                    //完成业务流程任务
                    this.completeBankCardApply(bankCardApplyBean);
                    taskBean = task;
                } else {
                    //完成任务
                    iWorkflowBizService.actEndTask(taskId, variables);
                    taskBean = task;
                }

            }
            if (taskBean != null) {
                if (approveStatus == 9) {
                    taskBean.setName("销卡");
                }
                card.setId(taskBean.getId());
                card.setAction(taskBean.getName());
            }
            card.setComment(bankCardApplyBean.getComment());
            card.setTs(df.format(new Date()));
            if (employeeLookupBean != null) {
                card.setActionName(employeeLookupBean.getUsername());
            } else {
                LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId).getD();
                if (loginUserBean != null) {
                    card.setActionName(loginUserBean.getUsername());
                }
            }
            actionRecords.add(card);
            //保存卡的信息
            bankCardApplyBean.setComment(null);
            bankCardApplyBean = this.saveCustomerCard(bankCardApplyBean).getD();
            //判断卡业务是否结束
            Boolean billIsFinish = iWorkflowBizService.actBusinessBillIsFinish(bankCardApplyBean.getBillTypeCode() + "." + bankCardApplyBean.getId()).getD();
            if(billIsFinish){
                bankCardApplyBean.setApproveStatus(ApproveStatus.APPROVE_PASSED);
            }
            BankCardApply bankCardApply = iBankCardApplyService.save(mappingService.map(bankCardApplyBean, BankCardApply.class));
            if(taskBean != null){
                //卡业务处理通知
                this.bankCardBusinessProcessMessage(bankCardApplyBean, taskBean, billIsFinish).getD();
                //保存工作流记录信息
                this.saveWorkFlowBill(bankCardApply.getId(),taskBean,bankCardApply.getApproveStatus());
            }
        bankCardApplyBean = mappingService.map(bankCardApply, BankCardApplyBean.class);
        bankCardApplyBean.setCustomerLoanBean(customerLoanBean);
        //获取卡信息
        CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
        if (customerCardBean != null) {
            bankCardApplyBean.setCustomerCard(customerCardBean);
        }
        return ResultBean.getSucceed().setD(bankCardApplyBean).setM(taskBean == null ? "" : taskBean.getName() + "操作成功！");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Error:卡业务流程处理异常", e);
        }
        return  ResultBean.getSucceed();
    }

    @Override
    public ResultBean<BankCardBean> actApprovedBankCardApply(BankCardBean bankCardBean, Integer approveStatus, Integer start, String loginUserId) {
        BankCardApply bankCardApply = iBankCardApplyService.getOne(bankCardBean.getId());
        BankCardApplyBean bankCardApplyBean = mappingService.map(bankCardApply, BankCardApplyBean.class);
        if (bankCardBean.getSwipingMoney() != null) {
            bankCardApplyBean.setSwipingMoney(bankCardBean.getSwipingMoney());
        }
        if (bankCardBean.getSwipingPeriods() != null && bankCardBean.getSwipingPeriods() != 0) {
            bankCardApplyBean.setSwipingPeriods(bankCardBean.getSwipingPeriods());
        }
        if(bankCardBean.getApplyTime() != null){
            bankCardApplyBean.setApplyTime(bankCardBean.getApplyTime());
        }
        if (bankCardBean.getTakeTime() != null) {
            bankCardApplyBean.setTakeTime(bankCardBean.getTakeTime());
        }
        if (bankCardBean.getReplaceActivateTime() != null) {
            bankCardApplyBean.setReplaceActivateTime(bankCardBean.getReplaceActivateTime());
        }
        if (bankCardBean.getReplaceActivateName() != null) {
            bankCardApplyBean.setReplaceActivateName(bankCardBean.getReplaceActivateName());
        }
        if (bankCardBean.getChangeAmountTime() != null) {
            bankCardApplyBean.setChangeAmountTime(bankCardBean.getChangeAmountTime());
        }
        if (bankCardBean.getReceiveTrusteeTime() != null) {
            bankCardApplyBean.setReceiveTrusteeTime(bankCardBean.getReceiveTrusteeTime());
        }
        if (bankCardBean.getReceiveCardName() != null) {
            bankCardApplyBean.setReceiveCardName(bankCardBean.getReceiveCardName());
        }
        if (bankCardBean.getSwipingTrusteeTime() != null) {
            bankCardApplyBean.setSwipingTrusteeTime(bankCardBean.getSwipingTrusteeTime());
        }
        if (bankCardBean.getSwipingName() != null) {
            bankCardApplyBean.setSwipingName(bankCardBean.getSwipingName());
        }
        if (bankCardBean.getFirstReimbursement() != null) {
            bankCardApplyBean.setFirstReimbursement(bankCardBean.getFirstReimbursement());
        }
        if (bankCardBean.getStatus() != null) {
            bankCardApplyBean.setStatus(bankCardBean.getStatus());
        }
        if(bankCardBean.getComment() != null){
            bankCardApplyBean.setComment(bankCardBean.getComment());
        }
        bankCardApplyBean.setCustomerCard(new CustomerCardBean());
        CustomerCardBean customerCardBean = bankCardApplyBean.getCustomerCard();
        if (bankCardBean.getCardNo() != null) {
            customerCardBean.setCardNo(bankCardBean.getCardNo());
        }
        if (bankCardBean.getCvv() != null) {
            customerCardBean.setCvv(bankCardBean.getCvv());
        }
        if (bankCardBean.getExpireDate() != null) {
            customerCardBean.setExpireDate(bankCardBean.getExpireDate());
        }
        if (bankCardBean.getInitPassword() != null) {
            customerCardBean.setInitPassword(bankCardBean.getInitPassword());
        }

        if (!bankCardBean.getCustomerImages().isEmpty()) {
            //处理档案资料
            iCustomerImageFileBizService.actSaveCustomerImages(bankCardApply.getCustomerId(),
                    bankCardApply.getCustomerTransactionId(),
                    bankCardBean.getCustomerImages()); //整体保存档案资料
        }
        bankCardApplyBean = this.actApprovedBankCardApply(bankCardApplyBean, approveStatus, start, loginUserId).getD();
        return ResultBean.getSucceed().setD(mappingService.map(bankCardApplyBean, BankCardBean.class));
    }

    /**
     * 保存客户卡信息
     *
     * @param bankCardApplyBean
     * @return
     */
    private ResultBean<BankCardApplyBean> saveCustomerCard(BankCardApplyBean bankCardApplyBean) {
        if (bankCardApplyBean.getCustomerCard() != null) {
            CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApplyBean.getCustomerTransactionId()).getD();
            if (customerCardBean == null) {
                customerCardBean = new CustomerCardBean();
            }
            customerCardBean.setCustomerId(bankCardApplyBean.getCustomerId());
            customerCardBean.setCustomerTransactionId(bankCardApplyBean.getCustomerTransactionId());
            if (bankCardApplyBean.getCustomerCard().getCardNo() != null) {
                customerCardBean.setCardNo(bankCardApplyBean.getCustomerCard().getCardNo());
            }
            if (bankCardApplyBean.getCustomerCard().getCvv() != null) {
                customerCardBean.setCvv(bankCardApplyBean.getCustomerCard().getCvv());
            }
            if (bankCardApplyBean.getCustomerCard().getExpireDate() != null) {
                customerCardBean.setExpireDate(bankCardApplyBean.getCustomerCard().getExpireDate());
            }
            if (bankCardApplyBean.getCustomerCard().getInitPassword() != null) {
                customerCardBean.setInitPassword(bankCardApplyBean.getCustomerCard().getInitPassword());
            }
            iCustomerBizService.actSaveCustomerCard(customerCardBean);
        }
        return ResultBean.getSucceed().setD(bankCardApplyBean);
    }

    /**
     * 完成业务流程制卡任务
     *
     * @return
     */
    private void completeBankCardApply(BankCardApplyBean bankCardApply) {
        try {
            SignInfo signInfo = new SignInfo();
            signInfo.setResult(ApproveStatus.APPROVE_PASSED);
            iWorkflowBizService.actSignForTransaction(bankCardApply.getCustomerTransactionId(), bankCardApply.getBusinessTypeCode(), bankCardApply.getBillTypeCode(), signInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 预约刷卡完成时自动完成卡业务处理的渠道领卡任务
     *
     * @param appointSwipindCardId
     */
    @Override
    public void actCompleteReceiveDiscount(String appointSwipindCardId) {
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.getOne(appointSwipindCardId);
        if (appointSwipingCard != null && appointSwipingCard.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
            BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
            String actionId = appointSwipingCard.getLoginUserId();
            //获取预约刷卡的审批人
            List<SignInfo> signInfos = iWorkflowBizService.actGetSingInfosByFlowCodeAndSourceId(appointSwipingCard.getBillTypeCode(), appointSwipingCard.getId()).getD();
            for (SignInfo signInfo:signInfos) {
                if(signInfo.getFlag() == SignInfo.SIGN_PASS){
                    actionId = signInfo.getUserId();
                }
            }
            EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(actionId).getD();
            try {
                //获取当前任务
                List<TaskBean> tasklist = iWorkflowBizService.actGetBillTasks(bankCardApply.getBillTypeCode() + "." + bankCardApply.getId()).getD();
                //将完成的任务存到列表中
                List<CardActionRecord> actionRecords = bankCardApply.getActionRecords();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (TaskBean task : tasklist) {
                    if (task.getTaskDefinitionKey().equals("BankCard_ReceiveDiscount")) {
                        CardActionRecord card = new CardActionRecord();
                        String taskId = task.getId();
                        Map<String, Object> variables = new HashMap<String, Object>();
                        variables.put("approveStatus", appointSwipingCard.getApproveStatus());
                        //完成任务
                        iWorkflowBizService.actEndTask(taskId, variables);
                        card.setId(taskId);
                        card.setAction(task.getName());
                        card.setTs(df.format(new Date()));
                        if (employeeLookupBean != null) {
                            card.setActionName(employeeLookupBean.getUsername());
                        } else {
                            LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(appointSwipingCard.getLoginUserId()).getD();
                            if (loginUserBean != null) {
                                card.setActionName(loginUserBean.getUsername());
                            }
                        }

                        actionRecords.add(card);
                        bankCardApply.setReceiveDiscountTime(DateTimeUtils.getCreateTime());
                        if(bankCardApply.getReceiveCardName() == null){
                            bankCardApply.setReceiveCardName(card.getActionName());
                        }
                        bankCardApply.setStatus(BankCardApplyBean.BKSTATUS_GET);

                        bankCardApply = iBankCardApplyService.save(bankCardApply);//完成渠道领卡
                        BankCardApplyBean bankCardApplyBean = mappingService.map(bankCardApply, BankCardApplyBean.class);
                        Boolean billIsFinish = iWorkflowBizService.actBusinessBillIsFinish(bankCardApplyBean.getBillTypeCode() + "." + bankCardApplyBean.getId()).getD();
                        //卡业务处理通知
                        this.bankCardBusinessProcessMessage(bankCardApplyBean, task, billIsFinish).getD();
                        //保存工作流操作记录
                        try {
                            this.saveWorkFlowBill(bankCardApplyBean.getId(),task,bankCardApplyBean.getApproveStatus());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        logger.error(messageService.getMessage("MSG_BANKCARDAPPLY_RECEIVEDISCOUNTFAILED"));
                    }
                }
            }catch (Exception e){
                logger.error("卡业务流程处理异常",e);
            }
        } else {
            logger.error(messageService.getMessage("MSG_BANKCARDAPPLY_RECEIVEDISCOUNTNOPASSED"));
        }
    }

    /**
     * 渠道刷卡完成时自动完成卡业务处理的渠道刷卡任务
     *
     * @param swipindCardId
     */
    @Override
    public void actCompleteSwipingCardShop(String swipindCardId) {
        SwipingCard swipingCard = iSwipingCardService.getOne(swipindCardId);
        if (swipingCard != null && swipingCard.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
            BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(swipingCard.getCustomerTransactionId());
            String actionId = swipingCard.getLoginUserId();
            //获取渠道刷卡的审批人
            List<SignInfo> signInfos = iWorkflowBizService.actGetSingInfosByFlowCodeAndSourceId(swipingCard.getBillTypeCode(), swipingCard.getId()).getD();
            for (SignInfo signInfo:signInfos) {
                if(signInfo.getFlag() == SignInfo.SIGN_PASS){
                    actionId = signInfo.getUserId();
                }
            }
            EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(actionId).getD();
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(bankCardApply.getCustomerTransactionId());
            //获取借款信息
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            //获取还款信息
            CustomerRepaymentBean customerRepaymentBean = iCustomerBizService.actGetCustomerRepaymentByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
            try {
                //获取当前任务
                List<TaskBean> tasklist = iWorkflowBizService.actGetBillTasks(bankCardApply.getBillTypeCode() + "." + bankCardApply.getId()).getD();
                //将完成的任务存到列表中
                List<CardActionRecord> actionRecords = bankCardApply.getActionRecords();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (TaskBean task : tasklist) {
                    if (task.getTaskDefinitionKey().equals("BankCard_SwipingShop")) {
                        CardActionRecord card = new CardActionRecord();
                        String taskId = task.getId();
                        Map<String, Object> variables = new HashMap<String, Object>();
                        variables.put("approveStatus", swipingCard.getApproveStatus());
                        //完成任务
                        iWorkflowBizService.actEndTask(taskId, variables);
                        card.setId(taskId);
                        card.setAction(task.getName());
                        card.setTs(df.format(new Date()));
                        if (employeeLookupBean != null) {
                            card.setActionName(employeeLookupBean.getUsername());
                        } else {
                            LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(swipingCard.getLoginUserId()).getD();
                            if (loginUserBean != null) {
                                card.setActionName(loginUserBean.getUsername());
                            }
                        }
                        actionRecords.add(card);
                        if (task.getTaskDefinitionKey().equals("BankCard_SwipingShop")) {
                            bankCardApply.setSwipingMoney(swipingCard.getPayAmount());
                            if (customerRepaymentBean != null) {
                                bankCardApply.setDefaultReimbursement(customerRepaymentBean.getRepayment());
                                bankCardApply.setFirstReimbursement(customerRepaymentBean.getFirstRepaymentDate());
                                bankCardApply.setBillingDate(customerRepaymentBean.getBillingDate());
                            }
                            bankCardApply.setSwipingPeriods(customerLoanBean.getRateType().getMonths());
                            bankCardApply.setSwipingShopTime(DateTimeUtils.getCreateTime());
                            bankCardApply.setStatus(BankCardApplyBean.BKSTATUS_SWIPING);

                        }
                        Boolean billIsFinish = iWorkflowBizService.actBusinessBillIsFinish(bankCardApply.getBillTypeCode() + "." + bankCardApply.getId()).getD();
                        if(billIsFinish){
                            bankCardApply.setApproveStatus(ApproveStatus.APPROVE_PASSED);
                        }
                        bankCardApply = iBankCardApplyService.save(bankCardApply);//完成渠道刷卡
                        BankCardApplyBean bankCardApplyBean = mappingService.map(bankCardApply, BankCardApplyBean.class);

                        //卡业务处理通知
                        this.bankCardBusinessProcessMessage(bankCardApplyBean, task, billIsFinish).getD();
                        //保存工作流记录信息
                        try {
                            this.saveWorkFlowBill(bankCardApplyBean.getId(),task,bankCardApplyBean.getApproveStatus());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        logger.error(messageService.getMessage("MSG_BANKCARDAPPLY_SWIPINGSHOPFAILED"));
                    }
                }
            }catch (Exception e){
                logger.error("卡业务流程处理异常",e);
            }

        } else {
            logger.error(messageService.getMessage("MSG_BANKCARDAPPLY_NOPASSED"));
        }
    }

    @Override
    public void actDeleteBankCardApply(BankCardApplyBean bankCardApplyBean) {
        iBankCardApplyService.deleteBankCard(bankCardApplyBean);
    }

    /**
     * 卡业务处理通知
     *
     * @param bankCardApply
     * @param task
     * @return
     */
    public ResultBean<BankCardApplyBean> bankCardBusinessProcessMessage(BankCardApplyBean bankCardApply, TaskBean task, Boolean billIsFinish) {
        //TODO 消息处理时发送的参数需要在TemplateService中添加模版的方法
        try {
            //TODO 启卡
            Map templateData = new HashMap<>();
            Map sendMap = new HashMap<>();
            List<String> toList = new ArrayList<>();
            if (bankCardApply.getEmployeeId() != null) {
                toList.add(bankCardApply.getEmployeeId());
            }
            sendMap.put("bd_employee", toList);
            String eventType = bankCardApply.getBusinessTypeCode() + "_" + bankCardApply.getBillTypeCode() + "_" + task.getTaskDefinitionKey();
            MsgRecordBean msgRecordBean = new MsgRecordBean(eventType, bankCardApply.getCustomerTransactionId(), templateData, null, sendMap);
            Map extraFields = new HashMap<>();
            extraFields.put("businessType", bankCardApply.getBusinessTypeCode());
            extraFields.put("billId", bankCardApply.getCustomerTransactionId());
            // 这里的map是为了给pad Push数据时告诉pad跳转时使用
            Map ctrlMap = new HashMap<>();
            ctrlMap.put("afterOpenAction", "3"); // go activity
            // TODO: 2017/10/16 根据系统参配项获取activity
            ctrlMap.put("go_activity", iWorkflowBizService.getPushSendValue(billIsFinish, bankCardApply.getCustomerTransactionId(), bankCardApply.getBillTypeCode()));
            ctrlMap.put("extraFields", extraFields);
            msgRecordBean.setPushCtrlMap(ctrlMap);
            iAmqpBizService.actSendMq(eventType, new Object[]{bankCardApply.getId()}, msgRecordBean);
            //刷卡后将交易信息添加到区块链中
            if(bankCardApply.getStatus().equals(BankCardApplyBean.BKSTATUS_SWIPING)){
                CustomerBean customerBean = iCustomerBizService.actGetCustomerById(bankCardApply.getCustomerId()).getD();
                PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(bankCardApply.getCustomerTransactionId());
                CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
                String hash = EncryUtil.MD5(customerBean.getName()+customerBean.getIdentifyNo());
                Map map = new HashMap<String,Object>();
                map.put("eventType","BankCard_Swiping");
                map.put("Hash",hash);
                map.put("customerName",customerBean.getName());
                map.put("identifyNo",customerBean.getIdentifyNo());
                map.put("creditAmount",customerLoanBean.getCreditAmount());
                if(bankCardApply.getSwipingShopTime() != null){
                    map.put("loanDate",bankCardApply.getSwipingShopTime());
                }
                if(bankCardApply.getSwipingTrusteeTime() != null){
                    map.put("loanDate",bankCardApply.getSwipingTrusteeTime());
                }
                //区块链刷卡完成后添加链上数据，暂时注释
                //iAmqpBizService.actSendWeb3jMsg(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultBean.getSucceed().setD(bankCardApply);
    }

    @Override
    public ResultBean<Integer> actCheckBankCardApplyStatus(String transactionId) {
        if (transactionId != null) {
            BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(transactionId);
            if (bankCardApply != null) {
                return ResultBean.getSucceed().setD(bankCardApply.getStatus());
            }
        }
        return ResultBean.getFailed();
    }

    /**
     * 获取分期经理的签约单分页信息
     *
     * @param isPass
     * @param loginUserId （分期经理的用户ID）
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ResultBean<DataPageBean<BankCardApplyListBean>> actGetBankCards(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize) {
        Page<BankCardApply> bankCards = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_ID_NULL"), loginUserId));
        }

        //List<String> tids = iCustomerTransactionService.getTransactionIds(loginUserId, isPass);
        List<Integer> as = new ArrayList<Integer>();
        if (isPass) {
            //bankCards = iBankCardApplyService.findCompletedItemsByUser(BankCardApply.class, loginUserId, tids, pageIndex, pageSize);
            as.add(ApproveStatus.APPROVE_PASSED);
            bankCards = this.iBankCardApplyService.findByLoginUserIdAndApproveStatusIn(loginUserId,as,pageIndex,pageSize);
            if (bankCards == null || bankCards.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_LOGINUSERID_HISTORY_NULL"));
            }
        } else {
            as.add(ApproveStatus.APPROVE_INIT);
            as.add(ApproveStatus.APPROVE_ONGOING);
            bankCards = this.iBankCardApplyService.findByLoginUserIdAndApproveStatusIn(loginUserId,as,pageIndex,pageSize);
            //bankCards = iBankCardApplyService.findPendingItemsByUser(BankCardApply.class, loginUserId, tids, pageIndex, pageSize);
            if (bankCards == null || bankCards.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_LOGINUSERID_NULL"));
            }
        }

        DataPageBean<BankCardApplyListBean> destination = new DataPageBean<BankCardApplyListBean>();
        destination.setPageSize(bankCards.getSize());
        destination.setTotalCount(bankCards.getTotalElements());
        destination.setTotalPages(bankCards.getTotalPages());
        destination.setCurrentPage(bankCards.getNumber());
        destination.setResult(this.getBankCardList(bankCards.getContent()));
        return ResultBean.getSucceed().setD(destination);
    }

    /**
     * 代启卡失败后客户自启卡
     *
     * @param id
     * @param initPassword
     * @return
     */
    @Override
    public ResultBean<BankCardApplyListBean> actSignBankCardByReplaceActivate(String id, Integer approveStatus, String initPassword, String loginUserId) {
        //代启卡失败后完成自启卡任务
        BankCardApply bankCardApply = iBankCardApplyService.getOne(id);
        if (bankCardApply != null && bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_ACTIVATEFAILED) {
            EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
            CustomerCardBean customerCard = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(bankCardApply.getCustomerTransactionId());
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            try {
                //获取当前任务
                List<TaskBean> tasklist = iWorkflowBizService.actGetBillTasks(bankCardApply.getBillTypeCode() + "." + bankCardApply.getId()).getD();
                //将完成的任务存到列表中
                List<CardActionRecord> actionRecords = bankCardApply.getActionRecords();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (TaskBean task : tasklist) {
                    if (task.getTaskDefinitionKey().equals("BankCard_Actice")) {
                        CardActionRecord card = new CardActionRecord();
                        String taskId = task.getId();
                        Map<String, Object> variables = new HashMap<String, Object>();
                        variables.put("approveStatus", approveStatus);
                        //完成任务
                        iWorkflowBizService.actEndTask(taskId, variables);
                        card.setId(taskId);
                        card.setAction(task.getName());
                        card.setTs(df.format(new Date()));
                        if (employeeLookupBean != null) {
                            card.setActionName(employeeLookupBean.getUsername());
                        } else {
                            LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId).getD();
                            if (loginUserBean != null) {
                                card.setActionName(loginUserBean.getUsername());
                            }
                        }
                        actionRecords.add(card);
                        if (task.getTaskDefinitionKey().equals("BankCard_Actice")) {
                            bankCardApply.setActivateTime(DateTimeUtils.getCreateTime());
                            if (customerLoanBean != null) {
                                bankCardApply.setSwipingMoney(customerLoanBean.getSwipingAmount());
                            }
                            if (approveStatus == ApproveStatus.APPROVE_PASSED) {
                                bankCardApply.setStatus(BankCardApplyBean.BKSTATUS_ACTIVATE);
                            } else if (approveStatus == ApproveStatus.APPROVE_REJECT) {
                                bankCardApply.setStatus(BankCardApplyBean.BKSTATUS_CANCEL);
                            }
                            customerCard.setInitPassword(initPassword);
                        }
                        bankCardApply = iBankCardApplyService.save(bankCardApply);//完成渠道刷卡
                        iCustomerBizService.actSaveCustomerCard(customerCard);//保存初始密码
                        BankCardApplyBean bankCardApplyBean = mappingService.map(bankCardApply, BankCardApplyBean.class);
                        Boolean billIsFinish = iWorkflowBizService.actBusinessBillIsFinish(bankCardApplyBean.getBillTypeCode() + "." + bankCardApplyBean.getId()).getD();
                        //卡业务处理通知
                        this.bankCardBusinessProcessMessage(bankCardApplyBean, task, billIsFinish).getD();
                        //保存工作流记录信息
                        try {
                            this.saveWorkFlowBill(bankCardApplyBean.getId(),task,bankCardApplyBean.getApproveStatus());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        BankCardApplyListBean bankCardApplyListBean = mappingService.map(bankCardApply, BankCardApplyListBean.class);
                        return ResultBean.getSucceed().setD(bankCardApplyListBean).setM(messageService.getMessage("MSG_BANKCARDAPPLY_REPLACEACTIVESUCCEED"));
                    } else {
                        logger.error(messageService.getMessage("MSG_BANKCARDAPPLY_REPLACEACTIVEFAILED"));
                        return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_REPLACEACTIVEFAILED"));
                    }
                }
            }catch (Exception e){
                logger.error("卡业务流程处理异常",e);
            }
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_REPLACEACTIVEFAILED"));
        }

        return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_GETTASKERROR"));
    }

    /**
     * 重新制卡
     *
     * @param bankCardId
     * @return
     */
    @Override
    public ResultBean<BankCardApplyBean> actReMakeCard(String bankCardId, String comment, String loginUserId) {
        EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
        BankCardApply bankCardApply = iBankCardApplyService.getOne(bankCardId);
        if (bankCardApply != null) {
            try {
                //获取当前任务
                List<TaskBean> tasklist = iWorkflowBizService.actGetBillTasks(bankCardApply.getBillTypeCode() + "." + bankCardApply.getId()).getD();
                //将完成的任务存到列表中
                List<CardActionRecord> actionRecords = bankCardApply.getActionRecords();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CardActionRecord card = new CardActionRecord();
                TaskBean taskBean = null;
                for (TaskBean task : tasklist) {
                    String taskId = task.getId();
                    Map<String, Object> variables = new HashMap<String, Object>();
                    variables.put("approveStatus", 9);
                    //完成任务
                    iWorkflowBizService.actEndTask(taskId, variables);
                    taskBean = task;
                }
                if (taskBean != null) {
                    card.setId(taskBean.getId());
                }
                card.setAction("重新制卡");
                card.setTs(df.format(new Date()));
                card.setComment(comment);
                if (employeeLookupBean != null) {
                    card.setActionName(employeeLookupBean.getUsername());
                } else {
                    LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId).getD();
                    if (loginUserBean != null) {
                        card.setActionName(loginUserBean.getUsername());
                    }
                }
                actionRecords.add(card);
                bankCardApply.setStatus(BankCardApplyBean.BKSTATUS_INIT);
                bankCardApply = iBankCardApplyService.save(bankCardApply);//重新制卡
                List<TaskBean> list = iWorkflowBizService.actGetBillTasks(bankCardApply.getBillTypeCode() + "." + bankCardApply.getId()).getD();
                if (list.isEmpty()) {
                    return this.actStartBankCardApply(mappingService.map(bankCardApply, BankCardApplyBean.class));
                }
            }catch (Exception e){
                logger.error("卡业务流程处理异常",e);
            }
        }

        return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_REMAKECARDERROR"));
    }

    private List<BankCardApplyListBean> getBankCardList(List<BankCardApply> bankCards) {
        List<BankCardApplyListBean> result = new ArrayList<BankCardApplyListBean>();
        for (BankCardApply bankCardApply : bankCards) {
            BankCardApplyListBean bankCardApplyListBean = mappingService.map(bankCardApply, BankCardApplyListBean.class);
            CustomerCardBean customerCard = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(bankCardApply.getCustomerId()).getD();
            if (customerBean != null) {
                bankCardApplyListBean.setCustomerName(customerBean.getName());
                bankCardApplyListBean.setCustomerIdentifyNo(customerBean.getIdentifyNo());
                bankCardApplyListBean.setCustomerCell(customerBean.getCells() != null && customerBean.getCells().size() > 0 ? customerBean.getCells().get(0) : "");
            }
            if (customerCard != null) {
                bankCardApplyListBean.setCardNo(customerCard.getCardNo());
                bankCardApplyListBean.setCvv(customerCard.getCvv());
                bankCardApplyListBean.setExpireDate(customerCard.getExpireDate());
                bankCardApplyListBean.setInitPassword(customerCard.getInitPassword());
                bankCardApplyListBean.setCardNo(customerCard.getCardNo());
            }
            result.add(bankCardApplyListBean);
        }

        return result;
    }

    /**
     * 计算某个月份刷卡信息
     *
     * @param beforeDate
     * @return
     */
    @Override
    public ResultBean<List<BankCardApplyBean>> getSwingCardBeforeEndDate(String beforeDate) {
        String regex = String.format("^%s", beforeDate);

        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        criteria.orOperator(Criteria.where("swipingTrusteeTime").regex(regex, "m"), Criteria.where("swipingShopTime").regex(regex, "m"));
        List<BankCardApply> bankCardApplyList = mongoTemplate.find(Query.query(criteria), BankCardApply.class);
        return ResultBean.getSucceed().setD(mappingService.map(bankCardApplyList, BankCardApplyBean.class)).setM(String.format("%s月的数据", beforeDate));
    }

    /**
     * 日报
     *
     * @param date
     * @param
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId, String date, BankCardApplyBean bankCardApply) {

        Map<Object, Object> dailyReport = iBankCardApplyService.getDailyReport(orgId, date, mappingService.map(bankCardApply, BankCardApply.class));
        if (dailyReport != null) {
            return ResultBean.getSucceed().setD(dailyReport);
        }

        return ResultBean.getFailed();
    }

    /**
     * 通过支行ID获取卡业务信息
     *
     * @param searchBean
     * @return
     */
    @Override
    public ResultBean<DataPageBean<BankCardListBean>> actGetBankCardApplyListByCashSourceId(SearchBean searchBean) {

        Page<BankCardApply> bankCardApply = iBankCardApplyService.findAllBySearchBean(BankCardApply.class, searchBean, SearchBean.STAGE_ORDER,null);
        if (bankCardApply != null) {
            DataPageBean<BankCardListBean> destination = new DataPageBean<BankCardListBean>();
            destination.setPageSize(bankCardApply.getSize());
            destination.setTotalCount(bankCardApply.getTotalElements());
            destination.setTotalPages(bankCardApply.getTotalPages());
            destination.setCurrentPage(bankCardApply.getNumber());
            destination.setResult(this.getBankCards(bankCardApply.getContent()));
            return ResultBean.getSucceed().setD(destination);
        } else {
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_NOLISTBYCASHSOURCEID"));
        }
    }

    /**
     * 卡业务app详情页面
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<BankCardBean> actGetBankCardApplyById(String id) {
        BankCardApply bankCardApply = iBankCardApplyService.getOne(id);
        if (bankCardApply != null) {
            BankCardBean bankCardBean = mappingService.map(bankCardApply, BankCardBean.class);
            //客户信息
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(bankCardApply.getCustomerId()).getD();
            if (customerBean != null) {
                bankCardBean.setName(customerBean.getName());
                bankCardBean.setIdentifyNo(customerBean.getIdentifyNo());
            }
            //客户卡信息
            CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
            if (customerCardBean != null) {
                bankCardBean.setCardNo(customerCardBean.getCardNo());
                bankCardBean.setExpireDate(customerCardBean.getExpireDate());
                bankCardBean.setCvv(customerCardBean.getCvv());
                bankCardBean.setInitPassword(customerCardBean.getInitPassword());
            }
            //客户还款信息
            CustomerRepaymentBean customerRepaymentBean = iCustomerBizService.actGetCustomerRepaymentByCustomerTransactionId(bankCardApply.getCustomerTransactionId()).getD();
            if (customerRepaymentBean != null) {
                bankCardBean.setFirstRepayment(customerRepaymentBean.getFirstRepayment());
                bankCardBean.setMonthRepayment(customerRepaymentBean.getMonthRepayment());
                bankCardBean.setTotalRepayment(customerRepaymentBean.getTotalRepayment());
            }
            return ResultBean.getSucceed().setD(bankCardBean);
        }
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<BankCardApplyBean> actsaveReceiveCardName(String customerTransactionId, String receiveCardName) {

        //根据交易id查到卡业务
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(customerTransactionId);
        bankCardApply.setReceiveCardName(receiveCardName);
        BankCardApply save = iBankCardApplyService.save(bankCardApply);
        return ResultBean.getSucceed().setD(mappingService.map(bankCardApply, BankCardApplyBean.class));
    }

    private List<BankCardListBean> getBankCards(List<BankCardApply> bankCards) {
        List<BankCardListBean> result = new ArrayList<BankCardListBean>();
        for (BankCardApply bankCardApply : bankCards) {
            BankCardListBean bankCardListBean = mappingService.map(bankCardApply, BankCardListBean.class);
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(bankCardApply.getCustomerId()).getD();
            if (customerBean != null) {
                bankCardListBean.setName(customerBean.getName());
                bankCardListBean.setIdentifyNo(customerBean.getIdentifyNo());
            }
            result.add(bankCardListBean);
        }
        return result;
    }

    public ResultBean<List<String>> getDailySwipingMoneyTransactionIds(String date) {
        List<String> trIds = iBankCardApplyService.getDailySwipingMoneyTransactionIds(date);
        return ResultBean.getSucceed().setD(trIds);
    }

    @Override
    public ResultBean<List<BankCardListBean>> actGetBankCardApplyByTaskDefinitionKey(String definitionKey,String cashSourceId) {
        Map map = new HashMap<>();
        map.put("ACT_ID_", definitionKey);
        List<ExecutionBean> list = iWorkflowBizService.actFindExecutionBySql("select * from ACT_RU_EXECUTION E where E.ACT_ID_ = #{ACT_ID_} and BUSINESS_KEY_ like 'A011%' ", map).getD();
        List<BankCardApply> bankCardApplies = new ArrayList<>();
        for (ExecutionBean executionBean : list) {
            String key = executionBean.getBusinessKey();
            key = key.substring(key.indexOf('.') + 1);
            BankCardApply bankCardApply = iBankCardApplyService.findByIdAndCashSourceIdOrderByTsDesc(key,cashSourceId);
            if (bankCardApply != null) {
                bankCardApplies.add(bankCardApply);
            }
        }
        if (bankCardApplies.size() > 0) {
            bankCardApplies.sort(new Comparator<BankCardApply>() {
                @Override
                public int compare(BankCardApply o1, BankCardApply o2) {
                    return o1.getTs().compareTo(o2.getTs());
                }
            });
        }
        //List<BankCardApplyBean> bankCardApplyBeen = this.actGetBankCardApplyByCardStep(definitionKey).getD();
        if(bankCardApplies != null && bankCardApplies.size() > 0){
            List<BankCardListBean> bankCards = this.getBankCards(mappingService.map(bankCardApplies, BankCardApply.class));
            return ResultBean.getSucceed().setD(bankCards);
        }else{
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_NOLISTBYCASHSOURCEID"));
        }
    }

    /**
     * 保存工作流操作记录
     * @param id
     * @param task
     * @param approvestatus
     * @return
     * @throws Exception
     */
    private ResultBean saveWorkFlowBill(String id,TaskBean task,Integer approvestatus) throws Exception {
        WorkFlowBillBean workFlowBill = iWorkflowBizService.actGetBillWorkflow(id).getD();
        if (workFlowBill == null) {
            throw new Exception("未找到工作流对象workFlowBill, id:" + id);
        }
        List<TaskBean> taskList = iWorkflowBizService.actGetBillTasks("A011." + id).getD();
        if(taskList != null && taskList.size() > 0){
            workFlowBill.setCurrentTask(taskList.get(0).getTaskDefinitionKey());
        }else{
            workFlowBill.setCurrentTask(null);
        }
        workFlowBill.setCurrentTasks(this.getCurrentTaskKeys(taskList));
        workFlowBill.setCompletedTask(task !=  null ? task.getTaskDefinitionKey() : "");
        workFlowBill.setApproveStatus(approvestatus);
        return iWorkflowBizService.actSaveWorkFlowBill(workFlowBill);
    }

    private List<String> getCurrentTaskKeys(List<TaskBean> tasks) {
        List<String> taskKeys = new ArrayList<String>();
        tasks.forEach(task -> {
            taskKeys.add(task.getTaskDefinitionKey());
        });

        return taskKeys;
    }

    @Override
    public ResultBean<String> actGetBankCardApplyApproveStatus(String transactionId) {
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(transactionId);
        if(bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_CANCEL){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLYCANCEL_NOTPAYMENTBILL"));
        }
        if(bankCardApply == null){
            return ResultBean.getSucceed();
        } else if(bankCardApply != null){
            //银行刷
            if(bankCardApply.getSwipingShopTime() != null){
                if(bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_SWIPING || bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_CANCEL){
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_NOTPAYMENTBILL"));
                }
            }else if(bankCardApply.getSwipingTrusteeTime() != null){//店刷
                if(bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_SWIPING || bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_GET || bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_CANCEL){
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_BANKCARDAPPLY_NOTPAYMENTBILL"));
                }
            }
        }
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<BankCardApplyBean> actStartBankCardByTransaction(CustomerTransactionBean customerTransactionBean) {

        BankCardApplyBean bankCardApply = new BankCardApplyBean();

        if (customerTransactionBean.getBusinessTypeCode() != null) {
            bankCardApply.setBusinessTypeCode(customerTransactionBean.getBusinessTypeCode());
        }
        if (customerTransactionBean.getLoginUserId() != null) {
            bankCardApply.setLoginUserId(customerTransactionBean.getLoginUserId());
        }
        if (customerTransactionBean.getOrginfoId() != null) {
            bankCardApply.setOrginfoId(customerTransactionBean.getOrginfoId());
        }
        if (customerTransactionBean.getEmployeeId() != null) {
            bankCardApply.setEmployeeId(customerTransactionBean.getEmployeeId());
        }
        if (customerTransactionBean.getCarDealerId() != null) {
            bankCardApply.setCarDealerId(customerTransactionBean.getCarDealerId());
        }
        if (customerTransactionBean.getCustomerId() != null) {
            bankCardApply.setCustomerId(customerTransactionBean.getCustomerId());
        }
        if (customerTransactionBean.getId() != null) {
            bankCardApply.setCustomerTransactionId(customerTransactionBean.getId());
        }
        /*CustomerLoanBean d = iCustomerBizService.actGetCustomerLoanById(customerTransactionBean.getCustomerId()).getD();
        if(d.getChargePaymentWay() != null){
            bankCardApply.setChargePaymentWay(d.getChargePaymentWay());
        }*/
        if (customerTransactionBean.getCashSourceId() != null) {
            bankCardApply.setCashSourceId(customerTransactionBean.getCashSourceId());
        }
        bankCardApply.setTs(customerTransactionBean.getTs());
        return this.actStartBankCardApply(bankCardApply);
    }

    @Override
    public ResultBean actDeleteReport(BankCardApplyBean bankCardApplyBean,String loginUserId) {
        if(bankCardApplyBean.getStatus() == BankCardApplyBean.BKSTATUS_SWIPING || bankCardApplyBean.getStatus() == BankCardApplyBean.BKSTATUS_CANCEL){
            List<String> codes = new ArrayList<String>();
            codes.add("B013");
            codes.add("B014");
            codes.add("B035");
            List<CustomerImageFileBean> customerImageFiles = iCustomerImageFileBizService.actFindByCustomerIdAndCustomerImageTypesAndCustomerTransactionId(bankCardApplyBean.getCustomerId(),
                    bankCardApplyBean.getCustomerTransactionId(),
                    codes).getD();
            if(customerImageFiles != null && customerImageFiles.size() > 0){
                for (CustomerImageFileBean cfiles:customerImageFiles) {
                    int successNum = 0;
                    for (String  c : cfiles.getFileIds()){
                        ResultBean<FileBean> resultBean  = iFileBizService.actRealDeleteFileById(c);
                        String filename = null;
                        if(resultBean != null && resultBean.getD() != null && resultBean.getD().getFileName()!=null){
                            filename = resultBean.getD().getFileName();
                        }
                        logger.info("客户ID:"+bankCardApplyBean.getCustomerId()+"征信报告清除操作人:"+loginUserId+",清除时间:"+DateTimeUtils.getCreateTime()+"文件名称:"+filename);
                        System.out.println("客户ID:"+bankCardApplyBean.getCustomerId()+"征信报告清除操作人:"+loginUserId+",清除时间:"+DateTimeUtils.getCreateTime()+"文件名称:"+filename);
                        if(resultBean.isSucceed()){
                            successNum++;
                            logger.error("青云文件删除成功,bcp_file_id:" + c);
                            System.out.println("青云文件删除成功,bcp_file_id:" + c);
                            iFileBizService.actDeleteFile(c);
                        } else {
                            logger.error("青云文件删除失败,bcp_file_id:" + c);
                            System.out.println("青云文件删除失败,bcp_file_id:" + c);
                        }
                    }
                    if(successNum == cfiles.getFileIds().size()){
                        cfiles.setFileIds(new ArrayList<String>());
                        iCustomerImageFileBizService.actSaveCustomerImage(cfiles);
                    }

                    /*if(cfiles.getFileIds().size() > 0 && cfiles.getFileIds() != null){
                        for (int i = 0;i<cfiles.getFileIds().size();i++){
                            ResultBean<FileBean> resultBean = iFileBizService.actRealDeleteFileById(cfiles.getFileIds().get(i));
                            String filename = null;
                            if(resultBean != null && resultBean.getD() != null && resultBean.getD().getFileName()!=null){
                                filename = resultBean.getD().getFileName();
                            }
                            logger.info("客户ID:"+bankCardApplyBean.getCustomerId()+"征信报告清除操作人:"+loginUserId+",清除时间:"+DateTimeUtils.getCreateTime()+"文件名称:"+filename);
                            System.out.println("客户ID:"+bankCardApplyBean.getCustomerId()+"征信报告清除操作人:"+loginUserId+",清除时间:"+DateTimeUtils.getCreateTime()+"文件名称:"+filename);
                            if(resultBean.isSucceed()){
                                iFileBizService.actDeleteFile(cfiles.getFileIds().get(i));
                                cfiles.getFileIds().
                                cfiles.setFileIds(cfiles.getFileIds());
                                iCustomerImageFileBizService.actSaveCustomerImage(cfiles);
                            }
                        }
                    }*/

                }
                return ResultBean.getSucceed().setD("清除成功");
            }else {
                return ResultBean.getSucceed().setD("无相关征信报告");
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<BankCardApplyBean>> actGetByStatus() {
        List<Integer> ds = new ArrayList<Integer>();
        ds.add(BankCardApplyBean.BKSTATUS_SWIPING);
        ds.add(BankCardApplyBean.BKSTATUS_CANCEL);
        List<BankCardApply> bankCardApplies = iBankCardApplyService.getByStatus(ds);
        if(bankCardApplies != null){
            return ResultBean.getSucceed().setD(mappingService.map(bankCardApplies,BankCardApplyBean.class));
        }
        return ResultBean.getFailed();
    }
}
