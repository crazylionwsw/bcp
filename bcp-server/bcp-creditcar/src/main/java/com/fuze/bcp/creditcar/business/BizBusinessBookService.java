package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.businessbook.BusinessBookBean;
import com.fuze.bcp.api.creditcar.bean.businessbook.BusinessBookExcelBean;
import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationRecord;
import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationResult;
import com.fuze.bcp.api.creditcar.service.IBusinessBookBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.TemplateService;
import com.fuze.bcp.utils.AmountUtil;
import com.fuze.bcp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ${Liu} on 2017/12/18.
 */
@Service
public class BizBusinessBookService implements IBusinessBookBizService{

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IBankCardApplyService iBankCardApplyService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IAppointPaymentService iAppointPaymentServcie;

    @Autowired
    ISwipingCardService iSwipingCardService;

    @Autowired
    IDmvpledgeService iDmvpledgeService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    ICancelOrderService iCancelOrderService;

    @Autowired
    IDeclarationHistorysService iDeclarationHistorysService;

    @Autowired
    ICreditPhotographService iCreditPhotographService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    TemplateService templateService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Override
    public ResultBean<List<BusinessBookBean>> actGetBusinessBooks(String selectTime) {
        List<BusinessBookBean> businessBookList = new ArrayList<BusinessBookBean>();
        List<CustomerTransactionBean> customerTransactions = iCustomerTransactionBizService.actGetListsBySomeConditions(null, selectTime,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(),"ts", true).getD();;
        for(int i = 0;i<customerTransactions.size();i++){
            BusinessBookBean moreTransactionInfo = this.getMoreTransactionInfo(customerTransactions.get(i),i);
            businessBookList.add(moreTransactionInfo);
        }
        return ResultBean.getSucceed().setD(mappingService.map(businessBookList,BusinessBookBean.class));
    }

    @Override
    public ResultBean<List<BusinessBookExcelBean>> actExportBusinessBook(String selectTime) {
        List<BusinessBookExcelBean> businessBookExcel = new ArrayList<BusinessBookExcelBean>();
        List<CustomerTransactionBean> customerTransactions = iCustomerTransactionBizService.actGetListsBySomeConditions(null, selectTime,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(),"ts", true).getD();;
        for(int i = 0;i<customerTransactions.size();i++){
            BusinessBookExcelBean moreTransactionInfo = this.getMoreTransactionInfoToExcel(customerTransactions.get(i),i);
            businessBookExcel.add(moreTransactionInfo);
        }
        return ResultBean.getSucceed().setD(mappingService.map(businessBookExcel,BusinessBookExcelBean.class));
    }


