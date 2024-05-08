package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillBean;
import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillListBean;
import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.creditcar.service.IPaymentBillBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.BusinessExchange;
import com.fuze.bcp.creditcar.domain.DecompressBill;
import com.fuze.bcp.creditcar.domain.PaymentBill;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.service.IDecompressService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.creditcar.service.IPaymentBillService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 缴费单服务类
 */
@Service
public class BizPaymentBillService implements IPaymentBillBizService{

    private static final Logger logger = LoggerFactory.getLogger(BizPaymentBillService.class);

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IPaymentBillService iPaymentBillService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    IDecompressService iDecompressService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<PaymentBillSubmissionBean> actCreatePaymentBill(String transactionId) {

        PaymentBillSubmissionBean paymentBillSubmissionBean = new PaymentBillSubmissionBean();
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(transactionId).getD();

        if(customerTransaction != null){
                paymentBillSubmissionBean = this.initPaymentBillByTransaction(customerTransaction.getId());
                List<ImageTypeFileBean> imageTypeFile = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(paymentBillSubmissionBean.getCustomerId(),
                        paymentBillSubmissionBean.getCustomerTransactionId(),
                        paymentBillSubmissionBean.getBusinessTypeCode(), "A025").getD();
                paymentBillSubmissionBean.setCustomerImages(imageTypeFile);

                if(paymentBillSubmissionBean == null){
                    logger.info(String.format(messageService.getMessage("MSG_PAYMENTBILL_CREATEORDERSUCCEED"), paymentBillSubmissionBean.getCustomerTransactionId()));
                }
                return ResultBean.getSucceed().setD(paymentBillSubmissionBean);
            }
        return ResultBean.getFailed();
    }

