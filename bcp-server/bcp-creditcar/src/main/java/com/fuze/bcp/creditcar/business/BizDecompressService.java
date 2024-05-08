package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillBean;
import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillListBean;
import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillBean;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.creditcar.service.IDecompressBizService;
import com.fuze.bcp.api.creditcar.service.IPaymentBillBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.BusinessExchange;
import com.fuze.bcp.creditcar.domain.DecompressBill;
import com.fuze.bcp.creditcar.service.IDecompressService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Liu} on 2018/3/8.
 */
@Service
public class BizDecompressService implements IDecompressBizService{

    private static final Logger logger = LoggerFactory.getLogger(BizDecompressService.class);

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IDecompressService iDecompressService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    IPaymentBillBizService paymentBillBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IPaymentBillBizService iPaymentBillBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    MappingService mappingService;


    @Override
    public ResultBean<DecompressBillSubmissionBean> actCreateDecompress(String transactionId) {
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId).getD();
        if(customerTransaction == null){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
        //DecompressBill decompressBill = iDecompressService.findByCustomerTransactionId(transactionId);
        DecompressBillSubmissionBean decompressBillSubmissionBean = new DecompressBillSubmissionBean();
//        if(decompressBill == null){
            decompressBillSubmissionBean = this.initDecompressBillByTransaction(customerTransaction);
            List<ImageTypeFileBean> imageTypeFileBeen = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(decompressBillSubmissionBean.getCustomerId(),
                    decompressBillSubmissionBean.getCustomerTransactionId(),
                    decompressBillSubmissionBean.getBusinessTypeCode(), "A031").getD();
            decompressBillSubmissionBean.setCustomerImages(imageTypeFileBeen);
            if(decompressBillSubmissionBean == null){
                logger.info(String.format(messageService.getMessage("MSG_DECOMPRESSBill_CREATEORDERSUCCEED"), decompressBillSubmissionBean.getCustomerTransactionId()));
            }
            return ResultBean.getSucceed().setD(decompressBillSubmissionBean);
//        }else {
//            decompressBillSubmissionBean = this.initDecompressBillByTransaction(customerTransaction);
//            List<ImageTypeFileBean> imageTypeFileBeen = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(decompressBillSubmissionBean.getCustomerId(),
//                    decompressBillSubmissionBean.getCustomerTransactionId(),
//                    decompressBillSubmissionBean.getBusinessTypeCode(), "A031").getD();
//            decompressBillSubmissionBean.setCustomerImages(imageTypeFileBeen);
//            decompressBillSubmissionBean.setPaymentAccount(decompressBill.getPaymentAccount());
//            decompressBillSubmissionBean.setPaymentType(decompressBill.getPaymentType());
//            decompressBillSubmissionBean.setDecompressType(decompressBill.getDecompressType());
//            decompressBillSubmissionBean.setDecompressTime(decompressBill.getDecompressTime());
//            decompressBillSubmissionBean.setReceiptAccount(decompressBill.getReceiptAccount());
//            decompressBillSubmissionBean.setDecompressAmount(decompressBill.getDecompressAmount());
//            decompressBillSubmissionBean.setId(decompressBill.getId());
//            return ResultBean.getSucceed().setD(decompressBillSubmissionBean);
//        }

    }

    private DecompressBillSubmissionBean initDecompressBillByTransaction(CustomerTransactionBean customerTransaction){
        DecompressBillSubmissionBean decompressBillSubmissionBean = new DecompressBillSubmissionBean();
        decompressBillSubmissionBean.setCustomerId(customerTransaction.getCustomerId());
        decompressBillSubmissionBean.setCustomerTransactionId(customerTransaction.getId());
        decompressBillSubmissionBean.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
        decompressBillSubmissionBean.setCarDealerId(customerTransaction.getCarDealerId());
        return decompressBillSubmissionBean;
    }