    public BusinessBookBean getMoreTransactionInfo(CustomerTransactionBean customerTransaction,int i){
        BusinessBookBean  businessBookBean = new BusinessBookBean();
        businessBookBean.setIndexNumber(i+1);
        //1.客户信息
        CustomerBean customer = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();
        if(customer != null){
            businessBookBean.setCustomerName(customer.getName());
            businessBookBean.setIdentifyNo(customer.getIdentifyNo());
            businessBookBean.setCells(customer.getCells().get(0));
        }

        //2.资质信息
        CustomerDemand customerDemand = iCustomerDemandService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(customerDemand != null){
            if(customerDemand.getPledgeCustomerId() != null){
                CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerDemand.getPledgeCustomerId()).getD();
                businessBookBean.setPledgeCustomerName(customerBean.getName());
            }


        }
        //3.签约信息
        PurchaseCarOrder purchaseCarOrder = iOrderService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(purchaseCarOrder != null && purchaseCarOrder.getApproveStatus() != ApproveStatus.APPROVE_INIT && customerDemand.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
            String carTypeName = this.getCarTypeName(purchaseCarOrder.getCustomerCarId());
            if(carTypeName != null){
                businessBookBean.setCarTypeFullName(carTypeName);
            }
            CustomerLoanBean customerLoan = this.getCustomerLoan(purchaseCarOrder.getCustomerLoanId());
            if(customerLoan != null){
                businessBookBean.setApplyAmount(AmountUtil.getMoneyTwo(customerLoan.getApplyAmount().toString()));//车价
                businessBookBean.setDownPaymentRatio(customerLoan.getDownPaymentRatio().toString()+"%");//首付比例
                businessBookBean.setCreditAmount(AmountUtil.getMoneyNumber(customerLoan.getCreditAmount().toString()));//申请金额
                if(customerLoan.getApprovedCreditAmount()!= null){
                    businessBookBean.setApprovedCreditAmount(AmountUtil.getMoneyNumber(customerLoan.getApprovedCreditAmount().toString()));
                }
                businessBookBean.setMonths(customerLoan.getRateType().getMonths().toString());//期限
            }
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
            if(carDealerBean != null){
                businessBookBean.setCadelarName(carDealerBean.getName());
                EmployeeBean employeeBean = iOrgBizService.actGetEmployee(carDealerBean.getEmployeeId()).getD();
                if(employeeBean != null){
                    businessBookBean.setEmployeeName(employeeBean.getUsername());
                }
            }
            if(customerLoan.getCompensatoryInterest() == 1){  //是否贴息
                businessBookBean.setCompensatoryInterest("是");
            }else if(customerLoan.getCompensatoryInterest() == 0){
                businessBookBean.setCompensatoryInterest("否");
            }
            if(customerLoan.getChargePaymentWay().equals("WHOLE")){
                businessBookBean.setChargePaymentWay("否");
            }else if(customerLoan.getChargePaymentWay().equals("STAGES")){
                businessBookBean.setChargePaymentWay("是");
            }
            if(customerLoan.getBankFeeAmount() != null){
                businessBookBean.setBankFeeAmount(AmountUtil.getAmountTwo(customerLoan.getBankFeeAmount()));//手续费
            }
        }else if(customerDemand != null && purchaseCarOrder == null){
            String carTypeName = this.getCarTypeName(customerDemand.getCustomerCarId());
            if(carTypeName != null){
                businessBookBean.setCarTypeFullName(carTypeName);
            }
            CustomerLoanBean customerLoan = this.getCustomerLoan(customerDemand.getCustomerLoanId());
            if(customerLoan != null){
                businessBookBean.setApplyAmount(AmountUtil.getMoneyTwo(customerLoan.getApplyAmount().toString()));//车价
                businessBookBean.setDownPaymentRatio(customerLoan.getDownPaymentRatio().toString()+"%");//首付比例
                businessBookBean.setCreditAmount(AmountUtil.getMoneyNumber(customerLoan.getCreditAmount().toString()));//申请金额
                businessBookBean.setMonths(customerLoan.getRateType().getMonths().toString());//期限
                if(customerLoan.getBankFeeAmount() != null){
                    businessBookBean.setBankFeeAmount(AmountUtil.getAmountTwo(customerLoan.getBankFeeAmount()));
                }
                if(customerLoan.getChargePaymentWay().equals("WHOLE")){
                    businessBookBean.setChargePaymentWay("否");
                }else if(customerLoan.getChargePaymentWay().equals("STAGES")){
                    businessBookBean.setChargePaymentWay("是");
                }
            }
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(customerDemand.getCarDealerId()).getD();
            if(carDealerBean != null){
                businessBookBean.setCadelarName(carDealerBean.getName());
                EmployeeBean employeeBean = iOrgBizService.actGetEmployee(carDealerBean.getEmployeeId()).getD();
                if(employeeBean != null){
                    businessBookBean.setEmployeeName(employeeBean.getUsername());
                }
            }
        }

        //6.指标人信息
        if(customerDemand != null){
            if(customerDemand.getRelation() != null){
                if(customerDemand.getRelation().equals("0")){
                    businessBookBean.setRelation("本人");
                }else if(customerDemand.getRelation().equals("1")){
                    businessBookBean.setRelation("父子");
                }else if(customerDemand.getRelation().equals("2")){
                    businessBookBean.setRelation("配偶");
                }else if(customerDemand.getRelation().equals("3")){
                    businessBookBean.setRelation("其他");
                }
            }
        }

        //垫资信息
        AppointPayment appointPayment = iAppointPaymentServcie.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(appointPayment != null){
            if(appointPayment.getPayTime() != null){
                businessBookBean.setPayTime(appointPayment.getPayTime());//垫资时间
            }
            if(appointPayment.getAppointPayAmount() != null){
                businessBookBean.setAppointPayAmount(AmountUtil.getAmountTwo(appointPayment.getAppointPayAmount()));//垫资金额
            }
        }

        //客户卡信息
        CustomerCardBean customerCard = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(customerTransaction.getId()).getD();
        if(customerCard != null){
            businessBookBean.setCardNo(customerCard.getCardNo());
        }

