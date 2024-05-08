package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.bean.DealerSharingRatioBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.BusinessExcelBean;
import com.fuze.bcp.api.creditcar.bean.CarValuationBean;
import com.fuze.bcp.api.creditcar.bean.CompensatoryExcelBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferBean;
import com.fuze.bcp.api.creditcar.service.*;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerCardBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.PadCustomerTransactionBean;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.StringHelper;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/8/8.
 */
@Service
public class BizCarTransactionService implements ICarTransactionBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCarTransactionService.class);

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    MappingService mappingService;

    @Autowired
    ICarValuationBizService iCarValuationBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    ISwipingCardService iSwipingCardService;

    @Autowired
    ICarRegistryService iCarRegistryService;

    @Autowired
    ICarTransferBizService iCarTransferBizService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    IDmvpledgeService iDmvpledgeService;

    @Autowired
    IDeclarationService iDeclarationService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 获取交易的概览信息
     *
     * @param tid
     * @return
     */
    public ResultBean<TransactionSummaryBean> actGetTransactionSummary(String tid) {
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(tid).getD();
        TransactionSummaryBean transactionSummary;
        if (customerTransaction == null) {
            transactionSummary = new TransactionSummaryBean();
        } else {
            transactionSummary = this.actGetTransactionSummary(mappingService.map(customerTransaction, CustomerTransactionBean.class), null, null, null, null);
        }

        return ResultBean.getSucceed().setD(transactionSummary);
    }

    @Override
    public ResultBean<DataPageBean<TransactionSummaryBean>> actGetTransactions(String loginUserId, List<Integer> statusList, Integer pageIndex, Integer pageSize) {
        DataPageBean<CustomerTransactionBean> page = iCustomerTransactionBizService.actGetPagesBySomeConditions(loginUserId, null,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),statusList, pageIndex,pageSize,"ts", true).getD();
        DataPageBean<TransactionSummaryBean> returnPage = new DataPageBean<TransactionSummaryBean>();
        if (page != null) {
            List<TransactionSummaryBean> returnList = new ArrayList<>();

            List<CustomerTransactionBean> transactions = page.getResult();
            for (CustomerTransactionBean transaction : transactions) {
                returnList.add(this.actGetTransactionSummary(mappingService.map(transaction, CustomerTransactionBean.class), null, null, null, null));
            }
            returnPage.setPageSize(page.getPageSize());
            returnPage.setCurrentPage(page.getCurrentPage());
            returnPage.setTotalCount(page.getTotalCount());
            returnPage.setTotalPages(page.getTotalPages());
            returnPage.setResult(returnList);
        }
        return ResultBean.getSucceed().setD(returnPage);
    }

    /**
     * 模糊查询交易列表
     */
    @Override
    public ResultBean<DataPageBean<TransactionSummaryBean>> actSearchTransactions(CustomerBean customerBean, String loginUserId, int pageIndex, int pageSize) {
        if (customerBean == null) {
            ResultBean.getFailed().setM("请输入查询条件！");
        }
        List<CustomerBean> customerBeanList = iCustomerBizService.actSearchCustomer(customerBean).getD();
        List<String> customerIds = new ArrayList<String>();
        for (CustomerBean cb : customerBeanList) {
            customerIds.add(cb.getId());
        }
        DataPageBean<CustomerTransactionBean> transactions = iCustomerTransactionBizService.actGetPagesBySomeConditions(loginUserId, null,customerIds,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(), pageIndex,pageSize,"ts", true).getD();
        DataPageBean<TransactionSummaryBean> destination = new DataPageBean<TransactionSummaryBean>();
        destination.setPageSize(transactions.getPageSize());
        destination.setTotalCount(transactions.getTotalCount());
        destination.setTotalPages(transactions.getTotalPages());
        destination.setCurrentPage(transactions.getCurrentPage());

        List<CustomerTransactionBean> transactionsList = transactions.getResult();
        List<TransactionSummaryBean> summaryList = new ArrayList<TransactionSummaryBean>();
        for (CustomerTransactionBean transaction : transactionsList) {
            summaryList.add(this.actGetTransactionSummary(transaction.getId()).getD());
        }
        destination.setResult(summaryList);
        return ResultBean.getSucceed().setD(destination);

    }

    public TransactionSummaryBean actGetTransactionSummary(CustomerTransactionBean customerTransaction, CustomerBean customerBean, CustomerCarBean customerCarBean, CustomerLoanBean customerLoanBean, CarDealerBean carDealerBean) {
        String tid = customerTransaction.getId();
        TransactionSummaryBean transactionSummary = new TransactionSummaryBean();

        transactionSummary.setId(customerTransaction.getId());
        transactionSummary.setStatus(customerTransaction.getStatus());
        transactionSummary.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
        transactionSummary.setTs(customerTransaction.getTs());
        //客户信息

        if (customerBean == null)
            customerBean = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();

        if (customerBean != null) {
            transactionSummary.setCustomerId(customerBean.getId());
            transactionSummary.setCustomerName(customerBean.getName());
            if (customerBean.getCells() != null && customerBean.getCells().size() > 0) {
                transactionSummary.setCustomerCell(customerBean.getCells().get(0));
            }
            transactionSummary.setCustomerIdentifyNo(customerBean.getIdentifyNo());
            transactionSummary.setCustomerGender(customerBean.getGender());
        }

        //车辆信息
        if (customerCarBean == null)
            customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(tid).getD();

        if (customerCarBean != null) {
            //appointSwipingCardListBean.setVin(customerCarBean.getVin());
            transactionSummary.setCarColor(customerCarBean.getCarColorName());
            transactionSummary.setCarEmissions(customerCarBean.getMl());

            if (customerCarBean.getCarTypeId() != null) {
                transactionSummary.setCarTypeId(customerCarBean.getCarTypeId());
                CarTypeBean carTypeBean = iCarTypeBizService.actGetCarTypeById(customerCarBean.getCarTypeId()).getD();
                if (carTypeBean != null) {
                    transactionSummary.setCarTypeName(carTypeBean.getFullName());
                }
            }

            transactionSummary.setGuidePrice(customerCarBean.getGuidePrice());
            transactionSummary.setEvaluatePrice(customerCarBean.getEvaluatePrice());

            Double evaluatePrice = customerCarBean.getEvaluatePrice();
            if (evaluatePrice == null) {
                if (StringHelper.isNotBlock(customerCarBean.getVin())) {
                    CarValuationBean carValuationBean = this.iCarValuationBizService.actGetValuationByVin(customerCarBean.getVin()).getD();
                    if (carValuationBean != null) {
                        transactionSummary.setEvaluatePrice(carValuationBean.getPrice() != null ? carValuationBean.getPrice() : carValuationBean.getInitialValuationPrice());
                    }
                }
            }
        }

        //借款信息
        if (customerLoanBean == null)
            customerLoanBean = iCustomerBizService.actGetCustomerLoanByTransactionId(tid).getD();

        String branchCode = transactionSummary.getBusinessTypeCode();
        if (customerLoanBean != null) {
            transactionSummary.setRealPrice(customerLoanBean.getApplyAmount());
            transactionSummary.setReceiptPrice(customerLoanBean.getReceiptPrice());
            transactionSummary.setCreditAmount(customerLoanBean.getCreditAmount());
            transactionSummary.setCreditRatio(customerLoanBean.getCreditRatio());
            transactionSummary.setApprovedCreditAmount(customerLoanBean.getApprovedCreditAmount());
            transactionSummary.setDownPayment(customerLoanBean.getDownPayment());
            transactionSummary.setDownPaymentRatio(customerLoanBean.getDownPaymentRatio());
            transactionSummary.setCompensatoryInterest(customerLoanBean.getCompensatoryInterest());
            if (customerLoanBean.getRateType() != null) {
                transactionSummary.setMonths(customerLoanBean.getRateType().getMonths());
                transactionSummary.setRatio(customerLoanBean.getRateType().getRatio());
            }
            if (customerLoanBean.getIsNeedPayment() != null) {
                branchCode += customerLoanBean.getIsNeedPayment().toString();
            } else {
                branchCode += "0";
            }
        }

        //经销商信息
        if (carDealerBean == null)
            carDealerBean = iCarDealerBizService.actGetOneCarDealer(customerTransaction.getCarDealerId()).getD();
        if (carDealerBean != null) {
            transactionSummary.setCarDealerId(carDealerBean.getId());
            transactionSummary.setCarDealerName(carDealerBean.getName());
            transactionSummary.setCarDealerAddress(carDealerBean.getAddress());
        }

        //获取系统业务阶段配置
        transactionSummary.setTransactionStage(iCustomerTransactionBizService.actGetTransactionStage(tid, branchCode, "CAR_BIZ_WORKFLOW_STAGE").getD());

        return transactionSummary;
    }

    @Override
    public ResultBean<DataPageBean<PadCustomerTransactionBean>> actGetTransactionsByLoginUserId(String loginUserId, List<Integer> statusList, Integer pageIndex, Integer pageSize) {
        DataPageBean<CustomerTransactionBean> customers =  iCustomerTransactionBizService.actGetPagesBySomeConditions(loginUserId, null,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),statusList, pageIndex,pageSize,"ts", true).getD();
        if (customers != null) {
            DataPageBean<PadCustomerTransactionBean> pageBean = new DataPageBean<PadCustomerTransactionBean>();
            List<PadCustomerTransactionBean> pctList = new ArrayList<>();
            for (CustomerTransactionBean transaction : customers.getResult()) {
                PadCustomerTransactionBean ptb = mappingService.map(transaction, PadCustomerTransactionBean.class);
                if (ptb != null) {
                    CustomerBean customer = iCustomerBizService.actGetCustomerById(ptb.getCustomerId()).getD();
                    if (customer != null) {
                        ptb.setCustomerMame(customer.getName());
                        ptb.setIdentifyNo(customer.getIdentifyNo());
                        if (customer.getCells() != null && customer.getCells().size() > 0) {
                            ptb.setCell(customer.getCells().get(0));
                        }
                    }
                    CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(ptb.getCarDealerId()).getD();
                    if (carDealer != null) {
                        ptb.setCarDealerName(carDealer.getName());
                    }
                    CustomerCarBean customerCar = iCustomerBizService.actGetCustomerCarByTransactionId(transaction.getId()).getD();
                    if (customerCar != null) {
                        ptb.setCarColor(customerCar.getCarColorName());
                        ptb.setGuidePrice(customerCar.getGuidePrice());
                        ptb.setEvaluatePrice(customerCar.getEvaluatePrice());
                        if (StringUtils.isNotBlank(customerCar.getCarTypeId())) {
                            CarTypeBean carTypeBean = this.iCarTypeBizService.actGetCarTypeById(customerCar.getCarTypeId()).getD();
                            ptb.setCarString(carTypeBean != null ? carTypeBean.getFullName() : "未知车型");
                        }
                        if (StringHelper.isNotBlock(customerCar.getVin())) {
                            CarValuationBean carValuationBean = this.iCarValuationBizService.actGetValuationByVin(customerCar.getVin()).getD();
                            if (carValuationBean != null) {
                                ptb.setEvaluatePrice(carValuationBean.getInitialValuationPrice());
                            }
                        }
                    }
                    CustomerLoanBean customerLoan = iCustomerBizService.actGetCustomerLoanByTransactionId(ptb.getId()).getD();
                    if (customerLoan != null) {
                        if (customerLoan.getRateType() != null) {
                            ptb.setRateType(customerLoan.getRateType());
                        }
                        ptb.setCreditAmount(customerLoan.getCreditAmount());
                        ptb.setCreditRatio(customerLoan.getCreditRatio());
                        ptb.setDownPayment(customerLoan.getDownPayment());
                        ptb.setDownPaymentRatio(customerLoan.getDownPaymentRatio());
                        ptb.setRealPrice(customerLoan.getApplyAmount());
                        ptb.setReceiptPrice(customerLoan.getReceiptPrice());
                    }
                    CustomerDemand customerDemand = this.iCustomerDemandService.findByCustomerTransactionId(ptb.getId());
                    if (customerDemand != null) {
                        ptb.setCustomerDemandId(customerDemand.getId());
                        ptb.setApproveStatus(customerDemand.getApproveStatus());
                    }
                    pctList.add(ptb);
                }
            }
            pageBean.setPageSize(customers.getPageSize());
            pageBean.setCurrentPage(customers.getCurrentPage());
            pageBean.setTotalCount(customers.getTotalCount());
            pageBean.setTotalPages(customers.getTotalPages());
            pageBean.setResult(pctList);
            return ResultBean.getSucceed().setD(pageBean);
        }
        return ResultBean.getSucceed().setD(this.mappingService.map(customers, PadCustomerTransactionBean.class));
    }

    /**
     * 通过交易ID查询渠道的垫资策略
     *
     * @param customerTransactionId
     * @return
     */
    @Override
    public ResultBean<Integer> actGetPaymentPolicy(String customerTransactionId) {
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerTransactionId);
        if (purchaseCarOrder != null) {
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(purchaseCarOrder.getCarDealerId()).getD();
            if (carDealerBean != null) {
                if (customerLoanBean.getCompensatoryInterest().equals(0)) {//商贷
                    if (carDealerBean.getPaymentPolicy() != null) {
                        return ResultBean.getSucceed().setD(carDealerBean.getPaymentPolicy().getBusiness());
                    } else {
                        return ResultBean.getSucceed();
                    }
                } else if (customerLoanBean.getCompensatoryInterest().equals(1)) {//贴息
                    if (carDealerBean.getPaymentPolicy() != null) {
                        return ResultBean.getSucceed().setD(carDealerBean.getPaymentPolicy().getDiscount());
                    } else {
                        return ResultBean.getSucceed();
                    }
                }
            }

        }
        return ResultBean.getFailed();
    }

    public ResultBean<CustomerTransactionBean> actCreateFileNumberByTransactionId(String id){
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(id).getD();
        if (customerTransaction == null){
            return ResultBean.getFailed();
        }
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(id);
        if (StringUtils.isEmpty(customerTransaction.getFileNumber())){
            customerTransaction.setFileNumber(iCustomerTransactionBizService.actCreateCustomerNumber(purchaseCarOrder.getTs(), purchaseCarOrder.getBusinessTypeCode()).getD());
        }
        customerTransaction = iCustomerTransactionBizService.actSaveCustomerTransaction(customerTransaction).getD();
        return ResultBean.getSucceed().setD(mappingService.map(customerTransaction,CustomerTransactionBean.class));
    }

    @Override
    public ResultBean<List<CompensatoryExcelBean>> actExportCompensatoryTransactions(Boolean compensatory,Boolean business,Boolean nc,Boolean oc,String swipingCardTime) {
        List<CompensatoryExcelBean> compensatoryExcelBean = new ArrayList<CompensatoryExcelBean>();
        SearchBean searchBean = new SearchBean();
        searchBean.setCompensatory(compensatory);
        searchBean.setBusiness(business);
        searchBean.setNc(nc);
        searchBean.setOc(oc);
        searchBean.setSwipingCardTime(swipingCardTime);
        searchBean.setSortName("carDealerId");
        List<CustomerTransactionBean> customerTransactions = iCustomerTransactionBizService.actGetAllBySearchBean(searchBean).getD();
        if(customerTransactions != null && customerTransactions.size() > 0){
            int a = 0;
            String lastName = customerTransactions.get(0).getCarDealerId();
            for (CustomerTransactionBean cus:customerTransactions) {
                if (!lastName.equals(cus.getCarDealerId())) {
                    a = 1;
                }else {
                    a = a + 1;
                }
                lastName = cus.getCarDealerId();
                CompensatoryExcelBean customerTransactionExcelBean = this.actGetCompensatoryInfo(cus);
                customerTransactionExcelBean.setIndexNumber(a);
                compensatoryExcelBean.add(customerTransactionExcelBean);
            }
        }
        if(compensatoryExcelBean == null && compensatoryExcelBean.size() == 0){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_NULLTRANSACTIONEXCEL"));
        }
        return ResultBean.getSucceed().setD(mappingService.map(compensatoryExcelBean,CompensatoryExcelBean.class));
    }

    public CompensatoryExcelBean actGetCompensatoryInfo(CustomerTransactionBean customerTransaction){
        CompensatoryExcelBean compensatoryExcelBean = new CompensatoryExcelBean();
        CarDealerBean dealerBean = iCarDealerBizService.actGetCarDealer(customerTransaction.getCarDealerId()).getD();
        if(dealerBean != null){
            compensatoryExcelBean.setCarDealerName(dealerBean.getName());
        }

        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();
        if(customerBean != null){
            compensatoryExcelBean.setApplyName(customerBean.getName());
        }

        PurchaseCarOrder purchaseCarOrder = iOrderService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(purchaseCarOrder != null && purchaseCarOrder.getApproveDate() != null){
            compensatoryExcelBean.setApproveTime(purchaseCarOrder.getApproveDate());
        }

        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoan(customerTransaction.getId()).getD();
        if(customerLoanBean != null){
            compensatoryExcelBean.setCreditAmount(this.getMoneyTwo(customerLoanBean.getCreditAmount().toString()));
            compensatoryExcelBean.setMonth(customerLoanBean.getRateType().getMonths());
            compensatoryExcelBean.setReceiptPrice(this.getMoneyTwo(customerLoanBean.getReceiptPrice().toString()));
            compensatoryExcelBean.setDownPayment(this.getMoneyTwo(customerLoanBean.getDownPayment().toString()));
        }

        CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(customerTransaction.getId()).getD();
        if(customerCarBean != null){
            compensatoryExcelBean.setVin(customerCarBean.getVin());
            CarTypeBean carTypeBean = iCarTypeBizService.actGetCarTypeById(customerCarBean.getCarTypeId()).getD();
            if(carTypeBean != null){
                compensatoryExcelBean.setCarModelNumber(carTypeBean.getFullName());
            }
        }

        CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(customerTransaction.getId()).getD();
        if(customerCardBean != null && customerCardBean.getCardNo() != null){
            compensatoryExcelBean.setCardNo(customerCardBean.getCardNo());
        }

        compensatoryExcelBean.setCardType("身份证");
        CustomerDemand customerDemand = iCustomerDemandService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(customerDemand.getPledgeCustomerId() != null){
            CustomerBean customerBean1 = iCustomerBizService.actGetCustomerById(customerDemand.getPledgeCustomerId()).getD();
            compensatoryExcelBean.setPledgeCustomerName(customerBean1.getName());
            compensatoryExcelBean.setIdentifyNo(customerBean1.getIdentifyNo());
        }

        return compensatoryExcelBean;
    }

    @Override
    public ResultBean<List<BusinessExcelBean>> actExportBusinessTransactions(Boolean compensatory,Boolean business,Boolean nc,Boolean oc,String swipingCardTime) {
        List<BusinessExcelBean> businessExcelBeenList = new ArrayList<BusinessExcelBean>();
        SearchBean searchBean = new SearchBean();
        searchBean.setCompensatory(compensatory);
        searchBean.setBusiness(business);
        searchBean.setNc(nc);
        searchBean.setOc(oc);
        searchBean.setSwipingCardTime(swipingCardTime);
        searchBean.setSortName("carDealerId");
        List<CustomerTransactionBean> customerTransactions = iCustomerTransactionBizService.actGetAllBySearchBean(searchBean).getD();
        if(customerTransactions != null){
            for (CustomerTransactionBean cus:customerTransactions) {
                BusinessExcelBean businessExcelBean = this.actGetBusinessInfo(cus);
                businessExcelBeenList.add(businessExcelBean);
            }
        }
        if(businessExcelBeenList == null && businessExcelBeenList.size() == 0){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_NULLTRANSACTIONEXCEL"));
        }
        return ResultBean.getSucceed().setD(mappingService.map(businessExcelBeenList,BusinessExcelBean.class));
    }

    public BusinessExcelBean actGetBusinessInfo(CustomerTransactionBean customerTransaction){
        BusinessExcelBean businessExcelBean = new BusinessExcelBean();
        CarDealerBean dealerBean = iCarDealerBizService.actGetCarDealer(customerTransaction.getCarDealerId()).getD();
        if(dealerBean != null){
            businessExcelBean.setDealerName(dealerBean.getName());
            businessExcelBean.setEmployeeName(this.getEmployeeName(dealerBean.getEmployeeId()));
        }
        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();
        if(customerBean != null){
            businessExcelBean.setCreditName(customerBean.getName());
            businessExcelBean.setIdentifyNo(customerBean.getIdentifyNo());
        }
        PurchaseCarOrder purchaseCarOrder = iOrderService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(purchaseCarOrder != null){
            if(purchaseCarOrder.getApproveDate() != null){
                businessExcelBean.setApproveTime(purchaseCarOrder.getApproveDate());
            }
            if(purchaseCarOrder.getFeeItemList() != null && purchaseCarOrder.getFeeItemList().size() > 0){
                for (FeeValueBean fee:purchaseCarOrder.getFeeItemList()) {
                    if(fee.getCode().equals("DIANZIFEE")){
                        businessExcelBean.setAppointServiceFee(this.getMoneyTwo(fee.getFee().toString()));
                    }
                }
            }

        }

        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoan(customerTransaction.getId()).getD();
        if(customerLoanBean != null){
            businessExcelBean.setCreditAmount(this.getMoneyTwo(customerLoanBean.getCreditAmount().toString()));
            businessExcelBean.setMonth(customerLoanBean.getRateType().getMonths());
            businessExcelBean.setReceiptPrice(this.getMoneyTwo(customerLoanBean.getReceiptPrice().toString()));
            businessExcelBean.setLoanServiceFee(this.getMoneyTwo(customerLoanBean.getLoanServiceFee().toString()));
        }

        AppointPayment appointPayment = iAppointPaymentService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(appointPayment != null && appointPayment.getPayTime() != null){
            businessExcelBean.setPayTime(appointPayment.getPayTime());
        }

        BankCardApplyBean bankCardApplyBean = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(customerTransaction.getId()).getD();
        if(bankCardApplyBean != null){
            if(bankCardApplyBean.getSwipingShopTime() != null){
                businessExcelBean.setSwipingCardTime(bankCardApplyBean.getSwipingShopTime());
            }else if(bankCardApplyBean.getSwipingTrusteeTime() != null){
                businessExcelBean.setSwipingCardTime(bankCardApplyBean.getSwipingTrusteeTime());
            }
        }

        CarRegistry carRegistry = iCarRegistryService.findAvailableOneByCustomerTransactionId(customerTransaction.getId());
        if(carRegistry != null && carRegistry.getCarModelNumber() != null){
            businessExcelBean.setCarModelNumber(carRegistry.getCarModelNumber());
        }

        CustomerCardBean customerCardBean = iCustomerBizService.actGetCustomerCardByCustomerTransactionId(customerTransaction.getId()).getD();
        if(customerCardBean != null){
            businessExcelBean.setCardNo(customerCardBean.getCardNo());
        }

        businessExcelBean.setBusinessName(this.getEmployeeName(customerTransaction.getEmployeeId()));

        if(customerTransaction.getBusinessTypeCode().equals("NC")){
            businessExcelBean.setBusinessType("新车");
        }else if(customerTransaction.getBusinessTypeCode().equals("OC")){
            businessExcelBean.setBusinessType("二手车");
        }

        return businessExcelBean;
    }

    public String getEmployeeName(String id){
        EmployeeBean employeeBean = iOrgBizService.actGetEmployee(id).getD();
        if(employeeBean == null){
            return null;
        }
        return employeeBean.getUsername();
    }

    /**
     *金额保留两位小数
     */
    private String getMoneyTwo(String money){
        BigDecimal moneyString = new BigDecimal(money);
        moneyString = moneyString.setScale(2,BigDecimal.ROUND_HALF_UP);
        return moneyString.toString();
    }


    @Override
    public ResultBean<CarDealerBean> actGetCardealerTransfer(String bid, List<String> tids) {
        List<CarDealerBean> carDealerListBean = iCarDealerBizService.actGetCarDealer(tids).getD();
        CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(bid).getD();

        for (CarDealerBean cr:carDealerListBean) {
            List<String> bList = new ArrayList<String>();
            for (int i=0;i<cr.getBusinessManIds().size();i++) {
                if(!carDealerBean.getBusinessManIds().contains(cr.getBusinessManIds().get(i))){
                    bList.add(cr.getBusinessManIds().get(i));
                }
            }
            bList.addAll(carDealerBean.getBusinessManIds());
            carDealerBean.setBusinessManIds(bList);
            iCarDealerBizService.actSaveCarDealer(carDealerBean);
        }

        for (String tid: tids) {
            DealerSharingRatioBean sharingRatioBean = iCarDealerBizService.actGetDealerSharingRatio(tid).getD();

            if(sharingRatioBean != null){
                sharingRatioBean.setCarDealerId(bid);
                iCarDealerBizService.actSaveDealerSharingRatio(sharingRatioBean);
            }
            List<CarValuationBean> carValuations = iCarValuationBizService.actGetCarValuation(tid).getD();
            if(carValuations != null){
                for (CarValuationBean cvn:carValuations) {
                    cvn.setCarDealerId(bid);
                    iCarValuationBizService.actSaveCarValuation(cvn);
                }
            }
        }

        List<CustomerTransactionBean> customerTransactions = iCustomerTransactionBizService.actGetListsBySomeConditions(null, null,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(),"ts", true).getD();;
        if(customerTransactions.size() > 0){
            for (CustomerTransactionBean transaction:customerTransactions) {
                CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(transaction.getId());
                if(customerDemand != null){
                    customerDemand.setCarDealerId(bid);
                    iCustomerDemandService.save(customerDemand);
                }

                PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transaction.getId());
                if(purchaseCarOrder != null){
                    purchaseCarOrder.setCarDealerId(bid);

                    iOrderService.save(purchaseCarOrder);
                }
                BankCardApplyBean bankCardApplyBean = iBankCardApplyBizService.actFindBankCardApplyByTransactionId(transaction.getId()).getD();
                if(bankCardApplyBean != null){
                    bankCardApplyBean.setCarDealerId(bid);
                    iBankCardApplyBizService.actSaveBankCardApply(bankCardApplyBean);
                }

                AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(transaction.getId());
                if(appointPayment != null){
                    appointPayment.setCarDealerId(bid);
                    iAppointPaymentService.save(appointPayment);
                }
                AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(transaction.getId());
                if(appointSwipingCard != null){
                    appointSwipingCard.setCarDealerId(bid);
                    iAppointSwipingCardService.save(appointSwipingCard);
                }

                SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(transaction.getId());
                if(swipingCard != null){
                    swipingCard.setCarDealerId(bid);
                    iSwipingCardService.save(swipingCard);
                }

                CarRegistry carRegistry = iCarRegistryService.findByCustomerTransactionId(transaction.getId());
                if(carRegistry != null){
                    carRegistry.setCarDealerId(bid);
                    iCarRegistryService.save(carRegistry);
                }

                CarTransferBean carTransferBean = iCarTransferBizService.actGetByCustomerTransactionId(transaction.getId()).getD();
                if(carTransferBean != null){
                    carTransferBean.setCarDealerId(bid);
                    iCarTransferBizService.actSaveCarTransfer(carTransferBean);
                }

                DMVPledge dmvPledge = iDmvpledgeService.findByCustomerTransactionId(transaction.getId());
                if(dmvPledge != null){
                    dmvPledge.setCarDealerId(bid);
                    iDmvpledgeService.save(dmvPledge);
                }

                Declaration declaration = iDeclarationService.findByCustomerTransactionId(transaction.getId());
                if(declaration != null){
                    declaration.setCarDealerId(bid);
                    iDeclarationService.save(declaration);
                }

                CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(transaction.getId()).getD();
                if(customerTransaction != null){
                    customerTransaction.setCarDealerId(bid);
                    iCustomerTransactionBizService.actSaveCustomerTransaction(customerTransaction);
                }

//                BalanceAccountDetailBean balanceAccountDetail = iBalanceAccountBizService.actGetBalanceAccountDetail(transaction.getId()).getD();
//                if(balanceAccountDetail != null){
//                    balanceAccountDetail.setCardealerId(bid);
//                    iBalanceAccountBizService.actSaveBalanceAccountDetail(balanceAccountDetail);
//                }
//
//                ChargeFeePlanBean chargeFeePlanBean = iChargeFeePlanBizService.actChargeFeePlan(transaction.getId()).getD();
//                if(chargeFeePlanBean != null){
//                    chargeFeePlanBean.setCardealerId(bid);
//                    iChargeFeePlanBizService.actSaveChargeFeePlan(chargeFeePlanBean);
//                }

            }
        }

        iCarDealerBizService.actDeleteCarDealerByIds(tids);
        return ResultBean.getSucceed().setD(mappingService.map(carDealerBean,CarDealerBean.class));
    }

    @Override
    public ResultBean<String> actCheckTransactionBillImageTypeFileExited(String billTypeCode, String customerTransactionId){

        StringBuilder noneImageType = new StringBuilder();

        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(billTypeCode).getD();
        if ( billType == null || billType.getRequiredImageTypeCodes().size() == 0){
            return ResultBean.getSucceed().setD(noneImageType);
        }

        List<String> requiredImageTypeCodes = billType.getRequiredImageTypeCodes();
        for (String imageTypeCode : requiredImageTypeCodes){
            CustomerImageTypeBean customerImageTypeBean = iCustomerImageTypeBizService.actFindCustomerImageTypeByCode(imageTypeCode).getD();
            CustomerImageFileBean customerImageFileBean = iCustomerImageFileBizService.actGetCustomerImageFile(null, customerTransactionId, imageTypeCode).getD();
            if (customerImageFileBean == null || customerImageFileBean.getFileIds().size() == 0){
                noneImageType.append(customerImageTypeBean.getName() + "/");
            }
        }
        if (noneImageType.length() > 0){
            noneImageType.delete(noneImageType.length()-1,noneImageType.length());
            return ResultBean.getSucceed().setD(String.format(messageService.getMessage("MSG_CUSTOMERTRANSACTION_BILL_REQUIRED_IMAGETYPE_NULL"),noneImageType));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<PadCustomerTransactionBean>> actGetAllTransactionsByLoginUserId(String loginUserId, Integer pageIndex, Integer pageSize) {
        DataPageBean<CustomerTransactionBean> page = iCustomerTransactionBizService.actGetPagesBySomeConditions(loginUserId, null,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(), pageIndex,pageSize,"ts", true).getD();
        if (page != null) {
            DataPageBean<PadCustomerTransactionBean> pageBean = new DataPageBean<PadCustomerTransactionBean>();
            List<PadCustomerTransactionBean> pctList = getPctList(page.getResult());
            pageBean.setPageSize(page.getPageSize());
            pageBean.setCurrentPage(page.getCurrentPage());
            pageBean.setTotalCount(page.getTotalCount());
            pageBean.setTotalPages(page.getTotalPages());
            pageBean.setResult(pctList);
            return ResultBean.getSucceed().setD(pageBean);
        } else {
            return ResultBean.getSucceed().setM("数据为空！");
        }
    }

    @Override
    public ResultBean<List<PadCustomerTransactionBean>> actGetTransactionQuery(String loginUserId, String inputStr) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("name").regex(".*?\\" + inputStr + ".*"), Criteria.where("identifyNo").regex(".*?\\" + inputStr + ".*")
                , Criteria.where("address").regex(".*?\\" + inputStr + ".*"));
        List<BasicDBObject> cutomers = mongoTemplate.find(new Query(criteria), BasicDBObject.class, "so_customer");
        List<String> customerIds = new ArrayList();
        for (BasicDBObject object : cutomers) {
            customerIds.add(object.getString("_id"));
        }
        List transactions = iCustomerTransactionBizService.actGetListsBySomeConditions(loginUserId, null,customerIds,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(),"ts", true).getD();;
        if (transactions != null) {
            List<PadCustomerTransactionBean> padCustomerTransactionBeen = getPctList(transactions);
            return ResultBean.getSucceed().setD(padCustomerTransactionBeen);
        }
        return ResultBean.getFailed();
    }

    private List<PadCustomerTransactionBean> getPctList(List<CustomerTransactionBean> transactions) {
        List<PadCustomerTransactionBean> pctList = new ArrayList<>();
        for (CustomerTransactionBean transaction : transactions) {
            PadCustomerTransactionBean ptb = mappingService.map(transaction, PadCustomerTransactionBean.class);
            if (ptb != null) {
                CustomerBean customer = iCustomerBizService.actGetCustomerById(ptb.getCustomerId()).getD();
                if (customer != null) {
                    ptb.setCustomerMame(customer.getName());
                    ptb.setIdentifyNo(customer.getIdentifyNo());
                    if (customer.getCells() != null && customer.getCells().size() > 0) {
                        ptb.setCell(customer.getCells().get(0));
                    }
                }
                CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(ptb.getCarDealerId()).getD();
                if (carDealer != null) {
                    ptb.setCarDealerName(carDealer.getName());
                    ptb.setCarDealerAddress(carDealer.getAddress());
                }
                CustomerCarBean customerCar = iCustomerBizService.actGetCustomerCarByTransactionId(transaction.getId()).getD();
                if (customerCar != null) {
                    ptb.setCarColor(customerCar.getCarColorName());
                    ptb.setGuidePrice(customerCar.getGuidePrice());
                    ptb.setEvaluatePrice(customerCar.getEvaluatePrice());
                    if (StringUtils.isNotBlank(customerCar.getCarTypeId())) {
                        CarTypeBean carTypeBean = this.iCarTypeBizService.actGetCarTypeById(customerCar.getCarTypeId()).getD();
                        ptb.setCarString(carTypeBean != null ? carTypeBean.getFullName() : "未知车型");
                    }
                    if (StringHelper.isNotBlock(customerCar.getVin())) {
                        CarValuationBean carValuationBean = this.iCarValuationBizService.actGetValuationByVin(customerCar.getVin()).getD();
                        if (carValuationBean != null) {
                            ptb.setEvaluatePrice(carValuationBean.getInitialValuationPrice());
                        }
                    }
                }
                CustomerLoanBean customerLoan = iCustomerBizService.actGetCustomerLoanByTransactionId(ptb.getId()).getD();
                if (customerLoan != null) {
                    if (customerLoan.getRateType() != null) {
                        ptb.setRateType(customerLoan.getRateType());
                    }
                    ptb.setCreditAmount(customerLoan.getCreditAmount());
                    ptb.setCreditRatio(customerLoan.getCreditRatio());
                    ptb.setDownPayment(customerLoan.getDownPayment());
                    ptb.setDownPaymentRatio(customerLoan.getDownPaymentRatio());
                    ptb.setRealPrice(customerLoan.getApplyAmount());
                    ptb.setReceiptPrice(customerLoan.getReceiptPrice());
                }
                CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(ptb.getId());
                if (customerDemand != null) {
                    ptb.setCustomerDemandId(customerDemand.getId());
                    ptb.setApproveStatus(customerDemand.getApproveStatus());
                }
                pctList.add(ptb);
            }
        }
        return pctList;
    }

}
