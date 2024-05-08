package com.fuze.bcp.creditcar.business;

import com.alibaba.dubbo.rpc.RpcContext;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.*;
import com.fuze.bcp.api.creditcar.service.ICarValuationBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.bean.CustomerCarBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.creditcar.domain.CarValuation;
import com.fuze.bcp.creditcar.domain.CustomerDemand;
import com.fuze.bcp.creditcar.domain.PurchaseCarOrder;
import com.fuze.bcp.creditcar.service.ICarValuationService;
import com.fuze.bcp.creditcar.service.ICustomerDemandService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.service.IJingZhenGuService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Lily on 2017/8/14.
 */
@Service
public class BizCarValuationService implements ICarValuationBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCarValuationService.class);

    @Autowired
    ICarValuationService iCarValuationService;

    @Autowired
    IJingZhenGuService iJingZhenGuService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    private ICustomerImageTypeBizService iCustomerImageTypeBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    MessageService messageService;

    @Autowired
    MappingService mappingService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResultBean<List<ValuationListBean>> actFindCarValuationsByCarDealerId(String carDealerId) {
        //TODO 只有评估通过的并且签约未提交的才可以在下拉框中展示
        List<CarValuation> carValuations = iCarValuationService.findAllByCarDealerId(carDealerId);
        List<ValuationListBean> carValuationBeans = new ArrayList<ValuationListBean>();
        for (CarValuation carValuation : carValuations) {
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByVehicleEvaluateInfoIdAndDataStatus(carValuation.getId(), DataStatus.SAVE);
            if (purchaseCarOrder != null && purchaseCarOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
                    CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(purchaseCarOrder.getCustomerTransactionId()).getD();
                    if (customerTransaction != null && !(customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_FINISH)) {
                        continue;
                    }
            }
            ValuationListBean valuation = mappingService.map(carValuation, ValuationListBean.class);

            //获取车型全称
            CarTypeBean carType = iCarTypeBizService.actGetCarTypeById(carValuation.getCarTypeId()).getD();
            valuation.setCarType(carType.getFullName());
            valuation.setGuidePrice(carType.getPrice());
            if (carValuation.getValuationInfo() != null)
                valuation.setValuationUrl(carValuation.getValuationInfo().getReportUrl());
            carValuationBeans.add(valuation);
        }
        if (carValuationBeans.isEmpty()){
            String code = messageService.getMessage("MSG_CARVALUATION_NULL_ID");
            return ResultBean.getFailed().setM(code);
        }
        return ResultBean.getSucceed().setD(carValuationBeans);
    }

    @Override
    public ResultBean<List<ValuationListBean>> actGetCarValuationsOnBusinessExchange(String carDealerId,String vehicleEvaluateInfoId) {
        List<CarValuation> carValuations = iCarValuationService.findAllByCarDealerId(carDealerId);
        List<ValuationListBean> carValuationBeans = new ArrayList<ValuationListBean>();
        CarValuation availableOne = iCarValuationService.getAvailableOne(vehicleEvaluateInfoId);
        if(availableOne != null){
            carValuationBeans.add(this.getValuationInfo(availableOne));
        }
        for (CarValuation carValuation : carValuations) {
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByVehicleEvaluateInfoIdAndDataStatus(carValuation.getId(), DataStatus.SAVE);
            if (purchaseCarOrder != null && purchaseCarOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
                CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(purchaseCarOrder.getCustomerTransactionId()).getD();
                if (customerTransaction != null && !(customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_FINISH)) {
                    continue;
                }
            }
//            ValuationListBean valuation = mappingService.map(carValuation, ValuationListBean.class);
//
//            //获取车型全称
//            CarTypeBean carType = iCarTypeBizService.actGetCarTypeById(carValuation.getCarTypeId()).getD();
//            valuation.setCarType(carType.getFullName());
//            valuation.setGuidePrice(carType.getPrice());
//            if (carValuation.getValuationInfo() != null)
//                valuation.setValuationUrl(carValuation.getValuationInfo().getReportUrl());
            carValuationBeans.add(this.getValuationInfo(carValuation));
        }
        if (carValuationBeans.isEmpty()){
            String code = messageService.getMessage("MSG_CARVALUATION_NULL_ID");
            return ResultBean.getFailed().setM(code);
        }
        return ResultBean.getSucceed().setD(carValuationBeans);
    }

    private ValuationListBean getValuationInfo(CarValuation carValuation){
        ValuationListBean valuation = mappingService.map(carValuation, ValuationListBean.class);
        //获取车型全称
        CarTypeBean carType = iCarTypeBizService.actGetCarTypeById(carValuation.getCarTypeId()).getD();
        valuation.setCarType(carType.getFullName());
        valuation.setGuidePrice(carType.getPrice());
        if (carValuation.getValuationInfo() != null)
            valuation.setValuationUrl(carValuation.getValuationInfo().getReportUrl());
        return valuation;
    }

    /**
     * 获取评估列表（带分页）
     *
     * @param page
     * @return
     */
    public ResultBean<List<ValuationListBean>> actGetValuations(Integer page) {
        Page<CarValuation> carValuationPage = iCarValuationService.getAvailableCarValuations(page);
        if (carValuationPage == null) {
            return ResultBean.getSucceed();
        }

        List<ValuationListBean> carValuationBeans = this.getValuationList(carValuationPage);


        return ResultBean.getSucceed().setD(carValuationBeans);
    }

    public ResultBean<DataPageBean<ValuationListBean>> actGetValuations(Integer pageindex, Integer pagesize) {
        String operatorId = RpcContext.getContext().getAttachment("OperatorId");

        //获取CarDealer列表
        //iCarDealerBizService.actGetCarDealersByLoginUserId()
        Page<CarValuation> carValuationPage = iCarValuationService.getAvailableCarValuations(operatorId, pageindex, pagesize);
        if (carValuationPage == null) {
            return ResultBean.getSucceed();
        }
        //List<ValuationListBean> valuationList = this.getValuationList(carValuationPage);
        DataPageBean<ValuationListBean> destination = new DataPageBean<ValuationListBean>();
        destination.setPageSize(carValuationPage.getSize());
        destination.setTotalCount(carValuationPage.getTotalElements());
        destination.setTotalPages(carValuationPage.getTotalPages());
        destination.setCurrentPage(carValuationPage.getNumber());
        destination.setResult(mappingService.map(carValuationPage.getContent(),ValuationListBean.class));
        return ResultBean.getSucceed().setD(destination);
    }

    private List<ValuationListBean> getValuationList(Page<CarValuation> carValuationPage) {

        List<CarValuation> carValuations = carValuationPage.getContent();
            List<ValuationListBean> carValuationBeans = new ArrayList<ValuationListBean>();
            int flag = 0;
            for (CarValuation carValuation : carValuations) {
                List<PurchaseCarOrder> purchaseCarOrders = iOrderService.findAllByVehicleEvaluateInfoIdAndDataStatus(carValuation.getId(), DataStatus.SAVE);
                for (PurchaseCarOrder purchaseCarOrder:purchaseCarOrders) {
                    if (purchaseCarOrder != null && purchaseCarOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
                        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(purchaseCarOrder.getCustomerTransactionId()).getD();
                        if (customerTransaction != null && !(customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_FINISH)) {
                            flag = 1;
                            continue;
                        }else{
                            flag = 0;
                        }
                    }
                }
                if(flag == 1){
                    continue;
                }

            ValuationListBean valuation = mappingService.map(carValuation, ValuationListBean.class);

            if (carValuation.getValuationInfo() != null) {
                valuation.setValuationUrl(carValuation.getValuationInfo().getReportUrl());
            }

            //获取车型全称--非空判断
            if (carValuation.getCarTypeId()!= null){
                CarTypeBean carType = iCarTypeBizService.actGetCarTypeById(carValuation.getCarTypeId()).getD();
                valuation.setCarType(carType.getFullName());
            }

            carValuationBeans.add(valuation);
        }
        return carValuationBeans;
    }

    public ResultBean<CarValuationBean> actDoOnlineValuation(CarValuationBean valuation) {
        //TODO: 需要用规则引擎来进行验证
        if (valuation.getCarTypeId() == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CARTYPEBULL")));
        }
        if (valuation.getFirstRegistryDate() == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BUYDATE_NULL")));
        }
        if (valuation.getMileage() == null && valuation.getMileage() <= 0.00) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_MILAGE_NULL")));
        }

        try {
            CarTypeBean carType = iCarTypeBizService.actGetCarTypeById(valuation.getCarTypeId()).getD();

/*            List<CarTypeBean> carTypes = this.actFindCarTypesByVin(valuation.getVin()).getD();
            if (carTypes != null && carTypes.size() > 0)
                carType = carTypes.get(0);*/
            //发送评估接口获取评估结果
            JSONObject jsonObject = iJingZhenGuService.getValuationInfo(carType.getRefStyleId(), valuation.getFirstRegistryDate(), String.valueOf(valuation.getMileage()), valuation.getPvovinceId(), null);
            if (jsonObject != null) {
                ValuationInfo vi = new ValuationInfo();
                vi.setReportUrl(jsonObject.getString("PtvUrl"));
                vi.setEvaluateSourceCode(valuation.getOnlineEvaluateSourceCode());
                //vi.setPrice(jsonObject.getString("B2CPrices"));
                vi.setValuationDate(DateTimeUtils.getCreateTime());
                valuation.setValuationInfo(vi);
            } else {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_VALUTAION"));
            }

        } catch (Exception ex) {
            logger.error(messageService.getMessage("MSG_VALUTAION"), ex);
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_VALUTAION"));
        }

        CarValuation oldValuation = iCarValuationService.getOne(valuation.getId());
        valuation.setCustomerTransactionId(oldValuation.getCustomerTransactionId());

        CarValuation carValuation = iCarValuationService.save(mappingService.map(valuation, CarValuation.class));
        return ResultBean.getSucceed().setD(mappingService.map(carValuation, CarValuationBean.class));
    }

    /**
     * 修改评估数据
     *
     * @param valuation
     * @return
     */
    public ResultBean<Map<String, Object>> actSaveValuation(ValuationSubmissionBean valuation) {
        //通过ID获取已经保存的数据
        CarValuation carValuation = iCarValuationService.getOne(valuation.getId());

        if (valuation.getCustomerImages() != null && valuation.getCustomerImages().size() > 0) {
            //处理档案资料
            List<CustomerImageFileBean> customerImageFileBeanList = iCustomerImageFileBizService.actSaveCustomerImages(valuation.getCustomerImages()).getD();
            List<String> customerImageIds = new ArrayList<String>();
            for (CustomerImageFileBean cifb : customerImageFileBeanList) {
                customerImageIds.add(cifb.getId());
            }

            carValuation.setCustomerImageIds(customerImageIds);
        }
        carValuation.setMaintenanceMileage(valuation.getMaintenanceMileage());
        carValuation.setOnlineEvaluateSourceCode(valuation.getOnlineEvaluateSourceCode());
        carValuation.setInitialValuationPrice(valuation.getInitialValuationPrice());
        carValuation.setPrice(valuation.getPrice());

        carValuation = iCarValuationService.save(carValuation);

        //同步二手车信息到交易信息中
/*        if (carValuation.getCustomerTransactionId() != null) {
            List<CustomerCarBean> customerCars = iCustomerBizService.actGetCustomerCarsByTransactionId(carValuation.getCustomerTransactionId()).getD();
            if (customerCars != null) {
                for(CustomerCarBean car : customerCars) {
                    car.setCarTypeId(carValuation.getCarTypeId());
                    car.setVin(carValuation.getVin());
                    car.setLicenseNumber(carValuation.getLicenceNumber());
                    car.setCarModelNumber(carValuation.getCarModelNumber());
                    car.setMileage(carValuation.getMileage());
                    car.setMaintenanceMileage(carValuation.getMaintenanceMileage());
                    car.setInitialValuationPrice(carValuation.getInitialValuationPrice());
                    car.setEvaluatePrice(carValuation.getPrice());
                    car.setFirstRegistryDate(carValuation.getFirstRegistryDate());

                    iCustomerBizService.actSaveCustomerCar(car);
                }
            }
        }*/
        //更新CustomerCar
        // CustomerDemand
        CustomerDemand cd = iCustomerDemandService.findByVehicleEvaluateInfoId(valuation.getId());
        if (cd != null){
            CustomerCarBean car = iCustomerBizService.actGetCustomerCarById(cd.getCustomerCarId()).getD();
            syncCustomerCar(valuation, car);
        }
        PurchaseCarOrder order = iOrderService.findByVehicleEvaluateInfoId(valuation.getId());
        if (order != null){
            CustomerCarBean car = iCustomerBizService.actGetCustomerCarById(order.getCustomerCarId()).getD();
            syncCustomerCar(valuation, car);
        }

        HashMap map = new HashMap();
        map.put("id", carValuation.getId());
        return ResultBean.getSucceed().setD(map).setM("评估单保存成功！");
    }

    private void syncCustomerCar(ValuationSubmissionBean valuation, CustomerCarBean car) {
        car.setCarTypeId(valuation.getCarTypeId());
        car.setVin(valuation.getVin());
        car.setLicenseNumber(valuation.getLicenceNumber());
        car.setCarModelNumber(valuation.getCarModelNumber());
        car.setMileage(valuation.getMileage());
        car.setMaintenanceMileage(valuation.getMaintenanceMileage());
        car.setInitialValuationPrice(valuation.getInitialValuationPrice());
        car.setEvaluatePrice(valuation.getPrice());
        car.setFirstRegistryDate(valuation.getFirstRegistryDate());

        iCustomerBizService.actSaveCustomerCar(car);
    }

    /**
     * 修改评估数据
     *
     * @param valuation
     * @return
     */
    public ResultBean<Map<String, Object>> actConfirmValuation(ValuationSubmissionBean valuation) {
        this.actSaveValuation(valuation).getD();
        this.actSignValuation(valuation.getId(), ApproveStatus.APPROVE_PASSED);

        HashMap map = new HashMap();
        map.put("id", valuation.getId());
        return ResultBean.getSucceed().setD(map).setM("评估完成！");
    }

    public ResultBean actSaveValuation(CarValuationBean valuation) {
        CarValuation carValuation = mappingService.map(valuation, CarValuation.class);

        iCarValuationService.save(carValuation);
        return ResultBean.getSucceed();
    }

    public ResultBean<CarValuationBean> actNewValuation(CarValuationBean valuation) {

        if (StringUtils.isBlank(valuation.getVin()) && StringUtils.isBlank(valuation.getCarTypeId())) {
            return ResultBean.getFailed().setM("VIN不能為空");
        }
        CarValuation carValuation = iCarValuationService.findAvailableOneByVin(valuation.getVin());
        if (carValuation == null) {
            carValuation = mappingService.map(valuation, CarValuation.class);
            carValuation = iCarValuationService.save(carValuation);
        }else{
            if(carValuation != null && carValuation.getApproveStatus() != ApproveStatus.APPROVE_PASSED){
                carValuation.setLicenceNumber(valuation.getLicenceNumber());
                carValuation.setCarModelNumber(valuation.getCarModelNumber());
                carValuation.setMileage(valuation.getMileage());
                carValuation.setCarTypeId(valuation.getCarTypeId());
                carValuation.setFirstRegistryDate(valuation.getFirstRegistryDate());
                carValuation.setInitialValuationPrice(valuation.getInitialValuationPrice());
                carValuation.setMaintenanceMileage(valuation.getMaintenanceMileage());
                carValuation = iCarValuationService.save(carValuation);
            }

        }

        return ResultBean.getSucceed().setD(mappingService.map(carValuation, CarValuationBean.class));
    }

    /***
     * 校验VIN
     * @param vin
     * @return
     */
    public ResultBean actCheckValuation(String vin) {
        CarValuation carValuation = iCarValuationService.findAvailableOneByVin(vin);
        if (carValuation != null) {
            //是否已签约
//            if (carValuation.getFinishOrder().equals(Boolean.TRUE)) {
//                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_CARSOLD"));
//            }
            /*//是否已签约
            if (carValuation.getFinishOrder().equals(Boolean.TRUE)) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_CARSOLD"));
            }*/
            //车辆未通过评估
            if (carValuation.getApproveStatus().equals(ApproveStatus.APPROVE_REJECT)) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_NOPASSORDISCARD"));
            }
        }

        return ResultBean.getSucceed();
    }

    public ResultBean actCreateValuation(String demandId) {
        //
        CustomerDemand customerDemand = iCustomerDemandService.getOne(demandId);

        //
        CustomerCarBean car = iCustomerBizService.actGetCustomerCarByTransactionId(customerDemand.getCustomerTransactionId()).getD();

        CarValuationBean valuation = new CarValuationBean();
        valuation.setCarTypeId(car.getCarTypeId());
        //valuation.setCustomerTransactionId(customerDemand.getCustomerTransactionId());
        valuation.setVin(car.getVin());
        valuation.setLicenceNumber(car.getLicenseNumber());
        valuation.setCarModelNumber(car.getCarModelNumber());
        valuation.setMileage(car.getMileage());
        valuation.setMaintenanceMileage(car.getMaintenanceMileage());
        // 9月４号再次确认　不保存页面提交的　评估来源
        // valuation.setOnlineEvaluateSourceCode(car.getEvaluateType());
        valuation.setInitialValuationPrice(car.getInitialValuationPrice());
        valuation.setPrice(car.getEvaluatePrice());
        valuation.setFirstRegistryDate(car.getFirstRegistryDate());

        valuation.setFinishOrder(false);
        valuation.setColor(car.getCarColor());
        valuation.setDataStatus(DataStatus.SAVE);
        valuation.setCarDealerId(customerDemand.getCarDealerId());
        valuation.setLoginUserId(customerDemand.getLoginUserId());
        CarValuationBean carValuationBean = this.actNewValuation(valuation).getD();
        //评估单创建成功后将评估单Id保存在资质中
        if(carValuationBean != null){
            customerDemand.setVehicleEvaluateInfoId(carValuationBean.getId());
            iCustomerDemandService.save(customerDemand);
        }
        //评估单创建成功后将评估单保存在签约中
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerDemand.getCustomerTransactionId());
        if(purchaseCarOrder != null){
            purchaseCarOrder.setVehicleEvaluateInfoId(carValuationBean.getId());
            iOrderService.save(purchaseCarOrder);
        }
        return ResultBean.getSucceed().setD(carValuationBean);
    }

    /**
     * 使用评估单
     *
     * @param valuation
     * @param customerId
     * @param transactionId
     * @param orderId
     * @return
     */
    public ResultBean actUseValuation(CarValuationBean valuation, String customerId, String transactionId, String orderId) {
        //修改档案资料
        iCustomerImageFileBizService.actAssignImagesToTransaction(valuation.getCustomerImageIds(), customerId, transactionId);

        //修改评估单
        CarValuation carValuation = iCarValuationService.getOne(valuation.getId());

        //carValuation.setFinishOrder(true);
        //carValuation.setOrderId(orderId);

        iCarValuationService.save(carValuation);

        CustomerImageFileBean carValuationImageFileBean = null;
        if(carValuation.getCustomerImageIds() != null && carValuation.getCustomerImageIds().size() > 0){
            for (String cimgId:carValuation.getCustomerImageIds()) {
                carValuationImageFileBean = iCustomerImageFileBizService.actFindCustomerImageById(cimgId).getD();
                if(carValuationImageFileBean != null){
                    if(carValuationImageFileBean.getCustomerImageTypeCode().equals("B063")){
                        if (carValuationImageFileBean != null && carValuationImageFileBean.getFileIds().size() > 0){
                            CustomerImageFileBean imageFileBean = iCustomerImageFileBizService.actGetCustomerImageFile(customerId, transactionId, "B063").getD();
                            if(imageFileBean == null){
                                imageFileBean.setCustomerId(customerId);
                                imageFileBean.setCustomerTransactionId(transactionId);
                                imageFileBean.setCustomerImageTypeCode("B063");
                            }
                            imageFileBean.setFileIds(carValuationImageFileBean.getFileIds());
                            iCustomerImageFileBizService.actSaveCustomerImage(imageFileBean);
                        }
                    }
                }
            }

        }

        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean actUnuseValuation(CarValuationBean valuation) {
        //修改评估单
        CarValuation carValuation = iCarValuationService.getOne(valuation.getId());
        carValuation.setFinishOrder(false);
        carValuation.setOrderId(null);

        iCarValuationService.save(carValuation);

        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<String> actSubmitValuation(ValuationSubmissionBean valuation) {
        if (valuation.getVin() == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_NONEVIN"));
        }
        CarValuation carValuation = iCarValuationService.findAvailableOneByVin(valuation.getVin());
        if (carValuation != null) {
            if(carValuation.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_SIGN_COMPLETE"));
            }
            List<PurchaseCarOrder> orders = iOrderService.findAllByVehicleEvaluateInfoIdAndDataStatus(carValuation.getId(),DataStatus.SAVE);
            for(PurchaseCarOrder order : orders) {
                if (order != null) {
                    CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(order.getCustomerTransactionId()).getD();
                    if (customerTransaction != null && !(customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_FINISH)) {
                        if(order.getApproveStatus() == ApproveStatus.APPROVE_PASSED)
                            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARVALUATION_SAVEERROR"));
                    }
                }
            }

            CarValuation newCarValuation = mappingService.map(valuation, CarValuation.class);
            newCarValuation.setId(carValuation.getId());
            //newCarValuation.setCustomerTransactionid(carValuation.getCustomerTransactionid());
            carValuation = newCarValuation;
        } else {
            if (valuation.getId() != null ) {
                carValuation = iCarValuationService.getOne(valuation.getId());
                //valuation.setCustomerTransactionId(carValuation.getCustomerTransactionId());
            }
            carValuation = mappingService.map(valuation, CarValuation.class);
        }

        //保存档案资料
        if (valuation.getCustomerImages() != null && valuation.getCustomerImages().size() > 0) {
            //处理档案资料
            List<CustomerImageFileBean> customerImageFileBeanList = iCustomerImageFileBizService.actSaveCustomerImages(valuation.getCustomerImages()).getD();
            List<String> customerImageIds = new ArrayList<String>();
            for (CustomerImageFileBean cifb : customerImageFileBeanList) {
                customerImageIds.add(cifb.getId());
            }

            carValuation.setCustomerImageIds(customerImageIds);
        }
        carValuation.setDataStatus(DataStatus.SAVE);
        carValuation.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
        carValuation = iCarValuationService.save(carValuation);
        //开始进入审批流
        HashMap map = new HashMap();
        map.put("id", carValuation.getId());
        if(carValuation != null){
            return ResultBean.getSucceed().setD(map);
        }else{
            return ResultBean.getFailed().setD(map);
        }
    }

    /**
     * 启动车辆评估流程
     *
     * @param carValuation
     * @return
     */
    private ResultBean<CarValuation> startCarValuationFlow(CarValuation carValuation) {

        //签批信息
        SignInfo signInfo = new SignInfo(carValuation.getLoginUserId(), carValuation.getEmployeeId(), SignInfo.SIGN_PASS,SignInfo.FLAG_COMMIT, carValuation.getComment());

        String collectionName = null;
        try {
            collectionName = CarValuation.getMongoCollection(carValuation);
        } catch (Exception e) {
            // TODO: 2017/9/9
            e.printStackTrace();
        }
        /*SignCondition signCondition = new SignCondition("vin", carValuation.getVin(), "so_car_valuation", "queryMaintenance", "0");
        Map<String, List<SignCondition>> map = new HashMap<>();
        List<SignCondition> signConditions = new ArrayList<>();
        signConditions.add(signCondition);
        map.put("CarInfo_Submit", signConditions);*/
        ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSubmit(carValuation.getBusinessTypeCode(), carValuation.getId(), carValuation.getBillTypeCode(), signInfo, collectionName, null, carValuation.getCustomerTransactionId());
        if (resultBean != null) {
            if(resultBean.isSucceed()){
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    carValuation = iCarValuationService.getOne(carValuation.getId());
                }else{
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            }else if(resultBean.failed()){
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(carValuation);
    }

    public ResultBean<CarValuationBean> actSignValuation(String carValuationId, Integer approveStatus) {
        //获取评估数据
        CarValuation carValuation = iCarValuationService.getOne(carValuationId);

        SignInfo signInfo = new SignInfo(carValuation.getLoginUserId(), carValuation.getEmployeeId(), approveStatus, carValuation.getComment());

        WorkFlowBillBean workFlowBill = null;
        try {
            workFlowBill = iWorkflowBizService.actSignBill(carValuation.getId(), signInfo).getD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (workFlowBill == null) {
            return ResultBean.getFailed().setM("该评估单正在审核中或已经审核完成！请勿重复提交。");
        }

        carValuation = iCarValuationService.getOne(carValuationId);
        //改变状态
        //carValuation.setApproveStatus(workFlowBill.getApproveStatus());
/*        if (workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            carValuation.setDataStatus(DataStatus.DISCARD);
        }*/
        //carValuation = iCarValuationService.save(carValuation);
        return ResultBean.getSucceed().setD(mappingService.map(carValuation, CarValuationBean.class));

    }

    public ResultBean actSignValuation(ValuationSubmissionBean valuation, Integer approveStatus) {

        SignInfo signInfo = new SignInfo();
        signInfo.setResult(approveStatus);
        signInfo.setComment(valuation.getComment());
        signInfo.setFromSalesman(true);
        signInfo.setUserId(valuation.getLoginUserId());

        WorkFlowBillBean workFlowBill = null;
        try {
            workFlowBill = iWorkflowBizService.actSignBill(valuation.getId(), signInfo).getD();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //改变状态
        CarValuation carValuation = mappingService.map(valuation, CarValuation.class);
        carValuation.setApproveStatus(workFlowBill.getApproveStatus());
        if (workFlowBill.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            carValuation.setDataStatus(DataStatus.DISCARD);
        }
        iCarValuationService.save(carValuation);

        return ResultBean.getSucceed();
    }

    /**
     * 重新评估
     *
     * @param valuation
     * @return
     */
    public ResultBean actRedoValuation(ValuationSubmissionBean valuation) {

        //废弃旧的评估记录
        iCarValuationService.discard(valuation.getId());
        //重新提交评估单
        valuation.setId(null);
        valuation.setApproveStatus(ApproveStatus.APPROVE_INIT);

        ResultBean res = this.actSubmitValuation(valuation);
        Map map = (Map)res.getD();
        String vid = (String)map.get("id");
        CarValuation cv = iCarValuationService.getOne(vid);
        //更新订单里面的评估单ID
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(cv.getCustomerTransactionId());
        purchaseCarOrder.setVehicleEvaluateInfoId(cv.getId());
        iOrderService.save(purchaseCarOrder);

        //更新资质审核中的评估单ID
        CustomerDemand customerDemand =  iCustomerDemandService.findByCustomerTransactionId(cv.getCustomerTransactionId());
        customerDemand.setVehicleEvaluateInfoId(cv.getId());
        iCustomerDemandService.save(customerDemand);


        return  res;

    }

    /**
     * 评估单是否可以重新进行评估
     * 1.评估单已经
     * @param valuation
     * @return
     */
    private boolean  canRedoValuation(ValuationSubmissionBean valuation){
        //1.评估单对应的订单已经提交审批流，无法进行重新评估
        PurchaseCarOrder order = iOrderService.findByCustomerTransactionId(valuation.getCustomerTransactionId());
        if(order.getDataStatus() == DataStatus.TEMPSAVE || order.getApproveStatus() == ApproveStatus.APPROVE_REJECT){
            return true;
        }

        return false;
    }

    /**
     * 重新评估
     *
     * @param id
     * @return
     */
    public ResultBean actRedoValuation(String id) {

        CarValuation submission = iCarValuationService.getOne(id);
        //废弃旧的评估记录
        //iCarValuationService.discard(submission.getId());
        String oldId = submission.getId();
        //初始化新评估单
        //submission.setId(null);
        submission.setMaintenanceDate(null);
        submission.setMaintenanceUrl(null);
        submission.setMaintenanceMileage(null);
        submission.setFinishOrder(false);
        submission.setQueryMaintenance(0);
        submission.setInitialValuationPrice(null);
        submission.setValuationInfo(null);
        submission.setPrice(null);
        submission.setCustomerImageIds(null);
        submission.setApproveStatus(ApproveStatus.APPROVE_INIT);

        submission = iCarValuationService.save(submission);

        //重置workflow
        LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(submission.getLoginUserId()).getD();
        String activitiUserId = loginUserBean.getActivitiUserId();

        iWorkflowBizService.actDiscardBillWorkFlow(submission.getId(), activitiUserId);

        HashMap map = new HashMap();
        map.put("id", submission.getId());
        return ResultBean.getSucceed().setD(map);
    }

    public ResultBean actDiscardValuation(String id) {
        iCarValuationService.discard(id);
        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<CarValuationBean> actFindValuationByVin(String carTypeId, String vin) {
        CarValuation carValuation = iCarValuationService.findOneByCarTypeIdAndVin(carTypeId, vin);
        return ResultBean.getSucceed().setD(mappingService.map(carValuation, CarValuationBean.class));
    }

    @Override
    public ResultBean<CarValuationBean> actGetValuationByVin(String vin) {
        CarValuation carValuation = iCarValuationService.findOneByVin(vin);
        if (carValuation == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CARVALUATION_NULL_BY_VIN"), vin));
        } else {
            ResultBean result = ResultBean.getSucceed().setD(mappingService.map(carValuation, CarValuationBean.class));
            if (carValuation.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
                result.setM(messageService.getMessage("MSG_CARVALUATION_NOT_APPROVE"));
            }
            return result;
        }
    }

    @Override
    public ResultBean<ValuationSubmissionBean> actFindValuationById(String id) {
        CarValuation carValuation = iCarValuationService.getOne(id);
        if (carValuation == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CARVALUATION_NULL_BY_ID"), id));
        }
        ValuationSubmissionBean valuation = mappingService.map(carValuation, ValuationSubmissionBean.class);


        /**
         * 返回档案资料类型与文件
         */
        List<ImageTypeFileBean> imageTypeFiles = new ArrayList<ImageTypeFileBean>();
        if (carValuation.getCustomerImageIds() != null && carValuation.getCustomerImageIds().size() > 0) {
            imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(
                    carValuation.getCustomerImageIds()).getD();
        } else {

            //获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(carValuation.getBillTypeCode()).getD();
            //档案类型编码
            List<String> imageTypeCodes = billType.getRequiredImageTypeCodes();

            if (imageTypeCodes.size() > 0) {
                List<CustomerImageTypeBean> customerImageTypes = iCustomerImageTypeBizService.actFindCustomerImageTypesByCodes(imageTypeCodes).getD();
                imageTypeFiles = mappingService.map(customerImageTypes, ImageTypeFileBean.class);
            }
        }

        valuation.setCustomerImages(imageTypeFiles);

        return ResultBean.getSucceed().setD(valuation);
    }

    @Override
    public ResultBean<CarValuationBean> actFindCarValuationById(String id) {
        CarValuation carValuation = iCarValuationService.getOne(id);

        if (carValuation == null) {
            return ResultBean.getSucceed();
        }
        String code = carValuation.getBillTypeCode();
        //通过编码获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();

        CarValuationBean valuation = mappingService.map(carValuation, CarValuationBean.class);
        valuation.setBillType(billType);
        return ResultBean.getSucceed().setD(valuation);
    }

    @Override
    public ResultBean<DataPageBean<CarValuationBean>> actGetCarValuations(Integer currentPage,int approveStatus) {
        Page<CarValuation> carValuations;
        if (approveStatus == -1) {
            carValuations = iCarValuationService.getAllCarValuatiOrderByTs(currentPage);
        } else {
            carValuations = iCarValuationService.getAllByApproveStatusOrderByTsDesc(approveStatus, currentPage);
        }
        return ResultBean.getSucceed().setD(mappingService.map(carValuations,CarValuationBean.class));
    }

    @Override
    public ResultBean<CarValuationBean> actSaveCarValuation(CarValuationBean carValuationBean) {
        CarValuation carValuation = iCarValuationService.save(mappingService.map(carValuationBean, CarValuation.class));

        /**
         * 根据评估单更新签约中的评估报告
         */
        if(carValuation != null && carValuation.getCustomerImageIds() != null && carValuation.getCustomerImageIds().size()>0){
            List<PurchaseCarOrder> carOrders = iOrderService.findAllByVehicleEvaluateInfoId(carValuation.getId());
            for (PurchaseCarOrder carOrder:carOrders) {
                CustomerTransactionBean transaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(carOrder.getCustomerTransactionId()).getD();
                if(transaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING){
                    CustomerImageFileBean customerImageFileBean = iCustomerImageFileBizService.actFindByCustomerIdAndCustomerImageTypeAndCustomerTransactionId(transaction.getCustomerId(), transaction.getId(), "B063").getD();
                    if(customerImageFileBean.getFileIds().size() == 0){
                        for (String imgId:carValuation.getCustomerImageIds()) {
                            CustomerImageFileBean imageFileBean = iCustomerImageFileBizService.actFindCustomerImageById(imgId).getD();
                            if(imageFileBean != null){
                                if(imageFileBean.getCustomerImageTypeCode().equals("B063")){
                                    customerImageFileBean.setFileIds(imageFileBean.getFileIds());
                                    iCustomerImageFileBizService.actSaveCustomerImage(customerImageFileBean);
                                }
                            }

                        }
                    }
                }
            }
        }
        return ResultBean.getSucceed().setD(mappingService.map(carValuation,CarValuationBean.class));
    }

    @Override
    public ResultBean<CarValuationBean> actSavePassCarValuation(CarValuationBean carValuationBean){
        carValuationBean.setApproveStatus(ApproveStatus.APPROVE_PASSED);
        CarValuation carValuation = iCarValuationService.save(mappingService.map(carValuationBean, CarValuation.class));
        return ResultBean.getSucceed().setD(mappingService.map(carValuation,CarValuationBean.class));
    }

    @Override
    public ResultBean<CarValuationBean> actCancelCarValuation(CarValuationBean carValuationBean) {
        carValuationBean.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
        CarValuation carValuation = iCarValuationService.save(mappingService.map(carValuationBean, CarValuation.class));
        if(carValuation == null){
            return ResultBean.getFailed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(carValuation,CarValuationBean.class));
    }

    @Override
    public ResultBean<CarValuationBean> actDeleteCarValuation(String id) {
        CarValuation carValuation = iCarValuationService.delete(id);
        if(carValuation != null){
            return ResultBean.getSucceed().setD(mappingService.map(carValuation,CarValuationBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<CarValuationBean>> actSearchCarValuations(Integer currentPage, CarValuationBean carValuationBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").gt(DataStatus.TEMPSAVE));
        if(!StringUtils.isEmpty(carValuationBean.getCarType()))
            query.addCriteria(Criteria.where("carType").regex(Pattern.compile("^.*"+ carValuationBean.getCarType() +".*$", Pattern.CASE_INSENSITIVE)));
        if(!StringUtils.isEmpty(carValuationBean.getVin()))
            query.addCriteria(Criteria.where("vin").regex(Pattern.compile("^.*"+ carValuationBean.getVin() +".*$", Pattern.CASE_INSENSITIVE)));
        if(!StringUtils.isEmpty(carValuationBean.getLicenceNumber()))
            query.addCriteria(Criteria.where("licenceNumber").regex(Pattern.compile("^.*"+ carValuationBean.getLicenceNumber() +".*$", Pattern.CASE_INSENSITIVE)));

        Pageable pageable = new PageRequest(currentPage, 20);
        query.with(CarValuation.getSortDESC("ts")).with(pageable);
        List list = mongoTemplate.find(query,CarValuation.class);
        Page<CarValuation> carValuations = new PageImpl(list,pageable, mongoTemplate.count(query,CarValuation.class));
        return ResultBean.getSucceed().setD(mappingService.map(carValuations,CarValuationBean.class));
    }

    @Override
    public ResultBean<Boolean> actGetCarValuationFinshOrder(String carValuationId) {
        CarValuation carValuation = iCarValuationService.getOne(carValuationId);
        if(carValuation == null){
            return ResultBean.getFailed();
        }
        List<PurchaseCarOrder> orders = iOrderService.findAllByVehicleEvaluateInfoIdAndDataStatus(carValuation.getId(), DataStatus.SAVE);
        for (PurchaseCarOrder or:orders) {
            CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(or.getCustomerTransactionId()).getD();
            if(customerTransaction != null && customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_PROCESSING){
                return ResultBean.getSucceed().setD(true);
            }
        }

        return ResultBean.getSucceed().setD(false);
    }

    @Override
    public ResultBean<List<CarValuationBean>> actGetCarValuation(String carDealerId) {
        List<CarValuation> carValuation = iCarValuationService.findAllByCarDealerId(carDealerId);
        return ResultBean.getSucceed().setD(mappingService.map(carValuation,CarValuationBean.class));
    }

}