    private ResultBean<DecompressBillSubmissionBean> saveDecompressBill(DecompressBillSubmissionBean decompressBillSubmissionBean,CustomerTransactionBean customerTransaction){
        if(decompressBillSubmissionBean != null){
            DecompressBill decompressBill = mappingService.map(decompressBillSubmissionBean, DecompressBill.class);
            decompressBill.setLoginUserId(customerTransaction.getLoginUserId());
            decompressBill.setEmployeeId(customerTransaction.getEmployeeId());
            DecompressBill save = iDecompressService.tempSave(decompressBill);
            return ResultBean.getSucceed().setD(mappingService.map(save,DecompressBillSubmissionBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DecompressBillSubmissionBean> actSubmitDecompress(DecompressBillSubmissionBean decompressBillSubmissionBean) {
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(decompressBillSubmissionBean.getCustomerTransactionId()).getD();
        if(customerTransaction == null){
            return ResultBean.getFailed();
        }
        if(customerTransaction.getStatus() != CustomerTransactionBean.TRANSACTION_FINISH){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_TRANSACTION_NOTCOMPLETE"));
        }

        DecompressBill bill = iDecompressService.findByCustomerTransactionId(decompressBillSubmissionBean.getCustomerTransactionId());
        if(decompressBillSubmissionBean.getId() == null){
            if(bill != null && bill.getApproveStatus() == ApproveStatus.APPROVE_REAPPLY){
                return  ResultBean.getFailed().setM(messageService.getMessage("MSG_DECOMPRESS_ONGOING"));
            }
        }
        if(bill != null && (bill.getApproveStatus() == ApproveStatus.APPROVE_ONGOING || bill.getApproveStatus() == ApproveStatus.APPROVE_PASSED)){
            return  ResultBean.getFailed().setM(messageService.getMessage("MSG_DECOMPRESS_ONGOING"));
        }

        if(decompressBillSubmissionBean.getId() != null){
            ResultBean result = iDecompressService.getEditableBill(decompressBillSubmissionBean.getId());
            if(result.failed()) return result;
        }
        if(decompressBillSubmissionBean.getId() != null){
            DecompressBill decompressBill = iDecompressService.getAvailableOne(decompressBillSubmissionBean.getId());
            if(decompressBill != null && decompressBill.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_DECOMPRESSBILL_NOTMORESUBMIT"));
            }
        }
        List<String> errMsg = new ArrayList<String>();
        ResultBean<DecompressBillSubmissionBean> billCheckNull = this.sbmitDecompressBillCheckNull(decompressBillSubmissionBean);
        if(billCheckNull.failed()){
            errMsg.addAll(billCheckNull.getL());
        }
        if(errMsg.size() > 0){
            return ResultBean.getFailed().setL(errMsg);
        }
        DecompressBill decompressBill = mappingService.map(decompressBillSubmissionBean, DecompressBill.class);
        decompressBill.setDecompressAmount(decompressBillSubmissionBean.getDecompressAmount());
        decompressBill.setDecompressTime(decompressBillSubmissionBean.getDecompressTime());
        decompressBill.setDecompressType(decompressBillSubmissionBean.getDecompressType());
        decompressBill.setPaymentAccount(decompressBillSubmissionBean.getPaymentAccount());
        decompressBill.setReceiptAccount(decompressBillSubmissionBean.getReceiptAccount());
        decompressBill.setLoginUserId(customerTransaction.getLoginUserId());
        decompressBill.setCarDealerId(customerTransaction.getCarDealerId());
        decompressBill.setCustomerId(customerTransaction.getCustomerId());
        decompressBill.setEmployeeId(customerTransaction.getEmployeeId());
        decompressBill.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
        iCustomerImageFileBizService.actSaveCustomerImages(customerTransaction.getCustomerId(),customerTransaction.getId(),decompressBillSubmissionBean.getCustomerImages());
        DecompressBill save = iDecompressService.save(decompressBill);
        DecompressBill decompressBill1 = this.startFlowDecompressBill(save, decompressBillSubmissionBean.getComment()).getD();
        if(decompressBill1 != null){
            decompressBill1.setStatus(DecompressBillBean.DECOMPRESSSTATUS_SUBMIT);
            DecompressBill decompressBill2 = iDecompressService.save(decompressBill1);
            if(decompressBill2 != null){
                try{
                    String type = "PAD";
                    paymentBillBizService.actCreatePaymentBillByDecompress(decompressBill2.getCustomerTransactionId(),"",type);
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error(String.format(messageService.getMessage("MSG_PAYMENTBILL_FAILURE")),e);
                }
            }
        }
        logger.info(decompressBill1.getBillTypeCode() + ":" + decompressBill1.getId() + messageService.getMessage("MSG_DECOMPRESSBILL_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(decompressBill1,DecompressBillSubmissionBean.class)).setM(messageService.getMessage("MSG_DECOMPRESSBILL_SUBMIT"));
    }

    public ResultBean<DecompressBillSubmissionBean> sbmitDecompressBillCheckNull(DecompressBillSubmissionBean decompressBillSubmissionBean){
        ResultBean resultBean = ResultBean.getFailed();
        if(decompressBillSubmissionBean.getDecompressAmount() == null || decompressBillSubmissionBean.getDecompressAmount() == 0){
            resultBean.addL(messageService.getMessage("MSG_DECOMPRESSBILL_DECOMPRESSAMOUNTNULL"));
        }
        if(decompressBillSubmissionBean.getReceiptAccount() == null){
            resultBean.addL(messageService.getMessage("MSG_DECOMPRESSBILL_RECEIPTACCOUNTNULL"));
        }
        if(decompressBillSubmissionBean.getPaymentType() == null){
            resultBean.addL(messageService.getMessage("MSG_DECOMPRESSBILL_PAYMENTTYPENULL"));
        }
        if(resultBean.getL().size() > 0){
            return resultBean;
        }
        return ResultBean.getSucceed().setD(decompressBillSubmissionBean);
    }

    /**
     *解押流程
     */
    private ResultBean<DecompressBill> startFlowDecompressBill(DecompressBill decompressBill,String comment){
        SignInfo signInfo = new SignInfo(decompressBill.getLoginUserId(), decompressBill.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //启动工作流并进行提交操作
        String collectionName = null;
        try {
            collectionName = BusinessExchange.getMongoCollection(decompressBill);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(decompressBill.getBusinessTypeCode(), decompressBill.getId(), decompressBill.getBillTypeCode(), signInfo, collectionName, null, decompressBill.getCustomerTransactionId());
        if (resultBean != null) {
            if (resultBean.isSucceed()) {
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    decompressBill = iDecompressService.getOne(decompressBill.getId());
                } else {
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_PAYMENTWORKFLOWNULL_SUBMIT"));
                }
            } else if (resultBean.failed()) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_PAYMENTWORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(decompressBill);
    }

    @Override
    public ResultBean<DecompressBillListBean> actGetDecompressInfo(String decompressId) {
        DecompressBillSubmissionBean info = this.getDecompressBillSubmissionInfo(decompressId);
        if(info != null){
            return ResultBean.getSucceed().setD(info);
        }
        return ResultBean.getFailed();
    }

    private DecompressBillSubmissionBean getDecompressBillSubmissionInfo(String decompressId){
        DecompressBill decompressBill = iDecompressService.getAvailableOne(decompressId);
        DecompressBillSubmissionBean decompressBillSubmissionBean = new DecompressBillSubmissionBean();
        if(decompressBill != null){
            decompressBillSubmissionBean.setId(decompressBill.getId());
            decompressBillSubmissionBean.setCustomerId(decompressBill.getCustomerId());
            decompressBillSubmissionBean.setCustomerTransactionId(decompressBill.getCustomerTransactionId());
            decompressBillSubmissionBean.setPaymentAccount(decompressBill.getPaymentAccount());
            decompressBillSubmissionBean.setReceiptAccount(decompressBill.getReceiptAccount());
            decompressBillSubmissionBean.setDecompressType(decompressBill.getDecompressType());
            decompressBillSubmissionBean.setDecompressTime(decompressBill.getDecompressTime());
            decompressBillSubmissionBean.setPaymentType(decompressBill.getPaymentType());
            decompressBillSubmissionBean.setDecompressType(decompressBill.getDecompressType());
            decompressBillSubmissionBean.setDecompressAmount(decompressBill.getDecompressAmount());
            decompressBillSubmissionBean.setCustomerImages(iCustomerImageFileBizService.actGetBillImageTypesWithFiles(decompressBill.getCustomerId(),decompressBill.getCustomerTransactionId(),decompressBill.getBusinessTypeCode(),decompressBill.getBillTypeCode()).getD());
            return decompressBillSubmissionBean;
        }
        return null;
    }

    @Override
    public ResultBean<DecompressBillListBean> actGetDecomresss(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize) {
        Page<DecompressBill> decompressBills = null;
        if(StringHelper.isBlock(loginUserId)){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_DECOMRESS_LOGINUSERID_ID_NULL"),loginUserId));
        }
        List<String> transactionIds = iCustomerTransactionBizService.actGetTransactionIdsOnDecompress(loginUserId, isPass).getD();
        if(isPass){
            decompressBills = this.iDecompressService.findCompletedItemsByUser(DecompressBill.class, loginUserId, transactionIds, pageIndex, pageSize);
            if(decompressBills ==null || decompressBills.getTotalElements() < 0){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_DECOMRESS_LOGINUSERID_HISTORY_NULL"));
            }
        }else {
            decompressBills = this.iDecompressService.findPendingItemsByUser(DecompressBill.class, loginUserId, transactionIds, pageIndex, pageSize);
            if(decompressBills ==null || decompressBills.getTotalElements() < 0){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_DECOMRESS_LOGINUSERID_NULL"));
            }
        }

        DataPageBean<DecompressBillListBean> destination = new DataPageBean<DecompressBillListBean>();
        destination.setPageSize(decompressBills.getSize());
        destination.setTotalCount(decompressBills.getTotalElements());
        destination.setTotalPages(decompressBills.getTotalPages());
        destination.setCurrentPage(decompressBills.getNumber());
        destination.setResult(this.getDecompressBillList(decompressBills.getContent()));
        return ResultBean.getSucceed().setD(destination);
    }
    private List<DecompressBillListBean> getDecompressBillList(List<DecompressBill> decompressBills){
        List<DecompressBillListBean> decompressBillList = new ArrayList<DecompressBillListBean>();
        for (DecompressBill decompressBill:decompressBills) {
            DecompressBillListBean decompressBillListBean = mappingService.map(decompressBill, DecompressBillListBean.class);
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(decompressBill.getCustomerTransactionId()).getD();
            //客户信息
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerTransaction.getCustomerId()).getD();
            if(customerBean != null){
                decompressBillListBean.setCustomerName(customerBean.getName());
                decompressBillListBean.setIdentifyNo(customerBean.getIdentifyNo());
                if(customerBean.getCells() != null && customerBean.getCells().size() > 0){
                    decompressBillListBean.setCell(customerBean.getCells().get(0));
                }
            }
            //车信息
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(customerTransaction.getId()).getD();
            if(customerCarBean != null){
                decompressBillListBean.setCarColor(customerCarBean.getCarColorName());
                decompressBillListBean.setCarEmissions(customerCarBean.getMl());
                CarTypeBean carTypeBean = iCarTypeBizService.actGetCarTypeById(customerCarBean.getCarTypeId()).getD();
                if(carTypeBean != null){
                    decompressBillListBean.setFullName(carTypeBean.getFullName());
                }
            }

            //经销商信息
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(customerTransaction.getCarDealerId()).getD();
            if(carDealerBean != null){
                decompressBillListBean.setCardealerName(carDealerBean.getName());
                decompressBillListBean.setCarDealerAddress(carDealerBean.getAddress());
            }

            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanByTransactionId(customerTransaction.getId()).getD();
            if(customerLoanBean != null){
                decompressBillListBean.setCompensatoryInterest(customerLoanBean.getCompensatoryInterest());
            }
            decompressBillListBean.setDecompressAmount(decompressBill.getDecompressAmount());
            List<Map<String,String>> decompress_type = (List<Map<String,String>>) iParamBizService.actGetList("DECOMPRESS_TYPE").getD();
            for (Map map : decompress_type){
                if(map.get("code").equals(decompressBill.getDecompressType())){
                    decompressBillListBean.setDecompressType((String) map.get("name"));
                }
            }
            decompressBillListBean.setDecompressTime(decompressBill.getDecompressTime());
            decompressBillListBean.setApproveStatus(decompressBill.getApproveStatus());
            decompressBillListBean.setTs(decompressBill.getTs());
            decompressBillListBean.setCustomerTransactionId(customerTransaction.getId());
            decompressBillListBean.setBusinessTypeCode(customerTransaction.getBusinessTypeCode());
            decompressBillList.add(decompressBillListBean);
        }
        return decompressBillList;
    }

    @Override
    public ResultBean<DecompressBillBean> actSearchDecomresss(String userId, SearchBean searchBean) {
        Page<DecompressBill> decompressBillPage = iDecompressService.findAllBySearchBean(DecompressBill.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(decompressBillPage,DecompressBillBean.class));
    }

    @Override
    public ResultBean<DecompressBillBean> actGetDecompress(String decompressId,Integer dataStatus) {
        DecompressBill decompressBill;
        if (dataStatus == 0){
            decompressBill = iDecompressService.getOne(decompressId);
        }else {
            decompressBill = iDecompressService.getAvailableOne(decompressId);
        }
        if(decompressBill != null){
            BillTypeBean billTypeBean = iBaseDataBizService.actGetBillType(decompressBill.getBillTypeCode()).getD();
            DecompressBillBean decompressBillBean = mappingService.map(decompressBill, DecompressBillBean.class);
            decompressBillBean.setBillType(billTypeBean);
            return ResultBean.getSucceed().setD(decompressBillBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DecompressBillBean> actSaveDecompress(DecompressBillBean decompressBillBean,String userId) {
        if(decompressBillBean.getDataStatus() == 0){
            decompressBillBean.setDataStatus(1);
        }
        decompressBillBean.setApproveStatus(ApproveStatus.APPROVE_PASSED);
        decompressBillBean.setStatus(DecompressBillBean.DECOMPRESSSTATUS_CONFIRM);
        decompressBillBean.setApproveDate(DateTimeUtils.getCreateTime());
        decompressBillBean.setApproveUserId(userId);
        decompressBillBean.setApproveDate(DateTimeUtils.getCreateTime());
        DecompressBill decompressBill = iDecompressService.save(mappingService.map(decompressBillBean, DecompressBill.class));
        if(decompressBill != null){
            PaymentBillBean pc = iPaymentBillBizService.actCreatePaymentBillByDecompress(decompressBill.getCustomerTransactionId(), userId, "PC").getD();
        }
        return ResultBean.getSucceed().setD(mappingService.map(decompressBill,DecompressBillBean.class));
    }

    @Override
    public ResultBean<DecompressBillBean> actSignDecompress(String decompressId, SignInfo signInfo) {
        //提交审核任务
        try {
            ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSignBill(decompressId, signInfo);
            if(resultBean.failed()){
                return ResultBean.getFailed().setM(resultBean.getM());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }
        //获取解押信息
        DecompressBill decompressBill = iDecompressService.getOne(decompressId);
        //通过的话将业务状态改为已确认
        if(signInfo.getResult() == ApproveStatus.APPROVE_PASSED){
            decompressBill.setStatus(DecompressBillBean.DECOMPRESSSTATUS_CONFIRM);
            decompressBill = iDecompressService.save(decompressBill);
        }
        return ResultBean.getSucceed().setD(mappingService.map(decompressBill,DecompressBillBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    @Override
    public ResultBean<DecompressBillBean> actGetDecompressByTransactionId(String customerTransactionId) {
        DecompressBill decompressBill = iDecompressService.findByCustomerTransactionId(customerTransactionId);
        return ResultBean.getSucceed().setD(mappingService.map(decompressBill,DecompressBillBean.class));
    }

    @Override
    public ResultBean<DecompressBillBean> actCreateDecompressOnPc(String transactionId, String userId) {
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(transactionId).getD();
        if(customerTransaction == null){
            return ResultBean.getFailed();
        }
        DecompressBill decompressBill = iDecompressService.findByCustomerTransactionId(transactionId);
        if(decompressBill == null){
            DecompressBillSubmissionBean decompressBillSubmissionBean = this.initDecompressBillByTransaction(customerTransaction);
            decompressBillSubmissionBean.setLoginUserId(userId);
            EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(userId).getD();
            if(employeeBean != null){
                decompressBillSubmissionBean.setEmployeeId(employeeBean.getId());
            }
            DecompressBill decompressBill1 = iDecompressService.tempSave(mappingService.map(decompressBillSubmissionBean, DecompressBill.class));
            if(decompressBill1 != null){
                return ResultBean.getSucceed().setD(mappingService.map(decompressBill1,DecompressBillBean.class));
            }
        }else {
            return ResultBean.getSucceed().setD(mappingService.map(decompressBill,DecompressBillBean.class));
        }
        return null;
    }
}
