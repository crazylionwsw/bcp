package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.bd.service.*;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentExcelBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentListBean;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.customerfeebill.CustomerFeeBean;
import com.fuze.bcp.api.creditcar.service.IAppointPaymentBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignCondition;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预约垫资微服务
 * Created by zqw on 2017/8/16.
 */
@Service
public class BizAppointPaymentService implements IAppointPaymentBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizAppointPaymentService.class);

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    ICustomerFeeBillService iCustomerFeeBillService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    ICashSourceBizService iCashSourceBizService;

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    IPaymentBillService iPaymentBillService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    /**
     * 创建预约垫资单
     *
     * @param orderId
     * @return
     */
    @Override
    public ResultBean<AppointPaymentBean> actCreateAppointPayment(String orderId) {
        //TODO 汇款信息部分待设计
        PurchaseCarOrder purchaseCarOrder = iOrderService.getOne(orderId);
        if (purchaseCarOrder == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
        if(purchaseCarOrder.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
            logger.error(messageService.getMessage("MSG_APPOINTPAYMENT_NOCREATE"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_NOCREATE"));
        }
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(purchaseCarOrder.getCustomerTransactionId()).getD();
        CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(customerTransaction.getCarDealerId()).getD();
        //从垫资政策获取是否需要垫资
        if(customerLoanBean.getIsNeedPayment() != null && customerLoanBean.getIsNeedPayment() == 1){
            /*//不贴息的时候才会创建预约垫资单
            if (customerLoanBean.getCompensatoryAmount() == null || customerLoanBean.getCompensatoryInterest() == 0) {}*/
            //TODO 在创建单据时需要将一些不可更改的数据赋值
            AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
            if (appointPayment == null) {
                appointPayment = new AppointPayment();
                appointPayment.setBusinessTypeCode(purchaseCarOrder.getBusinessTypeCode());
                appointPayment.setLoginUserId(purchaseCarOrder.getLoginUserId());
                appointPayment.setEmployeeId(purchaseCarOrder.getEmployeeId());
                appointPayment.setCustomerId(purchaseCarOrder.getCustomerId());
                appointPayment.setCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
                //垫资时可以更换渠道
                appointPayment.setCarDealerId(customerTransaction.getCarDealerId());
                //创建时获取当前渠道的默认账户
                List<PayAccount> payAccounts = carDealerBean.getPayAccounts();
                if (payAccounts != null && !payAccounts.isEmpty()) {
                    for (PayAccount payAccount : payAccounts) {
                        if (payAccount.getDefaultAccount() == PayAccount.ACCOUTCHECK_ALL) {
                            appointPayment.setPayAccount(payAccount);
                        }
                    }
                }
                // 10-18 初始化时获取渠道的支付方式和垫资时间
                appointPayment.setChargeParty(carDealerBean.getPaymentMethod());
                // 10-31 初始化垫资金额(垫资策略下只考虑垫资政策，正常垫资直接调接口)
                appointPayment.setAppointPayAmount(this.actGetAppointPaymentAmount(appointPayment.getCustomerTransactionId(),appointPayment.getChargeParty().toString()).getD());
                /*if(customerLoanBean.getCompensatoryInterest() == 0 ){//商贷
                    appointPayment.setAppointPayAmount(this.actGetAppointPaymentAmount(appointPayment.getCustomerTransactionId(),appointPayment.getChargeParty().toString()).getD());
                }else if(customerLoanBean.getCompensatoryInterest() == 1){//贴息
                    appointPayment.setAppointPayAmount(customerLoanBean.getCreditAmount());
                }*/

                if (purchaseCarOrder.getBusinessTypeCode().equals("OC")) {
                    appointPayment.setAdvancedPay(carDealerBean.getPaymentOldTime());
                } else {
                    appointPayment.setAdvancedPay(carDealerBean.getPaymentNewTime());
                }

                // 10-12 增加 是否贴息状态 和 贴息金额
                appointPayment.setCompensatoryAmount(customerLoanBean.getCompensatoryAmount());
                appointPayment = iAppointPaymentService.tempSave(appointPayment);
                //TODO 垫资完成之后创建还款单(有发生垫资情况)，不贴息也没有垫资情况创建刷卡单
                return ResultBean.getSucceed().setD(mappingService.map(appointPayment, AppointPaymentBean.class));
            }
        }

        return null;
    }



    /**
     * 预约刷卡代垫贴息额，创建预约垫资单
     *
     * @param swipingCardId
     * @return
     */
    @Override
    public ResultBean<AppointPaymentBean> actCreateAppointPaymentByswipingCardId(String swipingCardId) {
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.getOne(swipingCardId);
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
        AppointSwipingCard swipingCard = iAppointSwipingCardService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
        //若是代垫贴息额，则创建预约垫资单；若不代垫贴息额，则删除预约垫资单
        if (appointSwipingCard != null) {
            if (appointSwipingCard.getIsNeedLoaning() == 1) {
                AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
                if (appointPayment == null) {
                    appointPayment = new AppointPayment();
                    //TODO 在创建单据时需要将一些不可更改的数据赋值
                    appointPayment.setBusinessTypeCode(appointSwipingCard.getBusinessTypeCode());
                    appointPayment.setLoginUserId(appointSwipingCard.getLoginUserId());
                    appointPayment.setEmployeeId(appointSwipingCard.getEmployeeId());
                    appointPayment.setCustomerId(appointSwipingCard.getCustomerId());
                    appointPayment.setCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
                    //垫资时可以更换渠道
                    appointPayment.setCarDealerId(purchaseCarOrder.getCarDealerId());
                    //创建时获取当前渠道的默认账户
                    List<PayAccount> payAccounts = carDealerBean.getPayAccounts();
                    if (payAccounts != null && !payAccounts.isEmpty()) {
                        for (PayAccount payAccount : payAccounts) {
                            if (payAccount.getDefaultAccount() == PayAccount.ACCOUTCHECK_ALL) {
                                appointPayment.setPayAccount(payAccount);
                            }
                        }
                    }
                    appointPayment.setAppointPayTime(appointSwipingCard.getAppointPayTime());
                    appointPayment.setPickupDate(appointSwipingCard.getPickupDate());
                    // 10-18 初始化是获取渠道的支付方式和垫资时间
                    appointPayment.setChargeParty(carDealerBean.getPaymentMethod());
                    // 10-31 初始化垫资金额(特殊垫资情况下垫资金额默认为贷款金额)
                    appointPayment.setAppointPayAmount(customerLoanBean.getCreditAmount());
                    /*if(customerLoanBean.getCompensatoryInterest() == 0 ){//商贷
                        appointPayment.setAppointPayAmount(this.actGetAppointPaymentAmount(appointPayment.getCustomerTransactionId(),appointPayment.getChargeParty().toString()).getD());
                    }else if(customerLoanBean.getCompensatoryInterest() == 1){//贴息
                        appointPayment.setAppointPayAmount(customerLoanBean.getCreditAmount());
                    }*/
                    if (purchaseCarOrder.getBusinessTypeCode().equals("OC")) {
                        appointPayment.setAdvancedPay(carDealerBean.getPaymentOldTime());
                    } else {
                        appointPayment.setAdvancedPay(carDealerBean.getPaymentNewTime());
                    }
                    // 10-12 增加 是否贴息状态 和 贴息金额 字段方便pad 端计算
                    appointPayment.setIsNeedLoaning(appointSwipingCard.getIsNeedLoaning());
                    appointPayment.setNeedCompensatory(customerLoanBean.getCompensatoryInterest());
                    appointPayment.setCompensatoryAmount(customerLoanBean.getCompensatoryAmount());
                    appointPayment = iAppointPaymentService.tempSave(appointPayment);
                    return ResultBean.getSucceed().setD(mappingService.map(appointPayment, AppointPaymentBean.class));
                }
            } else if (appointSwipingCard.getIsNeedLoaning() == 0) {//不代垫删除
                AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
                if (appointPayment != null) {
                    iAppointPaymentService.delete(appointPayment.getId());
                }
            }
        }

        return null;
    }

    public ResultBean<Double> actGetAppointPaymentAmount(String tid, String chargeParty) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(tid);
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();

        /***************垫资金额计算方法***************/
        // NC:差额： 趸交(垫资金额 = 贷款金额 -贷款服务费-杂费)（CreditAmount - LoanServiceFee - Sum(purchaseCarOrder.getFeeItemList()))
        // OC:差额： 趸交(垫资金额 = 贷款金额 -贷款服务费-杂费 - 银行手续费)（CreditAmount - LoanServiceFee - Sum(purchaseCarOrder.getFeeItemList()) - bankFeeAmount)
        // 差额： 分期(贷款金额 -贷款服务费 -杂费 )（CreditAmount - LoanServiceFee - Sum(purchaseCarOrder.getFeeItemList()))
        // 全额：（CreditAmount)
        if (chargeParty.equals("0")) { //差额
            Double totalFeeAmount = 0.0;
            Double amount = 0.0;
            for (FeeValueBean feeValue : purchaseCarOrder.getFeeItemList()) {
                totalFeeAmount += feeValue.getFee();
            }
            if(purchaseCarOrder.getBusinessTypeCode().equals("NC")){
                if (customerLoanBean.getChargePaymentWay().equals("WHOLE")) { //趸交
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount ;
                } else { //分期
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount ;
                }
            }else if(purchaseCarOrder.getBusinessTypeCode().equals("OC")){
                if (customerLoanBean.getChargePaymentWay().equals("WHOLE")) { //趸交
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount - customerLoanBean.getBankFeeAmount();
                } else { //分期
                    amount = customerLoanBean.getCreditAmount() - customerLoanBean.getLoanServiceFee() - totalFeeAmount ;
                }
            }
            return ResultBean.getSucceed().setD(amount);
        } else {
            return ResultBean.getSucceed().setD(customerLoanBean.getCreditAmount());
        }
    }

    /**
     * 保存预约垫资单 （暂存，不进审批流）
     *
     * @param appointPaymentSubmissionBean
     * @return
     */
    @Override
    public ResultBean<AppointPaymentBean> actSaveAppointPayment(AppointPaymentSubmissionBean appointPaymentSubmissionBean) {

        //todo 小乐说，申请垫资二手车时，页面不显示垫资金额，但是其他地方需要，所以将贷款金额set到垫资金额中去
        ResultBean result = iAppointPaymentService.getEditableBill(appointPaymentSubmissionBean.getId());
        if (result.failed()) return result;
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(appointPaymentSubmissionBean.getCustomerTransactionId());
        if (result.failed()) return result;

        AppointPayment appointPayment = iAppointPaymentService.getOne(appointPaymentSubmissionBean.getId());
        appointPayment.setPickupDate(appointPaymentSubmissionBean.getPickupDate());
        appointPayment.setAppointPayTime(appointPaymentSubmissionBean.getAppointPayTime());
        appointPayment.setAdvancedPay(appointPaymentSubmissionBean.getAdvancedPay());
        appointPayment.setChargeParty(appointPaymentSubmissionBean.getChargeParty());
        appointPayment.setPaymentType(appointPaymentSubmissionBean.getPaymentType());
        appointPayment.setAppointPayAmount(appointPaymentSubmissionBean.getAppointPayAmount());
        if (appointPaymentSubmissionBean.getPayAccount() != null) {
            appointPayment.setPayAccount(mappingService.map(appointPaymentSubmissionBean.getPayAccount(), PayAccount.class));
        }

        //处理档案资料
        iCustomerImageFileBizService.actSaveCustomerImages(appointPayment.getCustomerId(),
                appointPayment.getCustomerTransactionId(),
                appointPaymentSubmissionBean.getCustomerImages()); //整体保存档案资料
        appointPayment = iAppointPaymentService.save(appointPayment);

        //更新借款信息
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanByTransactionId(appointPayment.getCustomerTransactionId()).getD();
        customerLoanBean.setAdvancedPayment(appointPayment.getAppointPayAmount());
        iCustomerBizService.actSaveCustomerLoan(customerLoanBean);
        //保存汇款单信息
        /*CustomerFeeBean customerFee = appointPaymentSubmissionBean.getCustomerFeeBean();
        if (customerFee != null) {

            CustomerFeeBill customerFeeBill = mappingService.map(appointPaymentSubmissionBean.getCustomerFeeBean(), CustomerFeeBill.class);
            CustomerFeeBill customerFeeBill1 = iCustomerFeeBillService.findByCustomerTransactionId(appointPayment.getCustomerTransactionId());
            if (customerFeeBill1 != null) {
                customerFeeBill.setId(customerFeeBill1.getId());
            }

            customerFeeBill.setCustomerTransactionId(appointPaymentSubmissionBean.getCustomerTransactionId());

            PayAccount payAccount = new PayAccount();
            payAccount.setBankName(customerFee.getCollectionAgency());
            payAccount.setAccountNumber(customerFee.getReceiptAccount());
            payAccount.setAccountName(customerFee.getReceiver());
            customerFeeBill.setReceiveAccount(payAccount);

            PayAccount payAccount2 = new PayAccount();
            payAccount2.setAccountName(customerFee.getDrawee());
            payAccount2.setAccountNumber(customerFee.getPaymentAccount());
            customerFeeBill.setPayAccount(payAccount2);
            iCustomerFeeBillService.save(customerFeeBill);
        }*/

        if (appointPayment != null) {
            appointPaymentSubmissionBean.setId(appointPayment.getId());
            return ResultBean.getSucceed().setD(appointPaymentSubmissionBean).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
    }

    /**
     * 保存PAD端提交的预约垫资单 （数据转换）
     *
     * @param appointPaymentSubmissionBean
     * @return
     */
    @Override
    public ResultBean<AppointPaymentBean> actSavePadAppointPayment(AppointPaymentSubmissionBean appointPaymentSubmissionBean) {
       /* if (appointPaymentSubmissionBean != null && appointPaymentSubmissionBean.getPayAccount() != null && appointPaymentSubmissionBean.getPayAccount().getAccountNature() != null) {
            switch (appointPaymentSubmissionBean.getPayAccount().getAccountNature()) {
                case "0:1":
                    appointPaymentSubmissionBean.getPayAccount().setAccountWay(2);
                    break;
                case "1:0":
                    appointPaymentSubmissionBean.getPayAccount().setAccountWay(1);
                    break;
                default:
                    appointPaymentSubmissionBean.getPayAccount().setAccountWay(0);
                    break;
            }
        }*/
        //垫资项(名称) 贴息额：0  贷款额：1 贴息额+贷款额：2
        switch (appointPaymentSubmissionBean.getPaymentType()) {
            case "0:1":
                appointPaymentSubmissionBean.setPaymentType("0");
                break;
            case "1:0":
                appointPaymentSubmissionBean.setPaymentType("1");
                break;
            case "1:1":
                appointPaymentSubmissionBean.setPaymentType("2");
                break;
            default:
                break;
        }
        return this.actSaveAppointPayment(appointPaymentSubmissionBean);
    }

    /**
     * 提交预约垫资单 （保存，并进审批流）
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<AppointPaymentBean> actSubmitAppointPayment(String id, String comment) {
        ResultBean result = iAppointPaymentService.getEditableBill(id);
        if (result.failed()) return result;

        AppointPayment appointPayment = (AppointPayment) result.getD();
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(appointPayment.getCustomerTransactionId());
        if (result.failed()) return result;
        CustomerTransactionBean transaction = (CustomerTransactionBean) result.getD();
        if(appointPayment.getCarDealerId() != null){
            //TODO 当前垫资渠道和签约渠道不一致不可以提交
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(appointPayment.getCustomerTransactionId());
            if(!appointPayment.getCarDealerId() .equals(purchaseCarOrder.getCarDealerId()) ){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_CADELARUNLIKENESS"));
            }
            //TODO 当前垫资的渠道必须是在正常合作状态，否则提示"分期经理当前渠道不在合作状态"
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(appointPayment.getCarDealerId()).getD();
            if(carDealerBean != null && carDealerBean.getStatus() != CarDealerBean.STATUS_ONGOING){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_STATUSNOONGING"));
            }
        }

        // 获取卡业务处理流程，判断制卡是否完成
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(appointPayment.getCustomerTransactionId());
        if (bankCardApply != null) {
            if(bankCardApply.getStatus() == BankCardApplyBean.BKSTATUS_CANCEL){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_BANKCARDCANCEL"));
            }else if (bankCardApply.getApplyTime() == null) {//若制卡未完成，则不允许预约垫资
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_BANKCARAPPLYNOAPPLY"));
            }
        }

        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(appointPayment.getCustomerTransactionId());
        //若是特殊垫资，必须在预约刷卡通过之后才可以提交预约垫资
        if (appointSwipingCard != null) {
            if (appointSwipingCard.getIsNeedLoaning() == 1 && appointSwipingCard.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_APPOINTSWIPINGCARDNOPASSED"));
            }
        }

        appointPayment.setCarDealerId(transaction.getCarDealerId()); //TODO: CarDealerId应该从PAD端传入


        //启动工作流
        appointPayment = this.startAppointPayment(appointPayment, comment).getD();
        if (appointPayment != null) {
            //启动工作流返回的数据没有审核状态，直接赋值保存会覆盖审核状态，所以需要先查询再保存
            appointPayment.setStatus(AppointPaymentBean.APPOINTPAYMENTSTATUS_SUBMIT);
            iAppointPaymentService.save(appointPayment);
        }
        logger.info(appointPayment.getBillTypeCode() + ":" + appointPayment.getId() + messageService.getMessage("MSG_APPOINTPAYMENT_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(appointPayment, AppointPaymentBean.class)).setM(messageService.getMessage("MSG_APPOINTPAYMENT_SUBMIT"));
    }


    /**
     * 获取分期经理待办的预约垫资单
     *
     * @param isPass
     * @param loginUserId
     * @param currentPage
     * @return
     */
    @Override
    public ResultBean<List<AppointPaymentListBean>> actGetAppointPayments(Boolean isPass, String loginUserId, Integer currentPage, Integer currentSize) {
        Page<AppointPayment> appointPayments = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_APPOINTPAYMENT_LOGINUSERID_ID_NULL"), loginUserId));
        }

        List<String> tids = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if (isPass) {
            appointPayments = this.iAppointPaymentService.findCompletedItemsByUser(AppointPayment.class, loginUserId, tids, currentPage, currentSize);
            if (appointPayments == null || appointPayments.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_LOGINUSERID_HISTORY_NULL"));
            }
        } else {
            appointPayments = this.iAppointPaymentService.findPendingItemsByUser(AppointPayment.class, loginUserId, tids, currentPage, currentSize);
            if (appointPayments == null || appointPayments.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_APPOINTPAYMENT_LOGINUSERID_NULL"));
            }
        }


        AppointPaymentListBean appointPaymentListBean = null;
        DataPageBean<AppointPaymentListBean> destination = new DataPageBean<AppointPaymentListBean>();
        destination.setPageSize(appointPayments.getSize());
        destination.setTotalCount(appointPayments.getTotalElements());
        destination.setTotalPages(appointPayments.getTotalPages());
        destination.setCurrentPage(appointPayments.getNumber());
        for (AppointPayment appointPayment : appointPayments.getContent()) {
            if (appointPayment != null) {
                appointPaymentListBean = mappingService.map(appointPayment, AppointPaymentListBean.class);
                appointPaymentListBean.setAccountName(appointPayment.getPayAccount() == null ? null : appointPayment.getPayAccount().getAccountName());
                //获取交易概览
                TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(appointPayment.getCustomerTransactionId()).getD();
                transactionSummary.setApproveStatus(appointPayment.getApproveStatus());
                appointPaymentListBean.setTransactionSummary(transactionSummary);
            }

            destination.getResult().add(appointPaymentListBean);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    /**
     * 通过交易ID查看详情信息
     *
     * @param transactionId
     * @return
     */
    @Override
    public ResultBean<AppointPaymentSubmissionBean> actInitAppointPaymentsByTransactionId(String transactionId) {
        AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(transactionId);
        AppointPaymentSubmissionBean appointPaymentSubmissionBean = mappingService.map(appointPayment, AppointPaymentSubmissionBean.class);

        //档案类型
        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(appointPayment.getCustomerId(),
                appointPayment.getCustomerTransactionId(),
                appointPayment.getBusinessTypeCode(),
                appointPayment.getBillTypeCode()).getD();
        appointPaymentSubmissionBean.setCustomerImages(imageTypeFiles);
        // 设置汇款信息
        //  根据transactionId查询汇款单
        /*CustomerFeeBill customerFeeBill = iCustomerFeeBillService.findByCustomerTransactionId(transactionId);
        CustomerFeeBean customerFee = new CustomerFeeBean();
        if (customerFeeBill != null) {
            customerFee = mappingService.map(customerFeeBill, CustomerFeeBean.class);
            customerFee.setCollectionAgency(customerFeeBill.getReceiveAccount().getBankName());
            customerFee.setReceiptAccount(customerFeeBill.getReceiveAccount().getAccountNumber());
            customerFee.setReceiver(customerFeeBill.getReceiveAccount().getAccountName());
            customerFee.setPaymentAccount(customerFeeBill.getPayAccount().getAccountNumber());
            customerFee.setDrawee(customerFeeBill.getPayAccount().getAccountName());
            appointPaymentSubmissionBean.setCustomerFeeBean(customerFee);
        } else {
            appointPaymentSubmissionBean.setCustomerFeeBean(this.createCustomerFee(transactionId));
        }*/

        return ResultBean.getSucceed().setD(appointPaymentSubmissionBean);
    }

    /**
     * 通过交易ID查询汇款信息
     *
     * @param transactionId
     * @return
     */
    private CustomerFeeBean createCustomerFee(String transactionId) {
        HashMap<String, String> bankAccount = (HashMap<String, String>) iParamBizService.actGetMap("RECEIVE_BANK_ACCOUNT").getD();
        CustomerFeeBean customerFee = new CustomerFeeBean();

        customerFee.setCustomerTransactionId(transactionId);

        //收款帐户
        customerFee.setReceiver(bankAccount.get("ACCOUNT_NAME"));
        customerFee.setReceiptAccount(bankAccount.get("ACCOUNT_NUMBER"));
        customerFee.setCollectionAgency(bankAccount.get("BANK_NAME"));

        //贷款服务费
        CustomerLoanBean customerLoan = iCustomerBizService.actGetCustomerLoanByTransactionId(transactionId).getD();
        customerFee.setLoanServiceFee(customerLoan.getLoanServiceFee());

        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);

        List<FeeValueBean> feeItemList = purchaseCarOrder.getFeeItemList();
        Double fee = 0.00;
        if (feeItemList != null) {
            for (FeeValueBean feeValue : feeItemList) {
                fee += feeValue.getFee();
            }
        }
        customerFee.setTotalFee(customerLoan.getLoanServiceFee() + fee);
        customerFee.setFeeItemList(feeItemList);

        return customerFee;
    }

    /**
     * 预约垫资签批
     * @param id
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<AppointPaymentBean> actSignAppointPayment(String id, SignInfo signInfo) {
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
        AppointPayment appointPayment = iAppointPaymentService.getOne(id);
        //预约垫资完成后更新垫资支付时间
        if(appointPayment.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
            this.actUpdateAppointPaymentTime(appointPayment.getId(), DateTimeUtils.getCreateTime());
        }
        return ResultBean.getSucceed().setD(mappingService.map(appointPayment, AppointPaymentBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    /**
     * 获取预约垫资单
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<AppointPaymentBean> actGetAppointPayment(String id) {
        AppointPayment appointPayment = iAppointPaymentService.getOne(id);
        if (appointPayment == null){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_APPOINTPAYMENT_ID_NOT_FIND"),id));
        }
        String code = appointPayment.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
        AppointPaymentBean appointPaymentBean = mappingService.map(appointPayment, AppointPaymentBean.class);
        appointPaymentBean.setBillType(billType);
        return ResultBean.getSucceed().setD(appointPaymentBean);
    }

    @Override
    public ResultBean<AppointPaymentBean> actSearchAppointPayments(String userId,SearchBean searchBean) {
        Page<AppointPayment> appointPayments = iAppointPaymentService.findAllBySearchBean(AppointPayment.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(appointPayments,AppointPaymentBean.class));
    }

    @Override
    public ResultBean<AppointPaymentBean> actUpdateStatus(String id, Integer status) {
        AppointPayment appointPayment = iAppointPaymentService.getOne(id);
        if (appointPayment != null) {
            appointPayment.setStatus(status);
            appointPayment = iAppointPaymentService.save(appointPayment);
        }
        return ResultBean.getSucceed().setD(mappingService.map(appointPayment, AppointPaymentBean.class));
    }

    private Map getConditions(Integer branchId, String id) {
        Map<String, List<SignCondition>> params = new HashMap();
        //提交垫资
        List signConditions1 = new ArrayList<>();
        SignCondition branch = new SignCondition("branchId", String.valueOf(branchId), true);
        signConditions1.add(branch);
        params.put("AppointPayment_Submit", signConditions1);
        return params;
    }

    /**
     * 启动预约垫资流程
     *
     * @param appointPayment
     * @param comment
     * @return
     */
    private ResultBean<AppointPayment> startAppointPayment(AppointPayment appointPayment, String comment) {
        //进行审批
        String collectionMame = null;
        AppointSwipingCard appointSwipingCard = null;
        Integer branchId = 0;
        try {
            collectionMame = AppointSwipingCard.getMongoCollection(appointPayment);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }

        //是否需要部门经理审批
        CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(appointPayment.getCarDealerId()).getD(); //获取经销商
        if (carDealerBean != null) {
            if (appointPayment.getBusinessTypeCode().equals("NC")) { //新车业务
                if (!carDealerBean.getPaymentNewTime().equals(appointPayment.getAdvancedPay()) ) {
                    branchId = 1;
                    //comment += "<br>垫资时间点与渠道不同需要部门经理审批";
                }
            }
            if (appointPayment.getBusinessTypeCode().equals("OC")) { //二手车业务
                if (!carDealerBean.getPaymentOldTime().equals(appointPayment.getAdvancedPay()) ) {
                    branchId = 1;
                    //comment += "<br>垫资时间点与渠道不同需要部门经理审批";
                }
            }

            //差额/全额与渠道配置不同
            /*if (!appointPayment.getChargeParty().equals(carDealerBean.getPaymentMethod())) {
                branchId = 1;
                //comment += "<br>垫资方式与渠道不同,需要部门经理审批";
            }*/
        }
        if (!(appointPayment.getPayAccount() == null ? null : appointPayment.getPayAccount().getDefaultAccount()) .equals(PayAccount.ACCOUTCHECK_ALL) ) {
            branchId = 1;
            //comment += "<br>不是默认账户,需要部门经理审批";
        }
        //垫资政策中不需要垫资，特殊情况又需要垫资时需要部门经理审批
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(appointPayment.getCustomerTransactionId());
        if(purchaseCarOrder != null){
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            if(customerLoanBean != null){
                if(customerLoanBean.getIsNeedPayment() == 0 && appointPayment.getIsNeedLoaning() == 1){//垫资政策中不需要垫资又需要特殊垫资
                    branchId = 1;
                    //comment += "<br>特殊垫资需要部门经理审批";
                }
            }
        }
        //新车业务选择平行进口车需要部门审批
        if(appointPayment.getBusinessTypeCode().equals("NC")){
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
            if(customerCarBean != null && customerCarBean.getParallelImport().equals("1")){
                branchId = 1;
            }
        }

        Map<String, List<SignCondition>> params = getConditions(branchId, appointPayment.getCustomerTransactionId());
        SignInfo signInfo = new SignInfo(appointPayment.getLoginUserId(), appointPayment.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        ResultBean resultBean = iWorkflowBizService.actSubmit(appointPayment.getBusinessTypeCode(), appointPayment.getId(), appointPayment.getBillTypeCode(), signInfo, collectionMame, params, appointPayment.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    appointPayment = iAppointPaymentService.getOne(appointPayment.getId());
                    appointPayment.setTs(DateTimeUtils.getCreateTime());
                    appointPayment = iAppointPaymentService.save(appointPayment);
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(appointPayment);
    }

    private ResultBean<AppointPaymentBean> actUpdateAppointPaymentTime(String id,String paymentTime) {
        AppointPayment appointPayment = iAppointPaymentService.getOne(id);
        if (appointPayment != null) {
            appointPayment.setPayTime(paymentTime);
            appointPayment = iAppointPaymentService.save(appointPayment);
        }
        return ResultBean.getSucceed().setD(mappingService.map(appointPayment, AppointPaymentBean.class));
    }

    @Override
    public ResultBean<AppointPaymentBean> actGetAppointPaymentByCustomerTransactionId(String customerTransactionId) {
        AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(customerTransactionId);
        if (appointPayment != null) {
            return ResultBean.getSucceed().setD(mappingService.map(appointPayment, AppointPaymentBean.class));
        }
        return ResultBean.getFailed();
    }

    /**
     * 日报
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId ,String date, AppointPaymentBean t) {
        Map<Object, Object> dailyReport = iAppointPaymentService.getDailyReport(orgId, date, mappingService.map(t, AppointPayment.class));
        if(dailyReport != null){
            return ResultBean.getSucceed().setD(dailyReport);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Map<Object, Object>> getEmployeeReport(String date, AppointPaymentBean t, String employeeId) {
        AppointPayment map = mappingService.map(t, AppointPayment.class);
        Map<Object, Object> employeeReport = iAppointPaymentService.getEmployeeReport(employeeId, date, map);
        if(employeeReport != null){
            return ResultBean.getSucceed().setD(employeeReport);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Map<Object, Object>> getChannelReport(String date, AppointPaymentBean t, String loginUserId) {
        EmployeeBean d = iOrgBizService.actFindEmployeeByLoginUserId(loginUserId).getD();
        String orginfoId = d.getOrgInfoId();
        List<EmployeeBean> employeeIds = iOrgBizService.actGetOrgEmployees(orginfoId).getD();
        AppointPayment map = mappingService.map(t, AppointPayment.class);
        Map<Object, Object> hashMap = new HashMap<Object, Object>();
        Integer total= 0;
        Integer ongoing= 0;
        Integer pass= 0;
        Integer reapply= 0;
        for (EmployeeBean employee:employeeIds) {
            Map<Object, Object> objectMap = getEmployeeReport(date, t,employee.getLoginUserId()).getD();
            total= total+Integer.parseInt(objectMap.get("total").toString());
            ongoing= ongoing+Integer.parseInt(objectMap.get("ongoing").toString());
            pass = pass +Integer.parseInt(objectMap.get("passed").toString());
            reapply = reapply +Integer.parseInt(objectMap.get("reapply").toString());
            hashMap.put("total", total);
            hashMap.put("ongoing", ongoing);
            hashMap.put("passed",pass);
            hashMap.put("reapply",reapply);
        }
        if(hashMap != null){
            return ResultBean.getSucceed().setD(hashMap);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<Map>> getAllCustomerByApproveStatus(Integer as) {

        List<AppointPayment> items = null;
        if (as == ApproveStatus.APPROVE_INIT) {
            items = iAppointPaymentService.findAll(AppointPayment.getTsSort());
        } else {
            items = iAppointPaymentService.findAllByDataStatusAndApproveStatus(DataStatus.SAVE,as,AppointPayment.getTsSort());
        }
        return convertCustomerMapList(items);
    }

    private ResultBean<List<Map>> convertCustomerMapList(List<AppointPayment> items) {
        List<Map>   resultList = new ArrayList<Map>();
        for(AppointPayment t:items){
            CustomerTransactionBean customerTransactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(t.getCustomerTransactionId()).getD();
            if( customerTransactionBean.getStatus()== CustomerTransactionBean.TRANSACTION_CANCELLING || customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED){
                continue;
            }
            Map  dataMap = new HashMap();
            CustomerBean customer = iCustomerBizService.actGetCustomerById(customerTransactionBean.getCustomerId()).getD();
            dataMap.put("customerName",customer!=null?customer.getName():t.getCustomerId());
            dataMap.put("orderTime",t.getTs());
            dataMap.put("days", SimpleUtils.daysBetween(t.getTs(),SimpleUtils.getCreateTime()));
            dataMap.put("customerId",t.getCustomerId());
            dataMap.put("id",t.getId());
            dataMap.put("businessTypeName",t.getBusinessTypeCode());
            dataMap.put("employeeName",iOrgBizService.actGetEmployee(customerTransactionBean.getEmployeeId()).getD().getUsername());
            resultList.add(dataMap);
        }
        return ResultBean.getSucceed().setD(resultList);
    }

    /**
     *     预约垫资通过之后，发送邮件，将垫资支付电子回单作为附件发送过去
     * @param id
     */
    @Override
    public void actSendAppointPaymentVoucher(String id){
        AppointPayment appointPayment = iAppointPaymentService.getAvailableOne(id);
        if (appointPayment.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
            logger.error(String.format(messageService.getMessage("MSG_APPOINTPAYMENT_NOT_PASS"), id));
            return;
        }
        String customerImageTypeCode = iParamBizService.actGetString("APPOINTPAYMENT_CUSTOMERIMAGETYPE_CODE").getD();
        if (!StringUtils.isEmpty(customerImageTypeCode)) {
            List<String> imageTypeCodes = new ArrayList<String>();
            imageTypeCodes.add(customerImageTypeCode);
            iAppointPaymentService.sendImagesAndContractsToEmployee(appointPayment, imageTypeCodes, new ArrayList<String>());
        } else {
            logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"), "APPOINTPAYMENT_CUSTOMERIMAGETYPE_CODE"));
            return;
        }
    }

    @Override
    public ResultBean<List<AppointPaymentBean>> actGetCustomerTransactionsByCarDealerId(String carDealerId) {
        List<AppointPayment> all = iAppointPaymentService.findAllByCardearlerId(carDealerId, AppointPayment.getSortASC("ts"));
        return ResultBean.getSucceed().setD(mappingService.map(all,AppointPaymentBean.class));
    }

    @Override
    public ResultBean<AppointPaymentBean> actSaveAppointPaymentInfo(AppointPaymentBean appointPaymentBean) {
        AppointPayment appointPayment = mappingService.map(appointPaymentBean, AppointPayment.class);
        AppointPayment appointpayment = iAppointPaymentService.save(appointPayment);
        return ResultBean.getSucceed().setD(mappingService.map(appointpayment,AppointPaymentBean.class));
    }

    @Override
    public ResultBean<List<AppointPaymentExcelBean>> actExportAppointPayBusinessBook(String selectTime) {
        List<AppointPaymentExcelBean> appointPaymentExcelBeen = new ArrayList<AppointPaymentExcelBean>();
        List<AppointPayment> paymentInfo = iAppointPaymentService.getAppointPaymentBySelectTime(selectTime);
        for (int i = 0; i < paymentInfo.size(); i++) {
            AppointPaymentExcelBean appointPaymentInfo = this.getAppointPaymentInfo(paymentInfo.get(i), i);
            appointPaymentExcelBeen.add(appointPaymentInfo);
        }
        return ResultBean.getSucceed().setD(mappingService.map(appointPaymentExcelBeen,AppointPaymentExcelBean.class));
    }

    private AppointPaymentExcelBean getAppointPaymentInfo(AppointPayment appointPayment,int i){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("#0.0");
        DecimalFormat deci = new DecimalFormat("#");//取整，不包含小数位
        AppointPaymentExcelBean appointPaymentExcel = new AppointPaymentExcelBean();
        appointPaymentExcel.setIndexNumber(i+1);
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(appointPayment.getCustomerTransactionId()).getD();
        CashSourceBean cashSourceBean = this.getCashSource(customerTransaction.getCashSourceId());
        if(cashSourceBean != null){
            appointPaymentExcel.setCashSource(cashSourceBean.getShortName());
        }
        CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(appointPayment.getCarDealerId()).getD();
        if(carDealerBean != null){
            CashSourceBean cashSourceBean1 = this.getCashSource(carDealerBean.getCooperationCashSourceId());
            if(cashSourceBean1 != null){
                appointPaymentExcel.setCooperationCashSource(cashSourceBean1.getShortName());
            }
        }

        if(customerTransaction.getBusinessTypeCode().equals("NC")){
            appointPaymentExcel.setBusinessType("新车");
        }else if(customerTransaction.getBusinessTypeCode().equals("OC")){
            appointPaymentExcel.setBusinessType("二手车");
        }

        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(appointPayment.getCustomerId()).getD();
        if(customerBean != null){
            appointPaymentExcel.setCustomerInfo(customerBean.getName()+"【"+customerBean.getIdentifyNo()+"】");
        }
        EmployeeBean employee = iOrgBizService.actGetEmployee(appointPayment.getEmployeeId()).getD();
        if(employee != null){
            appointPaymentExcel.setBusinessManInfo(employee.getUsername()+"【"+employee.getCell()+"】");
        }
        CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(customerTransaction.getId()).getD();
        if(customerCardBean != null && customerCardBean.getCardNo() != null && !"".equals(customerCardBean.getCardNo())){
            appointPaymentExcel.setCardNo(customerCardBean.getCardNo());
        }
        appointPaymentExcel.setAppointPayAmount(decimalFormat.format(appointPayment.getAppointPayAmount()));
        appointPaymentExcel.setPayTime(TimeUtil.getTimeTransMinutes(appointPayment.getPayTime()));
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanByTransactionId(appointPayment.getCustomerTransactionId()).getD();
        appointPaymentExcel.setCreditAmount(decimalFormat.format(customerLoanBean.getCreditAmount()));
        appointPaymentExcel.setMonth(customerLoanBean.getRateType().getMonths());
        DecimalFormat df = new DecimalFormat("0%");
        appointPaymentExcel.setRateType(df.format(customerLoanBean.getRateType().getRatio()));//点位
        appointPaymentExcel.setSwipingRate(df.format(customerLoanBean.getRateType().getRatio()));//刷卡点位
        if(customerLoanBean.getChargePaymentWay().equals("WHOLE")){
            appointPaymentExcel.setChargePaymentWay("趸交");
        }else if(customerLoanBean.getChargePaymentWay().equals("STAGES")){
            appointPaymentExcel.setChargePaymentWay("分期");
            if(appointPayment.getChargeParty() == 1){
                double digitTwo = FormatUtils.formatDigitTwo(customerLoanBean.getLoanServiceFee());
                if(digitTwo > 0){
                    appointPaymentExcel.setReallyLoanServiceFee(digitTwo);
                }

            }
            String format = deci.format(customerLoanBean.getLoanServiceFee());
            if(format != null && Double.valueOf(format) > 0){
                appointPaymentExcel.setStagesLoanServiceFee(format);
            }

        }

//        appointPaymentExcel.setBankFeeAmount(customerLoanBean.getBankFeeAmount().toString());
        appointPaymentExcel.setBankFeeAmount(deci.format(customerLoanBean.getBankFeeAmount()));
        if(customerLoanBean.getLoanServiceFee()>0){
            appointPaymentExcel.setLoanServiceFee(customerLoanBean.getLoanServiceFee().toString());
        }



        //实收手续从缴费单中的“手续费（商贷）”里取值
        List<PaymentBill> paymentBills = iPaymentBillService.findByCustomerTransactionIdAndDataStatus(customerTransaction.getId(), DataStatus.SAVE);
        if(paymentBills != null && paymentBills.size() > 0){
            Double chareeAmount = 0.00;
            for (PaymentBill paymentBill:paymentBills) {
                if(paymentBill.getChargeFee() != null && paymentBill.getChargeFee() > 0){
                    chareeAmount += paymentBill.getChargeFee();
                }
            }
            if(chareeAmount != null && chareeAmount > 0.00){
                appointPaymentExcel.setRealityBankFeeAmount(AmountUtil.getAmountTwo(chareeAmount));
            }
        }
        BankCardApply bankCardApply = iBankCardApplyService.findAvailableOneByCustomerTransactionId(appointPayment.getCustomerTransactionId());
        if(bankCardApply != null){
            appointPaymentExcel.setSwipingMoney(decimalFormat.format(bankCardApply.getSwipingMoney()));
            if(bankCardApply.getSwipingShopTime() != null){
                appointPaymentExcel.setSwipingTime(TimeUtil.getTimeTransDay(bankCardApply.getSwipingShopTime()));
            }else if(bankCardApply.getSwipingTrusteeTime() != null){
                appointPaymentExcel.setSwipingTime(TimeUtil.getTimeTransDay(bankCardApply.getSwipingTrusteeTime()));
            }
            appointPaymentExcel.setSwipingMonth(bankCardApply.getSwipingPeriods());
        }

        /**
         * 趸交、分期还款金额
         */
        if(customerLoanBean.getChargePaymentWay().equals("WHOLE")){
           appointPaymentExcel.setRepayBankFeeMoney(deci.format(customerLoanBean.getBankFeeAmount()));

           Double yuAmount = Math.floor(customerLoanBean.getCreditAmount()/customerLoanBean.getRateType().getMonths());
           Double chaAmount = yuAmount * (customerLoanBean.getRateType().getMonths() - 1);
            Double firstRepayAmount = customerLoanBean.getCreditAmount() - chaAmount;
           Double monthlyRepayAmount = chaAmount / (customerLoanBean.getRateType().getMonths() - 1);

           appointPaymentExcel.setRepaymentAmountFirstMonth(decimalFormat.format(firstRepayAmount));
            //客户每期还款额 =  刷卡金额/期数
            double monthCustomerPayment = bankCardApply.getSwipingMoney() / customerLoanBean.getRateType().getMonths();
            appointPaymentExcel.setRepaymentAmountMonthly(decimalFormat.format(monthCustomerPayment));
            appointPaymentExcel.setRepaymentAmountMonthlyRound(decimalFormat1.format(monthlyRepayAmount));
        }

        /**
         * 分期银行手续费还款
         */
        if(customerLoanBean.getChargePaymentWay().equals("STAGES")){
            appointPaymentExcel.setRepayBankFeeMoney(deci.format(customerLoanBean.getBankFeeAmount()));
            appointPaymentExcel.setRepayStagesBankFee(customerLoanBean.getBankFeeAmount());

            Double firstMoney = customerLoanBean.getBankFeeAmount()/customerLoanBean.getRateType().getMonths();
            Double chaMoney = firstMoney * (customerLoanBean.getRateType().getMonths() - 1);
            Double firstRepayFee = customerLoanBean.getBankFeeAmount() - chaMoney;
            Double monthlyRepayFee = chaMoney / (customerLoanBean.getRateType().getMonths() - 1);
            appointPaymentExcel.setRepaymentFeeFirstMonth(decimalFormat.format(firstRepayFee));
            appointPaymentExcel.setRepaymentFeeMonthly(decimalFormat.format(monthlyRepayFee));
            appointPaymentExcel.setRepaymentFeeMonthlyRound(deci.format(monthlyRepayFee));

            Double yuAmount = Math.floor(customerLoanBean.getCreditAmount()/customerLoanBean.getRateType().getMonths());
            Double chaAmount = yuAmount * (customerLoanBean.getRateType().getMonths() - 1);
            Double firstRepayAmount = customerLoanBean.getCreditAmount() - chaAmount;
            Double monthlyRepayAmount = chaAmount / (customerLoanBean.getRateType().getMonths() - 1);
            appointPaymentExcel.setRepaymentAmountFirstMonth(decimalFormat.format(firstRepayAmount));
            //客户每期还款额 =  刷卡金额/期数
            double monthCustomerFee = bankCardApply.getSwipingMoney() / customerLoanBean.getRateType().getMonths();
            appointPaymentExcel.setRepaymentAmountMonthly(decimalFormat.format(monthCustomerFee));
            appointPaymentExcel.setRepaymentAmountMonthlyRound(decimalFormat1.format(monthCustomerFee));

        }

        return appointPaymentExcel;
    }

    private CashSourceBean getCashSource(String id){
        CashSourceBean cashSourceBean = iCashSourceBizService.actGetCashSource(id).getD();
        if(cashSourceBean.getDataStatus() == DataStatus.SAVE){
            return cashSourceBean;
        }
        return null;
    }

}