        //卡业务信息
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(customerTransaction.getId());
        if(bankCardApply != null){
            if(bankCardApply.getFirstReimbursement() != null){
                businessBookBean.setFirstRepaymentDate(TimeUtil.getTimeTransDay(bankCardApply.getFirstReimbursement()));
            }
            if(bankCardApply.getReceiveTrusteeTime() != null){
                businessBookBean.setReceiveCardTime(TimeUtil.getTimeTransMinutes(bankCardApply.getReceiveTrusteeTime()));//领卡时间
            }else if(bankCardApply.getReceiveDiscountTime() != null){
                businessBookBean.setReceiveCardTime(TimeUtil.getTimeTransMinutes(bankCardApply.getReceiveDiscountTime()));
            }

            if(bankCardApply.getSwipingShopTime() != null){  //渠道刷卡的时间不为空
                businessBookBean.setSwipingAddress("店刷"); //刷卡地点
                businessBookBean.setSwipingCardTime(TimeUtil.getTimeTransSecond(bankCardApply.getSwipingShopTime()));
                businessBookBean.setPayAmount(AmountUtil.getAmountNumber(bankCardApply.getSwipingMoney()));
                businessBookBean.setSwipingAmount(AmountUtil.getMoneyNumber(bankCardApply.getSwipingMoney().toString()));
            }else if(bankCardApply.getSwipingTrusteeTime() != null){ //代刷卡时间不为空
                businessBookBean.setSwipingAddress("银行刷");
                businessBookBean.setSwipingCardTime(TimeUtil.getTimeTransSecond(bankCardApply.getSwipingTrusteeTime()));
                businessBookBean.setPayAmount(AmountUtil.getAmountNumber(bankCardApply.getSwipingMoney()));
                businessBookBean.setSwipingAmount(AmountUtil.getMoneyNumber(bankCardApply.getSwipingMoney().toString()));
            }
        }

        if(purchaseCarOrder != null){
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
            if(customerCarBean != null){
                businessBookBean.setVin(customerCarBean.getVin());
                businessBookBean.setMotorNumber(customerCarBean.getMotorNumber());
                businessBookBean.setLicenseNumber(customerCarBean.getLicenseNumber());
                businessBookBean.setRegistryNumber(customerCarBean.getRegistryNumber());
            }
        }

        //取消业务
        List<CancelOrder> cancelOrders = iCancelOrderService.getCancelOrdersByCustomerTransactionId(customerTransaction.getId());
        if(cancelOrders != null){
            if(cancelOrders.size() > 0){
                CancelOrder cancelOrder = cancelOrders.get(cancelOrders.size()-1);
                businessBookBean.setCancelReason(cancelOrder.getReason());//取消原因
            }

        }

        //报批历史
        DeclarationHistorys declarationHistorys = iDeclarationHistorysService.findOneByCustomerTransactionId(customerTransaction.getId());
        if(declarationHistorys != null){
            DeclarationRecord declarationRecord = declarationHistorys.getHistoryRecords().get(declarationHistorys.getHistoryRecords().size() - 1);
            if(declarationRecord != null){
                businessBookBean.setReportTime(declarationRecord.getEmailTime());//报批时间
            }
            DeclarationResult declarationResult = declarationRecord.getDeclarationResult();
            if( declarationResult != null){
                businessBookBean.setReplyTime(declarationResult.getTs());//批复时间
            }
        }


        //征信报告生成
        CreditPhotograph creditPhotograph = iCreditPhotographService.findByCustomerId(customerTransaction.getCustomerId());
        if(creditPhotograph != null){
            if(creditPhotograph.getSubmitTime() != null){
                businessBookBean.setCreditQueryTime(TimeUtil.getTimeTransSecond(creditPhotograph.getSubmitTime()));//征信查询时间
            }
        }

        if(customerTransaction.getBusinessTypeCode() != null){
            if(customerTransaction.getBusinessTypeCode().equals("OC")){
                businessBookBean.setBusinessType("二手车");
            }else if(customerTransaction.getBusinessTypeCode().equals("NC")){
                businessBookBean.setBusinessType("新车");
            }
        }

        //抵押信息
        DMVPledge dmvPledge = iDmvpledgeService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(dmvPledge != null){
            CustomerBean cr = null;
            if(dmvPledge.getPledgeCustomerId() != null){
                cr = iCustomerBizService.actGetCustomerById(dmvPledge.getPledgeCustomerId()).getD();
                businessBookBean.setPledgeCustomerName(cr.getName());//抵押人
            }
            if(dmvPledge.getPledgeEndTime() != null){
                businessBookBean.setPledgeEndTime(TimeUtil.getTimeTransMinutes(dmvPledge.getPledgeEndTime()));//抵押登记时间
                businessBookBean.setBankCompleteTime(TimeUtil.getTimeTransMinutes(dmvPledge.getPledgeEndTime()));//银行归档时间
            }

        }