    public PaymentBillSubmissionBean initPaymentBillByTransaction(String transactionId){
        PaymentBillSubmissionBean paymentBillSubmissionBean = new PaymentBillSubmissionBean();
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(transactionId).getD();
        if(customerTransaction != null){
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerTransaction.getId());
            if(purchaseCarOrder != null){
                CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
                if(customerLoanBean != null){
                    paymentBillSubmissionBean.setLoanServiceFee(customerLoanBean.getLoanServiceFee());
                }
            }
            paymentBillSubmissionBean.setCustomerTransactionId(customerTransaction.getId());
            paymentBillSubmissionBean.setCustomerId(customerTransaction.getCustomerId());
            paymentBillSubmissionBean.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
            paymentBillSubmissionBean.setLoginUserId(customerTransaction.getLoginUserId());
            paymentBillSubmissionBean.setEmployeeId(customerTransaction.getEmployeeId());
            List<FeeBillBean> feeBill = this.getFeeBills(transactionId);
            paymentBillSubmissionBean.setFeeItemList(feeBill);
        }
        return paymentBillSubmissionBean;
    }

    private List<FeeBillBean> getFeeBills(String transactionId){
        List<FeeBillBean> feeBill = new ArrayList<>();
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        List<String> orderFeeCodeList = new ArrayList<String>();
        for (FeeValueBean feeValue : purchaseCarOrder.getFeeItemList()) {
            FeeBillBean fBill = new FeeBillBean();
            fBill.setName(feeValue.getName());
            fBill.setFee(feeValue.getFee());
            fBill.setCode(feeValue.getCode());
            fBill.setId(feeValue.getId());
            fBill.setIsChoose(0);
            feeBill.add(fBill);
            orderFeeCodeList.add(feeValue.getCode());
        }
        feeBill = this.getAllFees(feeBill,orderFeeCodeList);
        return feeBill;
    }

    private List<FeeBillBean> getAllFees(List<FeeBillBean> busList, List feeCodeList){
        List<FeeBillBean> feeBillBeen = new ArrayList<FeeBillBean>();

        //系统配置收费项
        List<FeeItemBean> feeItems = iBaseDataBizService.actGetFeeItems().getD();
        for (FeeValueBean fee:busList) {
            for (FeeItemBean feeitem:feeItems) {
                if(fee.getCode().equals(feeitem.getCode())){
                    fee.setName(feeitem.getName());
                }
            }
        }
        feeBillBeen.addAll(busList);
        List<String> feeitemcode = new ArrayList<String>();
        for (FeeItemBean feeitem:feeItems) {
            feeitemcode.add(feeitem.getCode());
        }
        for(String item : feeitemcode){
            if(!feeCodeList.contains(item)){
                for (FeeItemBean feeItem:feeItems) {
                    if(item.equals(feeItem.getCode())){
                        FeeBillBean feebill = new FeeBillBean();
                        feebill.setCode(feeItem.getCode());
                        feebill.setName(feeItem.getName());
                        feebill.setId(feeItem.getId());
                        feebill.setDataStatus(feeItem.getDataStatus());
                        feebill.setIsChoose(0);
                        feeBillBeen.add(feebill);
                    }
                }
            }
        }
        return feeBillBeen;
    }

    @Override
    public ResultBean<PaymentBillSubmissionBean> actSavePaymentBill(PaymentBillSubmissionBean paymentBillSubmissionBean) {
        PaymentBill paymentBill;
        if(StringHelper.isBlock(paymentBillSubmissionBean.getId())){
            paymentBill = new PaymentBill();
            paymentBill.setCustomerId(paymentBillSubmissionBean.getCustomerId());
            paymentBill.setCustomerTransactionId(paymentBillSubmissionBean.getCustomerTransactionId());
            paymentBill.setBusinessTypeCode(paymentBillSubmissionBean.getBusinessTypeCode());
            paymentBill.setFeeItemList(paymentBillSubmissionBean.getFeeItemList());
            iPaymentBillService.tempSave(paymentBill);
        }
        return ResultBean.getSucceed().setD(paymentBillSubmissionBean).setM(messageService.getMessage("MSG_PAYMENTBILL_SAVE_SUCCESS"));
    }

    public PaymentBillSubmissionBean getPaymentBillSubmissionInfo(String paymentBillId){
        PaymentBill paymentBill = iPaymentBillService.getAvailableOne(paymentBillId);
        if(paymentBill != null){
            PaymentBillSubmissionBean paymentBillSubmissionBean = new PaymentBillSubmissionBean();
            paymentBillSubmissionBean.setCustomerId(paymentBill.getCustomerId());
            paymentBillSubmissionBean.setCustomerTransactionId(paymentBill.getCustomerTransactionId());
            paymentBillSubmissionBean.setFeeItemList(paymentBill.getFeeItemList());
            paymentBillSubmissionBean.setId(paymentBill.getId());
            paymentBillSubmissionBean.setReceiptAccount(paymentBill.getReceiptAccount());
            paymentBillSubmissionBean.setPaymentAccount(paymentBill.getPaymentAccount());
            paymentBillSubmissionBean.setPaymentAmount(paymentBill.getPaymentAmount());
            paymentBillSubmissionBean.setPaymentTime(paymentBill.getPaymentTime());
            paymentBillSubmissionBean.setPayContentType(paymentBill.getPayContentType());
            paymentBillSubmissionBean.setPaymentType(paymentBill.getPaymentType());
            paymentBillSubmissionBean.setChargeStatus(paymentBill.getChargeStatus());
            paymentBillSubmissionBean.setChargeFee(paymentBill.getChargeFee());
            paymentBillSubmissionBean.setLoanServiceStatus(paymentBill.getLoanServiceStatus());
            paymentBillSubmissionBean.setLoanServiceFee(paymentBill.getLoanServiceFee());
            paymentBillSubmissionBean.setCustomerImages(iCustomerImageFileBizService.actGetBillImageTypesWithFiles(paymentBill.getCustomerId(),paymentBill.getCustomerTransactionId(),paymentBill.getBusinessTypeCode(),paymentBill.getBillTypeCode()).getD());
            return paymentBillSubmissionBean;
        }
        return null;
    }

    @Override
    public ResultBean<PaymentBillSubmissionBean> actSubmitPaymentBill(PaymentBillSubmissionBean paymentBillSubmissionBean) {

        if(paymentBillSubmissionBean.getId() != null){
            ResultBean result = iPaymentBillService.getEditableBill(paymentBillSubmissionBean.getId());
            if(result.failed()) return result;
        }

//        if(paymentBillSubmissionBean.getId() == null){
//            List<PaymentBill> paymentBillList = iPaymentBillService.getByCustomerTransactionId(paymentBillSubmissionBean.getCustomerTransactionId());
//            if(paymentBillList.size()> 0 && paymentBillList != null){
//                for (PaymentBill paymentBill:paymentBillList) {
//                    if(paymentBill.getApproveStatus() == ApproveStatus.APPROVE_REAPPLY || paymentBill.getApproveStatus() == ApproveStatus.APPROVE_ONGOING){
//                        return ResultBean.getFailed().setD(messageService.getMessage("MSG_PAYMENTBILL_NOTCOMPLETE"));
//                    }
//                }
//            }
//        }


        List<String> errMsg = new ArrayList<String>();
        ResultBean<PaymentBillSubmissionBean> paymentBillCheckNull = this.submitPaymentBillCheckNull(paymentBillSubmissionBean);
        if(paymentBillCheckNull.failed()){
            errMsg.addAll(paymentBillCheckNull.getL());
        }
        if(errMsg.size() > 0){
            return ResultBean.getFailed().setL(errMsg);
        }
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(paymentBillSubmissionBean.getCustomerTransactionId()).getD();
        PaymentBill paymentBill = mappingService.map(paymentBillSubmissionBean, PaymentBill.class);
        paymentBill.setPaymentAccount(paymentBillSubmissionBean.getPaymentAccount());
        paymentBill.setPaymentAmount(paymentBillSubmissionBean.getPaymentAmount());
        paymentBill.setPaymentType(paymentBillSubmissionBean.getPaymentType());
        paymentBill.setPaymentTime(paymentBillSubmissionBean.getPaymentTime());
        paymentBill.setReceiptAccount(paymentBillSubmissionBean.getReceiptAccount());
        paymentBill.setEmployeeId(customerTransaction.getEmployeeId());
        paymentBill.setLoginUserId(customerTransaction.getLoginUserId());
        paymentBill.setCarDealerId(customerTransaction.getCarDealerId());
        paymentBill.setFeeItemList(paymentBillSubmissionBean.getFeeItemList());
        paymentBill.setChargeStatus(paymentBillSubmissionBean.getChargeStatus());
        paymentBill.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
        paymentBill.setChargeFee(paymentBillSubmissionBean.getChargeFee());
        paymentBill.setLoanServiceStatus(paymentBillSubmissionBean.getLoanServiceStatus());
        paymentBill.setLoanServiceFee(paymentBillSubmissionBean.getLoanServiceFee());
        paymentBill.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
        iCustomerImageFileBizService.actSaveCustomerImages(customerTransaction.getCustomerId(),customerTransaction.getId(),paymentBillSubmissionBean.getCustomerImages());
        PaymentBill paymentBill1 = iPaymentBillService.save(paymentBill);
        this.stratPaymentBillFlow(paymentBill1,paymentBillSubmissionBean.getComment(),"","","");
        return ResultBean.getSucceed().setD(mappingService.map(paymentBill1,PaymentBillSubmissionBean.class));
    }

    public ResultBean<PaymentBillSubmissionBean> submitPaymentBillCheckNull(PaymentBillSubmissionBean paymentBillSubmissionBean){
        ResultBean resultBean = ResultBean.getFailed();
        if(paymentBillSubmissionBean.getPaymentAmount() == null || paymentBillSubmissionBean.getPaymentAmount() == 0){
            resultBean.addL(messageService.getMessage("MSG_PAYMENTBILL_PAYMENTAMOUNTNULL"));
        }
        if(paymentBillSubmissionBean.getReceiptAccount() == null){
            resultBean.addL(messageService.getMessage("MSG_PAYMENTBILL_RECEIPTACCOUNTNULL"));
        }
        if(paymentBillSubmissionBean.getPaymentType() == null){
            resultBean.addL(messageService.getMessage("MSG_PAYMENTBILL_PAYMENTTYPENULL"));
        }
        if(resultBean.getL().size() > 0){
            return resultBean;
        }
        return ResultBean.getSucceed().setD(paymentBillSubmissionBean);
    }

    private ResultBean<PaymentBill> stratPaymentBillFlow(PaymentBill paymentBill,String comment,String userId,String type,String employeeId){
        if(type.equals("PC")){
            paymentBill.setLoginUserId(userId);
            paymentBill.setEmployeeId(employeeId);
        }
        SignInfo signInfo = new SignInfo(paymentBill.getLoginUserId(), paymentBill.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //启动工作流并进行提交操作
        String collectionName = null;
        try {
            collectionName = BusinessExchange.getMongoCollection(paymentBill);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(paymentBill.getBusinessTypeCode(), paymentBill.getId(), paymentBill.getBillTypeCode(), signInfo, collectionName, null, paymentBill.getCustomerTransactionId());
        if (resultBean != null) {
            if (resultBean.isSucceed()) {
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    PaymentBill paymentBill1 = iPaymentBillService.getOne(paymentBill.getId());
                } else {
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_PAYMENTWORKFLOWNULL_SUBMIT"));
                }
            } else if (resultBean.failed()) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_PAYMENTWORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(paymentBill);
    }

    @Override
    public ResultBean<List<PaymentBillListBean>> actGetPaymentBills(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize) {
        Page<PaymentBill> paymentBills  = null;
        if(StringHelper.isBlock(loginUserId)){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PAYMENTBILL_LOGINUSERID_ID_NULL"),loginUserId));
        }
        List<String> transactionIds = iCustomerTransactionBizService.actGetTransactionIdsOnDecompress(loginUserId, isPass).getD();
        if(isPass){
            paymentBills = this.iPaymentBillService.findCompletedItemsByUser(PaymentBill.class, loginUserId, transactionIds, pageIndex, pageSize);
            if(paymentBills ==null || paymentBills.getTotalElements() < 0){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_PAYMENTBILL_LOGINUSERID_HISTORY_NULL"));
            }
        }else {
            paymentBills = this.iPaymentBillService.findPendingItemsByUser(PaymentBill.class, loginUserId, transactionIds, pageIndex, pageSize);
            if(paymentBills ==null || paymentBills.getTotalElements() < 0){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_PAYMENTBILL_LOGINUSERID_NULL"));
            }
        }

        DataPageBean<PaymentBillListBean> destination = new DataPageBean<PaymentBillListBean>();
        destination.setPageSize(paymentBills.getSize());
        destination.setTotalCount(paymentBills.getTotalElements());
        destination.setTotalPages(paymentBills.getTotalPages());
        destination.setCurrentPage(paymentBills.getNumber());
        destination.setResult(this.getPaymentBillList(paymentBills.getContent()));
        return ResultBean.getSucceed().setD(destination);
    }

    private List<PaymentBillListBean> getPaymentBillList(List<PaymentBill> paymentBills){
        List<PaymentBillListBean> paymentBillList = new ArrayList<PaymentBillListBean>();
        for (PaymentBill paymentBill:paymentBills) {

            PaymentBillListBean paymentBillListBean = mappingService.map(paymentBill, PaymentBillListBean.class);
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(paymentBill.getCustomerTransactionId()).getD();

            paymentBillListBean.setPaymentAmount(paymentBill.getPaymentAmount());
            paymentBillListBean.setPaymentType(paymentBill.getPaymentType());
            paymentBillListBean.setPaymentTime(paymentBill.getPaymentTime());
            paymentBillListBean.setBusinessTypeCode(paymentBill.getBusinessTypeCode());
            paymentBillListBean.setApproveStatus(paymentBill.getApproveStatus());
            paymentBillListBean.setTs(paymentBill.getTs());
            paymentBillListBean.setId(paymentBill.getId());

            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanByTransactionId(paymentBill.getCustomerTransactionId()).getD();
            paymentBillListBean.setCompensatoryInterest(customerLoanBean.getCompensatoryInterest());

            //客户信息
            CustomerBean customer = iCustomerBizService.actGetCustomerById(paymentBill.getCustomerId()).getD();
            if(customer != null){
                paymentBillListBean.setCustomerName(customer.getName());
                paymentBillListBean.setIdentifyNo(customer.getIdentifyNo());
                paymentBillListBean.setCell(customer.getCells().get(0));
            }

            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(customerTransaction.getId()).getD();
            if(customerCarBean != null){
                paymentBillListBean.setCarColor(customerCarBean.getCarColorName());
                paymentBillListBean.setCarEmissions(customerCarBean.getMl());
                CarTypeBean carTypeBean = iCarTypeBizService.actGetCarTypeById(customerCarBean.getCarTypeId()).getD();
                if(carTypeBean != null){
                    paymentBillListBean.setFullName(carTypeBean.getFullName());
                }
            }

            //经销商信息
            CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(paymentBill.getCarDealerId()).getD();
            if(carDealer != null){
                paymentBillListBean.setCardealerName(carDealer.getName());
                paymentBillListBean.setCarDealerAddress(carDealer.getAddress());
            }
            paymentBillList.add(paymentBillListBean);
        }
        return paymentBillList;
    }

    @Override
    public ResultBean<PaymentBillBean> actGetPaymentBill(String paymentBillId) {
        PaymentBill paymentBill = iPaymentBillService.getAvailableOne(paymentBillId);
        if (paymentBill != null){
            BillTypeBean billTypeBean = iBaseDataBizService.actGetBillType(paymentBill.getBillTypeCode()).getD();
            PaymentBillBean paymentBillBean = mappingService.map(paymentBill, PaymentBillBean.class);
            paymentBillBean.setBillType(billTypeBean);
            return ResultBean.getSucceed().setD(paymentBillBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PaymentBillBean> actSearchPaymentBill(String userId, SearchBean searchBean) {
        Page<PaymentBill> paymentBillPage = iPaymentBillService.findAllBySearchBean(PaymentBill.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(paymentBillPage,PaymentBillBean.class));
    }

    @Override
    public ResultBean<PaymentBillBean> actSignPaymentBill(String paymentBillId, SignInfo signInfo) {

        try {
            ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSignBill(paymentBillId, signInfo);
            if(resultBean.failed()){
                return ResultBean.getFailed().setM(resultBean.getM());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }

        PaymentBill paymentBill = iPaymentBillService.getOne(paymentBillId);
        if(signInfo.getResult() == ApproveStatus.APPROVE_PASSED){
            paymentBill.setApproveStatus(ApproveStatus.APPROVE_PASSED);
            iPaymentBillService.save(paymentBill);
        }

        return ResultBean.getSucceed().setD(mappingService.map(paymentBill,PaymentBillBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }


    @Override
    public ResultBean<PaymentBillBean> actGetPaymentBillInfo(String paymentBillId) {
        PaymentBillSubmissionBean paymentBillSubmissionBean = this.getPaymentBillSubmissionInfo(paymentBillId);
        if(paymentBillSubmissionBean != null){
            return ResultBean.getSucceed().setD(paymentBillSubmissionBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Integer> actGetPaymentBillCount(String transactionId) {
        List<PaymentBill> paymentBills = iPaymentBillService.findByCustomerTransactionIdAndDataStatus(transactionId, DataStatus.SAVE);
        return ResultBean.getSucceed().setD(paymentBills.size());
    }

    @Override
    public ResultBean<PaymentBillBean> actGetPaymentBillsByTransactionId(String transactionId) {
        List<PaymentBill> paymentBills = iPaymentBillService.findByCustomerTransactionIdAndDataStatus(transactionId, DataStatus.SAVE);
        return ResultBean.getSucceed().setD(mappingService.map(paymentBills,PaymentBillBean.class));
    }

    @Override
    public ResultBean<PaymentBillBean> actCreatePaymentBillByDecompress(String transactionId,String userId,String type) {
        DecompressBill decompressBill = iDecompressService.findByCustomerTransactionId(transactionId);

        if(decompressBill == null){
            return ResultBean.getFailed();
        }
        PaymentBill paymentBill = new PaymentBill();
        PaymentBill paymentBill1 = iPaymentBillService.getPaymentBillByPayContentTypeAndCustomerTransactionId("PLEDGEPAY", transactionId);
        Boolean flag = true;
        if(paymentBill1 != null){
            flag = false;
            paymentBill = paymentBill1;
        }
        paymentBill.setPaymentAmount(decompressBill.getDecompressAmount());
        paymentBill.setPaymentTime(decompressBill.getDecompressTime());
        paymentBill.setPaymentType(decompressBill.getPaymentType());
        paymentBill.setReceiptAccount(decompressBill.getReceiptAccount());
        paymentBill.setPaymentAccount(decompressBill.getPaymentAccount());
        paymentBill.setPayContentType("PLEDGEPAY");
        String employeeId = null;
        if(type.equals("PC")){
            paymentBill.setLoginUserId(userId);
            EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(userId).getD();
            if(employeeBean != null){
                employeeId = employeeBean.getId();
                paymentBill.setEmployeeId(employeeBean.getId());
            }
        }else if(type.equals("PAD")){
            paymentBill.setLoginUserId(decompressBill.getLoginUserId());
            paymentBill.setEmployeeId(decompressBill.getEmployeeId());
        }

        paymentBill.setCustomerId(decompressBill.getCustomerId());
        paymentBill.setCarDealerId(decompressBill.getCarDealerId());
        paymentBill.setCustomerTransactionId(decompressBill.getCustomerTransactionId());
        paymentBill.setBusinessTypeCode(decompressBill.getBusinessTypeCode());
        PaymentBill save = iPaymentBillService.save(paymentBill);
        if(flag){
            this.stratPaymentBillFlow(save,decompressBill.getComment(),userId,type,employeeId);
        }
        return ResultBean.getSucceed().setD(mappingService.map(save,PaymentBillBean.class));
    }
}
