package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.bean.FeeItemBean;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.bd.service.*;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.*;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.*;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.drools.bean.AccrualSubsidiesBean;
import com.fuze.bcp.api.drools.service.IDroolsBizService;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.msg.bean.NoticeBean;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignCondition;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TEMSignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.service.ICustomerContractService;
import com.fuze.bcp.creditcar.service.ICustomerDemandService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.MutexService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.NumberHelper;
import com.fuze.bcp.utils.SimpleUtils;
import com.fuze.bcp.utils.StringHelper;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 客户签约微服务
 * Created by Lily on 2017/7/19.
 */
@Service
public class BizOrderService implements IOrderBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizOrderService.class);

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ICustomerSurveyTemplateBizService iCustomerSurveyTemplateBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    ICarValuationBizService iCarValuationBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;
    @Autowired

    IAmqpBizService iAmqpBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    IDroolsBizService iDroolsBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    MutexService mutexService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    IProductBizService iProductBizService;

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Autowired
    ICustomerContractService iCustomerContractService;

    @Autowired
    ICustomerContractBizService iCustomerContractBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    @Autowired
    IFileBizService iFileBizService;


    /**
     * 资质提交时初始化客户签约
     *
     * @param customerDemandId
     * @return
     */
    @Override
    public ResultBean<OrderSubmissionBean> actCreateOrder(String customerDemandId) {
        CustomerDemand customerDemand = iCustomerDemandService.getOne(customerDemandId);
        OrderSubmissionBean orderSubmissionBean;
        PurchaseCarOrder purchaseCarOrder = this.iOrderService.findByCustomerTransactionId(customerDemand.getCustomerTransactionId());
        if (purchaseCarOrder == null) {
            //初始化签约单
            orderSubmissionBean = this.initOrderByTransactionId(customerDemand.getCustomerTransactionId());

            String today = SimpleUtils.getShortDate();
            String loginUserId = orderSubmissionBean.getLoginUserId();
            String carTypeId = orderSubmissionBean.getCustomerCar().getCarTypeId();
            String carDealerId = orderSubmissionBean.getCarDealerId();
            String customerId = orderSubmissionBean.getCustomerId();
            Query query = new Query(Criteria.where("loginUserId").is(loginUserId)
                    .and("carDealerId").is(carDealerId)
                    .and("customerId").is(customerId)
                    .and("ts").regex(String.format("^%s", today), "m"));
            List<PurchaseCarOrder> orders = mongoTemplate.find(query, PurchaseCarOrder.class);
            if (orders != null && orders.size() > 0) {
                for (PurchaseCarOrder order : orders) {
                    CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(order.getCustomerCarId()).getD();
                    if (customerCarBean != null && customerCarBean.getCarTypeId().equals(carTypeId)) {
                        return ResultBean.getFailed().setM("");
                    }
                }
            }

        } else {
            orderSubmissionBean = this.mergeDemandAndOrderToOrderSubmission(customerDemand, purchaseCarOrder);
            if (orderSubmissionBean.getId() == null && !mutexService.lockSaveObject(orderSubmissionBean.getBusinessTypeCode(), orderSubmissionBean.getLoginUserId(), "createOrder")) {
                return ResultBean.getFailed().setM("");
            }
        }
        try {


            //档案类型
            List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(orderSubmissionBean.getCreditMaster().getId(),
                    orderSubmissionBean.getCustomerTransactionId(),
                    orderSubmissionBean.getBusinessTypeCode(),
                    "A002").getD();
            orderSubmissionBean.setCustomerImages(imageTypeFiles);

            //初始化后暫存客户签约的信息
            ResultBean<OrderSubmissionBean> orderSubmissionBeanResultBean = this.actSaveOrder(orderSubmissionBean);
            if (orderSubmissionBeanResultBean.isSucceed()) {
                logger.info(String.format(messageService.getMessage("MSG_ORDER_CREATEORDERSUCCEED"), orderSubmissionBean.getCreditMaster() == null ? null : orderSubmissionBean.getCreditMaster().getName()));
            }
            return ResultBean.getSucceed().setD(orderSubmissionBean);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            mutexService.unLockSaveObject(orderSubmissionBean.getBusinessTypeCode(), orderSubmissionBean.getLoginUserId());
        }
    }

    /**
     * 保存客户签约单 (只做保存，不进审批流）
     *
     * @param orderSubmission
     * @return
     */
    @Override
    public ResultBean<OrderSubmissionBean> actSaveOrder(OrderSubmissionBean orderSubmission) {
        try {

            logger.error("OrderSubmissionBean SERVER save ================================================================================");
            if (orderSubmission.getCreditMaster() == null || StringHelper.isBlock(orderSubmission.getCreditMaster().getId())) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_CREDITMASTER_NULL"));
            }

            PurchaseCarOrder purchaseCarOrder;
            if (StringHelper.isBlock(orderSubmission.getId())) {
                purchaseCarOrder = new PurchaseCarOrder();
                purchaseCarOrder.setCustomerId(orderSubmission.getCreditMaster().getId());
                purchaseCarOrder.setCustomerTransactionId(orderSubmission.getCustomerTransactionId());
                purchaseCarOrder.setDataStatus(DataStatus.TEMPSAVE); //临时保存
                purchaseCarOrder.setApproveStatus(PurchaseCarOrder.APPROVE_INIT); // 初始审批状态
            } else {
                ResultBean result = iOrderService.getEditableBill(orderSubmission.getId());
                if (result.failed()) return result;
                purchaseCarOrder = (PurchaseCarOrder) result.getD();
                // 业务校验
                result = iCustomerTransactionBizService.actGetEditableTransaction(purchaseCarOrder.getCustomerTransactionId());
                if (result.failed()) return result;
                //资质客户信息改变时更新签约
                purchaseCarOrder.setCustomerId(orderSubmission.getCreditMaster().getId());
                purchaseCarOrder.setCustomerTransactionId(orderSubmission.getCustomerTransactionId());
            }
            //TODO 保存只校验客户信息
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(orderSubmission.getCreditMaster().getId()).getD();
            if (customerBean == null) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORDER_CREDITMASTER_NOTFIND"), orderSubmission.getCreditMaster().getId()));
            } else {
                //TODO 待确认　更新用户工作信息
                customerBean.setCustomerJob(orderSubmission.getCreditMaster().getCustomerJob());
                customerBean.setContactAddress(orderSubmission.getCreditMaster().getContactAddress());
                iCustomerBizService.actSaveCustomer(customerBean);
            }

            purchaseCarOrder.setBusinessTypeCode(orderSubmission.getBusinessTypeCode()); //业务类型
            purchaseCarOrder.setLoginUserId(orderSubmission.getLoginUserId()); //分期经理用户ID
            purchaseCarOrder.setCarDealerId(orderSubmission.getCarDealerId()); //经销商
            purchaseCarOrder.setDealerEmployeeId(orderSubmission.getDealerEmployeeId()); //经销商员工id
            purchaseCarOrder.setRepaymentAmountFirstMonth(orderSubmission.getRepaymentAmountFirstMonth()); //首月还款金额
            purchaseCarOrder.setRepaymentAmountMonthly(orderSubmission.getRepaymentAmountMonthly()); //首月还款金额
            purchaseCarOrder.setRepaymentAmountSum(orderSubmission.getRepaymentAmountSum()); //总还款金额
            purchaseCarOrder.setFeeItemList(orderSubmission.getFeeItemList()); //收费明细列表
            purchaseCarOrder.setPrintNeedEarningProof(orderSubmission.getPrintNeedEarningProof());//是否打印收入证明
            purchaseCarOrder.setIsStraight(orderSubmission.getIsStraight());//是否为直客模式

            //指标信息
            purchaseCarOrder.setPickCarDate(orderSubmission.getPickCarDate()); //提车日期
            purchaseCarOrder.setIndicatorStatus(orderSubmission.getIndicatorStatus()); //指标状态
            purchaseCarOrder.setMoveOutDate(orderSubmission.getMoveOutDate()); //外迁日期
            purchaseCarOrder.setRetrieveDate(orderSubmission.getRetrieveDate()); //预计指标获取日期
            purchaseCarOrder.setIndicatorComment(orderSubmission.getIndicatorComment()); //情况说明
            if (orderSubmission.getVehicleEvaluateInfoId() != null) {//二手车评估单id
                purchaseCarOrder.setVehicleEvaluateInfoId(orderSubmission.getVehicleEvaluateInfoId());
            }


            EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(orderSubmission.getLoginUserId()).getD();
            if (employee != null) {
                purchaseCarOrder.setEmployeeId(employee.getId()); //分期经理员工ID
                purchaseCarOrder.setOrginfoId(employee.getOrgInfoId()); //部门ID
            }

            //保存反担保人信息，如果有的话
            if (orderSubmission.getCounterGuarantor() != null && StringHelper.isNotBlock(orderSubmission.getCounterGuarantor().getIdentifyNo())) {
                CustomerBean guarantor = iCustomerBizService.actSubmitCustomer(orderSubmission.getCounterGuarantor()).getD();
                purchaseCarOrder.setCounterGuarantorId(guarantor.getId());
            }

            //暂存借款信息
            LoanSubmissionBean loanSubmissionBean = orderSubmission.getCustomerLoan();
            if(loanSubmissionBean != null){
                CustomerLoanBean loan = mappingService.map(loanSubmissionBean, CustomerLoanBean.class);
                RateType rateType = new RateType();
                rateType.setMonths(loanSubmissionBean.getMonths() == null ? 0 : loanSubmissionBean.getMonths());
                rateType.setRatio(loanSubmissionBean.getRatio() == null ? 0.0 : loanSubmissionBean.getRatio());
                loan.setRateType(rateType);

                //保存贷款信息
                if (!StringHelper.isBlock(loanSubmissionBean.getId())) {
                    CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(loanSubmissionBean.getId()).getD();
                    loan.setApprovedCreditAmount(customerLoanBean.getApprovedCreditAmount());
                }
                CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(orderSubmission.getCarDealerId()).getD();
                loan.setCompensatoryInterest(loanSubmissionBean.getCompensatoryAmount() > 0 ? 1 : 0);//是否贴息
                //通过判断是否贴息获取渠道垫资政策是否需要垫资
                loan.setIsNeedPayment(loan.getCompensatoryInterest() == 0 ? carDealer.getPaymentPolicy().getBusiness() : carDealer.getPaymentPolicy().getDiscount());//是否垫资
                loan.setCustomerId(purchaseCarOrder.getCustomerId());
                loan.setCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
                Double receiptPrice = loan.getReceiptPrice();//车辆开票价格
                Double realPrice = loan.getRealPrice();//预计成交价
                if (purchaseCarOrder.getBusinessTypeCode().equals("NC")) {
                    if(receiptPrice != null && realPrice != null){
                        Double min = (receiptPrice < realPrice) ? receiptPrice : realPrice;
                        loan.setApplyAmount(min);//分期申请车价
                    }else{
                        loan.setApplyAmount(null);//分期申请车价
                    }
                }
                if(orderSubmission.getCustomerCar() != null){
                    CustomerCarBean car = mappingService.map(orderSubmission.getCustomerCar(), CustomerCarBean.class);
                    Double evaluatePrice = null;//二手车评估价
                    if (car != null && car.getEvaluatePrice() != null) {
                        evaluatePrice = car.getEvaluatePrice();
                    }
                    if (purchaseCarOrder.getBusinessTypeCode().equals("OC")) {
                        Double min = (receiptPrice < realPrice) ? receiptPrice : realPrice;
                        min = (min < evaluatePrice) ? min : evaluatePrice;
                        loan.setApplyAmount(min);//分期申请车价
                    }
                }

                //  计算贴息方案
                Map<String, Object> map = iCustomerBizService.actCalculateCompensatoryWay(loan.getBankFeeAmount(), loan.getCompensatoryAmount(), loan.getRateType().getMonths()).getD();
                if (map != null && !"".equals((String) map.get("compensatoryWay"))) {
                    loan.setCompensatoryWay((String) map.get("compensatoryWay"));
                    loan.setCompensatoryMonth((Integer) map.get("compensatoryMonth"));
                }
                //签约数据暂存时借款信息也将暂存
                if (loan.getId() != null) {//借款信息存在直接保存
                    CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(loan.getId()).getD();
                    loan.setDataStatus(customerLoanBean.getDataStatus());
                    if (customerLoanBean != null) {
                        loan = iCustomerBizService.actSaveCustomerLoan(loan).getD();
                    }
                } else {//借款信息不存在则保存为暂存状态
                    loan.setDataStatus(DataStatus.TEMPSAVE);
                    loan = iCustomerBizService.actSaveCustomerLoan(loan).getD();
                }
                purchaseCarOrder.setCustomerLoanId(loan.getId());
            }else{
                purchaseCarOrder.setCustomerLoanId(null);
            }
            //暂存车辆信息
            if(orderSubmission.getCustomerCar() != null) {
                CustomerCarBean car = mappingService.map(orderSubmission.getCustomerCar(), CustomerCarBean.class);
                //保存车辆信息
                car.setCustomerId(purchaseCarOrder.getCustomerId());
                car.setCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
                //签约暂存时车辆信息也需要暂存
                if (car.getId() != null) {//车辆信息存在直接保存
                    CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(car.getId()).getD();
                    car.setDataStatus(customerCarBean.getDataStatus());
                    if (customerCarBean != null) {
                        car = iCustomerBizService.actSaveCustomerCar(car).getD();
                    }
                } else {//车辆信息不存在则存为暂存
                    car.setDataStatus(DataStatus.TEMPSAVE);
                    car = iCustomerBizService.actSaveCustomerCar(car).getD();
                }
                purchaseCarOrder.setCustomerCarId(car.getId());
            }else{
                purchaseCarOrder.setCustomerCarId(null);
            }

            //处理档案资料
            iCustomerImageFileBizService.actSaveCustomerImages(purchaseCarOrder.getCustomerId(),
                    purchaseCarOrder.getCustomerTransactionId(),
                    orderSubmission.getCustomerImages()); //整体保存档案资料

            purchaseCarOrder = iOrderService.save(purchaseCarOrder);
            //更新交易信息
            orderSubmission.setTs(purchaseCarOrder.getTs());
            orderSubmission.setId(purchaseCarOrder.getId());
            String carDealerMsg = messageService.getMessage("MSG_ORDER_CARDEALERMSG");
            if (purchaseCarOrder.getCarDealerId() != null) {
                CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
                if (carDealerBean.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
                    carDealerMsg = "";
                }
            }
            logger.info(purchaseCarOrder.getBillTypeCode() + "." + purchaseCarOrder.getId() + messageService.getMessage(messageService.getMessage("MSG_ORDER_SAVE_SUCCESS")) + carDealerMsg);
            return ResultBean.getSucceed().setD(orderSubmission).setM(messageService.getMessage("MSG_ORDER_SAVE_SUCCESS") + carDealerMsg);
        } catch (Exception e) {
            throw e;
        } finally {
            mutexService.unLockSaveObject(orderSubmission.getBusinessTypeCode(), orderSubmission.getLoginUserId());
        }
    }

    /**
     * 提交客户签约
     * @param id
     * @return
     */
    public ResultBean<PurchaseCarOrderBean> actSubmitOrder(String id, String comment) {
        ResultBean result = iOrderService.getEditableBill(id);
        if (result.failed()) return result;
        PurchaseCarOrder purchaseCarOrder = (PurchaseCarOrder) result.getD();
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(purchaseCarOrder.getCustomerTransactionId());
        if (result.failed()) return result;
        List<String> errorMsg = new ArrayList<>(); // 提交时签约校验List
        //TODO 提交时签约非空校验
        ResultBean<PurchaseCarOrderBean> submitOrderCheckNull = this.submitOrderCheckNull(purchaseCarOrder);
        if(submitOrderCheckNull.failed()){
            errorMsg.addAll(submitOrderCheckNull.getL());
        }
        //TODO 提交签约数据逻辑校验
        ResultBean<PurchaseCarOrderBean> submitOrderCheckData = this.submitOrderCheckData(purchaseCarOrder);
        if(submitOrderCheckData.failed()){
            errorMsg.addAll(submitOrderCheckData.getL());
        }
        if (errorMsg.size() > 0) {
            return ResultBean.getFailed().setL(errorMsg);
        }
        // 获取资质审查，判断资质审查是否通过
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
        //若资质审查未通过
        if (!customerDemand.getApproveStatus().equals(ApproveStatus.APPROVE_PASSED)) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_CUSTOMERDEMANDNOPASS"));
        }
        //车辆信息
        CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
        //借款信息
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();

        if (purchaseCarOrder.getBusinessTypeCode().equals("OC")) { //二手车业务校验
            if(customerCarBean != null){
                CarValuationBean carValuationBean = iCarValuationBizService.actGetValuationByVin(customerCarBean.getVin()).getD();
                if(carValuationBean == null)
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_VALUATIONNULL"));

                if (carValuationBean.getApproveStatus() != ApproveStatus.APPROVE_PASSED) { //判断评估是否完成
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_VALUATIONNOPASS"));
                }

                if(carValuationBean != null){
                    List<PurchaseCarOrder> orders = iOrderService.findAllByVehicleEvaluateInfoIdAndDataStatus(carValuationBean.getId(),DataStatus.SAVE);
                    for (PurchaseCarOrder order:orders){
                        if (order != null ) {
                            if (!order.getId().equals(purchaseCarOrder.getId())) {//判断评估单是否被使用
                                CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(order.getCustomerTransactionId()).getD();
                                if (customerTransaction != null && !(customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_FINISH)) {
                                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_VALUATIONFINISHORDER"));
                                }
                            }
                        }
                    }

                }

                iCarValuationBizService.actUseValuation(carValuationBean,
                        purchaseCarOrder.getCustomerId(),
                        purchaseCarOrder.getCustomerTransactionId(),
                        purchaseCarOrder.getId());
            }
        }

        //判断客户有没有分配调查问卷，分配的话查看有没有回答，没有回答则不允许签约，客户没有分配调查问卷则不影响签约提交。
        ResultBean<List<SurveyOption>> resultBean = iCustomerSurveyTemplateBizService.actGetTransactionSurveyResult(purchaseCarOrder.getCustomerTransactionId());
        if (resultBean.isSucceed()) {
            List<SurveyOption> optionList = resultBean.getD();
            if (optionList == null || optionList.size() == 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_SURVEYOPTION"));
            }
        } else if (resultBean.failed()) {
            //表示未分配调查问卷，不影响签约提交
            logger.info(messageService.getMessage(resultBean.getM()));
        }
        //渠道必须审核通过才可以提交签约信息
        CarDealerBean carDealerBean = null;
        if (purchaseCarOrder.getCarDealerId() != null) {
            carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
            if (carDealerBean.getApproveStatus() != ApproveStatus.APPROVE_PASSED) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_CARDEALERNOPASS"));
            }
        }else{
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_CARDEALERNONULL"));
        }

        //启动客户签约流程
        purchaseCarOrder = this.startOrderFlow(purchaseCarOrder, comment).getD();
        //签约提交成功后更新交易信息
        this.updateTransaction(purchaseCarOrder,carDealerBean).getD();
        //签约提交成功后将车辆信息和借款信息暂存的数据改为保存
        if (purchaseCarOrder != null) {


            customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();

            if (customerCarBean != null) {
                customerCarBean.setDataStatus(DataStatus.SAVE);
                iCustomerBizService.actSaveCustomerCar(customerCarBean);
            }
            customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            if (customerLoanBean != null) {
                customerLoanBean.setDataStatus(DataStatus.SAVE);
                iCustomerBizService.actSaveCustomerLoan(customerLoanBean);
            }
        }
        //TODO 限制重申之后不能更改是否贴息和贴息金额【重审功能已取消】
        /*WorkFlowBillBean workFlowBillBean = iWorkflowBizService.actGetBillWorkflow(purchaseCarOrder.getId()).getD();
        if (workFlowBillBean != null) {
            List<SignInfo> signInfos = workFlowBillBean.getSignInfos();
            if (signInfos != null && signInfos.size() > 0) {
                for (SignInfo signInfo : signInfos) {
                    if (signInfo.getFlag() == SignInfo.FLAG_AGAIN_COMMIT) {//判断是否重申
                        if (customerLoanBean != null) {//将之前保存的贴息金额和是否贴息与提交的比较是否相同
                            if (customerLoanBean.getCompensatoryAmount() == 0 && customerLoanBean.getCompensatoryAmount() > 0) {//原先订单不贴息，重审后提交改为贴息不允许
                                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_COMPENSATORYINTERESTISZERO"));
                            } else if (customerLoanBean.getCompensatoryAmount() > 0 && customerLoanBean.getCompensatoryAmount() == 0) {//原先订单贴息，重审后提交改为不贴息不允许
                                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_COMPENSATORYINTERESTNOZERO"));
                            }
                            CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
                            if(carDealer != null){
                                if(customerLoanBean.getCompensatoryInterest() == 0 && customerLoanBean.getIsNeedPayment() == 1 && carDealer.getPaymentPolicy().getBusiness() == 0){//商贷垫资不允许改为商贷不垫资
                                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_BUSINESSNEEDPAYMENT"));
                                }else if(customerLoanBean.getCompensatoryInterest() == 0 && customerLoanBean.getIsNeedPayment() == 0 && carDealer.getPaymentPolicy().getBusiness() == 1){//商贷不垫资不允许改为商贷垫资
                                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_BUSINESSNONEEDPAYMENT"));
                                }else if(customerLoanBean.getCompensatoryInterest() == 1 && customerLoanBean.getIsNeedPayment() == 1 && carDealer.getPaymentPolicy().getDiscount() == 0){//贴息垫资不允许改为贴息不垫资
                                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_DISCOUNTNEEDPAYMENT"));
                                }else if(customerLoanBean.getCompensatoryInterest() == 1 && customerLoanBean.getIsNeedPayment() == 0 && carDealer.getPaymentPolicy().getDiscount() == 1){//贴息不垫资不允许改为贴息垫资
                                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_DISCOUNTNONEEDPAYMENT"));
                                }
                            }

                        }
                    }
                }
            }
        }*/
        logger.info(purchaseCarOrder.getBillTypeCode() + ":" + purchaseCarOrder.getId() + messageService.getMessage("MSG_ORDER_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(purchaseCarOrder, PurchaseCarOrderBean.class)).setM(messageService.getMessage("MSG_ORDER_SUBMIT"));
    }

    private Map<String, List<SignCondition>> getConditions(Integer department) {
        Map<String, List<SignCondition>> params = new HashMap();
        //提交签约
        List signConditions = new ArrayList<>();
        SignCondition data = new SignCondition("department", String.valueOf(department), true);
        signConditions.add(data);
        params.put("Order_Submit", signConditions);
        return params;
    }

    /**
     * 启动签约审查流程
     *
     * @param purchaseCarOrder
     * @return
     */
    private ResultBean<PurchaseCarOrder> startOrderFlow(PurchaseCarOrder purchaseCarOrder, String comment) {
        //客户借款信息
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        //渠道信息
        CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
        List<SalesRate> dealerRateTypes = carDealerBean.getDealerRateTypes();
        Double ratio = 0.0;
        Double loanServiceFee = 0.0;
        //银行手续费率
        for (SalesRate saleRate : dealerRateTypes) {
            if (saleRate.getBusinessTypeCode().equals(purchaseCarOrder.getBusinessTypeCode())) {
                List<BusinessRate> rateTypeList = saleRate.getRateTypeList();
                for (BusinessRate businessRate : rateTypeList) {
                    List<RateType> typeList = businessRate.getRateTypeList();
                    for (RateType type : typeList) {
                        if (type.getMonths().equals(customerLoanBean.getRateType().getMonths())) {
                            ratio = type.getRatio();
                        }
                    }
                }
            }
        }
        //贷款服务费
        List<ServiceFee> serviceFeeEntityList = carDealerBean.getServiceFeeEntityList();
        for (ServiceFee serviceFee : serviceFeeEntityList) {
            if (serviceFee.getBusinessType().equals(purchaseCarOrder.getBusinessTypeCode())) {
                List<RateType> rateTypeList = serviceFee.getRateTypeList();
                for (RateType rateType : rateTypeList) {
                    if (rateType.getMonths().equals(customerLoanBean.getRateType().getMonths())) {
                        loanServiceFee = rateType.getRatio() * customerLoanBean.getCreditAmount();
                    }
                }
            }
        }
        SignInfo signInfo = new SignInfo(purchaseCarOrder.getLoginUserId(), purchaseCarOrder.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //进行审批
        String collectionMame = null;
        Integer department = BooleanDataBean.NUM_SWITH_FALSE;
        try {
            collectionMame = PurchaseCarOrder.getMongoCollection(purchaseCarOrder);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }
        //是否需要部门经理审批
        if (customerLoanBean.getRateType().getRatio() < ratio) {
            department = BooleanDataBean.NUM_SWITH_TRUE;
        }
        if (customerLoanBean.getLoanServiceFee() < loanServiceFee) {
            department = BooleanDataBean.NUM_SWITH_TRUE;
        }
        Map<String, List<SignCondition>> params = getConditions(department);
        ResultBean resultBean = iWorkflowBizService.actSubmit(purchaseCarOrder.getBusinessTypeCode(), purchaseCarOrder.getId(), purchaseCarOrder.getBillTypeCode(), signInfo, collectionMame, params, purchaseCarOrder.getCustomerTransactionId());
        if (resultBean != null) {
            if (resultBean.isSucceed()) {
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    purchaseCarOrder = iOrderService.getOne(purchaseCarOrder.getId());
                    //提交成功后更新提交时间
                    purchaseCarOrder.setTs(DateTimeUtils.getCreateTime());
                    purchaseCarOrder = iOrderService.save(purchaseCarOrder);
                    return ResultBean.getSucceed().setD(purchaseCarOrder);
                } else {
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            } else if (resultBean.failed()) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return null;
    }


    /**
     * 更新交易业务
     *
     * @return
     */
    private ResultBean<CustomerTransactionBean> updateTransaction(PurchaseCarOrder purchaseCarOrder,CarDealerBean carDealerBean ) {
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(purchaseCarOrder.getCustomerTransactionId()).getD();

        if (customerTransaction != null) {
            //检查业务数是否超限
            customerTransaction.setBusinessTypeCode(purchaseCarOrder.getBusinessTypeCode());
            customerTransaction.setCarDealerId(purchaseCarOrder.getCarDealerId());
            customerTransaction.setCustomerId(purchaseCarOrder.getCustomerId());
            EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(purchaseCarOrder.getLoginUserId()).getD();
            customerTransaction.setEmployeeId(employee.getId());
            customerTransaction.setOrginfoId(employee.getOrgInfoId());
            customerTransaction.setIsStraight(purchaseCarOrder.getIsStraight());//是否直客模式

            //  计算档案编号
            if (StringUtils.isEmpty(customerTransaction.getFileNumber())) {
                customerTransaction.setFileNumber(iCustomerTransactionBizService.actCreateCustomerNumber(DateTimeUtils.getCreateTime(), purchaseCarOrder.getBusinessTypeCode()).getD());
            }
            //更新报单行
            customerTransaction.setCashSourceId(carDealerBean.getCashSourceId());
            customerTransaction = iCustomerTransactionBizService.actSaveCustomerTransaction(customerTransaction).getD();
            return ResultBean.getSucceed().setD(mappingService.map(customerTransaction, CustomerTransactionBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));

    }

    /**
     * 客户签约审核
     *
     * @param orderId
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<PurchaseCarOrderBean> actSignOrder(String orderId, SignInfo signInfo) {
        //获取客户签约信息
        PurchaseCarOrder purchaseCarOrder = iOrderService.getOne(orderId);
        if(purchaseCarOrder != null){
            //客户信息
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(purchaseCarOrder.getCustomerId()).getD();
            //借款信息
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            try {
                ResultBean resultBean = null;
                WorkFlowBillBean workFlowBillBean = iWorkflowBizService.actGetBillWorkflow(orderId).getD();
                if (workFlowBillBean == null ) {
                    return ResultBean.getFailed();
                }
                if ("Order_Complete".equals(workFlowBillBean.getCurrentTask())) {
                    TEMSignInfo s = (TEMSignInfo) signInfo;

                    //获取是否为自雇人士
                    Integer isSelfEmployed = customerBean != null ? customerBean.getIsSelfEmployed() : 0;
                    //贷款金额
                    Double creditAmount = customerLoanBean != null ?customerLoanBean.getCreditAmount() : 0;
                    //首付比例
                    Double downPaymentRatio = customerLoanBean != null ?customerLoanBean.getDownPaymentRatio() : 0;
                    Map<?, ?> permission = iParamBizService.actGetMap("TEM_APPROVAL_PERMISSION").getD();
                    List<String> specialRoles = iAuthenticationBizService.actGetSpecialRoleByUserId(s.getUserId()).getD();
                    Map approveVars = new HashMap<>();
                    Integer isFinal = 0; // 默认初审
                    for (String role : specialRoles) {
                        Map<String,String> data = (Map<String, String>) permission.get(role);
                        Double downPaymentRatio1 = Double.parseDouble(data.get("downPaymentRatio")) ;
                        Double creditAmount1 = Double.parseDouble(data.get("creditAmount")) ;
                        Integer isSelfEmployed1 = Integer.valueOf(data.get("isSelfEmployed")) ;
                        s.setAuditRole(role);
                        if(downPaymentRatio > downPaymentRatio1 && creditAmount < creditAmount1 ){
                            if(isSelfEmployed1 == 0){
                                if(isSelfEmployed.equals(isSelfEmployed1)){
                                    isFinal = 1; // 终审
                                    break;
                                }
                            }else{
                                isFinal = 1; // 终审
                                break;
                            }
                        }

                    }
                    //若初审，不能进行拒绝操作
                    if(isFinal == 0 && signInfo.getResult() == ApproveStatus.APPROVE_REJECT){
                        return ResultBean.getFailed().setD(ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDERD_ISFINALNOREJECT")));
                    }
                    approveVars.put("final", isFinal);
                    s.setAuditStatus(isFinal);
                    s.setApproveVars(approveVars);
                    resultBean = iWorkflowBizService.actSignBill(orderId, s, true);
                } else {
                    resultBean = iWorkflowBizService.actSignBill(orderId, signInfo);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
            }
            //获取客户签约信息
            purchaseCarOrder = iOrderService.getOne(orderId);
            //查询资质流程是否结束，最终状态为拒绝则废弃签约数据
            ResultBean<Boolean> booleanResultBean = iWorkflowBizService.actBusinessBillIsFinish(purchaseCarOrder.getBillTypeCode() + "." + purchaseCarOrder.getId());
            if (booleanResultBean.isSucceed()) {
                if (booleanResultBean.getD()) {//流程结束

                    if(purchaseCarOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                        //签约通过且贷款服务费大于0，推送缴费单提醒
                        //CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
                        if(customerLoanBean.getLoanServiceFee() != null && customerLoanBean.getLoanServiceFee() > 0){
                            //CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerLoanBean.getCustomerId()).getD();
                            NoticeBean noticeBean = new NoticeBean();
                            noticeBean.setType("type_1");
                            noticeBean.setSendType("pad");
                            noticeBean.setContent("客户"+customerBean.getName()+"的缴费单未提交，请及时提交!");
                            noticeBean.setTitle("业务通知");
                            noticeBean.setFromGroup(1);
                            Set set = new HashSet<>();
                            set.add(purchaseCarOrder.getEmployeeId());
                            noticeBean.setLoginUserNames(set);
                            NoticeBean noticeBean1 = iMessageBizService.actSaveNotice(noticeBean).getD();
                            iAmqpBizService.actSendNotice(noticeBean1.getId());
                        }
                    }

                    //若签约拒绝，判断银行制卡是否存在，存在则直接销卡
                    if (purchaseCarOrder.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
                        BankCardApplyBean bankCardApplyBean = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(purchaseCarOrder.getCustomerTransactionId()).getD();
                        if (bankCardApplyBean != null) {
                            //将卡业务流程直接销卡
                            bankCardApplyBean.setStatus(BankCardApplyBean.BKSTATUS_CANCEL);
                            iBankCardApplyBizService.actApprovedBankCardApply(bankCardApplyBean, BankCardApplyBean.BKSTATUS_CANCEL, -1, signInfo.getUserId());

                        }
                    }

                }
            }
            return ResultBean.getSucceed().setD(mappingService.map(purchaseCarOrder, PurchaseCarOrderBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORDER_NOT_PASS"),purchaseCarOrder.getId()));
    }


    @Override
    public ResultBean<PurchaseCarOrderBean> actGetOrder(String customerId, String customerTransactionId) {
        return ResultBean.getSucceed().setD(mappingService.map(iOrderService.findByCustomerTransactionId(customerTransactionId), PurchaseCarOrderBean.class));
    }

    @Override
    public ResultBean<PurchaseCarOrderBean> actGetOrder(String orderId) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.getAvailableOne(orderId);
        String code = purchaseCarOrder.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
        PurchaseCarOrderBean purchaseCarOrderBean = mappingService.map(purchaseCarOrder, PurchaseCarOrderBean.class);
        purchaseCarOrderBean.setBillType(billType);
        return ResultBean.getSucceed().setD(purchaseCarOrderBean);
    }

    @Override
    public ResultBean<PurchaseCarOrderBean> actSearchOrders(String userId, SearchBean searchBean) {
        Page<PurchaseCarOrder> carOrders = iOrderService.findAllBySearchBean(PurchaseCarOrder.class, searchBean,SearchBean.STAGE_ORDER,userId);
        return ResultBean.getSucceed().setD(mappingService.map(carOrders,PurchaseCarOrderBean.class));
    }

    @Override
    public ResultBean<PurchaseCarOrderBean> actSaveOrder(PurchaseCarOrderBean purchaseCarOrderBean) {
        PurchaseCarOrder purchaseCarOrder = mappingService.map(purchaseCarOrderBean, PurchaseCarOrder.class);
        return ResultBean.getSucceed().setD(mappingService.map(iOrderService.save(purchaseCarOrder), PurchaseCarOrderBean.class));
    }


    @Override
    public ResultBean<List<OrderListBean>> actGetOrders(String loginUserId, Integer currentPage) {
        return null;
    }

    @Override
    public ResultBean<PurchaseCarOrderBean> actGetOrderByTransactionId(String transactionId) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        if (purchaseCarOrder != null) {
            return ResultBean.getSucceed().setD(mappingService.map(purchaseCarOrder, PurchaseCarOrderBean.class));
        } else {
            return ResultBean.getFailed();
        }
    }

    public ResultBean<OrderSubmissionBean> actGetTransactionOrder(String transactionId) {

        PurchaseCarOrder purchaseCarOrder = this.iOrderService.findByCustomerTransactionId(transactionId);
        OrderSubmissionBean orderSubmissionBean;
        if (purchaseCarOrder == null) {
            //通过CustomerDemand来初始化
            orderSubmissionBean = this.initOrderByTransactionId(transactionId);
        } else {
            orderSubmissionBean = this.mapOrderToOrderSubmission(purchaseCarOrder);
        }

        purchaseCarOrder = new PurchaseCarOrder();
        //档案类型
        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(orderSubmissionBean.getCreditMaster().getId(),
                orderSubmissionBean.getCustomerTransactionId(),
                orderSubmissionBean.getBusinessTypeCode(),
                purchaseCarOrder.getBillTypeCode()).getD();
        orderSubmissionBean.setCustomerImages(imageTypeFiles);

        return ResultBean.getSucceed().setD(orderSubmissionBean);

    }

    private OrderSubmissionBean mergeDemandAndOrderToOrderSubmission(CustomerDemand demand, PurchaseCarOrder order) {

        OrderSubmissionBean orderSubmissionBean = mappingService.map(order, OrderSubmissionBean.class);

        //反担保人信息
        if (StringHelper.isNotBlock(order.getCounterGuarantorId())) {
            CustomerBean counterGuarantor = iCustomerBizService.actGetCustomerById(order.getCounterGuarantorId()).getD();
            if (counterGuarantor != null) {
                orderSubmissionBean.setCounterGuarantor(counterGuarantor);
            }
        }

        CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(demand.getCustomerCarId()).getD();
        //获取车的信息
        CustomerCarBean customerCarBean2 = iCustomerBizService.actGetCustomerCarById(order.getCustomerCarId()).getD();
        if (customerCarBean != null) {
            PadCustomerCarBean customerCar = this.getCustomerCarForPad(customerCarBean); //获取资质中的数据
            customerCar.setId(null); //清空ID
            if (customerCarBean2 != null) { //如果签约已经保存，合并资质和签约的数据
                customerCar.setId(customerCarBean2.getId());
                customerCar.setConfigures(customerCarBean2.getConfigures());
                customerCar.setMl(customerCarBean2.getMl());
                customerCar.setTransferCount(customerCarBean2.getTransferCount());
                customerCar.setOperateStatus(customerCarBean2.getOperateStatus());
            }

            orderSubmissionBean.setCustomerCar(customerCar);
        }

        //获取贷款信息
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(demand.getCustomerLoanId()).getD();
        CustomerLoanBean customerLoanBean2 = iCustomerBizService.actGetCustomerLoanById(order.getCustomerLoanId()).getD();
        if (customerLoanBean != null) {
            LoanSubmissionBean customerLoan = this.getCustomerLoanForPad(customerLoanBean);//获取资质中的贷款信息
            customerLoan.setId(null);//清空ID
            if (customerLoanBean2 != null) { //如果签约已经保存，合并资质和签约的贷款数据
                customerLoan.setId(customerLoanBean2.getId());
                //customerLoan.setLoanServiceFee(customerLoanBean2.getLoanServiceFee());
            }
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(demand.getCarDealerId()).getD();
            //默认贴息金额
            if(carDealerBean != null ){//限定经营品牌
                if(carDealerBean.getBrandIsLimit() == 1){
                    AccrualSubsidiesBean accrualSubsidiesBean = new AccrualSubsidiesBean();
                    if(customerCarBean != null){
                        accrualSubsidiesBean.setCarTypeId(customerCarBean.getCarTypeId());
                    }
                    accrualSubsidiesBean.setMonths(customerLoanBean.getRateType().getMonths());
                    accrualSubsidiesBean.setDownPaymentRatio(customerLoanBean.getDownPaymentRatio());
                    accrualSubsidiesBean.setRatio(customerLoanBean.getRateType().getRatio()*100);
                    accrualSubsidiesBean.setCreditAmount(customerLoanBean.getCreditAmount());
                    accrualSubsidiesBean = iProductBizService.actGetCompensatory(accrualSubsidiesBean).getD();
                    if(accrualSubsidiesBean != null && accrualSubsidiesBean.getCompensatoryAmount() != null){
                        customerLoan.setCompensatoryAmount(accrualSubsidiesBean.getCompensatoryAmount());
                    }
                }

                //获取贷款服务费率
                List<ServiceFee> serviceFeeEntityList = carDealerBean.getServiceFeeEntityList();
                for (ServiceFee serviceFee:serviceFeeEntityList) {
                    if(serviceFee.getBusinessType().equals(demand.getBusinessTypeCode())){
                        List<RateType> rateTypeList = serviceFee.getRateTypeList();
                        for (RateType rateType:rateTypeList) {
                            if(rateType.getMonths().equals(customerLoan.getMonths())){
                                //默认计算贷款服务费
                                customerLoan.setLoanServiceFee(customerLoan.getCreditAmount() * rateType.getRatio());
                            }

                        }
                    }
                }
            }
            orderSubmissionBean.setCustomerLoan(customerLoan);
        }

        //从资质获取其它信息
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(order.getCustomerTransactionId());
        if (customerDemand != null) {
            orderSubmissionBean = this.getDataFromCustomerDemand(customerDemand, orderSubmissionBean);
            //其他信息
            orderSubmissionBean.setCarDealerId(customerDemand.getCarDealerId());
            orderSubmissionBean.setBusinessTypeCode(customerDemand.getBusinessTypeCode());
            orderSubmissionBean.setLoginUserId(customerDemand.getLoginUserId());
            orderSubmissionBean.setDealerEmployeeId(customerDemand.getDealerEmployeeId());

            orderSubmissionBean.setVehicleEvaluateInfoId(customerDemand.getVehicleEvaluateInfoId());
        }

        return orderSubmissionBean;
    }

    private OrderSubmissionBean mapOrderToOrderSubmission(PurchaseCarOrder order) {

        OrderSubmissionBean orderSubmissionBean = mappingService.map(order, OrderSubmissionBean.class);

        //反担保人信息
        if (StringHelper.isNotBlock(order.getCounterGuarantorId())) {
            CustomerBean counterGuarantor = iCustomerBizService.actGetCustomerById(order.getCounterGuarantorId()).getD();
            if (counterGuarantor != null) {
                orderSubmissionBean.setCounterGuarantor(counterGuarantor);
            }
        }

        //获取车的信息
        CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(order.getCustomerCarId()).getD();
        if (customerCarBean != null)
            orderSubmissionBean.setCustomerCar(this.getCustomerCarForPad(customerCarBean));

        //获取贷款信息
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(order.getCustomerLoanId()).getD();
        if (customerLoanBean != null)
            orderSubmissionBean.setCustomerLoan(this.getCustomerLoanForPad(customerLoanBean));

        //从资质获取其它信息
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(order.getCustomerTransactionId());
        if (customerDemand != null)
            orderSubmissionBean = this.getDataFromCustomerDemand(customerDemand, orderSubmissionBean);

        return orderSubmissionBean;
    }

    private PadCustomerCarBean getCustomerCarForPad(CustomerCarBean customerCarBean) {
        PadCustomerCarBean padCustomerCarBean = mappingService.map(customerCarBean, PadCustomerCarBean.class);
        if (StringHelper.isNotBlock(padCustomerCarBean.getVin())) {
            //行驶里程		    最后维保里程		 初步评估价格
            CarValuationBean carValuation = iCarValuationBizService.actGetValuationByVin(padCustomerCarBean.getVin()).getD();
            if (carValuation != null) {
                padCustomerCarBean.setMileage(carValuation.getMileage());
                padCustomerCarBean.setMaintenanceMileage(carValuation.getMaintenanceMileage());
                padCustomerCarBean.setInitialValuationPrice(carValuation.getInitialValuationPrice());
                if (carValuation.getPrice() != null) {
                    padCustomerCarBean.setEvaluatePrice(carValuation.getPrice());
                }
            }
        }

        return padCustomerCarBean;
    }

    private LoanSubmissionBean getCustomerLoanForPad(CustomerLoanBean customerLoanBean) {
        LoanSubmissionBean loanSubmissionBean = mappingService.map(customerLoanBean, LoanSubmissionBean.class);
        loanSubmissionBean.setMonths(customerLoanBean.getRateType().getMonths());
        loanSubmissionBean.setRatio(customerLoanBean.getRateType().getRatio());

        return loanSubmissionBean;
    }

    private OrderSubmissionBean getDataFromCustomerDemand(CustomerDemand customerDemand, OrderSubmissionBean orderSubmissionBean) {

        //借款主体信息
        if (StringHelper.isNotBlock(customerDemand.getCreditMasterId())) {
            CustomerBean creditMaster = iCustomerBizService.actGetCustomerById(customerDemand.getCreditMasterId()).getD();
            if (creditMaster != null) {
                orderSubmissionBean.setCreditMaster(creditMaster);
            }
        }
        //配偶信息
        if (StringHelper.isNotBlock(customerDemand.getMateCustomerId())) {
            CustomerBean mateCustomer = iCustomerBizService.actGetCustomerById(customerDemand.getMateCustomerId()).getD();
            if (mateCustomer != null) {
                orderSubmissionBean.setMateCustomer(mateCustomer);
            }
        }
        //指标人信息
        if (StringHelper.isNotBlock(customerDemand.getPledgeCustomerId())) {
            CustomerBean pledgeCustomer = iCustomerBizService.actGetCustomerById(customerDemand.getPledgeCustomerId()).getD();
            if (pledgeCustomer != null) {
                orderSubmissionBean.setPledgeCustomer(pledgeCustomer);
            }
        }
        orderSubmissionBean.setNeedCounterGuarantor(customerDemand.getNeedCounterGuarantor());
        //其他信息
        orderSubmissionBean.setRelation(customerDemand.getRelation()); //指标人关系
        return orderSubmissionBean;
    }

    @Override
    public ResultBean<OrderSubmissionBean> actInitOrderByTransactionId(String transactionId) {
        return null;
    }

    public OrderSubmissionBean initOrderByTransactionId(String transactionId) {
        OrderSubmissionBean orderSubmissionBean = new OrderSubmissionBean();
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId).getD();
        if (customerTransaction != null) {

            orderSubmissionBean.setCustomerTransactionId(customerTransaction.getId());
            orderSubmissionBean.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
            // customerDemand 信息
            CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(customerTransaction.getId());
            if (customerDemand != null) {
                orderSubmissionBean = this.getDataFromCustomerDemand(customerDemand, orderSubmissionBean);
                //其他信息
                orderSubmissionBean.setCarDealerId(customerDemand.getCarDealerId());
                orderSubmissionBean.setBusinessTypeCode(customerDemand.getBusinessTypeCode());
                orderSubmissionBean.setLoginUserId(customerDemand.getLoginUserId());
                orderSubmissionBean.setDealerEmployeeId(customerDemand.getDealerEmployeeId());

                //构建收费项列表
                List<APILookupBean> feeItems = iBaseDataBizService.actLookupFeeItems().getD();
                for (APILookupBean feeItem : feeItems) {
                    FeeValueBean feeValue = mappingService.map(feeItem, FeeValueBean.class);
                    orderSubmissionBean.getFeeItemList().add(feeValue);
                }
            }

            //COPY CustomerCarBean 客户会话中车的信息
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(customerDemand.getCustomerCarId()).getD();
            if (customerCarBean != null) {
                PadCustomerCarBean customerCar = this.getCustomerCarForPad(customerCarBean);
                customerCar.setId(null);
                orderSubmissionBean.setCustomerCar(customerCar);
            }

            //COPY CustomerLoanBean 客户借款信息
            CustomerLoanBean customerLoanBean = this.iCustomerBizService.actGetCustomerLoanById(customerDemand.getCustomerLoanId()).getD();
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(customerTransaction.getCarDealerId()).getD();
            if (customerLoanBean != null) {
                LoanSubmissionBean loanSubmission = this.getCustomerLoanForPad(customerLoanBean);
                loanSubmission.setId(null);
                //默认贴息金额
                if(carDealerBean != null ){
                    if(carDealerBean.getBrandIsLimit() == 1){//限定经营品牌
                        AccrualSubsidiesBean accrualSubsidiesBean = new AccrualSubsidiesBean();
                        if(customerCarBean != null){
                            accrualSubsidiesBean.setCarTypeId(customerCarBean.getCarTypeId());
                        }
                        accrualSubsidiesBean.setMonths(customerLoanBean.getRateType().getMonths());
                        accrualSubsidiesBean.setDownPaymentRatio(customerLoanBean.getDownPaymentRatio());
                        accrualSubsidiesBean.setRatio(customerLoanBean.getRateType().getRatio()*100);
                        accrualSubsidiesBean.setCreditAmount(customerLoanBean.getCreditAmount());
                        accrualSubsidiesBean = iProductBizService.actGetCompensatory(accrualSubsidiesBean).getD();
                        if(accrualSubsidiesBean != null && accrualSubsidiesBean.getCompensatoryAmount() != null){
                            loanSubmission.setCompensatoryAmount(accrualSubsidiesBean.getCompensatoryAmount());
                        }
                    }
                    //获取贷款服务费率
                    List<ServiceFee> serviceFeeEntityList = carDealerBean.getServiceFeeEntityList();
                    for (ServiceFee serviceFee:serviceFeeEntityList) {
                        if(serviceFee.getBusinessType().equals(customerDemand.getBusinessTypeCode())){
                            List<RateType> rateTypeList = serviceFee.getRateTypeList();
                            for (RateType rateType:rateTypeList) {
                                if(rateType.getMonths().equals(loanSubmission.getMonths())){
                                    //默认计算贷款服务费
                                    loanSubmission.setLoanServiceFee(loanSubmission.getCreditAmount() * rateType.getRatio());
                                }

                            }
                        }
                    }
                }

                orderSubmissionBean.setCustomerLoan(loanSubmission);
            }

            //二手车评估单
            if (customerDemand.getVehicleEvaluateInfoId() != null) {
                orderSubmissionBean.setVehicleEvaluateInfoId(customerDemand.getVehicleEvaluateInfoId());
            }

        }
        return orderSubmissionBean;
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
    public ResultBean<DataPageBean<OrderListBean>> actGetOrders(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize) {
        Page<PurchaseCarOrder> orders = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORDER_LOGINUSERID_ID_NULL"), loginUserId));
        }

        List<String> tids = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if (isPass) {
            orders = this.iOrderService.findCompletedItemsByUser(PurchaseCarOrder.class, loginUserId, tids, pageIndex, pageSize);
            if (orders == null || orders.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_LOGINUSERID_HISTORY_NULL"));
            }
        } else {
            orders = this.iOrderService.findPendingItemsByUser(PurchaseCarOrder.class, loginUserId, tids, pageIndex, pageSize);
            if (orders == null || orders.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ORDER_LOGINUSERID_NULL"));
            }
        }


        DataPageBean<OrderListBean> destination = new DataPageBean<OrderListBean>();
        destination.setPageSize(orders.getSize());
        destination.setTotalCount(orders.getTotalElements());
        destination.setTotalPages(orders.getTotalPages());
        destination.setCurrentPage(orders.getNumber());
        destination.setResult(this.getOrderList(orders.getContent()));
        return ResultBean.getSucceed().setD(destination);
    }


    private List<OrderListBean> getOrderList(List<PurchaseCarOrder> orders) {
        List<OrderListBean> result = new ArrayList<OrderListBean>();
        for (PurchaseCarOrder purchaseCarOrder : orders) {
            OrderListBean orderListBean = mappingService.map(purchaseCarOrder, OrderListBean.class);

            CustomerTransactionBean ct = iCustomerTransactionBizService.actFindCustomerTransactionById(purchaseCarOrder.getCustomerTransactionId()).getD();

            //客户信息
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(purchaseCarOrder.getCustomerId()).getD();

            //车辆信息
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();

            //借款信息
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();

            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();

            TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(mappingService.map(ct, CustomerTransactionBean.class),
                    customerBean,
                    customerCarBean,
                    customerLoanBean,
                    carDealerBean);
            transactionSummary.setApproveStatus(purchaseCarOrder.getApproveStatus());
            orderListBean.setTransactionSummary(transactionSummary);

            result.add(orderListBean);
        }

        return result;
    }

    /**
     * 日报
     *
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId,String date, PurchaseCarOrderBean t) {
        Map<Object, Object> dailyReport = iOrderService.getDailyReport(orgId ,date, mappingService.map(t, PurchaseCarOrder.class));
        if(dailyReport != null){
            return ResultBean.getSucceed().setD(dailyReport);
        }
        return ResultBean.getFailed();
    }

    //  Pad主页专用
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId,String date) {
        Map<Object, Object> dailyReport = iOrderService.getDailyReport(orgId ,date,new PurchaseCarOrder(),null);
        if(dailyReport != null){
            return ResultBean.getSucceed().setD(dailyReport);
        }
        return ResultBean.getFailed();
    }

    //  作废
    @Override
    public ResultBean<Map<Object, Object>> getChannelReport(String date, PurchaseCarOrderBean t, String loginUserId) {
        String orginfoId = iOrgBizService.actFindEmployeeByLoginUserId(loginUserId).getD().getOrgInfoId();

        PurchaseCarOrder map = mappingService.map(t, PurchaseCarOrder.class);
        Map<Object, Object> dealerReport = iOrderService.getCarDealerReport(orginfoId, date, map);
        if (dealerReport != null) {
            return ResultBean.getSucceed().setD(dealerReport);
        } else {
            return ResultBean.getFailed();
        }
    }

    @Override
    public ResultBean<Map<Object, Object>> getEmployeeReport(String date, PurchaseCarOrderBean t, String employeeId) {
        PurchaseCarOrder map = mappingService.map(t, PurchaseCarOrder.class);
        Map<Object, Object> dealerReport = iOrderService.getEmployeeReport(employeeId,date, map);
        if (dealerReport != null) {
            return ResultBean.getSucceed().setD(dealerReport);
        } else {
            return ResultBean.getFailed();
        }
    }


    @Override
    public ResultBean<List<Map>> getAllCustomerByApproveStatus(Integer as) {
        List<PurchaseCarOrder> items = null;
        if (as == ApproveStatus.APPROVE_INIT) {
            items = iOrderService.findAll(PurchaseCarOrder.getTsSort());
        } else {
            items = iOrderService.findAllByDataStatusAndApproveStatus(DataStatus.SAVE, as, PurchaseCarOrder.getTsSort());
        }
        return convertCustomerMapList(items);
    }

    private ResultBean<List<Map>> convertCustomerMapList(List<PurchaseCarOrder> items) {
        List<Map> resultList = new ArrayList<Map>();
        for (PurchaseCarOrder t : items) {
            CustomerTransactionBean customerTransactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(t.getCustomerTransactionId()).getD();
            if (customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING || customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED) {
                continue;
            }
            Map dataMap = new HashMap();
            CustomerBean customer = iCustomerBizService.actGetCustomerById(customerTransactionBean.getCustomerId()).getD();
            dataMap.put("customerName", customer != null ? customer.getName() : t.getCustomerId());
            dataMap.put("orderTime", t.getTs());
            dataMap.put("days", SimpleUtils.daysBetween(t.getTs(), SimpleUtils.getCreateTime()));
            dataMap.put("customerId", t.getCustomerId());
            dataMap.put("id", t.getId());
            dataMap.put("businessTypeName", t.getBusinessTypeCode());
            dataMap.put("employeeName", iOrgBizService.actGetEmployee(customerTransactionBean.getEmployeeId()).getD().getUsername());
            resultList.add(dataMap);
        }
        return ResultBean.getSucceed().setD(resultList);
    }


    @Override
    public ResultBean<List<CustomerLoanBean>> findAllCustomerLoan() {
        List<CustomerLoanBean> customerLoan = iCustomerBizService.actFindAllCustomerLoan().getD();
        if (customerLoan != null) {
            return ResultBean.getSucceed().setD(customerLoan);
        }
        return ResultBean.getFailed();
    }

    /**
     * 资质审查、客户签约数据对比
     *
     * @param propname
     * @param transactionId
     * @return 0 ：  不变或相等 -1 ： 数值变小  1 ： 数值变大 2 : 变化
     */
    @Override
    public ResultBean<Map> actCompareData(String propname, String transactionId) {
        //  资质信息
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(transactionId);
        CarDealerBean customerDemandCarDealerBean = iCarDealerBizService.actGetOneCarDealer(customerDemand.getCarDealerId()).getD();
        CustomerCarBean customerDemandCustomerCarBean = iCustomerBizService.actGetCustomerCarById(customerDemand.getCustomerCarId()).getD();
        CustomerLoanBean customerDemandCustomerLoanBean = iCustomerBizService.actGetCustomerLoanById(customerDemand.getCustomerLoanId()).getD();
        //  签约信息
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        CarDealerBean purchaseCarOrderCarDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
        CustomerCarBean purchaseCarOrderCustomerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
        CustomerLoanBean purchaseCarOrderCustomerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        Integer result = 0;
        Double difference = 0.0;
        if ("cardealerId".equals(propname) &&  customerDemand != null && !customerDemand.getCarDealerId().equals(purchaseCarOrder.getCarDealerId())) {
            result = 2;
        } else if ("carTypeId".equals(propname) && customerDemandCustomerCarBean != null && !customerDemandCustomerCarBean.getCarTypeId().equals(purchaseCarOrderCustomerCarBean.getCarTypeId())) {
            result = 2;
        } else if ("applyAmount".equals(propname)) {
            difference = purchaseCarOrderCustomerLoanBean.getApplyAmount() - customerDemandCustomerLoanBean.getApplyAmount();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("downPaymentRatio".equals(propname)) {
            difference = Double.parseDouble(NumberHelper.format((purchaseCarOrderCustomerLoanBean.getDownPaymentRatio() - customerDemandCustomerLoanBean.getDownPaymentRatio())));
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("creditAmount".equals(propname)) {
            difference =  purchaseCarOrderCustomerLoanBean.getCreditAmount() - customerDemandCustomerLoanBean.getCreditAmount();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("chargePaymentWay".equals(propname) && !customerDemandCustomerLoanBean.getChargePaymentWay().equals(purchaseCarOrderCustomerLoanBean.getChargePaymentWay())) {
            result = 2;
        } else if ("months".equals(propname)) {
            difference =  NumberHelper.toDouble(purchaseCarOrderCustomerLoanBean.getRateType().getMonths() - customerDemandCustomerLoanBean.getRateType().getMonths(),0);
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("loanServiceFee".equals(propname)) {
            difference = purchaseCarOrderCustomerLoanBean.getLoanServiceFee() - customerDemandCustomerLoanBean.getLoanServiceFee();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }
        Map map = new HashMap();
        map.put("result",result);
        map.put("difference",difference);
        return ResultBean.getSucceed().setD(map);
    }

    //  客户签约通过，发送档案资料--批贷函
    @Override
    public void actSendPurchaseCarOrderDocument(String id) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.getAvailableOne(id);
        if (purchaseCarOrder.getApproveStatus()!=ApproveStatus.APPROVE_PASSED){
            logger.error(String.format(messageService.getMessage("MSG_ORDER_NOT_PASS"), id));
            return;
        }
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
        String paramName = null;
        //   判断业务是否抵贷不一
        if (!StringUtils.isEmpty(customerDemand.getPledgeCustomerId()) && !customerDemand.getCreditMasterId().equals(customerDemand.getPledgeCustomerId())) {  //抵贷不一
            paramName = "PURCHASECARORDER_DOCUMENT_CODE_OTHER";
        } else {  //抵贷一致
            paramName = "PURCHASECARORDER_DOCUMENT_CODE_SELF";
        }
        String documentCode = iParamBizService.actGetString(paramName).getD();
        if (!StringUtils.isEmpty(documentCode)) {
            List<String> contractCodes = new ArrayList<>();
            contractCodes.add(documentCode);
            iOrderService.sendImagesAndContractsToEmployee(purchaseCarOrder, new ArrayList<String>(), contractCodes);
        } else {
            logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"), "PURCHASECARORDER_DOCUMENT_CODE"));
            return;
        }
    }

    /**
     * 签约提交数据校验
     * @param purchaseCarOrder
     * @return
     */
    private ResultBean<PurchaseCarOrderBean> submitOrderCheckData(PurchaseCarOrder purchaseCarOrder) {

        ResultBean resultBean = ResultBean.getFailed();
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        if(customerLoanBean != null){
            //贴息手续费不能分期
            if(customerLoanBean.getCompensatoryInterest() == 1){
                if(customerLoanBean.getChargePaymentWay().equals("STAGES")){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_TXNOSTAGES"));
                }
            }
            //不限定经营品牌不能选择贴息
            CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(purchaseCarOrder.getCarDealerId()).getD();
            //默认贴息金额
            if(carDealerBean != null && carDealerBean.getBrandIsLimit() == 0){//不限定经营品牌
                if(customerLoanBean.getCompensatoryInterest() == 1){//贴息
                    resultBean.addL(messageService.getMessage("MSG_ORDER_BRANDIDLIMIT"));
                }
            }
            //贷款金额与首付金额相加不等车辆价格的最小值
            Double applyAmount = customerLoanBean.getCreditAmount() + customerLoanBean.getDownPayment();
            if( !applyAmount.equals(customerLoanBean.getApplyAmount()) ){
                resultBean.addL(messageService.getMessage("MSG_ORDER_PRICEERROR"));
            }
            //贷款比例与首付比例相加不等100%
            if(customerLoanBean.getCreditRatio() + customerLoanBean.getDownPaymentRatio() != 100){
                resultBean.addL(messageService.getMessage("MSG_ORDER_RATIOERROR"));
            }
            //贷款金额不能低于银行贷款额度30000
            if(customerLoanBean.getCreditAmount() < 30000){
                resultBean.addL(messageService.getMessage("MSG_ORDER_CREDITAMOUNTERROR"));
            }
            if(purchaseCarOrder.getBusinessTypeCode().equals("NC")){
                //新车分期申请价格不能低于37500.00元
                if(customerLoanBean.getApplyAmount() < 37500){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_NCAPPLYAMOUNTERROR"));
                }
                //新车贷款比例不能高于80%
                if(customerLoanBean.getCreditRatio() > 80){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_NCCREDITRATIOERROR"));
                }
                //新车首付比例不能低于20%
                if(customerLoanBean.getDownPaymentRatio() < 20){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_NCDOWNPAYMENTERROR"));
                }
            }
            if(purchaseCarOrder.getBusinessTypeCode().equals("OC")){
                //二手车分期申请价格不能低于" +  42900.00+ "元
                if(customerLoanBean.getApplyAmount() < 37500){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_OCAPPLYAMOUNTERROR"));
                }
                //二手车贷款比例不能高于70%
                if(customerLoanBean.getCreditRatio() > 70){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_OCCREDITRATIOERROR"));
                }
                //二手车首付比例不能低于30%
                if(customerLoanBean.getDownPaymentRatio() < 30){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_OCDOWNPAYMENTERROR"));
                }
            }
            //银行手续费率不能低于2%
            if(customerLoanBean.getRateType().getRatio() < 0.02){
                resultBean.addL(messageService.getMessage("MSG_ORDER_RATIOLOWERROR"));
            }
            //贷款期数不能低于12期
            if(customerLoanBean.getRateType().getMonths() < 12){
                resultBean.addL(messageService.getMessage("MSG_ORDER_MONTHSERROR"));
            }
            //银行手续费必须等于银行手续费率*贷款金额！
            //Double bankFeeMoney = customerLoanBean.getRateType().getRatio() * customerLoanBean.getCreditAmount();
            BigDecimal ratio = new BigDecimal(Double.toString(customerLoanBean.getRateType().getRatio()));
            BigDecimal creditAmount = new BigDecimal(Double.toString(customerLoanBean.getCreditAmount()));
            Double bankFeeMoney = ratio.multiply(creditAmount).doubleValue();
            if(!bankFeeMoney .equals(customerLoanBean.getBankFeeAmount()) ){
                resultBean.addL(messageService.getMessage("MSG_ORDER_BANKFEEAMOUNTERROR"));
            }
            //银行手续费率不能高于100%
            if(customerLoanBean.getRateType().getRatio() > 1){
                resultBean.addL(messageService.getMessage("MSG_ORDER_RATIOHIGHERROR"));
            }
        }

        if(resultBean.getL().size() > 0){
            return resultBean;
        }

        return ResultBean.getSucceed().setD(mappingService.map(purchaseCarOrder,PurchaseCarOrderBean.class));
    }

    /**
     * 签约提交数据校验
     * @param purchaseCarOrder
     * @return
     */
    private ResultBean<PurchaseCarOrderBean> submitOrderCheckNull(PurchaseCarOrder purchaseCarOrder){
        ResultBean resultBean = ResultBean.getFailed();
        if(purchaseCarOrder.getBusinessTypeCode().equals("OC")){
            if(purchaseCarOrder.getVehicleEvaluateInfoId() == null){//评估信息
                resultBean.addL(messageService.getMessage("MSG_ORDER_VEHICLEEVALUATEINFOIDNULL"));
            }
        }
        if(purchaseCarOrder.getPickCarDate() == null || purchaseCarOrder.getPickCarDate().equals("")){//提车日期
            resultBean.addL(messageService.getMessage("MSG_ORDER_PICKCARDATENULL"));
        }
        if(purchaseCarOrder.getIndicatorStatus() != null && purchaseCarOrder.getIndicatorStatus() != 1 ){//不是现标需要校验
            if(purchaseCarOrder.getRetrieveDate() == null || purchaseCarOrder.getRetrieveDate().equals("")){//指标获取时间
                resultBean.addL(messageService.getMessage("MSG_ORDER_RETRIEVEDATENULL"));
            }
            if(purchaseCarOrder.getIndicatorComment() == null || purchaseCarOrder.getIndicatorComment().equals("")){//情况说明
                resultBean.addL(messageService.getMessage("MSG_ORDER_INDICATORCOMMENTNULL"));
            }
            if(purchaseCarOrder.getIndicatorStatus() == 2){//外迁
                if(purchaseCarOrder.getMoveOutDate() == null || purchaseCarOrder.getMoveOutDate().equals("")){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_MOVEOUTSTATUSNULL2"));
                }
            }
            if(purchaseCarOrder.getIndicatorStatus() == 3){//报废
                if(purchaseCarOrder.getMoveOutDate() == null || purchaseCarOrder.getMoveOutDate().equals("")){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_MOVEOUTSTATUSNULL3"));
                }
            }
            if(purchaseCarOrder.getIndicatorStatus() == 4){//本地置换
                if(purchaseCarOrder.getMoveOutDate() == null || purchaseCarOrder.getMoveOutDate().equals("")){
                    resultBean.addL(messageService.getMessage("MSG_ORDER_MOVEOUTSTATUSNULL4"));
                }
            }
        }
        //客户信息校验
        if(purchaseCarOrder.getCustomerId() == null){
            resultBean.addL(messageService.getMessage("MSG_ORDER_CUSTOMERIDNULL"));
        }else{
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(purchaseCarOrder.getCustomerId()).getD();
            if(customerBean != null){
                if (customerBean.getContactAddress() == null || "".equals(customerBean.getContactAddress())) { // 家庭住址
                    resultBean.addL(messageService.getMessage("MSG_ORDER_CONTACTADDRESSNULL"));
                }
                if(customerBean.getCustomerJob() != null){
                    if (customerBean.getCustomerJob().getCompanyAddress() == null || "".equals(customerBean.getCustomerJob().getCompanyAddress())) { // 地址
                        resultBean.addL(messageService.getMessage("MSG_ORDER_COMPANYADDRESSNULL"));
                    }
                    if (customerBean.getCustomerJob().getJob() == null || "".equals(customerBean.getCustomerJob().getJob())) { // 职务
                        resultBean.addL(messageService.getMessage("MSG_ORDER_JOBNULL"));
                    }
                    if (customerBean.getCustomerJob().getHrCell() == null || "".equals(customerBean.getCustomerJob().getHrCell())) { // 电话
                        resultBean.addL(messageService.getMessage("MSG_ORDER_HRCELLNULL"));
                    }
                    if(purchaseCarOrder.getPrintNeedEarningProof() == 1){// 打印收入证明
                        if(customerBean.getCustomerJob().getHrName() == null || customerBean.getCustomerJob().getHrName().equals("")){//人事负责人
                            resultBean.addL(messageService.getMessage("MSG_ORDER_HRNAMENULL"));
                        }
                        if(customerBean.getCustomerJob().getWorkDate() == null || customerBean.getCustomerJob().getWorkDate() == 0){//工作年限
                            resultBean.addL(messageService.getMessage("MSG_ORDER_WORKDATENULL"));
                        }
                        if(customerBean.getCustomerJob().getSalary() == null || customerBean.getCustomerJob().getSalary() == 0.00){//年收入
                            resultBean.addL(messageService.getMessage("MSG_ORDER_SALARYNULL"));
                        }
                    }
                }else{
                    resultBean.addL(messageService.getMessage("MSG_ORDER_COMPANYSNULL"));
                }
            }
        }
        //车辆信息校验
        if(purchaseCarOrder.getCustomerCarId() == null){//车辆id
            resultBean.addL(messageService.getMessage("MSG_ORDER_CUSTOMERCARNULL"));
        }else{
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
            if(customerCarBean != null){
                if(customerCarBean.getCarTypeId() == null){//车型
                    resultBean.addL(messageService.getMessage("MSG_ORDER_CUSTOMERCARNULL"));
                }

                if(customerCarBean.getParallelImport() != null && customerCarBean.getParallelImport().equals("1")){
                    if(customerCarBean.getConfigures() == null || customerCarBean.getConfigures().equals("")){//选择了平行进口车必须要添加配置说明
                        resultBean.addL(messageService.getMessage("MSG_ORDER_CONFIGURESNULL"));
                    }
                }
                if(purchaseCarOrder.getBusinessTypeCode().equals("OC")){
                    if(customerCarBean.getTransferCount() == null || customerCarBean.getTransferCount().equals("")){//过户次数
                        resultBean.addL(messageService.getMessage("MSG_ORDER_TRANSFERCOUNTNULL"));
                    }
                    if(customerCarBean.getLicenseNumber() == null || customerCarBean.getLicenseNumber().equals("")){//车牌号码
                        resultBean.addL(messageService.getMessage("MSG_ORDER_LICENSENUMBERNULL"));
                    }
                    if(customerCarBean.getVin() == null || customerCarBean.getVin().equals("")){//车架号
                        resultBean.addL(messageService.getMessage("MSG_ORDER_VINNULL"));
                    }
                    if(customerCarBean.getCarModelNumber() == null || customerCarBean.getCarModelNumber().equals("")){//车辆型号
                        resultBean.addL(messageService.getMessage("MSG_ORDER_CARMODELNUMNULL"));
                    }
                    if(customerCarBean.getFirstRegistryDate() == null || customerCarBean.getFirstRegistryDate().equals("")){//首次登记日期
                        resultBean.addL(messageService.getMessage("MSG_ORDER_FIRSTREGISTRYDATENULL"));
                    }
                    if(customerCarBean.getMileage() == null || customerCarBean.getMileage() == 0.00){//行驶里程
                        resultBean.addL(messageService.getMessage("MSG_ORDER_MILEAGENULL"));
                    }
                    if(customerCarBean.getMaintenanceMileage() == null || customerCarBean.getMaintenanceMileage().equals("")){//维保里程
                        resultBean.addL(messageService.getMessage("MSG_ORDER_MAINTENANCEMILEAGENULL"));
                    }
                    if(customerCarBean.getEvaluatePrice() == null || customerCarBean.getEvaluatePrice() == 0.00){//车辆评估价
                        resultBean.addL(messageService.getMessage("MSG_ORDER_EVALUATEPRICENULL"));
                    }
                }
                //车辆价格校验
                if(customerCarBean.getGuidePrice() == null){//官方指导价
                    resultBean.addL(messageService.getMessage("MSG_ORDER_GUIDEPRICENULL"));
                }
            }
        }
        //借款信息校验
        if(purchaseCarOrder.getCustomerLoanId() == null){ //借款信息
            resultBean.addL(messageService.getMessage("MSG_ORDER_CUSTOMERLOANIDNULL"));
        }else{
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            if(customerLoanBean != null){
                if(customerLoanBean.getReceiptPrice() == null || customerLoanBean.getReceiptPrice() == 0.00){//车辆开票价
                    resultBean.addL(messageService.getMessage("MSG_ORDER_RECEIPTPRICENULL"));
                }
                if(customerLoanBean.getRealPrice() == null || customerLoanBean.getRealPrice() == 0.00){//车辆成交价
                    resultBean.addL(messageService.getMessage("MSG_ORDER_REALPRICENULL"));
                }
                if(customerLoanBean.getCreditAmount() == null || customerLoanBean.getCreditAmount() == 0.00){//贷款金额
                    resultBean.addL(messageService.getMessage("MSG_ORDER_CREDITAMOUNTNULL"));
                }
                if(customerLoanBean.getCreditRatio() == null || customerLoanBean.getCreditRatio() == 0.00){//贷款比例
                    resultBean.addL(messageService.getMessage("MSG_ORDER_CREDITRATIONULL"));
                }
                if(customerLoanBean.getDownPayment() == null || customerLoanBean.getDownPayment() == 0.00){//首付金额
                    resultBean.addL(messageService.getMessage("MSG_ORDER_DOWNPAYMENTNULL"));
                }
                if(customerLoanBean.getDownPaymentRatio() == null || customerLoanBean.getDownPaymentRatio() == 0.00){//首付比例
                    resultBean.addL(messageService.getMessage("MSG_ORDER_PAYMENTRATIONULL"));
                }
                if(customerLoanBean.getRateType() != null && customerLoanBean.getRateType().getRatio() == 0.00){//银行手续费率
                    resultBean.addL(messageService.getMessage("MSG_ORDER_RATIONULL"));
                }
                if(customerLoanBean.getBankFeeAmount() == null || customerLoanBean.getBankFeeAmount() == 0.00){//银行手续费
                    resultBean.addL(messageService.getMessage("MSG_ORDER_BANKFEEAMOUNTNULL"));
                }
                if(customerLoanBean.getLoanServiceFee() == null){//贷款服务费
                    resultBean.addL(messageService.getMessage("MSG_ORDER_LOANSERVICEFEENULL"));
                }else if (customerLoanBean.getLoanServiceFee() < 0) {
                    resultBean.addL(messageService.getMessage("MSG_ORDER_LOANSERVICEFEEERROR"));
                }
            }
        }

        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
        if (customerDemand.getNeedCounterGuarantor() == 1) { // 是否需要反担保人
            //反担保信息校验
            if(purchaseCarOrder.getCounterGuarantorId()!= null){
                CustomerBean customerBean = iCustomerBizService.actGetCustomerById(purchaseCarOrder.getCustomerId()).getD();
                CustomerBean counterGuarantor = iCustomerBizService.actGetCustomerById(purchaseCarOrder.getCounterGuarantorId()).getD();
                if(counterGuarantor != null){
                    if(counterGuarantor.getName() == null || counterGuarantor.getName().equals("")){//反担保人姓名
                        resultBean.addL(messageService.getMessage("MSG_ORDER_LOANSERVICEFEENULL"));
                    }
                    if(counterGuarantor.getNationality() == null || counterGuarantor.getNationality().equals("")){//反担保人民族
                        resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTOR_NATIONALITY"));
                    }
                    if(counterGuarantor.getIdentifyNo() == null || counterGuarantor.getIdentifyNo().equals("")){//反担保人身份证号码
                        resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTOR_IDENTIFYNO"));
                    }else{//反担保人身份证号码与贷款人身份证号码不能相同
                        if(counterGuarantor.getIdentifyNo().equals(customerBean.getIdentifyNo())){
                            resultBean.addL(messageService.getMessage("MSG_SAME_IDENTIFYNO_CUSTOMER_COUNTERGUARANTOR"));
                        }
                    }
                    if(counterGuarantor.getAddress() == null || counterGuarantor.getAddress().equals("")){//反担保人身份证住址
                        resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTOR_ADDRESS"));
                    }
                    if(counterGuarantor.getAuthorizeBy() == null || counterGuarantor.getAuthorizeBy().equals("")){//反担保人签发机关
                        resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTOR_AUTHORIZEBY"));
                    }
                    if(counterGuarantor.getIdentifyValid() == null || counterGuarantor.getIdentifyValid().equals("")){//反担保人身份证有效期限
                        resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTOR_IDENTIFYVALID"));
                    }
                    if(counterGuarantor.getCustomerJob() != null){
                        if(counterGuarantor.getCustomerJob().getCompanyName() == null || counterGuarantor.getCustomerJob().getCompanyName().equals("")){//反担保人单位名称
                            resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTOR_JOB_COMPANYNAME"));
                        }
                        if(counterGuarantor.getCustomerJob().getCompanyAddress() == null || counterGuarantor.getCustomerJob().getCompanyAddress().equals("")){//反担保人单位地址
                            resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTOR_JOB_COMPANYADDRESS"));
                        }
                    }

                }
            }else{  // 缺少反担保人信息
                resultBean.addL(messageService.getMessage("MSG_NO_COUNTERGUARANTORNULL"));
            }
        }
        if(resultBean.getL().size() > 0){
            return resultBean;
        }

        return ResultBean.getSucceed().setD(mappingService.map(purchaseCarOrder,PurchaseCarOrderBean.class));
    }

    @Override
    public ResultBean<String> actGetOrderApproveStatus(String transactionId) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        if(purchaseCarOrder.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_NOTPASSED_NOTPAYMENT"));
        }
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<List<FeeValueBean>> actGetAllFeesOnOrder(String transactionId) {
        List<FeeValueBean> feeValueBean = new ArrayList<FeeValueBean>();
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        if(purchaseCarOrder != null){
            List<String> orderFeeCodelist = new ArrayList<String>();
            for (FeeValueBean fee:purchaseCarOrder.getFeeItemList()) {
                orderFeeCodelist.add(fee.getCode());
            }
            feeValueBean = this.getAllFees(purchaseCarOrder.getFeeItemList(), orderFeeCodelist);
        }
        return ResultBean.getSucceed().setD(feeValueBean);
    }

    private List<FeeValueBean> getAllFees(List<FeeValueBean> busList, List orderFeeCodelist){
        List<FeeValueBean> feeValueBean = new ArrayList<FeeValueBean>();
        //系统配置收费项
        List<FeeItemBean> feeItems = iBaseDataBizService.actGetFeeItems().getD();
        for (FeeValueBean fee:busList) {
            for (FeeItemBean feeitem:feeItems) {
                if(fee.getCode().equals(feeitem.getCode())){
                    fee.setName(feeitem.getName());
                }
            }
        }
        feeValueBean.addAll(busList);
        List<String> feeitemcode = new ArrayList<String>();
        for (FeeItemBean feeitem:feeItems) {
            feeitemcode.add(feeitem.getCode());
        }
        for(String item : feeitemcode){
            if(!orderFeeCodelist.contains(item)){
                for (FeeItemBean feeItem:feeItems) {
                    if(item.equals(feeItem.getCode())){
                        FeeValueBean feeValue = new FeeValueBean();
                        feeValue.setCode(feeItem.getCode());
                        feeValue.setName(feeItem.getName());
                        feeValue.setId(feeItem.getId());
                        feeValue.setDataStatus(feeItem.getDataStatus());
                        feeValueBean.add(feeValue);
                    }
                }
            }
        }
        return feeValueBean;
    }

}