        EmployeeBean employeeBusiness = iOrgBizService.actGetEmployee(customerTransaction.getEmployeeId()).getD();
        if(employeeBusiness != null){
            businessBookBean.setBusinessManName(employeeBusiness.getUsername());//分期经理
        }

        if(purchaseCarOrder != null){
            if(purchaseCarOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                if(purchaseCarOrder.getApproveUserId() != null){
                    String approveName = iOrgBizService.actGetNameByLoginUserId(purchaseCarOrder.getApproveUserId()).getD();
                    businessBookBean.setReviewName(approveName);//审查人
                }
            }
        }

        businessBookBean.setCommentInfo("");

        return businessBookBean;
    }


    /**
     *Excel导出使用
     */
    public BusinessBookExcelBean getMoreTransactionInfoToExcel(CustomerTransactionBean customerTransaction,int i){
        BusinessBookExcelBean  businessBookBean = new BusinessBookExcelBean();
        businessBookBean.setIndexNumber(i+1);
        //1.客户信息
        CustomerBean customer = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();
        if(customer != null){
            businessBookBean.setCustomerName(customer.getName());
            businessBookBean.setIdentifyNo(customer.getIdentifyNo());
            businessBookBean.setCells(customer.getCells().get(0));
        }

        //2.资质信息
        CustomerDemand customerDemand = iCustomerDemandService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(customerDemand != null){
            if(customerDemand.getPledgeCustomerId() != null){
                CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerDemand.getPledgeCustomerId()).getD();
                businessBookBean.setPledgeCustomerName(customerBean.getName());
            }


        }
        //3.签约信息
        PurchaseCarOrder purchaseCarOrder = iOrderService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(purchaseCarOrder != null && purchaseCarOrder.getApproveStatus() != ApproveStatus.APPROVE_INIT && customerDemand.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
            String carTypeName = this.getCarTypeName(purchaseCarOrder.getCustomerCarId());
            if(carTypeName != null){
                businessBookBean.setCarTypeFullName(carTypeName);
            }
            CustomerLoanBean customerLoan = this.getCustomerLoan(purchaseCarOrder.getCustomerLoanId());
            if(customerLoan != null){
                businessBookBean.setApplyAmount(AmountUtil.getMoneyTwo(customerLoan.getApplyAmount().toString()));//车价
                businessBookBean.setDownPaymentRatio(AmountUtil.getMoneyTwo(customerLoan.getDownPaymentRatio().toString())+"%");//首付比例
                businessBookBean.setCreditAmount(AmountUtil.getMoneyNumber(customerLoan.getCreditAmount().toString()));//申请金额
                if(customerLoan.getApprovedCreditAmount()!= null){
                    businessBookBean.setApprovedCreditAmount(AmountUtil.getMoneyNumber(customerLoan.getApprovedCreditAmount().toString()));
                }
                businessBookBean.setMonths(customerLoan.getRateType().getMonths().toString());//期限
            }
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
            if(carDealerBean != null){
                businessBookBean.setCadelarName(carDealerBean.getName());
                EmployeeBean employeeBean = iOrgBizService.actGetEmployee(carDealerBean.getEmployeeId()).getD();
                if(employeeBean != null){
                    businessBookBean.setEmployeeName(employeeBean.getUsername());
                }
            }
            if(customerLoan.getCompensatoryInterest() == 1){  //是否贴息
                businessBookBean.setCompensatoryInterest("是");
            }else if(customerLoan.getCompensatoryInterest() == 0){
                businessBookBean.setCompensatoryInterest("否");
            }
            if(customerLoan.getChargePaymentWay().equals("WHOLE")){
                businessBookBean.setChargePaymentWay("否");
            }else if(customerLoan.getChargePaymentWay().equals("STAGES")){
                businessBookBean.setChargePaymentWay("是");
            }
            if(customerLoan.getBankFeeAmount() != null){
                businessBookBean.setBankFeeAmount(AmountUtil.getMoneyTwo(customerLoan.getBankFeeAmount().toString()));//手续费
            }
        }else if(customerDemand != null && purchaseCarOrder == null){
            String carTypeName = this.getCarTypeName(customerDemand.getCustomerCarId());
            if(carTypeName != null){
                businessBookBean.setCarTypeFullName(carTypeName);
            }
            CustomerLoanBean customerLoan = this.getCustomerLoan(customerDemand.getCustomerLoanId());
            if(customerLoan != null){
                businessBookBean.setApplyAmount(AmountUtil.getMoneyTwo(customerLoan.getApplyAmount().toString()));//车价
                businessBookBean.setDownPaymentRatio(AmountUtil.getMoneyTwo(customerLoan.getDownPaymentRatio().toString())+"%");//首付比例
                businessBookBean.setCreditAmount(AmountUtil.getMoneyNumber(customerLoan.getCreditAmount().toString()));//申请金额
                businessBookBean.setMonths(customerLoan.getRateType().getMonths().toString());//期限
                if(customerLoan.getBankFeeAmount() != null){
                    businessBookBean.setBankFeeAmount(AmountUtil.getMoneyTwo(customerLoan.getBankFeeAmount().toString()));
                }
                if(customerLoan.getChargePaymentWay().equals("WHOLE")){
                    businessBookBean.setChargePaymentWay("否");
                }else if(customerLoan.getChargePaymentWay().equals("STAGES")){
                    businessBookBean.setChargePaymentWay("是");
                }
            }
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(customerDemand.getCarDealerId()).getD();
            if(carDealerBean != null){
                businessBookBean.setCadelarName(carDealerBean.getName());
                EmployeeBean employeeBean = iOrgBizService.actGetEmployee(carDealerBean.getEmployeeId()).getD();
                if(employeeBean != null){
                    businessBookBean.setEmployeeName(employeeBean.getUsername());
                }
            }
        }

        //6.指标人信息
        if(customerDemand != null){
            if(customerDemand.getRelation() != null){
                if(customerDemand.getRelation().equals("0")){
                    businessBookBean.setRelation("本人");
                }else if(customerDemand.getRelation().equals("1")){
                    businessBookBean.setRelation("父子");
                }else if(customerDemand.getRelation().equals("2")){
                    businessBookBean.setRelation("配偶");
                }else if(customerDemand.getRelation().equals("3")){
                    businessBookBean.setRelation("其他");
                }
            }
        }

        //垫资信息
        AppointPayment appointPayment = iAppointPaymentServcie.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(appointPayment != null){
            if(appointPayment.getPayTime() != null){
                businessBookBean.setPayTime(appointPayment.getPayTime());//垫资时间
            }
            if(appointPayment.getAppointPayAmount() != null){
                businessBookBean.setAppointPayAmount(AmountUtil.getMoneyTwo(appointPayment.getAppointPayAmount().toString()));//垫资金额
            }
        }

        //客户卡信息
        CustomerCardBean customerCard = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(customerTransaction.getId()).getD();
        if(customerCard != null){
            businessBookBean.setCardNo(customerCard.getCardNo());
        }

        //卡业务信息
        BankCardApply bankCardApply = iBankCardApplyService.findByCustomerTransactionId(customerTransaction.getId());
        if(bankCardApply != null){
            if(bankCardApply.getFirstReimbursement() != null){
                businessBookBean.setFirstRepaymentDate(TimeUtil.getTimeTransDay(bankCardApply.getFirstReimbursement()));
            }
            if(bankCardApply.getReceiveTrusteeTime() != null){
                businessBookBean.setReceiveCardTime(TimeUtil.getTimeTransMinutes(bankCardApply.getReceiveTrusteeTime()));//领卡时间
            }else if(bankCardApply.getReceiveDiscountTime() != null){
                businessBookBean.setReceiveCardTime(TimeUtil.getTimeTransMinutes(bankCardApply.getReceiveDiscountTime()));
            }

            if(bankCardApply.getSwipingShopTime() != null){  //渠道刷卡的时间不为空
                businessBookBean.setSwipingAddress("店刷"); //刷卡地点
                businessBookBean.setSwipingCardTime(TimeUtil.getTimeTransSecond(bankCardApply.getSwipingShopTime()));
                if(bankCardApply.getSwipingMoney() != null){
                    businessBookBean.setPayAmount(AmountUtil.getMoneyNumber(bankCardApply.getSwipingMoney().toString()));
                    businessBookBean.setSwipingAmount(AmountUtil.getMoneyNumber(bankCardApply.getSwipingMoney().toString()));
                }
            }else if(bankCardApply.getSwipingTrusteeTime() != null){ //代刷卡时间不为空
                businessBookBean.setSwipingAddress("银行刷");
                businessBookBean.setSwipingCardTime(TimeUtil.getTimeTransSecond(bankCardApply.getSwipingTrusteeTime()));
                if(bankCardApply.getSwipingMoney() != null){
                    businessBookBean.setPayAmount(AmountUtil.getMoneyNumber(bankCardApply.getSwipingMoney().toString()));
                    businessBookBean.setSwipingAmount(AmountUtil.getMoneyNumber(bankCardApply.getSwipingMoney().toString()));
                }

            }
        }

        if(purchaseCarOrder != null){
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
            if(customerCarBean != null){
                businessBookBean.setVin(customerCarBean.getVin());
                businessBookBean.setMotorNumber(customerCarBean.getMotorNumber());
                businessBookBean.setLicenseNumber(customerCarBean.getLicenseNumber());
                businessBookBean.setRegistryNumber(customerCarBean.getRegistryNumber());

            }
        }

        //取消业务
        List<CancelOrder> cancelOrders = iCancelOrderService.getCancelOrdersByCustomerTransactionId(customerTransaction.getId());
        if(cancelOrders != null){
            if(cancelOrders.size() > 0){
                CancelOrder cancelOrder = cancelOrders.get(cancelOrders.size()-1);
                businessBookBean.setCancelReason(cancelOrder.getReason());//取消原因
            }

        }

        //报批历史
        DeclarationHistorys declarationHistorys = iDeclarationHistorysService.findOneByCustomerTransactionId(customerTransaction.getId());
        if(declarationHistorys != null){
            DeclarationRecord declarationRecord = declarationHistorys.getHistoryRecords().get(declarationHistorys.getHistoryRecords().size() - 1);
            if(declarationRecord != null){
                businessBookBean.setReportTime(declarationRecord.getEmailTime());//报批时间
            }
            DeclarationResult declarationResult = declarationRecord.getDeclarationResult();
            if( declarationResult != null){
                businessBookBean.setReplyTime(declarationResult.getTs());//批复时间
            }
        }


        //征信报告生成
        CreditPhotograph creditPhotograph = iCreditPhotographService.findByCustomerId(customerTransaction.getCustomerId());
        if(creditPhotograph != null){
            if(creditPhotograph.getSubmitTime() != null){
                businessBookBean.setCreditQueryTime(TimeUtil.getTimeTransSecond(creditPhotograph.getSubmitTime()));//征信查询时间
            }
        }

        if(customerTransaction.getBusinessTypeCode() != null){
            if(customerTransaction.getBusinessTypeCode().equals("OC")){
                businessBookBean.setBusinessType("二手车");
            }else if(customerTransaction.getBusinessTypeCode().equals("NC")){
                businessBookBean.setBusinessType("新车");
            }
        }

        //抵押信息
        DMVPledge dmvPledge = iDmvpledgeService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(dmvPledge != null){
            CustomerBean cr = null;
            if(dmvPledge.getPledgeCustomerId() != null){
                cr = iCustomerBizService.actGetCustomerById(dmvPledge.getPledgeCustomerId()).getD();
                businessBookBean.setPledgeCustomerName(cr.getName());//抵押人
            }
            if(dmvPledge.getPledgeEndTime() != null){
                businessBookBean.setPledgeEndTime(TimeUtil.getTimeTransMinutes(dmvPledge.getPledgeEndTime()));//抵押登记时间
                businessBookBean.setBankCompleteTime(TimeUtil.getTimeTransMinutes(dmvPledge.getPledgeEndTime()));//银行归档时间
            }
        }

        EmployeeBean employeeBusiness = iOrgBizService.actGetEmployee(customerTransaction.getEmployeeId()).getD();
        if(employeeBusiness != null){
            businessBookBean.setBusinessManName(employeeBusiness.getUsername());//分期经理
        }

        if(purchaseCarOrder != null){
            if(purchaseCarOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                if(purchaseCarOrder.getApproveUserId() != null){
                    String approveName = iOrgBizService.actGetNameByLoginUserId(purchaseCarOrder.getApproveUserId()).getD();
                    businessBookBean.setReviewName(approveName);//审查人
                }
            }
        }

        businessBookBean.setCommentInfo("");

        return businessBookBean;
    }

    private CustomerLoanBean getCustomerLoan(String cLoanId){
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(cLoanId).getD();
        return customerLoanBean;
    }


    private String getCarTypeName(String cid){
        CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(cid).getD();
        if(customerCarBean != null){
            CarTypeBean carTypeBean = iCarTypeBizService.actGetCarTypeById(customerCarBean.getCarTypeId()).getD();
            if(carTypeBean != null){
                return carTypeBean.getFullName();
            }
        }
        return null;
    }







}
