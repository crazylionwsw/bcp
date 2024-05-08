package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppAppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardListBean;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.IAppointSwipingCardBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.AppointSwipingCard;
import com.fuze.bcp.creditcar.domain.BankCardApply;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.service.IAppointSwipingCardService;
import com.fuze.bcp.creditcar.service.IBankCardApplyService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gqr on 2017/8/17.
 */
@Service
public class BizAppointSwipingCardService implements IAppointSwipingCardBizService {


    private static final Logger logger = LoggerFactory.getLogger(BizAppointSwipingCardService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    /**
     * 创建预约刷卡单
     *
     * @param orderId
     * @return
     */
    @Override
    public ResultBean<AppointSwipingCardBean> actCreateAppointSwipingCard(String orderId) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.getOne(orderId);
        if (purchaseCarOrder == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
        if(purchaseCarOrder.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
            logger.error(messageService.getMessage("MSG_APPOINTSWIPING_NOCREATE"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTSWIPING_NOCREATE"));
        }
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        //从垫资政策获取是否需要垫资
        if(customerLoanBean.getIsNeedPayment() != null && customerLoanBean.getIsNeedPayment() == 0){
        /*//贴息的时候才会创建预约刷卡单
        if (customerLoanBean.getCompensatoryAmount() != null && customerLoanBean.getCompensatoryInterest() == 1) {}*/
            AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
            if (appointSwipingCard == null) {
                appointSwipingCard = new AppointSwipingCard();
                appointSwipingCard.setBusinessTypeCode(purchaseCarOrder.getBusinessTypeCode());
                appointSwipingCard.setLoginUserId(purchaseCarOrder.getLoginUserId());
                appointSwipingCard.setEmployeeId(purchaseCarOrder.getEmployeeId());
                appointSwipingCard.setCustomerId(purchaseCarOrder.getCustomerId());
                appointSwipingCard.setCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());

                //TODO 刷卡金额 == 贷款额度- 贴息额
                appointSwipingCard.setAppointPayAmount(customerLoanBean.getSwipingAmount());
                appointSwipingCard = iAppointSwipingCardService.tempSave(appointSwipingCard);

                return ResultBean.getSucceed().setD(mappingService.map(appointSwipingCard, AppointSwipingCardBean.class));
            }

        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
    }

    /**
     * 保存预约刷卡单(只做暂存)
     *
     * @param appointSwipingCardSubmissionBean
     * @return
     */
    @Override
    public ResultBean<AppointSwipingCardSubmissionBean> actSaveAppointSwipingCard(AppointSwipingCardSubmissionBean appointSwipingCardSubmissionBean) {
        ResultBean result = iAppointSwipingCardService.getEditableBill(appointSwipingCardSubmissionBean.getId());
        if (result.failed()) return result;
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(appointSwipingCardSubmissionBean.getCustomerTransactionId());
        if (result.failed()) return result;
        //PAD端提交数据
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.getOne(appointSwipingCardSubmissionBean.getId());
        appointSwipingCard.setPickupDate(appointSwipingCardSubmissionBean.getPickupDate());
        appointSwipingCard.setAppointPayTime(appointSwipingCardSubmissionBean.getAppointPayTime());
        appointSwipingCard.setAppointTakeTime(appointSwipingCardSubmissionBean.getAppointTakeTime());
        appointSwipingCard.setAppointPayAmount(appointSwipingCardSubmissionBean.getAppointPayAmount());
        appointSwipingCard.setIsNeedLoaning(appointSwipingCardSubmissionBean.getIsNeedLoaning());

        //处理档案资料
        iCustomerImageFileBizService.actSaveCustomerImages(appointSwipingCard.getCustomerId(),
                appointSwipingCard.getCustomerTransactionId(),
                appointSwipingCardSubmissionBean.getCustomerImages()); //整体保存档案资料

        appointSwipingCard = iAppointSwipingCardService.save(appointSwipingCard);

        //保存预约刷卡单成功后发送MQ消息
        try {
            MsgRecordBean msgRecordBean = new MsgRecordBean("NC_A026_AppointSwipingCard_Save", appointSwipingCard.getCustomerTransactionId(), null, null, null);
            iAmqpBizService.actSendMq("NC_A026_AppointSwipingCard_Save", new Object[]{appointSwipingCard.getId()}, msgRecordBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appointSwipingCard != null) {
            appointSwipingCardSubmissionBean.setId(appointSwipingCard.getId());
            return ResultBean.getSucceed().setD(appointSwipingCardSubmissionBean).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
    }

    /**
     * 提交预约刷卡
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<AppointSwipingCardBean> actSubmitAppointSwipingCard(String id, String comment) {
        ResultBean result = iAppointSwipingCardService.getEditableBill(id);
        if (result.failed()) return result;

        AppointSwipingCard appointSwipingCard = (AppointSwipingCard) result.getD();

        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(appointSwipingCard.getCustomerTransactionId());
        if (result.failed()) return result;

        // 获取卡业务处理流程，判断制卡是否完成
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
        //若制卡未完成，则不允许预约刷卡
        if(bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_CANCEL){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTSWIPINGCARD_BANKCARDCANCEL"));
        }else if (bankCardApply.getApplyTime() == null) {//若制卡未完成，则不允许预约垫资
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTSWIPINGCARD_BANKCARAPPLYNOAPPLY"));
        }

        //启动客户签约流程
        appointSwipingCard = this.startAppointSwipingCard(appointSwipingCard, comment).getD();
        if(appointSwipingCard != null){
            appointSwipingCard.setStatus(AppointSwipingCardBean.APPOINTSWIPINGSTATUS_SUBMIT);
            iAppointSwipingCardService.save(appointSwipingCard);
        }
        logger.info(appointSwipingCard.getBillTypeCode() + ":" + appointSwipingCard.getId() + messageService.getMessage("MSG_APPOINTSWIPINGCARD_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(appointSwipingCard, AppointSwipingCardBean.class)).setM(messageService.getMessage("MSG_APPOINTSWIPINGCARD_SUBMIT"));
    }

    @Override
    public ResultBean<AppointSwipingCardBean> actGetAppointSwipingCard(String id) {

        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.getOne(id);
        if (appointSwipingCard != null) {
            String code = appointSwipingCard.getBillTypeCode();
            //通过编码获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            AppointSwipingCardBean appointSwipingCardBean = mappingService.map(appointSwipingCard, AppointSwipingCardBean.class);
            appointSwipingCardBean.setBillType(billType);
            return ResultBean.getSucceed().setD(appointSwipingCardBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<AppointSwipingCardSubmissionBean> actGetAppointSwipingCardByTransactionId(String transactionId) {
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(transactionId);
        if (appointSwipingCard != null) {
            AppointSwipingCardSubmissionBean appointSwipingCardSubmissionBean = mappingService.map(appointSwipingCard, AppointSwipingCardSubmissionBean.class);

            //档案类型
            List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(appointSwipingCard.getCustomerId(),
                    transactionId,
                    appointSwipingCard.getBusinessTypeCode(),
                    appointSwipingCard.getBillTypeCode()).getD();

            appointSwipingCardSubmissionBean.setCustomerImages(imageTypeFiles);

            return ResultBean.getSucceed().setD(appointSwipingCardSubmissionBean);
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_SWIPINGCARD_TRANSACTION_NOT_FIND"), transactionId));
    }

    /**
     * 获取分期经理的预约刷卡单
     * @param isPass
     * @param loginUserId
     * @param currentPage
     * @param currentSize
     * @return
     */
    @Override
    public ResultBean<List<AppointSwipingCardListBean>> actGetAppointSwipingCards(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize) {
        Page<AppointSwipingCard> appointSwipingCards = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_APPOINTSWIPINGCARD_LOGINUSERID_ID_NULL"), loginUserId));
        }

        List<String> tids = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if(isPass){
            appointSwipingCards = this.iAppointSwipingCardService.findCompletedItemsByUser(AppointSwipingCard.class, loginUserId, tids, currentPage, currentSize);
            if (appointSwipingCards == null || appointSwipingCards.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTSWIPINGCARD_LOGINUSERID_HISTORY_NULL"));
            }
        }else{
            appointSwipingCards = this.iAppointSwipingCardService.findPendingItemsByUser(AppointSwipingCard.class, loginUserId, tids, currentPage, currentSize);
            if (appointSwipingCards == null || appointSwipingCards.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTSWIPINGCARD_LOGINUSERID_NULL"));
            }
        }


        AppointSwipingCardListBean appointSwipingCardListBean = null;
        DataPageBean<AppointSwipingCardListBean> destination = new DataPageBean<AppointSwipingCardListBean>();
        destination.setPageSize(appointSwipingCards.getSize());
        destination.setTotalCount(appointSwipingCards.getTotalElements());
        destination.setTotalPages(appointSwipingCards.getTotalPages());
        destination.setCurrentPage(appointSwipingCards.getNumber());
        for (AppointSwipingCard appointSwipingCard : appointSwipingCards.getContent()) {
            if (appointSwipingCard != null) {
                appointSwipingCardListBean = mappingService.map(appointSwipingCard, AppointSwipingCardListBean.class);

                //获取交易概览
                TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(appointSwipingCard.getCustomerTransactionId()).getD();
                transactionSummary.setApproveStatus(appointSwipingCard.getApproveStatus());
                appointSwipingCardListBean.setTransactionSummary(transactionSummary);
            }

            destination.getResult().add(appointSwipingCardListBean);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    /**
     * 根据交易ID获取详情数据
     *
     * @param transactionId
     * @return
     */
    @Override
    public ResultBean<AppointSwipingCardSubmissionBean> actInitAppointSwipingCardByTransactionId(String transactionId) {
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(transactionId);
        AppointSwipingCardSubmissionBean appointSwipingCardSubmissionBean = mappingService.map(appointSwipingCard, AppointSwipingCardSubmissionBean.class);

        //档案类型
        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(appointSwipingCard.getCustomerId(),
                appointSwipingCard.getCustomerTransactionId(),
                appointSwipingCard.getBusinessTypeCode(),
                appointSwipingCard.getBillTypeCode()).getD();
        appointSwipingCardSubmissionBean.setCustomerImages(imageTypeFiles);

        return ResultBean.getSucceed().setD(appointSwipingCardSubmissionBean);
    }

    /**
     * 预约刷卡签批
     * @param id
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<AppointSwipingCardBean> actSignAppointSwipingCard(String id, SignInfo signInfo) {
        //提交审核任务
        try {
            ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSignBill(id, signInfo);
            if(resultBean.failed()){
                return ResultBean.getFailed().setM(resultBean.getM());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }
        //获取预约刷卡信息
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.getOne(id);
        //通过的话将业务状态改为已确认
        if(signInfo.getResult() == ApproveStatus.APPROVE_PASSED){
            appointSwipingCard.setStatus(AppointSwipingCardBean.APPOINTSWIPINGSTATUS_CONFIRM);
            appointSwipingCard = iAppointSwipingCardService.save(appointSwipingCard);
        }
        //查询当前单据任务是否完成
        return ResultBean.getSucceed().setD(mappingService.map(appointSwipingCard, AppointSwipingCardBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    @Override
    public ResultBean<AppointSwipingCardBean> actSearchAppointSwipingCards(String userId, SearchBean searchBean) {
        Page<AppointSwipingCard> appointSwipingCards = iAppointSwipingCardService.findAllBySearchBean(AppointSwipingCard.class,searchBean, searchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(appointSwipingCards,AppointSwipingCardBean.class));
    }

    @Override
    public ResultBean<AppointSwipingCardBean> actGetAppointSwipingCardByCustomerTransactionId(String transactionId) {
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(transactionId);
        if (appointSwipingCard != null) {
            String code = appointSwipingCard.getBillTypeCode();
            //通过编码获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            AppointSwipingCardBean appointSwipingCardBean = mappingService.map(appointSwipingCard, AppointSwipingCardBean.class);
            appointSwipingCardBean.setBillType(billType);
            return ResultBean.getSucceed().setD(appointSwipingCardBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<AppointSwipingCardBean> actUpdateStatus(String id, Integer status){
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.getOne(id);
        if (appointSwipingCard != null) {
            appointSwipingCard.setStatus(status);
            appointSwipingCard = iAppointSwipingCardService.save(appointSwipingCard);
        }
        return ResultBean.getSucceed().setD(mappingService.map(appointSwipingCard, AppointSwipingCardBean.class));
    }

    /**
     * app端获取预约刷卡信息
     * @param customerTransactionId
     * @return
     */
    @Override
    public ResultBean<AppAppointSwipingCardBean> actGetAppAppointSwipingCardByCustomerTransactionId(String customerTransactionId) {
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(customerTransactionId);
        if(appointSwipingCard != null){
            // 业务校验
            ResultBean result = iCustomerTransactionBizService.actGetEditableTransaction(appointSwipingCard.getCustomerTransactionId());
            if (result.failed()) return result;
            if(appointSwipingCard.getApproveStatus() == ApproveStatus.APPROVE_ONGOING){
                return ResultBean.getSucceed().setD(mappingService.map(appointSwipingCard,AppAppointSwipingCardBean.class));
            }else if(appointSwipingCard.getApproveStatus() == ApproveStatus.APPROVE_INIT || appointSwipingCard.getApproveStatus() == ApproveStatus.APPROVE_INIT){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTSWIPINGCARD_NOSUBMIT"));
            }else{
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTSWIPINGCARD_ALREADYPASS"));
            }
        }
        return ResultBean.getFailed();
    }

    /**
     * 启动预约刷卡流程
     *
     * @param appointSwipingCard
     * @param comment
     * @return
     */
    private ResultBean<AppointSwipingCard> startAppointSwipingCard(AppointSwipingCard appointSwipingCard, String comment) {
        SignInfo signInfo = new SignInfo(appointSwipingCard.getLoginUserId(), appointSwipingCard.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //进行审批
        String collectionMame = null;
        try {
            collectionMame = AppointSwipingCard.getMongoCollection(appointSwipingCard);
        } catch (Exception e) {
            logger.error("启动预约刷卡流程", e);
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(appointSwipingCard.getBusinessTypeCode(), appointSwipingCard.getId(), appointSwipingCard.getBillTypeCode(), signInfo, collectionMame, null, appointSwipingCard.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    appointSwipingCard = iAppointSwipingCardService.getOne(appointSwipingCard.getId());
                    appointSwipingCard.setTs(DateTimeUtils.getCreateTime());
                    appointSwipingCard = iAppointSwipingCardService.save(appointSwipingCard);
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(appointSwipingCard);
    }

    @Override
    public ResultBean<AppointSwipingCardBean> actSaveAppointSwipingCardInfo(AppointSwipingCardBean appointSwipingCardBean) {
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.save(mappingService.map(appointSwipingCardBean, AppointSwipingCard.class));
        return ResultBean.getSucceed().setD(mappingService.map(appointSwipingCard,AppointSwipingCardBean.class));
    }
}
