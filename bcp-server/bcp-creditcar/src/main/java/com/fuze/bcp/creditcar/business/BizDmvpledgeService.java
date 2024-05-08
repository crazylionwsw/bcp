package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.CardActionRecord;
import com.fuze.bcp.api.creditcar.bean.DMVPledgeBean;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;
import com.fuze.bcp.api.creditcar.service.*;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TaskBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.CarRegistry;
import com.fuze.bcp.creditcar.domain.CarTransfer;
import com.fuze.bcp.creditcar.domain.DMVPledge;
import com.fuze.bcp.creditcar.service.ICarRegistryService;
import com.fuze.bcp.creditcar.service.ICarTransferService;
import com.fuze.bcp.creditcar.service.IDmvpledgeService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.EncryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by GQR on 2017/8/24.
 */
@Service
public class BizDmvpledgeService implements IDmvpledgeBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizDmvpledgeService.class);

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    IDmvpledgeService iDmvpledgeService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    ICarRegistryService iCarRegistryService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    ICarTransferService iCarTransferService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IAppointSwipingCardBizService iAppointSwipingCardBizService;

    @Autowired
    IAppointPaymentBizService iAppointPaymentBizService;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    ISwipingCardBizService iSwipingCardBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    MessageService messageService;

    /**
     * 日报
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId ,String date, DMVPledgeBean t) {
        Map<Object, Object> dailyReport = iDmvpledgeService.getDailyReport(orgId,date, mappingService.map(t, DMVPledge.class));
        if(dailyReport != null){
            return ResultBean.getSucceed().setD(dailyReport);
        }
        return ResultBean.getFailed();
    }

    public ResultBean<Map<Object, Object>> getEmployeeReport(String employeeId ,String date, DMVPledgeBean t){
        DMVPledge dmvPledge = mappingService.map(t,DMVPledge.class);
        Map<Object, Object> employeeReport = iDmvpledgeService.getEmployeeReport(employeeId,date,dmvPledge);
        if (employeeReport != null) {
            return ResultBean.getSucceed().setD(employeeReport);
        } else {
            return ResultBean.getFailed();
        }
    }

    /**
     * 创建车辆抵押信息
     * @param carRegistryId
     */
    @Override
    public void actCreateDMVPledge(String carRegistryId) {
        CarRegistry carRegistry = iCarRegistryService.getOne(carRegistryId);
        if(carRegistry != null){
            if(carRegistry.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                logger.error(messageService.getMessage("MSG_DMVPLEDGE_NOCREATE"));
                return;
            }
            DMVPledge dmvPledge = iDmvpledgeService.findByCustomerTransactionId(carRegistry.getCustomerTransactionId());
            if(dmvPledge == null){
                dmvPledge = new DMVPledge();
                dmvPledge.setBusinessTypeCode(carRegistry.getBusinessTypeCode());
                dmvPledge.setLoginUserId(carRegistry.getLoginUserId());
                dmvPledge.setEmployeeId(carRegistry.getEmployeeId());
                dmvPledge.setCustomerId(carRegistry.getCustomerId());
                dmvPledge.setCustomerTransactionId(carRegistry.getCustomerTransactionId());
                dmvPledge.setCarDealerId(carRegistry.getCarDealerId());
                dmvPledge.setStatus(0);
                iDmvpledgeService.save(dmvPledge);
                //启动车辆上牌流程
                dmvPledge = this.startDmvPledge(dmvPledge).getD();
                logger.info(carRegistry.getBillTypeCode() + ":" + carRegistry.getId() + messageService.getMessage("MSG_DMVPLEDGE_SUBMIT"));
            }
        }
    }

    /**
     * 创建车辆抵押信息(监控转移过户通过事件)
     * @param carTransferId
     */
    @Override
    public void actCreateDMVPledgeByCarTransfer(String carTransferId) {
        CarTransfer carTransfer = iCarTransferService.getOne(carTransferId);
        if(carTransfer != null){
            if(carTransfer.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                logger.error(messageService.getMessage("MSG_DMVPLEDGE_NOCREATE"));
                return;
            }
            DMVPledge dmvPledge = iDmvpledgeService.findByCustomerTransactionId(carTransfer.getCustomerTransactionId());
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(carTransfer.getCustomerTransactionId()).getD();
            if(dmvPledge == null){
                dmvPledge = new DMVPledge();
                dmvPledge.setBusinessTypeCode(carTransfer.getBusinessTypeCode());
                dmvPledge.setLoginUserId(carTransfer.getLoginUserId());
                dmvPledge.setEmployeeId(carTransfer.getEmployeeId());
                dmvPledge.setCustomerId(carTransfer.getCustomerId());
                dmvPledge.setCustomerTransactionId(carTransfer.getCustomerTransactionId());
                dmvPledge.setCarDealerId(customerTransaction.getCarDealerId());
                dmvPledge.setStatus(0);
                iDmvpledgeService.save(dmvPledge);
                //启动车辆上牌流程
                dmvPledge = this.startDmvPledge(dmvPledge).getD();
                logger.info(dmvPledge.getBillTypeCode() + ":" + dmvPledge.getId() + messageService.getMessage("MSG_DMVPLEDGE_SUBMIT"));
            }
        }
    }

    @Autowired
    IDealerSharingBizService iDealerSharingBizService;
    /**
     * 完成任务
     * @param dmvPledgeBean
     * @return
     */
    public ResultBean<DMVPledgeBean> actApprovedDmvpledge(DMVPledgeBean dmvPledgeBean,String loginUserId) {
        DMVPledge dmvPledge = mappingService.map(dmvPledgeBean, DMVPledge.class);
        dmvPledge.setLoginUserId(loginUserId);
        if(dmvPledge == null){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_DMVPLEDGE_NOTEXIST"),dmvPledgeBean.getId()));
        }
        LoginUserBean user = iAuthenticationBizService.actGetLoginUser(dmvPledgeBean.getLoginUserId()).getD();
        EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(dmvPledge.getLoginUserId()).getD();
        //获取当前任务
        List<TaskBean> tasklist = iWorkflowBizService.actGetBillTasks(dmvPledgeBean.getBillTypeCode()+"."+dmvPledgeBean.getId()).getD();
        //将完成的任务存到列表中
        List<CardActionRecord> actionRecords = dmvPledgeBean.getActionRecords();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CardActionRecord card = new CardActionRecord();
        String taskId = null;
        TaskBean taskBean = null;
        for (TaskBean task: tasklist) {
            if(dmvPledge.getStatus() == DMVPledgeBean.STATUS_RECEIVE && task.getTaskDefinitionKey() .equals("DMVP_PledgeDateReceive") ){//客户抵押资料签收
                taskId = task.getId();
                Map<String, Object> variables = new HashMap<String, Object>();
                //完成任务
                iWorkflowBizService.actEndTask(taskId,variables);
                taskBean = task;
                iDealerSharingBizService.actSaveSharingDetails(dmvPledge.getCustomerTransactionId(), dmvPledge.getPledgeDateReceiveTime(), 0);
            }else if(dmvPledge.getStatus() == DMVPledgeBean.STATUS_CONTRACT && task.getTaskDefinitionKey() .equals("DMVP_ContractStart") ){//银行抵押合同打印
                taskId = task.getId();
                Map<String, Object> variables = new HashMap<String, Object>();
                //完成任务
                iWorkflowBizService.actEndTask(taskId,variables);
                taskBean = task;
            }else if(dmvPledge.getStatus() == DMVPledgeBean.STATUS_TAKED && task.getTaskDefinitionKey() .equals("DMVP_TakeContract") ){//银行抵押合同盖章
                taskId = task.getId();
                Map<String, Object> variables = new HashMap<String, Object>();
                //完成任务
                iWorkflowBizService.actEndTask(taskId,variables);
                taskBean = task;
            }else if(dmvPledge.getStatus() == DMVPledgeBean.STATUS_START && task.getTaskDefinitionKey() .equals("DMVP_PledgeStart") ){//车管所抵押时间
                taskId = task.getId();
                Map<String, Object> variables = new HashMap<String, Object>();
                //完成任务
                iWorkflowBizService.actEndTask(taskId,variables);
                taskBean = task;
            }else if(dmvPledge.getStatus() == DMVPledgeBean.STATUS_END && task.getTaskDefinitionKey() .equals("DMVP_PledgeEnd") ){//抵押完成
                taskId = task.getId();
                Map<String, Object> variables = new HashMap<String, Object>();
                //完成任务
                iWorkflowBizService.actEndTask(taskId,variables);
                taskBean = task;
                //完成业务流程的抵押业务
                this.completeDmvpledge(dmvPledge);

            }
        }
        if(taskBean != null){
            card.setId(taskBean.getId());
            card.setAction(taskBean.getName());
            card.setTs(df.format(new Date()));
            if(employeeLookupBean != null){
                card.setActionName(employeeLookupBean.getUsername());
            }else {
                LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(dmvPledge.getLoginUserId()).getD();
                if (loginUserBean != null) {
                    card.setActionName(loginUserBean.getUsername());
                }
            }
            actionRecords.add(card);
            //抵押处理通知
            dmvPledge = mappingService.map(dmvPledgeBean, DMVPledge.class);
            dmvPledge  = iDmvpledgeService.save(dmvPledge);
            Boolean billIsFinish = iWorkflowBizService.actBusinessBillIsFinish(dmvPledge.getBillTypeCode() + "." + dmvPledge.getId()).getD();
            dmvPledge = this.dmvpledgeBusinessProcessMessage(dmvPledge, taskBean,billIsFinish,loginUserId).getD();
            dmvPledge = this.dmvpledgeCompleteDispose(dmvPledge).getD();

        }
        return ResultBean.getSucceed().setD(mappingService.map(dmvPledge,DMVPledgeBean.class));
    }

    /**
     * 抵押流程完成后自动完成交易流程
     * @return
     */
    private void completeDmvpledge(DMVPledge dmvPledge) {
        try {
            SignInfo signInfo = new SignInfo();
            signInfo.setResult(ApproveStatus.APPROVE_PASSED);
            iWorkflowBizService.actSignForTransaction(dmvPledge.getCustomerTransactionId(),dmvPledge.getBusinessTypeCode(),dmvPledge.getBillTypeCode(),signInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 业务流程完成时发送mq消息
     * @param dmvPledge
     * @param task
     * @param billIsFinish
     * @param loginUserId
     * @return
     */
    private ResultBean<DMVPledge> dmvpledgeBusinessProcessMessage(DMVPledge dmvPledge, TaskBean task,Boolean billIsFinish,String loginUserId) {
        //抵押完成后通知
        EmployeeLookupBean employeeLookupBean = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
        //TODO 启卡
        Map templateData = new HashMap<>();
        Map sendMap = new HashMap<>();
        List<String> toList = new ArrayList<>();
        if (dmvPledge.getEmployeeId() != null) {
            toList.add(dmvPledge.getEmployeeId());
        }
        if(employeeLookupBean != null){
            toList.add(employeeLookupBean.getId());
        }
        sendMap.put("bd_employee", toList);
        String eventType = dmvPledge.getBusinessTypeCode() + "_" + dmvPledge.getBillTypeCode() + "_" + task.getTaskDefinitionKey();
        MsgRecordBean msgRecordBean = new MsgRecordBean(eventType, dmvPledge.getCustomerTransactionId(), templateData, null, sendMap);
        Map extraFields = new HashMap<>();
        extraFields.put("businessType", dmvPledge.getBusinessTypeCode());
        extraFields.put("billId", dmvPledge.getCustomerTransactionId());
        // 这里的map是为了给pad Push数据时告诉pad跳转时使用
        Map ctrlMap = new HashMap<>();
        ctrlMap.put("afterOpenAction", "3"); // go activity
        // TODO: 2017/10/16 根据系统参配项获取activity
        ctrlMap.put("go_activity", iWorkflowBizService.getPushSendValue(billIsFinish, dmvPledge.getCustomerTransactionId(), dmvPledge.getBillTypeCode()));
        ctrlMap.put("extraFields", extraFields);
        msgRecordBean.setPushCtrlMap(ctrlMap);
        iAmqpBizService.actSendMq(eventType, new Object[]{}, msgRecordBean);
        return ResultBean.getSucceed().setD(dmvPledge);
    }

    /**
     * 抵押业务完成后的处理
     * @param dmvPledge
     * @return
     */
    private ResultBean<DMVPledge> dmvpledgeCompleteDispose(DMVPledge dmvPledge) {
        dmvPledge = iDmvpledgeService.getOne(dmvPledge.getId());
        if(dmvPledge != null && dmvPledge.getStatus().equals(DMVPledgeBean.STATUS_END)){
            PurchaseCarOrderBean purchaseCarOrderBean = iOrderBizService.actGetOrderByTransactionId(dmvPledge.getCustomerTransactionId()).getD();
            if(purchaseCarOrderBean != null){
                CustomerBean customerBean = iCustomerBizService.actGetCustomerById(purchaseCarOrderBean.getCustomerId()).getD();
                //签约通过后将交易信息添加到区块链中
                Map map = new HashMap<String,Object>();
                String hash = EncryUtil.MD5(customerBean.getName()+customerBean.getIdentifyNo());
                map.put("eventType","DMVP_PledgeEnd");
                map.put("Hash",hash);
                map.put("customerName",customerBean.getName());
                map.put("identifyNo",customerBean.getIdentifyNo());
                map.put("dmvpledgeDate",dmvPledge.getPledgeEndTime());
                //垫资存垫资时间，不垫资存刷卡时间
                BankCardApplyBean bankCardApplyBean = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(dmvPledge.getCustomerTransactionId()).getD();
                if(bankCardApplyBean.getSwipingShopTime() != null){
                    map.put("loanDate",bankCardApplyBean.getSwipingShopTime());
                }
                if(bankCardApplyBean.getSwipingTrusteeTime() != null){
                    map.put("loanDate",bankCardApplyBean.getSwipingTrusteeTime());
                }
                //区块链抵押完成后更新链上数据，暂时注释
                //iAmqpBizService.actSendWeb3jMsg(map);
                return ResultBean.getSucceed().setD(dmvPledge);
            }
        }
        return ResultBean.getSucceed().setD(dmvPledge);
    }

    /**
     * 分页列表
     * @param currentPage
     * @return
     */
    @Override
    public ResultBean<DMVPledgeBean> actGetDMVPledges(int currentPage,Integer status) {

        Page<DMVPledge> demands;
        if(status<0){
            demands = iDmvpledgeService.findAllByOrderByTsDesc(currentPage);
        }else {
            demands = iDmvpledgeService.findAllByStatusOrderByTsDesc(currentPage,status);
        }
        return ResultBean.getSucceed().setD(mappingService.map(demands, DMVPledgeBean.class));
    }

    /**
     * 根据ID回显
     * @param id
     * @return
     */
    @Override
    public ResultBean<DMVPledgeBean> actGetDmvpledge(String id) {
        DMVPledge dmvPledge=iDmvpledgeService.getOne(id);
        String code=dmvPledge.getBillTypeCode();
        BillTypeBean billType=iBaseDataBizService.actGetBillType(code).getD();
        DMVPledgeBean dmvPledgeBean=mappingService.map(dmvPledge,DMVPledgeBean.class);
        dmvPledgeBean.setBillType(billType);
        return ResultBean.getSucceed().setD(dmvPledgeBean);
    }

    @Override
    public ResultBean<DMVPledgeBean> actGetDmvpledgeByCustomerTransactionId(String transactionId){
        DMVPledge dmvPledge=iDmvpledgeService.findByCustomerTransactionId(transactionId);
        if (dmvPledge != null){
            String code=dmvPledge.getBillTypeCode();
            BillTypeBean billType=iBaseDataBizService.actGetBillType(code).getD();
            DMVPledgeBean dmvPledgeBean=mappingService.map(dmvPledge,DMVPledgeBean.class);
            dmvPledgeBean.setBillType(billType);
            return ResultBean.getSucceed().setD(dmvPledgeBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DMVPledgeBean> actSaveDMVPledge(DMVPledgeBean dmvPledgeBean) {
        DMVPledge dmvPledge = mappingService.map(dmvPledgeBean, DMVPledge.class);
        if (dmvPledge.getId() == null){
            dmvPledge = iDmvpledgeService.save(dmvPledge);
            return ResultBean.getSucceed().setD(mappingService.map(dmvPledge,DMVPledgeBean.class));
        }
        return ResultBean.getFailed();
    }

    /**
     * 启动车辆抵押流程
     * @param dmvPledge
     * @return
     */
    private ResultBean<DMVPledge> startDmvPledge(DMVPledge dmvPledge) {
        //如果需要的话，启动工作流。
        Document document = DMVPledge.class.getAnnotation(Document.class);
        String collectionName = document.collection();
        SignInfo signInfo = new SignInfo(dmvPledge.getLoginUserId(), dmvPledge.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, messageService.getMessage("MSG_DMVPLEDGE_COMMENT"));
        iWorkflowBizService.actCreateWorkflow(dmvPledge.getBusinessTypeCode(), dmvPledge.getId(), dmvPledge.getBillTypeCode(), collectionName, null, dmvPledge.getCustomerTransactionId());
        return ResultBean.getSucceed().setD(dmvPledge);

    }

    @Override
    public ResultBean<DMVPledgeBean> actSearchPromoteDMVPledges(SearchBean searchBean) {
        Page<DMVPledge> dmvPledges = iDmvpledgeService.findAllBySearchBean(DMVPledge.class,searchBean,SearchBean.STAGE_ORDER,null);
        return ResultBean.getSucceed().setD(mappingService.map(dmvPledges,DMVPledgeBean.class));
    }

    @Override
    public ResultBean<DMVPledgeBean> actSaveDMVPledgeInfo(DMVPledgeBean dmvPledgeBean) {
        DMVPledge dmvPledge = iDmvpledgeService.save(mappingService.map(dmvPledgeBean, DMVPledge.class));
        return ResultBean.getSucceed().setD(mappingService.map(dmvPledge,DMVPledgeBean.class));
    }
}
