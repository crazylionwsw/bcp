package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.bean.FeeItemBean;
import com.fuze.bcp.api.bd.bean.FeeValueBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentBean;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeBean;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeListBean;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeSubmitBean;
import com.fuze.bcp.api.creditcar.service.IAppointPaymentBizService;
import com.fuze.bcp.api.creditcar.service.IBusinessExchangeBizService;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.NumberHelper;
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Service
public class BizBusinessExchangeService implements IBusinessExchangeBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizBusinessExchangeService.class);

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IBusinessExchangeService iBusinessExchangeService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICarValuationService iCarValuationService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IAppointPaymentBizService iAppointPaymentBizService;

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<BusinessExchangeBean> actGetBusinessExchangeByTransaction(String id) {
        BusinessExchange businessExchange = iBusinessExchangeService.findAvailableOneByCustomerTransactionId(id);
        if(businessExchange != null){
            return ResultBean.getSucceed().setD(mappingService.map(businessExchange,BusinessExchangeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<BusinessExchangeBean> actSearchBusinessExchange(String userId, SearchBean searchBean) {
        Page<BusinessExchange> businessExchanges = iBusinessExchangeService.findAllBySearchBean(BusinessExchange.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(businessExchanges,BusinessExchangeBean.class));
    }

    @Override
    public ResultBean<BusinessExchangeSubmitBean> actSubmitBusinessExchange(BusinessExchangeSubmitBean businessExchangeSubmitBean) {
        if(businessExchangeSubmitBean.getCustomerTransactionId() != null && businessExchangeSubmitBean.getId() == null){
            BusinessExchange availableOne = iBusinessExchangeService.findAvailableOneByCustomerTransactionId(businessExchangeSubmitBean.getCustomerTransactionId());
            if(availableOne != null && availableOne.getApproveStatus() == ApproveStatus.APPROVE_REAPPLY){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_BUSINESSEXCHANGE_REAPPLY_NOTSUBMIT"));
            }
        }
        List<BusinessExchange> businessExchanges = iBusinessExchangeService.getByCustomerTransactionIdAndByApproveStatus(businessExchangeSubmitBean.getCustomerTransactionId(),ApproveStatus.APPROVE_ONGOING);
        if(businessExchanges.size() > 0){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_BUSINESSEXCHANGE_ONGONGING_NOTSUBMIT"));
        }
        BusinessExchange byCustomerTransactionId = iBusinessExchangeService.findByCustomerTransactionId(businessExchangeSubmitBean.getCustomerTransactionId());
        if(byCustomerTransactionId != null){
            if(businessExchangeSubmitBean.getId() != null){
                ResultBean result = iBusinessExchangeService.getEditableBill(businessExchangeSubmitBean.getId());
                if (result.failed()) return result;
            }
        }
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(businessExchangeSubmitBean.getCustomerTransactionId()).getD();

        //判断渠道垫资策略分为贴息与不贴息
        CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(customerTransaction.getCarDealerId()).getD();
        CarDealerBean carDealerBeanEx = iCarDealerBizService.actGetCarDealer(businessExchangeSubmitBean.getCarDealerId()).getD();
        //贴息时
        if(businessExchangeSubmitBean.getCustomerLoan().getCompensatoryAmount() != null && businessExchangeSubmitBean.getCustomerLoan().getCompensatoryAmount() > 0){
           if((carDealerBeanEx.getPaymentPolicy().getDiscount() == 0) != (carDealerBean.getPaymentPolicy().getDiscount() ==0)){
               return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALERPAYMENTPOLICY_NOT"));
           }else if((carDealerBeanEx.getPaymentPolicy().getDiscount() == 1) != (carDealerBean.getPaymentPolicy().getDiscount() ==1)){
               return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALERPAYMENTPOLICY_NOT"));
           }
//                if(carDealerBeanEx.getPaymentPolicy().getDiscount() != 0){
//                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALERPAYMENTPOLICY_NOT"));
//                }
        }else if(businessExchangeSubmitBean.getCustomerLoan().getCompensatoryAmount() == null && businessExchangeSubmitBean.getCustomerLoan().getCompensatoryAmount() == 0){
            if((carDealerBeanEx.getPaymentPolicy().getBusiness() == 0) != (carDealerBean.getPaymentPolicy().getBusiness() ==0)){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALERPAYMENTPOLICY_NOT"));
            }else if((carDealerBeanEx.getPaymentPolicy().getBusiness() == 1) != (carDealerBean.getPaymentPolicy().getBusiness() ==1)){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALERPAYMENTPOLICY_NOT"));
            }
//        } else if(carDealerBean.getPaymentPolicy() != null && carDealerBeanEx.getPaymentPolicy() != null){
//            if(carDealerBean.getPaymentPolicy().getBusiness() !=  carDealerBeanEx.getPaymentPolicy().getBusiness()){
//                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALERPAYMENTPOLICY_NOT"));
//            }
       }

        if(customerTransaction == null){
            return ResultBean.getFailed();
        }

        BusinessExchange businessExchange = mappingService.map(businessExchangeSubmitBean, BusinessExchange.class);
        String exVehicleEvaluateInfoId = businessExchangeSubmitBean.getVehicleEvaluateInfoId();
        if(exVehicleEvaluateInfoId != null){
            businessExchange.setExVehicleEvaluateInfoId(exVehicleEvaluateInfoId);
        }
        if(businessExchangeSubmitBean.getCustomerCar() != null){

            CustomerCarExchangeBean carExchangeBean = mappingService.map(businessExchangeSubmitBean.getCustomerCar(), CustomerCarExchangeBean.class);
            if(exVehicleEvaluateInfoId != null){
                CarValuation carValuation = iCarValuationService.getAvailableOne(exVehicleEvaluateInfoId);
                carExchangeBean.setCarModelNumber(carValuation.getCarModelNumber());
                carExchangeBean.setVin(carValuation.getVin());
                carExchangeBean.setCarTypeId(carValuation.getCarTypeId());
                carExchangeBean.setLicenseNumber(carValuation.getLicenceNumber());
                carExchangeBean.setMaintenanceMileage(carValuation.getMaintenanceMileage());
                carExchangeBean.setMileage(carValuation.getMileage());
                carExchangeBean.setEvaluatePrice(carValuation.getPrice());
            }
            carExchangeBean.setCustomerId(businessExchangeSubmitBean.getCustomerId());
            carExchangeBean.setCustomerTransactionId(businessExchangeSubmitBean.getCustomerTransactionId());
            carExchangeBean.setTs(DateTimeUtils.getCreateTime());
            CustomerCarExchangeBean carExchangeBean1 = iCustomerBizService.actSaveCustomerExchangeCar(carExchangeBean).getD();
            businessExchange.setCustomerExchangeCarId(carExchangeBean1.getId());
        }

        if(businessExchangeSubmitBean.getCustomerLoan() != null){
            CustomerLoanExchangeBean loanExchangeBean = mappingService.map(businessExchangeSubmitBean.getCustomerLoan(), CustomerLoanExchangeBean.class);
            LoanSubmissionBean loanSubmissionBean = businessExchangeSubmitBean.getCustomerLoan();
            if(loanSubmissionBean != null){
                RateType rateType = new RateType();
                rateType.setMonths(loanSubmissionBean.getMonths() == null ? 0 : loanSubmissionBean.getMonths());
                rateType.setRatio(loanSubmissionBean.getRatio() == null ? 0.0 : loanSubmissionBean.getRatio());
                loanExchangeBean.setRateType(rateType);
                loanExchangeBean.setCompensatoryInterest(loanSubmissionBean.getCompensatoryAmount() > 0 ? 1 : 0);
                Double receiptPrice = loanExchangeBean.getReceiptPrice();//车辆开票价格
                Double realPrice = loanExchangeBean.getRealPrice();//预计成交价
                if (customerTransaction.getBusinessTypeCode().equals("NC")) {
                    if(receiptPrice != null && realPrice != null){
                        Double min = (receiptPrice < realPrice) ? receiptPrice : realPrice;
                        loanExchangeBean.setApplyAmount(min);//分期申请车价
                    }else{
                        loanExchangeBean.setApplyAmount(null);//分期申请车价
                    }
                }

                CustomerCarBean car = mappingService.map(businessExchangeSubmitBean.getCustomerCar(), CustomerCarBean.class);
                Double evaluatePrice = null;//二手车评估价
                if (car != null && car.getEvaluatePrice() != null) {
                    evaluatePrice = car.getEvaluatePrice();
                }
                if (customerTransaction.getBusinessTypeCode().equals("OC")) {
                    Double min = (receiptPrice < realPrice) ? receiptPrice : realPrice;
                    min = (min < evaluatePrice) ? min : evaluatePrice;
                    loanExchangeBean.setApplyAmount(min);//分期申请车价
                }
            }

            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerTransaction.getId());
            CustomerLoanBean loanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            if(loanBean.getApprovedCreditAmount() != null){
                loanExchangeBean.setApprovedCreditAmount(loanBean.getApprovedCreditAmount());
            }


            loanExchangeBean.setSwipingAmount(loanExchangeBean.getCreditAmount());
            loanExchangeBean.setCustomerId(businessExchangeSubmitBean.getCustomerId());
            loanExchangeBean.setCustomerTransactionId(businessExchangeSubmitBean.getCustomerTransactionId());
            loanExchangeBean.setTs(DateTimeUtils.getCreateTime());
            CustomerLoanExchangeBean loanExchangeBean1 = iCustomerBizService.actSaveCustomerExchangeLoan(loanExchangeBean).getD();
            businessExchange.setCustomerExchangeLoanId(loanExchangeBean1.getId());
        }

        businessExchange.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
        businessExchange.setLoginUserId(customerTransaction.getLoginUserId());
        businessExchange.setCarDealerId(businessExchangeSubmitBean.getCarDealerId());
        businessExchange.setEmployeeId(customerTransaction.getEmployeeId());
        businessExchange.setExDealerEmployeeId(businessExchangeSubmitBean.getDealerEmployeeId());
        BusinessExchange businessExchange1 = iBusinessExchangeService.save(businessExchange);
        this.startBusinessExchangeFlow(businessExchange1,businessExchangeSubmitBean.getComment());
        return ResultBean.getSucceed().setD(mappingService.map(businessExchange1,BusinessExchangeSubmitBean.class));
    }

    /**
     *启动业务调整流程
     */
    private ResultBean<BusinessExchange> startBusinessExchangeFlow(BusinessExchange businessExchange,String comment){
        SignInfo signInfo = new SignInfo(businessExchange.getLoginUserId(), businessExchange.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //启动工作流并进行提交操作
        String collectionName = null;
        try {
            collectionName = BusinessExchange.getMongoCollection(businessExchange);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(businessExchange.getBusinessTypeCode(), businessExchange.getId(), businessExchange.getBillTypeCode(), signInfo, collectionName, null, businessExchange.getCustomerTransactionId());
        if (resultBean != null) {
            if (resultBean.isSucceed()) {
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    businessExchange = iBusinessExchangeService.getOne(businessExchange.getId());
                } else {
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            } else if (resultBean.failed()) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(businessExchange);
    }

    @Override
    public ResultBean<BusinessExchangeBean> actSignBusinessExchange(String businessExchangeId, SignInfo signInfo) {
        try {
            ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSignBill(businessExchangeId, signInfo);
            if(resultBean.failed()){
                return ResultBean.getFailed().setM(resultBean.getM());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }
        BusinessExchange businessExchange = iBusinessExchangeService.getOne(businessExchangeId);
        if(businessExchange.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
            //变更需要更改的内容
            this.updateBusinessTransactions(businessExchange.getId());
        }
        return ResultBean.getSucceed().setD(mappingService.map(businessExchange,BusinessExchangeBean.class)).setM("MSG_SUCESS_OPERATION");
    }

    public void updateBusinessTransactions(String businessExchangeId){
        BusinessExchange businessExchange = iBusinessExchangeService.getOne(businessExchangeId);
        if(businessExchange != null){
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(businessExchange.getCustomerTransactionId()).getD();
            customerTransaction.setCarDealerId(businessExchange.getCarDealerId());
            CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(businessExchange.getCarDealerId()).getD();
            if(carDealerBean != null){
                customerTransaction.setCashSourceId(carDealerBean.getCashSourceId());
            }

            iCustomerTransactionBizService.actSaveCustomerTransaction(customerTransaction);

            //更新签约信息
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerTransaction.getId());
            if(purchaseCarOrder != null){
                purchaseCarOrder.setCarDealerId(businessExchange.getCarDealerId());
                purchaseCarOrder.setFeeItemList(businessExchange.getFeeItemList());
                if(purchaseCarOrder.getBusinessTypeCode().equals("OC")){
                    purchaseCarOrder.setVehicleEvaluateInfoId(businessExchange.getExVehicleEvaluateInfoId());
                }
                iOrderService.save(purchaseCarOrder);
            }

            //更新借款信息
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoan(customerTransaction.getId()).getD();
            CustomerLoanExchangeBean customerLoanExchangeBean = iCustomerBizService.actGetCustomerExchangeLoan(businessExchange.getCustomerExchangeLoanId()).getD();
            if(customerLoanBean != null && customerLoanExchangeBean != null){
                customerLoanBean.setCashSourceId(carDealerBean.getCashSourceId());
                customerLoanBean.setRealPrice(customerLoanExchangeBean.getRealPrice());
                customerLoanBean.setReceiptPrice(customerLoanExchangeBean.getReceiptPrice());
                customerLoanBean.setApplyAmount(customerLoanExchangeBean.getApplyAmount());
                customerLoanBean.setDownPayment(customerLoanExchangeBean.getDownPayment());
                customerLoanBean.setDownPaymentRatio(customerLoanExchangeBean.getDownPaymentRatio());
                customerLoanBean.setCreditAmount(customerLoanExchangeBean.getCreditAmount());
                customerLoanBean.setCreditRatio(customerLoanExchangeBean.getCreditRatio());
                customerLoanBean.setRateType(customerLoanExchangeBean.getRateType());
                customerLoanBean.setBankFeeAmount(customerLoanExchangeBean.getBankFeeAmount());
                customerLoanBean.setChargePaymentWay(customerLoanExchangeBean.getChargePaymentWay());
                customerLoanBean.setLoanServiceFee(customerLoanExchangeBean.getLoanServiceFee());
                customerLoanBean.setCompensatoryAmount(customerLoanExchangeBean.getCompensatoryAmount());
                customerLoanBean.setCompensatoryInterest(customerLoanExchangeBean.getCompensatoryInterest());
                customerLoanBean.setSwipingAmount(customerLoanExchangeBean.getSwipingAmount());
                iCustomerBizService.actSaveCustomerLoan(customerLoanBean);
            }

            //更新购车信息
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(customerTransaction.getId()).getD();
            CustomerCarExchangeBean customerCarExchangeBean = iCustomerBizService.actGetCustomerExchangeCar(businessExchange.getCustomerExchangeCarId()).getD();
            if(customerCarBean != null && customerCarExchangeBean != null){
                customerCarBean.setCarColor(customerCarExchangeBean.getCarColor());
                customerCarBean.setCarColorName(customerCarExchangeBean.getCarColorName());
                customerCarBean.setMl(customerCarExchangeBean.getMl());
                customerCarBean.setCarModelNumber(customerCarExchangeBean.getCarModelNumber());
                customerCarBean.setCarTypeId(customerCarExchangeBean.getCarTypeId());
                customerCarBean.setConfigures(customerCarExchangeBean.getConfigures());
                customerCarBean.setEvaluatePrice(customerCarExchangeBean.getEvaluatePrice());
                customerCarBean.setGuidePrice(customerCarExchangeBean.getGuidePrice());
                customerCarBean.setLicenseNumber(customerCarExchangeBean.getLicenseNumber());
                customerCarBean.setMotorNumber(customerCarExchangeBean.getMotorNumber());
                customerCarBean.setOperateStatus(customerCarExchangeBean.getOperateStatus());
                customerCarBean.setParallelImport(customerCarExchangeBean.getParallelImport());
                customerCarBean.setVin(customerCarExchangeBean.getVin());
                iCustomerBizService.actSaveCustomerCar(customerCarBean);
            }

            //更新初始垫资信息
            CustomerLoanBean customerLoanBean1 = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            if(customerLoanBean1.getIsNeedPayment() != null && customerLoanBean1.getIsNeedPayment() == 1){
                AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
                if(appointPayment != null){
                    if(!appointPayment.getCarDealerId().equals(purchaseCarOrder.getCarDealerId())){
                        appointPayment.setCarDealerId(businessExchange.getCarDealerId());
                        CarDealerBean carDealerBean1 = iCarDealerBizService.actGetCarDealer(businessExchange.getCarDealerId()).getD();
                        //更新账户信息
                        List<PayAccount> payAccounts = carDealerBean1.getPayAccounts();
                        if (payAccounts != null && !payAccounts.isEmpty()) {
                            for (PayAccount payAccount : payAccounts) {
                                if (payAccount.getDefaultAccount() == PayAccount.ACCOUTCHECK_ALL) {
                                    appointPayment.setPayAccount(payAccount);
                                }
                            }
                        }
                        appointPayment.setChargeParty(carDealerBean1.getPaymentMethod());
                        appointPayment.setAppointPayAmount(iAppointPaymentBizService.actGetAppointPaymentAmount(appointPayment.getCustomerTransactionId(),appointPayment.getChargeParty().toString()).getD());
                        if (purchaseCarOrder.getBusinessTypeCode().equals("OC")) {
                            appointPayment.setAdvancedPay(carDealerBean.getPaymentOldTime());
                        } else {
                            appointPayment.setAdvancedPay(carDealerBean.getPaymentNewTime());
                        }
                        appointPayment.setDataStatus(appointPayment.getDataStatus());
                        appointPayment.setApproveStatus(appointPayment.getApproveStatus());
                        appointPayment.setStatus(appointPayment.getStatus());
                        iAppointPaymentService.save(appointPayment);
                    }
                }
            }

            //更新预约刷卡
            AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
            if(appointSwipingCard != null){
                appointSwipingCard.setAppointPayAmount(customerLoanBean1.getSwipingAmount());
                appointSwipingCard.setDataStatus(appointSwipingCard.getDataStatus());
                appointSwipingCard.setStatus(appointSwipingCard.getStatus());
                appointSwipingCard.setApproveStatus(appointSwipingCard.getApproveStatus());
            }

            //更新卡业务
            PurchaseCarOrder purchaseCarOrder1 = iOrderService.findByCustomerTransactionId(businessExchange.getCustomerTransactionId());
            BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(purchaseCarOrder1.getCustomerTransactionId());
            if(bankCardApply != null){
                CustomerLoanBean loanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder1.getCustomerLoanId()).getD();
                bankCardApply.setStatus(bankCardApply.getStatus());
                bankCardApply.setApproveStatus(bankCardApply.getApproveStatus());
                bankCardApply.setSwipingMoney(loanBean.getSwipingAmount());
                bankCardApply.setSwipingPeriods(loanBean.getRateType().getMonths());
                iBankCardApplyService.save(bankCardApply);
            }

        }
    }

    @Override
    public ResultBean<List<BusinessExchangeListBean>> actGetBusinessExchanges(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize) {
        Page<BusinessExchange> businessExchanges = null;
        if(StringHelper.isBlock(loginUserId)){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BUSINESSEXCHANGE_LOGINUSERID_ID_NULL"),loginUserId));
        }
        List<String> transactionIds = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if(isPass){
            businessExchanges = this.iBusinessExchangeService.findCompletedItemsByUser(BusinessExchange.class, loginUserId, transactionIds, pageIndex, pageSize);
            if(businessExchanges ==null || businessExchanges.getTotalElements() < 0){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_BUSINESSEXCHANGE_LOGINUSERID_HISTORY_NULL"));
            }
        }else {
            businessExchanges = this.iBusinessExchangeService.findPendingItemsByUser(BusinessExchange.class, loginUserId, transactionIds, pageIndex, pageSize);
            if(businessExchanges ==null || businessExchanges.getTotalElements() < 0){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_BUSINESSEXCHANGE_LOGINUSERID_NULL"));
            }
        }

        DataPageBean<BusinessExchangeListBean> destination = new DataPageBean<BusinessExchangeListBean>();
        destination.setPageSize(businessExchanges.getSize());
        destination.setTotalCount(businessExchanges.getTotalElements());
        destination.setTotalPages(businessExchanges.getTotalPages());
        destination.setCurrentPage(businessExchanges.getNumber());
        destination.setResult(this.getBusinessExchangeList(businessExchanges.getContent()));
        return ResultBean.getSucceed().setD(destination);
    }

    private List<BusinessExchangeListBean> getBusinessExchangeList(List<BusinessExchange> businessExchanges){
        List<BusinessExchangeListBean> bList = new ArrayList<BusinessExchangeListBean>();
        for (BusinessExchange businessEx:businessExchanges) {
            BusinessExchangeListBean exchangeListBean = mappingService.map(businessEx, BusinessExchangeListBean.class);
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(businessEx.getCustomerTransactionId()).getD();

            //客户信息
            CustomerBean customer = iCustomerBizService.actGetCustomerById(businessEx.getCustomerId()).getD();
            if(customer != null){
                exchangeListBean.setCustomerName(customer.getName());
                exchangeListBean.setIdentifyNo(customer.getIdentifyNo());
                exchangeListBean.setCell(customer.getCells().get(0));
            }

            CustomerCarExchangeBean customerCarExchangeBean = iCustomerBizService.actGetCustomerExchangeCar(businessEx.getCustomerExchangeCarId()).getD();
            if(customerCarExchangeBean != null){
                exchangeListBean.setCarColor(customerCarExchangeBean.getCarColorName());
                exchangeListBean.setCarEmissions(customerCarExchangeBean.getMl());
                CarTypeBean carTypeBean = iCarTypeBizService.actGetCarTypeById(customerCarExchangeBean.getCarTypeId()).getD();
                if(carTypeBean != null){
                    exchangeListBean.setFullName(carTypeBean.getFullName());
                }
            }

            CustomerLoanExchangeBean customerLoanExchangeBean = iCustomerBizService.actGetCustomerExchangeLoan(businessEx.getCustomerExchangeLoanId()).getD();
            if(customerLoanExchangeBean != null){
                exchangeListBean.setApplyAmount(customerLoanExchangeBean.getApplyAmount());
                exchangeListBean.setDownPayment(customerLoanExchangeBean.getDownPayment());
                exchangeListBean.setDownPaymentRatio(customerLoanExchangeBean.getDownPaymentRatio());
                exchangeListBean.setCreditAmount(customerLoanExchangeBean.getCreditAmount());
                exchangeListBean.setCreditRatio(customerLoanExchangeBean.getCreditRatio());
                exchangeListBean.setMonths(customerLoanExchangeBean.getRateType().getMonths());
                exchangeListBean.setRatio(customerLoanExchangeBean.getRateType().getRatio());
                exchangeListBean.setCompensatoryInterest(customerLoanExchangeBean.getCompensatoryInterest());
            }
            //经销商信息
            CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(businessEx.getCarDealerId()).getD();
            if(carDealer != null){
                exchangeListBean.setCardealerName(carDealer.getName());
                exchangeListBean.setCarDealerAddress(carDealer.getAddress());
            }

            exchangeListBean.setApproveStatus(businessEx.getApproveStatus());
            exchangeListBean.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
            exchangeListBean.setCustomerTransactionId(customerTransaction.getId());
            bList.add(exchangeListBean);
        }
        return bList;
    }

    @Override
    public ResultBean<BusinessExchangeSubmitBean> actGetBusinessExchangeInfo(String businessExchangeId) {
        BusinessExchange businessExchange = iBusinessExchangeService.getAvailableOne(businessExchangeId);
        BusinessExchangeSubmitBean businessExchangeSubmitBean = null;
        if(businessExchange != null){
            businessExchangeSubmitBean = this.getBusinessExchangeInfo(businessExchangeId);
            return ResultBean.getSucceed().setD(businessExchangeSubmitBean);
        }
        return ResultBean.getFailed();
    }

    public BusinessExchangeSubmitBean getBusinessExchangeInfo(String businessExchangeId){
        BusinessExchangeSubmitBean businessExchangeSubmitBean = new BusinessExchangeSubmitBean();
        BusinessExchange businessExchange = iBusinessExchangeService.getAvailableOne(businessExchangeId);
        if(businessExchange != null){
            businessExchangeSubmitBean.setApproveStatus(businessExchange.getApproveStatus());
            businessExchangeSubmitBean.setId(businessExchange.getId());
            businessExchangeSubmitBean.setBusinessTypeCode(businessExchange.getBusinessTypeCode());
            businessExchangeSubmitBean.setCustomerId(businessExchange.getCustomerId());
            businessExchangeSubmitBean.setCustomerTransactionId(businessExchange.getCustomerTransactionId());
            businessExchangeSubmitBean.setDealerEmployeeId(businessExchange.getExDealerEmployeeId());
            businessExchangeSubmitBean.setCarDealerId(businessExchange.getCarDealerId());
            //获取最新收费项
            businessExchangeSubmitBean.setFeeItemList(this.getNewBusinessExchangeFees(businessExchange));
            PadCustomerCarBean customerCarExchangeInfo = this.getCustomerCarExchangeInfo(businessExchange.getCustomerExchangeCarId());
            businessExchangeSubmitBean.setCustomerCar(customerCarExchangeInfo);
            LoanSubmissionBean customerLoanExchangeInfo = this.getCustomerLoanExchangeInfo(businessExchange.getCustomerExchangeLoanId());
            businessExchangeSubmitBean.setCustomerLoan(customerLoanExchangeInfo);
            businessExchangeSubmitBean.setVehicleEvaluateInfoId(businessExchange.getExVehicleEvaluateInfoId());
        }
        return businessExchangeSubmitBean;
    }

    private List<FeeValueBean> getNewBusinessExchangeFees(BusinessExchange businessExchange){
        List<String> exFeeCodeList = new ArrayList<String>();
        for (FeeValueBean fee:businessExchange.getFeeItemList()) {
            exFeeCodeList.add(fee.getCode());
        }
        List<FeeValueBean> feeValueBeen = this.getAllFees(businessExchange.getFeeItemList(), exFeeCodeList);
        return feeValueBeen;
    }

    public PadCustomerCarBean getCustomerCarExchangeInfo(String customerExchangeCarId){
        PadCustomerCarBean padCustomerCarBean  = new PadCustomerCarBean();
        CustomerCarExchangeBean exchangeBean = iCustomerBizService.actGetCustomerExchangeCar(customerExchangeCarId).getD();
        padCustomerCarBean.setId(exchangeBean.getId());
        padCustomerCarBean.setCarModelNumber(exchangeBean.getCarModelNumber());
        padCustomerCarBean.setRegistryNumber(exchangeBean.getRegistryNumber());
        padCustomerCarBean.setCarColor(exchangeBean.getCarColor());
        padCustomerCarBean.setCarColorName(exchangeBean.getCarColorName());
        padCustomerCarBean.setConfigures(exchangeBean.getConfigures());
        padCustomerCarBean.setEvaluatePrice(exchangeBean.getEvaluatePrice());
        padCustomerCarBean.setGuidePrice(exchangeBean.getGuidePrice());
        padCustomerCarBean.setVin(exchangeBean.getVin());
        padCustomerCarBean.setMotorNumber(exchangeBean.getMotorNumber());
        padCustomerCarBean.setOperateStatus(exchangeBean.getOperateStatus());
        padCustomerCarBean.setCarTypeId(exchangeBean.getCarTypeId());
        padCustomerCarBean.setMl(exchangeBean.getMl());
        padCustomerCarBean.setTransferCount(exchangeBean.getTransferCount());
        padCustomerCarBean.setParallelImport(exchangeBean.getParallelImport());
        return padCustomerCarBean;
    }

    public LoanSubmissionBean getCustomerLoanExchangeInfo(String customerExchangeLoanId){
        LoanSubmissionBean loanSubmissionBean = new LoanSubmissionBean();
        CustomerLoanExchangeBean loanExchangeBean = iCustomerBizService.actGetCustomerExchangeLoan(customerExchangeLoanId).getD();
        loanSubmissionBean.setId(loanExchangeBean.getId());
        loanSubmissionBean.setBankFeeAmount(loanExchangeBean.getBankFeeAmount());
        loanSubmissionBean.setReceiptPrice(loanExchangeBean.getReceiptPrice());
        loanSubmissionBean.setChargePaymentWay(loanExchangeBean.getChargePaymentWay());
        loanSubmissionBean.setLoanServiceFee(loanExchangeBean.getLoanServiceFee());
        loanSubmissionBean.setDownPayment(loanExchangeBean.getDownPayment());
        loanSubmissionBean.setDownPaymentRatio(loanExchangeBean.getDownPaymentRatio());
        loanSubmissionBean.setRealPrice(loanExchangeBean.getRealPrice());
        loanSubmissionBean.setMonths(loanExchangeBean.getRateType().getMonths());
        loanSubmissionBean.setRatio(loanExchangeBean.getRateType().getRatio());
        loanSubmissionBean.setCreditAmount(loanExchangeBean.getCreditAmount());
        loanSubmissionBean.setCreditRatio(loanExchangeBean.getCreditRatio());
        loanSubmissionBean.setCompensatoryAmount(loanExchangeBean.getCompensatoryAmount());
        return loanSubmissionBean;
    }

    @Override
    public ResultBean<BusinessExchangeBean> actGetBusinessExchange(String businessExchangeId) {
        BusinessExchange businessExchange = iBusinessExchangeService.getAvailableOne(businessExchangeId);
        String code = businessExchange.getBillTypeCode();
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
        BusinessExchangeBean businessExchangeBean = mappingService.map(businessExchange, BusinessExchangeBean.class);
        businessExchangeBean.setBillType(billType);
        return ResultBean.getSucceed().setD(businessExchangeBean);
    }

    @Override
    public ResultBean<Map> actCompareData(String propname, String transactionId,String businessExchangeId) {
        //签约信息
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        CarDealerBean orderCarDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
        CustomerCarBean orderCustomerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
        CustomerLoanBean orderCustomerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();

        //调整单信息
        BusinessExchange businessExchange = iBusinessExchangeService.getAvailableOne(businessExchangeId);
        CarDealerBean exCarDealerBean = iCarDealerBizService.actGetOneCarDealer(businessExchange.getCarDealerId()).getD();
        CustomerCarExchangeBean customerCarExchangeBean = iCustomerBizService.actGetCustomerExchangeCar(businessExchange.getCustomerExchangeCarId()).getD();
        CustomerLoanExchangeBean customerLoanExchangeBean = iCustomerBizService.actGetCustomerExchangeLoan(businessExchange.getCustomerExchangeLoanId()).getD();

        List<String> codeList = new ArrayList<String>();
        List<FeeItemBean> itemList = iBaseDataBizService.actGetFeeItems().getD();
        for (FeeItemBean feeitem:itemList) {
            codeList.add(feeitem.getCode());
        }

        Integer result = 0;
        Double difference = 0.0;
        if ("cardealerId".equals(propname) &&  purchaseCarOrder != null && !purchaseCarOrder.getCarDealerId().equals(businessExchange.getCarDealerId())) {
            result = 2;
        } else if ("carTypeId".equals(propname) && orderCustomerCarBean != null && !orderCustomerCarBean.getCarTypeId().equals(customerCarExchangeBean.getCarTypeId())) {
            result = 2;
        } else if ("applyAmount".equals(propname)) {
            difference = customerLoanExchangeBean.getApplyAmount() - orderCustomerLoanBean.getApplyAmount();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("downPaymentRatio".equals(propname)) {
            difference = Double.parseDouble(NumberHelper.format((customerLoanExchangeBean.getDownPaymentRatio() - orderCustomerLoanBean.getDownPaymentRatio())));
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("creditAmount".equals(propname)) {
            difference =  customerLoanExchangeBean.getCreditAmount() - orderCustomerLoanBean.getCreditAmount();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("chargePaymentWay".equals(propname) && !orderCustomerLoanBean.getChargePaymentWay().equals(customerLoanExchangeBean.getChargePaymentWay())) {
            result = 2;
        } else if ("months".equals(propname)) {
            difference =  NumberHelper.toDouble(customerLoanExchangeBean.getRateType().getMonths() - orderCustomerLoanBean.getRateType().getMonths(),0);
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        } else if ("loanServiceFee".equals(propname)) {
            difference = customerLoanExchangeBean.getLoanServiceFee() - orderCustomerLoanBean.getLoanServiceFee();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if("cashSourceId".equals(propname) && !orderCarDealerBean.getCashSourceId().equals(exCarDealerBean.getCashSourceId())){
            result = 2;
        }else if("cooperationCashSourceId".equals(propname) && !orderCarDealerBean.getCooperationCashSourceId().equals(exCarDealerBean.getCooperationCashSourceId())){
            result = 2;
        }else if ("receiptPrice".equals(propname)){ //车辆开票价
            difference = customerLoanExchangeBean.getReceiptPrice() - orderCustomerLoanBean.getReceiptPrice();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if ("creditRatio".equals(propname)){ //贷款比例
            difference = customerLoanExchangeBean.getCreditRatio() - orderCustomerLoanBean.getCreditRatio();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if ("ratio".equals(propname)){
            difference = customerLoanExchangeBean.getRateType().getRatio() - orderCustomerLoanBean.getRateType().getRatio();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if ("downPayment" .equals(propname)){ //首付金额
            difference = customerLoanExchangeBean.getDownPayment() - orderCustomerLoanBean.getDownPayment();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if ("bankFeeAmount" .equals(propname)){
            difference = customerLoanExchangeBean.getBankFeeAmount() - orderCustomerLoanBean.getBankFeeAmount();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if("compensatoryInterest".equals(propname) && !orderCustomerLoanBean.getCompensatoryInterest().equals(customerLoanExchangeBean.getCompensatoryInterest())) {
            result = 2;
        }else if ("compensatoryAmount" .equals(propname)){
            difference = customerLoanExchangeBean.getCompensatoryAmount() - orderCustomerLoanBean.getCompensatoryAmount();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if ("vin".equals(propname)){
            CarValuation carValuation = iCarValuationService.findAvailableOneByVin(propname);
            CarValuation carValuation1 = iCarValuationService.getAvailableOne(purchaseCarOrder.getVehicleEvaluateInfoId());
            difference = carValuation.getPrice() - carValuation1.getPrice();
            if (difference > 0) {
                result = 1;
            } else if (difference < 0) {
                result = -1;
            }
        }else if(codeList.contains(propname)){
            List<FeeValueBean> feeItems = purchaseCarOrder.getFeeItemList();
            List<FeeValueBean> feeValues = businessExchange.getFeeItemList();
            Double orderFee = null;
            for (FeeValueBean feeOrder:feeItems) {
                if(propname.equals(feeOrder.getCode())){
                    orderFee = feeOrder.getFee();
                    break;
                }
            }

            Double exchangeFee = null;
            for (FeeValueBean feeExchange:feeValues) {
                if(propname.equals(feeExchange.getCode())){
                    exchangeFee = feeExchange.getFee();
                    break;
                }
            }

            difference = exchangeFee - orderFee;
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

    @Override
    public ResultBean<List<FeeValueBean>> actGetALLFeesOnBusinessExchange(String transactionId) {
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

    private List<FeeValueBean> getAllFees(List<FeeValueBean> busList, List feeCodeList){
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
            if(!feeCodeList.contains(item)){
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
