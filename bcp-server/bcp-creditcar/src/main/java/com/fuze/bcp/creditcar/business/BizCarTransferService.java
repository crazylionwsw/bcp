package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferListBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransferBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.bean.TransactionSummaryBean;
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
import com.fuze.bcp.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 转移过户服务接口实现
 * Created by Lily on 2017/9/14.
 */
@Service
public class BizCarTransferService implements ICarTransferBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCarTransferService.class);

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    ISwipingCardService iSwipingCardService;

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ICarTransferService iCarTransferService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    /**
     * 初始化转移过户
     * @param orderId
     * @return
     */
    @Override
    public ResultBean<CarTransferBean> actCreateCarTransfer(String orderId) {
        //TODO 创建时不要将vin赋值
        PurchaseCarOrder purchaseCarOrder = iOrderService.getOne(orderId);
        if(purchaseCarOrder != null){
            if(purchaseCarOrder.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                logger.error(messageService.getMessage("MSG_CARTRANFER_NOCREATE"));
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARTRANFER_NOCREATE"));
            }
            CarTransfer carTransfer = iCarTransferService.findByCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(purchaseCarOrder.getCustomerTransactionId()).getD();
            if(carTransfer == null){
                carTransfer = new CarTransfer();
                carTransfer.setBusinessTypeCode(purchaseCarOrder.getBusinessTypeCode());
                carTransfer.setLoginUserId(purchaseCarOrder.getLoginUserId());
                carTransfer.setEmployeeId(purchaseCarOrder.getEmployeeId());
                carTransfer.setCustomerId(purchaseCarOrder.getCustomerId());
                carTransfer.setCustomerTransactionId(purchaseCarOrder.getCustomerTransactionId());
                carTransfer.setLicenseNumber(customerCarBean.getLicenseNumber());
                iCarTransferService.tempSave(carTransfer);
            }
        }
        return null;
    }

    /**
     * 转移过户数据暂存
     * @param carTransferSubmissionBean
     * @return
     */
    @Override
    public ResultBean<CarTransferSubmissionBean> actSaveCarTransfer(CarTransferSubmissionBean carTransferSubmissionBean) {
        ResultBean result = iCarTransferService.getEditableBill(carTransferSubmissionBean.getId());
        if (result.failed()) return result;
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(carTransferSubmissionBean.getCustomerTransactionId());
        if (result.failed()) return result;
        //PAD端提交数据
        CarTransfer carTransfer = iCarTransferService.getOne(carTransferSubmissionBean.getId());
        carTransfer.setTransferDate(carTransferSubmissionBean.getTransferDate());
        carTransfer.setRegistryNumber(carTransferSubmissionBean.getRegistryNumber());
        carTransfer.setVin(carTransferSubmissionBean.getVin());
        carTransfer.setLicenseNumber(carTransferSubmissionBean.getLicenseNumber());
        carTransfer.setMotorNumber(carTransferSubmissionBean.getMotorNumber());
        carTransfer.setCarModelNumber(carTransferSubmissionBean.getCarModelNumber());
        //处理档案资料
        iCustomerImageFileBizService.actSaveCustomerImages(carTransfer.getCustomerId(),
                carTransfer.getCustomerTransactionId(),
                carTransferSubmissionBean.getCustomerImages()); //整体保存档案资料
        carTransfer = iCarTransferService.save(carTransfer);
        if (carTransfer != null) {
            carTransferSubmissionBean.setId(carTransfer.getId());
            return ResultBean.getSucceed().setD(carTransferSubmissionBean).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
    }

    /**
     * 提交并进入工作流
     * @param id
     * @param comment
     * @return
     */
    @Override
    public ResultBean<CarTransferBean> actSubmitCarTransfer(String id, String comment) {
        ResultBean result = iCarTransferService.getEditableBill(id);
        if (result.failed()) return result;

        CarTransfer carTransfer = (CarTransfer)result.getD();
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(carTransfer.getCustomerTransactionId());
        if (result.failed()) return result;
        //若是正常垫资，必须在预约垫资通过之后才可以提交转移过户进入审批流
        AppointPayment appointPayment = iAppointPaymentService.findByCustomerTransactionId(carTransfer.getCustomerTransactionId());
        if(appointPayment != null && appointPayment.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARTRANSFER_APPOINTPAYMENTNOPASSED"));
        }
        //若需要特殊垫资，必须渠道刷卡通过才可以转移过户
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(carTransfer.getCustomerTransactionId());
        if (appointSwipingCard != null) {
            if(appointSwipingCard.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                    SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
                    if(swipingCard != null && swipingCard.getApproveStatus() != ApproveStatus.APPROVE_PASSED)
                        return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARTRANSFER_SWIPINGCARDNOPASSED"));
            }else{
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARTRANSFER_APPOINTSWIPINGCARDNOPASSED"));
            }
        }
        // TODO 提交时通过交易ID获取vin码，比较与客户车辆信息中vin码是否一致
        if(carTransfer.getVin() != null){
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(carTransfer.getCustomerTransactionId()).getD();
            if(!carTransfer.getVin().equals(customerCarBean.getVin())){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARTRANSFER_VINERROR"));
            }
        }
        //启动转移过户流程
        carTransfer = this.startCarTransfer(carTransfer, comment).getD();
        if(carTransfer != null){//更新客户车辆信息
            this.updateCustomerCar(carTransfer);
        }
        logger.info(carTransfer.getBillTypeCode() + ":" + carTransfer.getId() + messageService.getMessage("MSG_CARTRANSFER_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(carTransfer, CarTransferBean.class)).setM(messageService.getMessage("MSG_CARTRANSFER_SUBMIT"));
    }

    /**
     * 更新客户车辆信息
     * @param carTransfer
     */
    private void updateCustomerCar(CarTransfer carTransfer){
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(carTransfer.getCustomerTransactionId());
        if(purchaseCarOrder != null){
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
            if(customerCarBean != null){
                customerCarBean.setRegistryDate(carTransfer.getRegistryDate());
                customerCarBean.setLicenseNumber(carTransfer.getLicenseNumber());
                customerCarBean.setVin(carTransfer.getVin());
                customerCarBean.setRegistryNumber(carTransfer.getRegistryNumber());
                customerCarBean.setMotorNumber(carTransfer.getMotorNumber());
                customerCarBean.setCarModelNumber(carTransfer.getCarModelNumber());
                iCustomerBizService.actSaveCustomerCar(customerCarBean);
            }
        }
    }

    @Override
    public ResultBean<List<CarTransferListBean>> actGetCarTransfers(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize) {
        Page<CarTransfer> carTransfers = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CARTRANSFER_LOGINUSERID_ID_NULL"), loginUserId));
        }

        List<String> tids = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if(isPass){
            carTransfers = iCarTransferService.findCompletedItemsByUser(CarTransfer.class, loginUserId, tids, currentPage, currentSize);
            if (carTransfers == null || carTransfers.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARTRANSFER_LOGINUSERID_HISTORY_NULL"));
            }
        }else {
            carTransfers = iCarTransferService.findPendingItemsByUser(CarTransfer.class, loginUserId, tids, currentPage, currentSize);
            if (carTransfers == null || carTransfers.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARTRANSFER_LOGINUSERID_NULL"));
            }
        }

        CarTransferListBean carTransferListBean = null;
        DataPageBean<CarTransferListBean> destination = new DataPageBean<CarTransferListBean>();
        destination.setPageSize(carTransfers.getSize());
        destination.setTotalCount(carTransfers.getTotalElements());
        destination.setTotalPages(carTransfers.getTotalPages());
        destination.setCurrentPage(carTransfers.getNumber());
        for (CarTransfer carTransfer: carTransfers.getContent()) {
            if(carTransfer != null){
                carTransferListBean = mappingService.map(carTransfer, CarTransferListBean.class);
                //获取交易概览
                TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(carTransfer.getCustomerTransactionId()).getD();
                transactionSummary.setApproveStatus(carTransfer.getApproveStatus());
                carTransferListBean.setTransactionSummary(transactionSummary);
            }

            destination.getResult().add(carTransferListBean);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<CarTransferSubmissionBean> actInitCarTransferByTransactionId(String transactionId) {
        CarTransfer carTransfer = iCarTransferService.findByCustomerTransactionId(transactionId);
        CarTransferSubmissionBean carTransferSubmissionBean = mappingService.map(carTransfer, CarTransferSubmissionBean.class);

        //档案类型
        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(carTransfer.getCustomerId(),
                carTransfer.getCustomerTransactionId(),
                carTransfer.getBusinessTypeCode(),
                carTransfer.getBillTypeCode()).getD();
        carTransferSubmissionBean.setCustomerImages(imageTypeFiles);

        return ResultBean.getSucceed().setD(carTransferSubmissionBean);
    }

    @Override
    public ResultBean<CarTransferBean> actGetCarTransfer(String id) {
        CarTransfer carTransfer = iCarTransferService.getOne(id);
        if (carTransfer != null) {
            String code = carTransfer.getBillTypeCode();
            //通过编码获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            CarTransferBean carTransferBean = mappingService.map(carTransfer, CarTransferBean.class);
            carTransferBean.setBillType(billType);
            return ResultBean.getSucceed().setD(carTransferBean);

        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    @Override
    public ResultBean<CarTransferBean> actSaveCarTransfer(CarTransferBean carTransferBean) {
        CarTransfer carTransfer = mappingService.map(carTransferBean, CarTransfer.class);
        carTransfer = iCarTransferService.save(carTransfer);
        return ResultBean.getSucceed().setD(mappingService.map(carTransfer,CarTransferBean.class));
    }

    @Override
    public ResultBean<CarTransferBean> actSearchCarTransfers(String userId, SearchBean searchBean) {
        Page<CarTransfer> carTransfers = iCarTransferService.findAllBySearchBean(CarTransfer.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(carTransfers,CarTransferBean.class));
    }

    @Override
    public ResultBean<CarTransferBean> actGetByCustomerTransactionId(String customerTransactionId) {
        CarTransfer carTransfer = iCarTransferService.findByCustomerTransactionId(customerTransactionId);
        if (carTransfer != null) {
            return ResultBean.getSucceed().setD(mappingService.map(carTransfer, CarTransferBean.class));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
    }

    /**
     * 转移过户签批
     * @param id
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<CarTransferBean> actSignCarTransfer(String id, SignInfo signInfo) {
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
        //获取提车上牌信息
        CarTransfer carTransfer = iCarTransferService.getOne(id);
        //查询当前单据任务是否完成
        return ResultBean.getSucceed().setD(mappingService.map(carTransfer, CarTransferBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    private ResultBean<CarTransfer> startCarTransfer(CarTransfer carTransfer, String comment) {
        SignInfo signInfo = new SignInfo(carTransfer.getLoginUserId(), carTransfer.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT,comment);
        //进行审批
        String collectionMame = null;
        try {
            collectionMame = CarTransfer.getMongoCollection(carTransfer);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(carTransfer.getBusinessTypeCode(), carTransfer.getId(), carTransfer.getBillTypeCode(), signInfo, collectionMame, null, carTransfer.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    carTransfer = iCarTransferService.getOne(carTransfer.getId());
                    carTransfer.setTs(DateTimeUtils.getCreateTime());
                    carTransfer = iCarTransferService.save(carTransfer);
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(carTransfer);
    }

    /**
     * 获取日报信息
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId ,String date, CarTransferBean t) {
        return ResultBean.getSucceed().setD(iCarTransferService.getDailyReport(orgId,date, mappingService.map(t, CarTransfer.class)));
    }

    @Override
    public ResultBean<Map<Object, Object>> getEmployeeReport(String employeeId, String date, CarTransferBean t) {
        Map<Object, Object> employeeReport = iCarTransferService.getEmployeeReport(employeeId,date, mappingService.map(t, CarTransfer.class));
        if(employeeReport != null){
            return ResultBean.getSucceed().setD(employeeReport);
        }
        return ResultBean.getFailed();
    }

}


