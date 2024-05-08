package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistryListBean;
import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistrySubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarRegistryBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/8/19.
 */
@Service
public class BizCarRegistryService implements ICarRegistryBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCarRegistryService.class);

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    ICarRegistryService iCarRegistryService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IAppointPaymentService iAppointPaymentService;

    @Autowired
    ISwipingCardService iSwipingCardService;

    @Autowired
    IAppointSwipingCardService iAppointSwipingCardService;

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    public ResultBean<CarRegistryBean> actCreateCarRegistry(String paymentId) {
        //TODO 垫资通过，在保证垫资项中有贷款额的情况下可以直接创建车辆上牌，若垫资项中只有贴息额必须要渠道刷卡通过才可以创建车辆上牌
        //监控垫资完成后自动创建车辆上牌信息
        AppointPayment appointPayment = iAppointPaymentService.getOne(paymentId);
        if(appointPayment != null){
            if(appointPayment.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                logger.error(messageService.getMessage("MSG_CARREGISTRY_NOCREATE"));
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARREGISTRY_NOCREATE"));
            }
            if(appointPayment.getPaymentType().equals("1") || appointPayment.getPaymentType().equals("2")){
                initCarRegistry(appointPayment);
            }else if(appointPayment.getPaymentType().equals("0")){
                SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(appointPayment.getCustomerTransactionId());
                if(swipingCard != null){
                    if(swipingCard.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                        initCarRegistry(appointPayment);
                    }
                }else{
                    //TODO 发送MQ提示用户若只有垫贴息额且渠道刷卡未完成则不能直接创建车辆上牌信息，需要渠道刷卡完成才可以创建

                    Map sendMap = new HashMap<>();
                    List<String> toList = new ArrayList<>();
                    if (appointPayment.getEmployeeId() != null) {
                        toList.add(appointPayment.getEmployeeId());
                    }
                    sendMap.put("bd_employee", toList);
                    MsgRecordBean msgRecordBean = new MsgRecordBean("NC_A005_CarRegistry_NoCreate", appointPayment.getCustomerTransactionId(), null, null, sendMap);
                    iAmqpBizService.actSendMq("NC_A005_CarRegistry_NoCreate", new Object[]{appointPayment.getId()}, msgRecordBean);
                }
            }

        }
        return null;
    }

    /**
     * 初始化车辆上牌(渠道刷卡确认后)
     * @param swipingCardId
     * @return
     */
    @Override
    public ResultBean<CarRegistryBean> actCreateCarRegistryBySwipingCardId(String swipingCardId) {
        //TODO 刷卡通过，可以直接创建
        //监控渠道刷卡完成后自动创建车辆上牌信息
        SwipingCard swipingCard = iSwipingCardService.getOne(swipingCardId);
        if(swipingCard != null){
            if(swipingCard.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                logger.error(messageService.getMessage("MSG_CARREGISTRY_NOCREATE"));
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARREGISTRY_NOCREATE"));
            }
            initCarRegistry(swipingCard);
        }
        return null;
    }

    private void initCarRegistry(BaseBillEntity bill) {
        CarRegistry carRegistry = iCarRegistryService.findByCustomerTransactionId(bill.getCustomerTransactionId());
        if (carRegistry == null) {
            carRegistry = new CarRegistry();

            CustomerTransactionBean ct = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(bill.getCustomerTransactionId()).getD();
            carRegistry.setBusinessTypeCode(ct.getBusinessTypeCode());
            carRegistry.setLoginUserId(ct.getLoginUserId());
            carRegistry.setEmployeeId(ct.getEmployeeId());
            carRegistry.setCustomerId(ct.getCustomerId());
            carRegistry.setCustomerTransactionId(ct.getId());
            carRegistry.setCarDealerId(ct.getCarDealerId());
            iCarRegistryService.tempSave(carRegistry);
        }
    }

    @Override
    public ResultBean<CarRegistrySubmissionBean> actSaveCarRegistry(CarRegistrySubmissionBean carRegistrySubmissionBean) {
        ResultBean result = iCarRegistryService.getEditableBill(carRegistrySubmissionBean.getId());
        if (result.failed()) return result;
        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(carRegistrySubmissionBean.getCustomerTransactionId());
        if (result.failed()) return result;
        //PAD端提交数据
        CarRegistry carRegistry = iCarRegistryService.getOne(carRegistrySubmissionBean.getId());
        carRegistry.setPickDate(carRegistrySubmissionBean.getPickDate());
        carRegistry.setRegistryDate(carRegistrySubmissionBean.getRegistryDate());
        carRegistry.setLicenseNumber(carRegistrySubmissionBean.getLicenseNumber());
        carRegistry.setVin(carRegistrySubmissionBean.getVin());
        carRegistry.setCarModelNumber(carRegistrySubmissionBean.getCarModelNumber());
        carRegistry.setRegistryNumber(carRegistrySubmissionBean.getRegistryNumber());
        carRegistry.setMotorNumber(carRegistrySubmissionBean.getMotorNumber());
        carRegistry.setCarDealerId(carRegistrySubmissionBean.getCarDealerId());
        carRegistry.setVehicleType(carRegistrySubmissionBean.getVehicleType());
        //处理档案资料
        iCustomerImageFileBizService.actSaveCustomerImages(carRegistry.getCustomerId(),
                carRegistry.getCustomerTransactionId(),
                carRegistrySubmissionBean.getCustomerImages()); //整体保存档案资料
        carRegistry = iCarRegistryService.save(carRegistry);

        if (carRegistry != null) {
            carRegistrySubmissionBean.setId(carRegistry.getId());
            return ResultBean.getSucceed().setD(carRegistrySubmissionBean).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
    }

    @Override
    public ResultBean<CarRegistryBean> actSubmitCarRegistry(String id,String comment) {
        ResultBean result = iCarRegistryService.getEditableBill(id);
        if (result.failed()) return result;
        CarRegistry carRegistry = (CarRegistry) result.getD();

        // 业务校验
        result = iCustomerTransactionBizService.actGetEditableTransaction(carRegistry.getCustomerTransactionId());
        if (result.failed()) return result;
        //若需要特殊垫资，必须渠道刷卡通过才可以提交上牌
        AppointSwipingCard appointSwipingCard = iAppointSwipingCardService.findByCustomerTransactionId(carRegistry.getCustomerTransactionId());
        if (appointSwipingCard != null) {
            if (appointSwipingCard.getIsNeedLoaning() == 1) {
                SwipingCard swipingCard = iSwipingCardService.findByCustomerTransactionId(appointSwipingCard.getCustomerTransactionId());
                if(swipingCard != null && swipingCard.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARREGISTRY_SWIPINGCARDNOPASSED"));
                }
            }
        }
        //启动车辆上牌流程
        carRegistry = this.startCarRegistry(carRegistry, comment).getD();
        if(carRegistry != null){//更新客户车辆信息
            this.updateCustomerCar(carRegistry);
        }
        logger.info(carRegistry.getBillTypeCode() + ":" + carRegistry.getId() + messageService.getMessage("MSG_CARREGISTRY_SUBMIT"));
        return ResultBean.getSucceed().setD(mappingService.map(carRegistry, CarRegistryBean.class)).setM(messageService.getMessage("MSG_CARREGISTRY_SUBMIT"));
    }

    /**
     * 更新客户车辆信息
     * @param carRegistry
     */
    private void updateCustomerCar(CarRegistry carRegistry){
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(carRegistry.getCustomerTransactionId());
        if(purchaseCarOrder != null){
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(purchaseCarOrder.getCustomerCarId()).getD();
            if(customerCarBean != null){
                customerCarBean.setRegistryDate(carRegistry.getRegistryDate());
                customerCarBean.setLicenseNumber(carRegistry.getLicenseNumber());
                customerCarBean.setVin(carRegistry.getVin());
                customerCarBean.setRegistryNumber(carRegistry.getRegistryNumber());
                customerCarBean.setMotorNumber(carRegistry.getMotorNumber());
                customerCarBean.setCarModelNumber(carRegistry.getCarModelNumber());
                customerCarBean.setVehicleType(carRegistry.getVehicleType());
                iCustomerBizService.actSaveCustomerCar(customerCarBean);
            }
        }
    }

    @Override
    public ResultBean<List<CarRegistryListBean>> actGetCarRegistrys(Boolean isPass,String loginUserId, Integer currentPage, Integer currentSize) {
        Page<CarRegistry> carRegistries = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CARREGISTRY_LOGINUSERID_ID_NULL"), loginUserId));
        }

        List<String> tids = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if(isPass){
            carRegistries = this.iCarRegistryService.findCompletedItemsByUser(CarRegistry.class, loginUserId, tids, currentPage, currentSize);
            if (carRegistries == null || carRegistries.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARREGISTRY_LOGINUSERID_HISTORY_NULL"));
            }
        }else{
            carRegistries = this.iCarRegistryService.findPendingItemsByUser(CarRegistry.class, loginUserId, tids, currentPage, currentSize);
            if (carRegistries == null || carRegistries.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARREGISTRY_LOGINUSERID_NULL"));
            }
        }


        CarRegistryListBean carRegistryListBean = null;
        DataPageBean<CarRegistryListBean> destination = new DataPageBean<CarRegistryListBean>();
        destination.setPageSize(carRegistries.getSize());
        destination.setTotalCount(carRegistries.getTotalElements());
        destination.setTotalPages(carRegistries.getTotalPages());
        destination.setCurrentPage(carRegistries.getNumber());
        for (CarRegistry carRegistry: carRegistries.getContent()) {
            if(carRegistry != null){
                carRegistryListBean = mappingService.map(carRegistry, CarRegistryListBean.class);
                //获取交易概览
                TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(carRegistry.getCustomerTransactionId()).getD();
                transactionSummary.setApproveStatus(carRegistry.getApproveStatus());
                carRegistryListBean.setTransactionSummary(transactionSummary);
            }

            destination.getResult().add(carRegistryListBean);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<CarRegistrySubmissionBean> actInitCarRegistryByTransactionId(String transactionId) {
        CarRegistry carRegistry = iCarRegistryService.findByCustomerTransactionId(transactionId);
        CarRegistrySubmissionBean carRegistrySubmissionBean = mappingService.map(carRegistry, CarRegistrySubmissionBean.class);
        //档案类型

        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(carRegistry.getCustomerId(),
                carRegistry.getCustomerTransactionId(),
                carRegistry.getBusinessTypeCode(),
                carRegistry.getBillTypeCode()).getD();
        carRegistrySubmissionBean.setCustomerImages(imageTypeFiles);

        return ResultBean.getSucceed().setD(carRegistrySubmissionBean);
    }

    @Override
    public ResultBean<CarRegistryBean> actGetCarRegistry(String id) {
        CarRegistry carRegistry = iCarRegistryService.getOne(id);
        if ( carRegistry != null ){
            String billTypeCode = carRegistry.getBillTypeCode();
            BillTypeBean billTypeBean = iBaseDataBizService.actGetBillType(billTypeCode).getD();
            CarRegistryBean carRegistryBean = mappingService.map(carRegistry, CarRegistryBean.class);
            carRegistryBean.setBillType(billTypeBean);
            return ResultBean.getSucceed().setD(carRegistryBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CarRegistryBean> actSearchCarRegistries(String userId, SearchBean searchBean) {
        Page<CarRegistry> registries = iCarRegistryService.findAllBySearchBean(CarRegistry.class, searchBean, SearchBean.STAGE_ORDER, userId);
        return ResultBean.getSucceed().setD(mappingService.map(registries,CarRegistryBean.class));
    }

    @Override
    public ResultBean<CarRegistryBean> actSaveCarRegistry(CarRegistryBean carRegistryBean) {
        CarRegistry carRegistry = mappingService.map(carRegistryBean, CarRegistry.class);
        carRegistry = iCarRegistryService.save(carRegistry);
        return ResultBean.getSucceed().setD(mappingService.map(carRegistry,CarRegistryBean.class));
    }

    /**
     * 提车上牌签批
     * @param id
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<CarRegistryBean> actSignCarRegistry(String id, SignInfo signInfo) {
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
        CarRegistry carRegistry = iCarRegistryService.getOne(id);
        //查询当前单据任务是否完成
        return ResultBean.getSucceed().setD(mappingService.map(carRegistry, CarRegistryBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

    @Override
    public ResultBean<CarRegistryBean> actGetByCustomerTransactionId(String customerTransactionId) {
        CarRegistry carRegistry = iCarRegistryService.findByCustomerTransactionId(customerTransactionId);
        if ( carRegistry != null ){
            return ResultBean.getSucceed().setD(mappingService.map(carRegistry,CarRegistryBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CarRegistryBean> actGetByCustomer(CustomerBean customerBean) {
        CarRegistry carRegistryBean = iCarRegistryService.findByCustomer(customerBean);
        if(carRegistryBean != null){
            return ResultBean.getSucceed().setD(mappingService.map(carRegistryBean,CarRegistryBean.class));
        }
        return ResultBean.getFailed();
    }

    private ResultBean<CarRegistry> startCarRegistry(CarRegistry carRegistry, String comment) {
        SignInfo signInfo = new SignInfo(carRegistry.getLoginUserId(), carRegistry.getEmployeeId(), SignInfo.SIGN_PASS,SignInfo.FLAG_COMMIT, comment);
        //进行审批
        String collectionMame = null;
        try {
            collectionMame = CarTransfer.getMongoCollection(carRegistry);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(carRegistry.getBusinessTypeCode(), carRegistry.getId(), carRegistry.getBillTypeCode(), signInfo, collectionMame, null, carRegistry.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                     carRegistry = iCarRegistryService.getOne(carRegistry.getId());
                     carRegistry.setTs(DateTimeUtils.getCreateTime());
                     carRegistry = iCarRegistryService.save(carRegistry);
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(carRegistry);
    }

    /**
     * 日报
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId ,String date, CarRegistryBean t) {

        Map<Object, Object> dailyReport = iCarRegistryService.getDailyReport(orgId,date, mappingService.map(t, CarRegistry.class));
        if(dailyReport != null){
            return ResultBean.getSucceed().setD(dailyReport);
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Map<Object, Object>> getEmployeeReport(String employeeId, String date, CarRegistryBean t) {
        Map<Object, Object> employeeReport = iCarRegistryService.getEmployeeReport(employeeId,date, mappingService.map(t, CarRegistry.class));
        if(employeeReport != null){
            return ResultBean.getSucceed().setD(employeeReport);
        }
        return ResultBean.getFailed();
    }

}