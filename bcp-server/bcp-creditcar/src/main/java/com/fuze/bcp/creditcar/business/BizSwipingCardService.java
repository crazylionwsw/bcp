package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardListBean;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.creditcar.service.ISwipingCardBizService;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.bean.CustomerRepaymentBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.AppointPayment;
import com.fuze.bcp.creditcar.domain.AppointSwipingCard;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.domain.SwipingCard;
import com.fuze.bcp.creditcar.service.IAppointPaymentService;
import com.fuze.bcp.creditcar.service.IAppointSwipingCardService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.creditcar.service.ISwipingCardService;
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
import java.util.Map;

/**
 * Created by Lily on 2017/9/15.
 */
@Service
public class BizSwipingCardService implements ISwipingCardBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizSwipingCardService.class);

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ISwipingCardService iSwipingCardService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    @Override
    public ResultBean<SwipingCardBean> actCreateSwipingCard(String appointSwipingCardId) {
        //TODO 在给还款信息赋值时需要通过交易ID查看是否存在，存在更新，不存在创建
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.getOne(appointSwipingCardId);
        if (appointSwipingCard != null) {
            if(appointSwipingCard.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                logger.error(messageService.getMessage("MSG_SWIPINGCARD_NOCREATE"));
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_SWIPINGCARD_NOCREATE"));
            }
            SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
            CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId()).getD();
            if (swipingCard == null) {
                swipingCard = new SwipingCard();
                swipingCard.setBusinessTypeCode(appointSwipingCard.getBusinessTypeCode());
                swipingCard.setLoginUserId(appointSwipingCard.getLoginUserId());
                swipingCard.setEmployeeId(appointSwipingCard.getEmployeeId());
                swipingCard.setCustomerId(appointSwipingCard.getCustomerId());
                swipingCard.setCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
                swipingCard.setCarDealerId(appointSwipingCard.getCarDealerId());
                swipingCard.setPayAmount(appointSwipingCard.getAppointPayAmount());
                if(customerCardBean != null){
                    swipingCard.setCardNumber(customerCardBean.getCardNo());
                }
                iSwipingCardService.tempSave(swipingCard);
            }
        }
        return null;
    }

    @Override
    public ResultBean<SwipingCardSubmissionBean> actSaveSwipingCard(SwipingCardSubmissionBean swipingCardSubmissionBean) {
        ResultBean result = iSwipingCardService.getEditableBill(swipingCardSubmissionBean.getId());
        if (result.failed()) return result;
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(swipingCardSubmissionBean.getCustomerTransactionId());
        if (result.failed()) return result;
        //PAD端提交数据
        SwipingCard swipingCard = iSwipingCardService.getOne(swipingCardSubmissionBean.getId());
        swipingCard.setPayTime(swipingCardSubmissionBean.getPayTime());
        swipingCard.setPayAmount(swipingCardSubmissionBean.getPayAmount());
        swipingCard.setCardNumber(swipingCardSubmissionBean.getCardNumber());
        //处理渠道还款
        if (swipingCardSubmissionBean.getCustomerRepaymentBean() != null) {
            CustomerRepaymentBean customerRepayment = swipingCardSubmissionBean.getCustomerRepaymentBean();
            CustomerRepaymentBean customerRepaymentBean = iCustomerBizService.actGetCustomerRepaymentByCustomerTransactionId(swipingCardSubmissionBean.getCustomerTransactionId()).getD();
            PurchaseCarOrder order = iOrderService.findByCustomerTransactionId(swipingCard.getCustomerTransactionId());
            //还款信息存在则更新，不存在则创建
            if (customerRepaymentBean != null) {
                customerRepayment.setId(customerRepaymentBean.getId());
                customerRepayment.setCustomerTransactionId(swipingCardSubmissionBean.getCustomerTransactionId());
                customerRepayment.setCustomerId(swipingCardSubmissionBean.getCustomerId());
                customerRepayment.setTotalRepayment(order.getRepaymentAmountSum() != null ? Double.parseDouble(order.getRepaymentAmountSum()) : 0.0);
                iCustomerBizService.actSaveCustomerRepayment(customerRepayment);
            } else {
                customerRepayment.setCustomerTransactionId(swipingCardSubmissionBean.getCustomerTransactionId());
                customerRepayment.setCustomerId(swipingCardSubmissionBean.getCustomerId());
                customerRepayment.setTotalRepayment(order.getRepaymentAmountSum() != null ? Double.parseDouble(order.getRepaymentAmountSum()) : 0.0);
                customerRepayment = iCustomerBizService.actSaveCustomerRepayment(customerRepayment).getD();
            }
        }
        //处理档案资料
        iCustomerImageFileBizService.actSaveCustomerImages(swipingCard.getCustomerId(),
                swipingCard.getCustomerTransactionId(),
                swipingCardSubmissionBean.getCustomerImages()); //整体保存档案资料
        swipingCard = iSwipingCardService.save(swipingCard);
        if (swipingCard != null) {
            swipingCardSubmissionBean.setId(swipingCard.getId());
            return ResultBean.getSucceed().setD(swipingCardSubmissionBean).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
    }

    @Override
    public ResultBean<SwipingCardSubmissionBean> actSavePadSwipingCard(SwipingCardSubmissionBean swipingCardSubmissionBean) {
        CustomerRepaymentBean customerRepayment = new CustomerRepaymentBean();
        customerRepayment.setFirstRepayment(swipingCardSubmissionBean.getFirstRepayment());
        customerRepayment.setFirstRepaymentDate(swipingCardSubmissionBean.getFirstRepaymentDate());
        customerRepayment.setMonthRepayment(swipingCardSubmissionBean.getMonthRepayment());
        customerRepayment.setRepayment(swipingCardSubmissionBean.getRepayment());
        customerRepayment.setBillingDate(swipingCardSubmissionBean.getBillingDate());
        swipingCardSubmissionBean.setCustomerRepaymentBean(customerRepayment);
        return actSaveSwipingCard(swipingCardSubmissionBean);
    }

    @Override
    public ResultBean<SwipingCardBean> actSubmitSwipingCard(String id, String comment) {
        ResultBean result = iSwipingCardService.getEditableBill(id);
        if (result.failed()) return result;
        SwipingCard swipingCard = iSwipingCardService.getOne(id);
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(swipingCard.getCustomerTransactionId());
        if (result.failed()) return result;
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(swipingCard.getCustomerTransactionId());
        //若是特殊垫资，必须在预约垫资通过之后才可以提交渠道刷卡进入审批流
        if (appointSwipingCard != null) {
            if (appointSwipingCard.getIsNeedLoaning() == 1 ) {
                AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(swipingCard.getCustomerTransactionId());
                if(appointPayment != null && appointPayment.getApproveStatus() != ApproveStatus.APPROVE_PASSED)
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_SWIPING_APPOINTSWIPINGCARDNOPASSED"));
            }
        }
        swipingCard.setDataStatus(DataStatus.SAVE);
        swipingCard.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
        swipingCard = iSwipingCardService.save(swipingCard);
        //启动工作流
        swipingCard = this.startSwipingCard(swipingCard, comment).getD();
        if(swipingCard != null){
            swipingCard.setStatus(SwipingCardBean.SWIPINGSTATUS_SUBMIT);
            iSwipingCardService.save(swipingCard);
        }
        logger.info(swipingCard.getBillTypeCode() + ":" + swipingCard.getId() + messageService.getMessage("MSG_SWIPINGCARD_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(swipingCard, SwipingCardBean.class)).setM(messageService.getMessage("MSG_SWIPINGCARD_SUBMIT"));
    }

    @Override
    public ResultBean<List<SwipingCardListBean>> actGetSwipingCard(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize) {
        Page<SwipingCard> swipingCards = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_SWIPINGCARD_LOGINUSERID_ID_NULL"), loginUserId));
        }

        List<String> tids = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if(isPass){
            swipingCards = this.iSwipingCardService.findCompletedItemsByUser(SwipingCard.class, loginUserId, tids, currentPage, currentSize);
            if (swipingCards == null || swipingCards.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_SWIPINGCARD_LOGINUSERID_HISTORY_NULL"));
            }
        }else{
            swipingCards = this.iSwipingCardService.findPendingItemsByUser(SwipingCard.class, loginUserId, tids,currentPage, currentSize);
            if (swipingCards == null || swipingCards.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_SWIPINGCARD_LOGINUSERID_NULL"));
            }
        }


        SwipingCardListBean swipingCardListBean = null;
        DataPageBean<SwipingCardListBean> destination = new DataPageBean<SwipingCardListBean>();
        destination.setPageSize(swipingCards.getSize());
        destination.setTotalCount(swipingCards.getTotalElements());
        destination.setTotalPages(swipingCards.getTotalPages());
        destination.setCurrentPage(swipingCards.getNumber());
        for (SwipingCard swipingCard : swipingCards.getContent()) {
            if (swipingCard != null) {
                swipingCardListBean = mappingService.map(swipingCard, SwipingCardListBean.class);
                //获取交易概览
                TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(swipingCard.getCustomerTransactionId()).getD();
                transactionSummary.setApproveStatus(swipingCard.getApproveStatus());
                swipingCardListBean.setTransactionSummary(transactionSummary);
            }

            destination.getResult().add(swipingCardListBean);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<SwipingCardSubmissionBean> actInitSwipingCardByTransactionId(String transactionId) {
        SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(transactionId);
        SwipingCardSubmissionBean swipingCardSubmissionBean = mappingService.map(swipingCard, SwipingCardSubmissionBean.class);

        //档案类型
        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(swipingCard.getCustomerId(),
                swipingCard.getCustomerTransactionId(),
                swipingCard.getBusinessTypeCode(),
                swipingCard.getBillTypeCode()).getD();
        swipingCardSubmissionBean.setCustomerImages(imageTypeFiles);

        //显示还款信息
        CustomerRepaymentBean customerRepaymentBean = iCustomerBizService.actGetCustomerRepaymentByCustomerTransactionId(swipingCardSubmissionBean.getCustomerTransactionId()).getD();
        if (customerRepaymentBean != null) {
            swipingCardSubmissionBean.setCustomerRepaymentBean(customerRepaymentBean);
            swipingCardSubmissionBean.setFirstRepayment(customerRepaymentBean.getFirstRepayment());
            swipingCardSubmissionBean.setFirstRepaymentDate(customerRepaymentBean.getFirstRepaymentDate());
            swipingCardSubmissionBean.setMonthRepayment(customerRepaymentBean.getMonthRepayment());
            swipingCardSubmissionBean.setRepayment(customerRepaymentBean.getRepayment());
            swipingCardSubmissionBean.setBillingDate(customerRepaymentBean.getBillingDate());
        } else {
            PurchaseCarOrder order = iOrderService.findByCustomerTransactionId(swipingCardSubmissionBean.getCustomerTransactionId());
            if (order != null) {
                //首期还款额
                swipingCardSubmissionBean.setFirstRepayment(order.getRepaymentAmountFirstMonth() != null ? Double.parseDouble(order.getRepaymentAmountFirstMonth()) : 0.0);
                //月还款额
                swipingCardSubmissionBean.setMonthRepayment(order.getRepaymentAmountFirstMonth() != null ? Double.parseDouble(order.getRepaymentAmountMonthly()) : 0.0);
            }
        }
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        if (purchaseCarOrder != null) {
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            if (customerLoanBean != null) {
                if ("STAGES".equals(customerLoanBean.getChargePaymentWay())) {
                    swipingCardSubmissionBean.setChargePaymentWay("分期");
                } else {
                    swipingCardSubmissionBean.setChargePaymentWay("趸交");
                }
//                swipingCardSubmissionBean.setChargePaymentWay(customerLoanBean.getChargePaymentWay());
                swipingCardSubmissionBean.setRealityBankFeeAmount(customerLoanBean.getBankFeeAmount());
            }
        }
        return ResultBean.getSucceed().setD(swipingCardSubmissionBean);
    }

    @Override
    public ResultBean<SwipingCardBean> actGetSwipingCard(String id) {
        SwipingCard swipingCard = iSwipingCardService.getOne(id);
        if (swipingCard != null) {
            String code = swipingCard.getBillTypeCode();
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            SwipingCardBean swipingCardBean = mappingService.map(swipingCard, SwipingCardBean.class);
            swipingCardBean.setBillType(billType);
            return ResultBean.getSucceed().setD(swipingCardBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<SwipingCardBean> actGetSwipingCardByTransactionId(String transactionId){
        SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(transactionId);
        if (swipingCard != null) {
            String code = swipingCard.getBillTypeCode();
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            SwipingCardBean swipingCardBean = mappingService.map(swipingCard, SwipingCardBean.class);
            swipingCardBean.setBillType(billType);
            return ResultBean.getSucceed().setD(swipingCardBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<SwipingCardBean> actUpdateStatus(String id, Integer status){
        SwipingCard swipingCard = iSwipingCardService.getOne(id);
        if (swipingCard != null) {
            swipingCard.setStatus(status);
            swipingCard = iSwipingCardService.save(swipingCard);
        }
        return ResultBean.getSucceed().setD(mappingService.map(swipingCard, SwipingCardBean.class));
    }

    @Override
    public ResultBean<SwipingCardBean> actSignSwipingCard(String id, SignInfo signInfo) {
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
        //获取客户签约信息
        SwipingCard swipingCard = iSwipingCardService.getOne(id);
        //查询当前单据任务是否完成
        return ResultBean.getSucceed().setD(mappingService.map(swipingCard, SwipingCardBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    /**
     * 渠道刷卡日报
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgid,String date, SwipingCardBean t) {
        Map<Object, Object> dailyReport = iSwipingCardService.getDailyReport(orgid,date, mappingService.map(t, SwipingCard.class));
        if(dailyReport != null){
            return ResultBean.getSucceed().setD(dailyReport);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<SwipingCardBean> actSaveSwipingcard(SwipingCardBean swipingCardBean) {
        SwipingCard swipingCard = mappingService.map(swipingCardBean, SwipingCard.class);
        swipingCard.setApproveStatus(ApproveStatus.APPROVE_PASSED);
        swipingCard = iSwipingCardService.save(swipingCard);
        //渠道刷卡确认后发送mq消息
        /*try {
            MsgRecordBean msgRecordBean = new MsgRecordBean("NC_A019_SwipingCar_Confirm", swipingCard.getCustomerTransactionId(), null, null, null);
            iAmqpBizService.actSendMq("NC_A019_SwipingCar_Confirm", new Object[]{swipingCard.getId()}, msgRecordBean);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return ResultBean.getSucceed().setD(mappingService.map(swipingCard, SwipingCardBean.class));
    }

    @Override
    public ResultBean<SwipingCardBean> actSearchSwipingCards(String userId, SearchBean searchBean) {
        Page<SwipingCard> swipingCards = iSwipingCardService.findAllBySearchBean(SwipingCard.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(swipingCards,SwipingCardBean.class));
    }

    /**
     * 启动渠道刷卡流程
     *
     * @param swipingCard
     * @param comment
     * @return
     */
    private ResultBean<SwipingCard> startSwipingCard(SwipingCard swipingCard, String comment) {
        SignInfo signInfo = new SignInfo(swipingCard.getLoginUserId(), swipingCard.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //进行审批
        String collectionMame = null;
        try {
            collectionMame = SwipingCard.getMongoCollection(swipingCard);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }

        ResultBean resultBean = iWorkflowBizService.actSubmit(swipingCard.getBusinessTypeCode(), swipingCard.getId(), swipingCard.getBillTypeCode(), signInfo, collectionMame, null, swipingCard.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    swipingCard = iSwipingCardService.getOne(swipingCard.getId());
                    swipingCard.setTs(DateTimeUtils.getCreateTime());
                    swipingCard = iSwipingCardService.save(swipingCard);
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(swipingCard);
    }


    @Override
    public ResultBean<SwipingCardBean> actSaveSwipingCardInfo(SwipingCardBean swipingCardBean) {
        SwipingCard swipingCard = iSwipingCardService.save(mappingService.map(swipingCardBean, SwipingCard.class));
        return ResultBean.getSucceed().setD(mappingService.map(swipingCard,SwipingCardBean.class));
    }
}
