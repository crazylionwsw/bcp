package com.fuze.bcp.creditcar.business;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentBean;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentListBean;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.creditcar.service.IDealerRepaymentBizService;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.AppointPayment;
import com.fuze.bcp.creditcar.domain.DealerRepayment;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.service.IAppointPaymentService;
import com.fuze.bcp.creditcar.service.IDealerRepaymentService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 渠道还款服务接口实现
 * Created by Lily on 2017/9/15.
 */
@Service
public class BizDealerRepaymentService implements IDealerRepaymentBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizDealerRepaymentService.class);
    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    IDealerRepaymentService iDealerRepaymentService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    @Override
    public ResultBean<DealerRepaymentBean> actCreateDealerRepayment(String paymentId) {
        //TODO 垫资支付完成创建还款单
        AppointPayment appointPayment = iAppointPaymentService.getOne(paymentId);
        if (appointPayment == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
        if(appointPayment.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
            logger.error(messageService.getMessage("MSG_DEALERREPAYMENT_NOCREATE"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEALERREPAYMENT_NOCREATE"));
        }
        //贴息的时候才需要创建还款信息
        if(appointPayment.getIsNeedLoaning().equals(1)){
            //TODO 在创建单据时需要将一些不可更改的数据赋值
            DealerRepayment dealerRepayment = iDealerRepaymentService.findByCustomerTransactionId(appointPayment.getCustomerTransactionId());
            if (dealerRepayment == null) {
                dealerRepayment = new DealerRepayment();
                dealerRepayment.setBusinessTypeCode(appointPayment.getBusinessTypeCode());
                dealerRepayment.setLoginUserId(appointPayment.getLoginUserId());
                dealerRepayment.setEmployeeId(appointPayment.getEmployeeId());
                dealerRepayment.setCustomerId(appointPayment.getCustomerId());
                dealerRepayment.setCustomerTransactionId(appointPayment.getCustomerTransactionId());
                dealerRepayment.setAmount(appointPayment.getAppointPayAmount());
                dealerRepayment = iDealerRepaymentService.tempSave(dealerRepayment);
                return null;
            }
        }
        return null;
    }
    @Override
    public ResultBean<DealerRepaymentSubmissionBean> actSaveDealerRepayment(DealerRepaymentSubmissionBean dealerRepaymentSubmissionBean) {
        ResultBean resultBean = iDealerRepaymentService.getEditableBill(dealerRepaymentSubmissionBean.getId());
        if (resultBean.failed()) return resultBean;
        //PAD端提交数据
        DealerRepayment dealerRepayment = iDealerRepaymentService.getOne(dealerRepaymentSubmissionBean.getId());
        // 业务校验
        CustomerTransactionBean transaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(dealerRepayment.getCustomerTransactionId()).getD();
        if (transaction == null)
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_NONULL"));

        //需要判断单据状态是否取消或正在取消
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONCANCELLING"));
        }
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONCANCELLED"));
        }
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONSTOP"));
        }

        dealerRepayment.setRepaymentTime(dealerRepaymentSubmissionBean.getRepaymentTime());
        dealerRepayment.setAmount(dealerRepaymentSubmissionBean.getAmount());
        dealerRepayment.setTradeWay(dealerRepaymentSubmissionBean.getTradeWay());
        //处理档案资料
        iCustomerImageFileBizService.actSaveCustomerImages(dealerRepayment.getCustomerId(),
                dealerRepayment.getCustomerTransactionId(),
                dealerRepaymentSubmissionBean.getCustomerImages()); //整体保存档案资料
        dealerRepayment = iDealerRepaymentService.save(dealerRepayment);

        if (dealerRepayment != null) {
            return ResultBean.getSucceed().setD(dealerRepaymentSubmissionBean).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
    }

    @Override
    public ResultBean<DealerRepaymentBean> actSubmitDealerRepayment(String id, String comment) {
        ResultBean resultBean = iDealerRepaymentService.getEditableBill(id);
        if (resultBean.failed()) return resultBean;
        //获取渠道还款信息
        DealerRepayment dealerRepayment = iDealerRepaymentService.getOne(id);
        // 业务校验
        CustomerTransactionBean transaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(dealerRepayment.getCustomerTransactionId()).getD();
        if (transaction == null)
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_NONULL"));

        //需要判断单据状态是否取消或正在取消
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONCANCELLING"));
        }
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONCANCELLED"));
        }
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONSTOP"));
        }

        dealerRepayment.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
        dealerRepayment.setDataStatus(DataStatus.SAVE);
        dealerRepayment = iDealerRepaymentService.save(dealerRepayment);
        //启动工作流
        dealerRepayment = this.startDealerRepayment(dealerRepayment, comment).getD();
        if(dealerRepayment != null){
            dealerRepayment.setStatus(DealerRepaymentBean.DEALERREPAYMENTSTATUS_SUBMIT);
            iDealerRepaymentService.save(dealerRepayment);
        }
        //启动客户签约流程
        logger.info(dealerRepayment.getBillTypeCode() + ":" + dealerRepayment.getId() + messageService.getMessage("MSG_DEALERREPAYMENT_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(dealerRepayment, DealerRepayment.class)).setM(messageService.getMessage("MSG_DEALERREPAYMENT_SUBMIT"));
    }



    @Override
    public ResultBean<List<DealerRepaymentListBean>> actGetDealerRepayments(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize) {
        Page<DealerRepayment> dealerRepayments = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_DEALERREPAYMENT_LOGINUSERID_ID_NULL"), loginUserId));
        }

        //List<String> tids = iCustomerTransactionService.getTransactionIds(loginUserId, isPass);
        List<Integer> as = new ArrayList<Integer>();
        if(isPass){
            //dealerRepayments = this.iDealerRepaymentService.findCompletedItemsByUser(DealerRepayment.class, loginUserId, tids, currentPage, currentSize);
            as.add(ApproveStatus.APPROVE_PASSED);
            dealerRepayments = this.iDealerRepaymentService.findByLoginUserIdAndApproveStatusIn(loginUserId,as,currentPage,currentSize);
            if (dealerRepayments == null || dealerRepayments.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEALERREPAYMENT_LOGINUSERID_HISTORY_NULL"));
            }
        }else{
            //dealerRepayments = this.iDealerRepaymentService.findPendingItemsByUser(DealerRepayment.class, loginUserId, tids, currentPage, currentSize);
            as.add(ApproveStatus.APPROVE_INIT);
            as.add(ApproveStatus.APPROVE_ONGOING);
            as.add(ApproveStatus.APPROVE_REAPPLY);
            dealerRepayments = this.iDealerRepaymentService.findByLoginUserIdAndApproveStatusIn(loginUserId,as,currentPage,currentSize);
            if (dealerRepayments == null || dealerRepayments.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEALERREPAYMENT_LOGINUSERID_NULL"));
            }
        }


        DealerRepaymentListBean dealerRepaymentListBean = null;
        DataPageBean<DealerRepaymentListBean> destination = new DataPageBean<DealerRepaymentListBean>();
        destination.setPageSize(dealerRepayments.getSize());
        destination.setTotalCount(dealerRepayments.getTotalElements());
        destination.setTotalPages(dealerRepayments.getTotalPages());
        destination.setCurrentPage(dealerRepayments.getNumber());
        for (DealerRepayment dealerRepayment: dealerRepayments.getContent()) {
            if(dealerRepayment != null){
                PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(dealerRepayment.getCustomerTransactionId());
                CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
                AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(dealerRepayment.getCustomerTransactionId());
                dealerRepaymentListBean = mappingService.map(dealerRepayment, DealerRepaymentListBean.class);
                dealerRepaymentListBean.setCompensatoryAmount(customerLoanBean.getCompensatoryAmount());
                dealerRepaymentListBean.setAdvancedPayment(appointPayment.getAppointPayAmount());
                dealerRepaymentListBean.setPayTime(appointPayment.getPayTime());
                dealerRepaymentListBean.setRepaymentTime(dealerRepayment.getRepaymentTime());
                dealerRepaymentListBean.setAmount(appointPayment.getAppointPayAmount());
                //获取交易概览
                TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(dealerRepayment.getCustomerTransactionId()).getD();
                transactionSummary.setApproveStatus(dealerRepayment.getApproveStatus());
                dealerRepaymentListBean.setTransactionSummary(transactionSummary);
            }

            destination.getResult().add(dealerRepaymentListBean);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<DealerRepaymentSubmissionBean> actInitDealerRepaymentByTransactionId(String transactionId) {
        DealerRepayment dealerRepayment = iDealerRepaymentService.findByCustomerTransactionId(transactionId);
        DealerRepaymentSubmissionBean dealerRepaymentSubmissionBean = mappingService.map(dealerRepayment, DealerRepaymentSubmissionBean.class);

        //档案类型
        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(dealerRepayment.getCustomerId(),
                dealerRepayment.getCustomerTransactionId(),
                dealerRepayment.getBusinessTypeCode(),
                dealerRepayment.getBillTypeCode()).getD();
        dealerRepaymentSubmissionBean.setCustomerImages(imageTypeFiles);

        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(dealerRepayment.getCustomerTransactionId());
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(dealerRepayment.getCustomerTransactionId());
        dealerRepaymentSubmissionBean.setCompensatoryAmount(customerLoanBean.getCompensatoryAmount());
        dealerRepaymentSubmissionBean.setAdvancedPayment(appointPayment.getAppointPayAmount());
        dealerRepaymentSubmissionBean.setPayTime(appointPayment.getPayTime());
        return ResultBean.getSucceed().setD(dealerRepaymentSubmissionBean);
    }

    @Override
    public ResultBean<DealerRepaymentBean> actGetDealerRepayment(String id) {
        DealerRepayment dealerRepayment = iDealerRepaymentService.getOne(id);
        if(dealerRepayment != null){
            String code = dealerRepayment.getBillTypeCode();
            BillTypeBean billType   =   iBaseDataBizService.actGetBillType(code).getD();
            DealerRepaymentBean dealerRepaymentBean = mappingService.map(dealerRepayment,DealerRepaymentBean.class);
            dealerRepaymentBean.setBillType(billType);
            return ResultBean.getSucceed().setD(dealerRepaymentBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DealerRepaymentBean> actSaveDealerRepayment(DealerRepaymentBean dealerRepaymentBean) {
        DealerRepayment dealerRepayment = mappingService.map(dealerRepaymentBean,DealerRepayment.class);
        dealerRepayment.setApproveStatus(ApproveStatus.APPROVE_PASSED);
        dealerRepayment = iDealerRepaymentService.save(dealerRepayment);
        return ResultBean.getSucceed().setD(mappingService.map(dealerRepayment,DealerRepaymentBean.class));
    }

    @Override
    public ResultBean<DealerRepaymentBean> actSearchDealerRepayment(String userId, SearchBean searchBean) {
        Page<DealerRepayment> dealerRepayments = iDealerRepaymentService.findAllBySearchBean(DealerRepayment.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(dealerRepayments,DealerRepaymentBean.class));
    }

    @Override
    public ResultBean<DealerRepaymentBean> actUpdateStatus(String id, Integer status) {
        DealerRepayment dealerRepayment = iDealerRepaymentService.getOne(id);
        if(dealerRepayment != null){
            dealerRepayment.setStatus(status);
            dealerRepayment = iDealerRepaymentService.save(dealerRepayment);
        }
        return ResultBean.getSucceed().setD(mappingService.map(dealerRepayment,DealerRepaymentBean.class));
    }

    @Override
    public ResultBean<DealerRepaymentBean> actSignDealerRepayment(String id, SignInfo signInfo) {
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
        //获取预约垫资信息
        DealerRepayment dealerRepayment = iDealerRepaymentService.getOne(id);
        //查询当前单据任务是否完成
        return ResultBean.getSucceed().setD(mappingService.map(dealerRepayment, DealerRepaymentBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    /**
     * 启动渠道还款
     * @param dealerRepayment
     * @param comment
     * @return
     */
    private ResultBean<DealerRepayment> startDealerRepayment(DealerRepayment dealerRepayment, String comment) {
        SignInfo signInfo = new SignInfo(dealerRepayment.getLoginUserId(), dealerRepayment.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //进行审批
        String collectionMame = null;
        try {
            collectionMame = DealerRepayment.getMongoCollection(dealerRepayment);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }

        ResultBean resultBean = iWorkflowBizService.actSubmit(dealerRepayment.getBusinessTypeCode(), dealerRepayment.getId(), dealerRepayment.getBillTypeCode(), signInfo, collectionMame, null, dealerRepayment.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    dealerRepayment = iDealerRepaymentService.getOne(dealerRepayment.getId());
                    dealerRepayment.setTs(DateTimeUtils.getCreateTime());
                    dealerRepayment = iDealerRepaymentService.save(dealerRepayment);
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(dealerRepayment);
    }
}
