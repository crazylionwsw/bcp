package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.BillTypeBean;
import com.fuze.bcp.api.bd.bean.BusinessTypeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.bean.DemandListBean;
import com.fuze.bcp.api.creditcar.bean.DemandSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarTransactionBizService;
import com.fuze.bcp.api.creditcar.service.ICarValuationBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.drools.service.IDroolsBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
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
import com.fuze.bcp.creditcar.service.ICustomerDemandService;
import com.fuze.bcp.creditcar.service.IOrderService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.MutexService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.IdcardUtils;
import com.fuze.bcp.utils.SimpleUtils;
import com.fuze.bcp.utils.StringHelper;
import org.apache.commons.lang3.StringUtils;
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
 * 资质审查微服务
 * Created by Lily on 2017/7/19.
 */
@Service
public class BizCustomerDemandService implements ICustomerDemandBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerDemandService.class);

    @Autowired
    ICustomerDemandService iCustomerDemandService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    ICarValuationBizService iCarValuationBizService;

    @Autowired
    IOrderService iOrderService;
    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    IDroolsBizService iDroolsBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;
    @Autowired
    IAmqpBizService iAmqpBizService;

    @Autowired
    MutexService mutexService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICarTransactionBizService iCarTransactionBizService;

    private Map<String, List<SignCondition>> getConditions(String id) {

        Map<String, List<SignCondition>> params = new HashMap();

        Map<String, Object> condition = new HashMap<>();
        condition.put(SignCondition.MONGOSORT, "_id"); // paymentPolicy compensatoryInterest
        condition.put("customerTransactionId", id);

        SignCondition isNeedPayment = new SignCondition(condition, "cus_loan", "isNeedPayment", "0");

        SignCondition advancedPayment = new SignCondition(condition, "cus_loan", "advancedPayment", "0");
        SignCondition needCompensatory = new SignCondition("customerTransactionId", id, "so_appoint_swipingcard", "isNeedLoaning", "0");

        List s1 = new ArrayList<>();
        s1.add(isNeedPayment);
        List s3 = new ArrayList<>();
        s3.add(advancedPayment);
        List s4 = new ArrayList<>();
        s4.add(needCompensatory);
        params.put("A002", s1);
        params.put("A004", s4);
        params.put("A026", s4);
        return params;
    }



    /**
     * 提交资质审查
     *
     * @param demandSubmission
     * @return
     */
    @Override
    public ResultBean<CustomerDemandBean> actSubmitCustomerDemand(DemandSubmissionBean demandSubmission) {
        try {
            // 新增数据还没上锁，不予执行
            if (demandSubmission.getId() == null && !mutexService.lockSaveObject(demandSubmission.getBusinessTypeCode(), demandSubmission.getLoginUserId(), "saveCustomerDemand")) {
                return ResultBean.getFailed().setM("请勿频繁提交");
            }
            if (demandSubmission.getId() == null) {
                String today = SimpleUtils.getShortDate();
                String identifyNo = demandSubmission.getCreditMaster().getIdentifyNo();
                String carTypeId = demandSubmission.getCustomerCar().getCarTypeId();
                String cardealerId = demandSubmission.getCarDealerId();
                String loginUserId = demandSubmission.getLoginUserId();
                CustomerBean customerBean = iCustomerBizService.actGetCustomerByIdentifyNo(identifyNo).getD();
                if (customerBean != null) {
                    List customerIds = new ArrayList<>();
                    customerIds.add(customerBean.getId());
                    List<CustomerTransactionBean> transactions = (List<CustomerTransactionBean>) iCustomerTransactionBizService.actGetListsBySomeConditions(loginUserId,null,customerIds,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<Integer>(),"ts", true).getD();
                    for (CustomerTransactionBean transaction : transactions) {
                        CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarByTransactionId(transaction.getId()).getD();
                        if (customerCarBean != null && transaction.getTs().indexOf(today) != -1) {
                            if (carTypeId.equals(customerCarBean.getCarTypeId()) && cardealerId.equals(transaction.getCarDealerId())) {
                                return ResultBean.getFailed().setM("当天禁止提交重复数据");
                            }
                        }
                    }
                }
            }
            ResultBean result;
            //保存贷款主体信息
            CustomerBean creditMaster;
            CustomerDemand customerDemand;
            CustomerTransactionBean transaction;
            //TODO 现业务为提交时保存并进入工作流完成自己的任务，修改时只保存数据。需要考虑修改时可能是被驳回的数据，也需要进入审批流完成任务。
            if (StringHelper.isBlock(demandSubmission.getId())) {
                //添加客户信息
                creditMaster = iCustomerBizService.actSubmitCustomer(demandSubmission.getCreditMaster()).getD();

                //启动业务
                result = this.startTransaction(creditMaster.getId(), demandSubmission);
                if (result.failed())
                    return result;
                transaction = (CustomerTransactionBean) result.getD();
                //启动客户交易流程
                String collectionName = null;
                try {
                    //TODO: 交易模块拆分出来之后，creditcar不能依赖transaction，所以这里只能写死  CustomerTransaction.getMongoCollection(transaction)
                    collectionName = "cus_transaction";
                } catch (Exception e) {
                    //// TODO: 2017/9/9
                    e.printStackTrace();
                }
                if (transaction.getBusinessTypeCode().equals("NC")) { //启动新车业务流程
                    Map<String, List<SignCondition>> params = getConditions(transaction.getId());
                    iWorkflowBizService.actCreateWorkflow(transaction.getBusinessTypeCode(), transaction.getId(), transaction.getBusinessTypeCode(), collectionName, params, transaction.getId());
                } else if (transaction.getBusinessTypeCode().equals("OC")) { //启动二手车业务流程
                    Map<String, List<SignCondition>> params = getConditions(transaction.getId());
                    iWorkflowBizService.actCreateWorkflow(transaction.getBusinessTypeCode(), transaction.getId(), transaction.getBusinessTypeCode(), collectionName, params, transaction.getId());
                }

                customerDemand = new CustomerDemand();
                customerDemand.setDataStatus(DataStatus.TEMPSAVE); //数据状态
                customerDemand.setCustomerTransactionId(transaction.getId());
                customerDemand.setCashSourceId(transaction.getCashSourceId()); //支行
                demandSubmission.getCustomerLoan().setId(customerDemand.getCustomerLoanId());
                demandSubmission.getCustomerCar().setId(customerDemand.getCustomerCarId());
            } else {
                //判断单据是否可以编辑
                result = iCustomerDemandService.getEditableBill(demandSubmission.getId());
                if (result.failed()) return result;
                customerDemand = (CustomerDemand) result.getD();
                // 业务校验
                result = iCustomerTransactionBizService.actGetEditableTransaction(demandSubmission.getCustomerTransactionId());
                if (result.failed()) return result;
                CustomerBean customerBean = demandSubmission.getCreditMaster();
                //    根据客户身份证号 解析信息
                customerBean = actParseCustomerIdentifyNo(customerBean).getD();
                
                //通过身份证号查询
                CustomerBean customer = iCustomerBizService.actGetCustomerByIdentifyNo(customerBean.getIdentifyNo()).getD();
                if (customer != null) { //客户已存在，只更新客户信息
                    CustomerJobBean oldcustomerJob = customer.getCustomerJob();
                    //更新客户信息
                    CustomerJobBean newcustomerJob = customerBean.getCustomerJob();
                    if (oldcustomerJob != null) {
                        newcustomerJob.setCompanyAddress(oldcustomerJob.getCompanyAddress());
                        newcustomerJob.setJob(oldcustomerJob.getJob());
                        newcustomerJob.setWorkDate(oldcustomerJob.getWorkDate());
                        newcustomerJob.setHrName(oldcustomerJob.getHrName());
                        newcustomerJob.setHrCell(oldcustomerJob.getHrCell());
                        newcustomerJob.setSalary(oldcustomerJob.getSalary());
                    }
                    customerBean.setCustomerJob(newcustomerJob);
                    customerBean.setIsSelfEmployed(customer.getIsSelfEmployed());
                    customerBean.setId(customer.getId());
                    customerBean.setComment(customer.getComment());
                    customerBean.setDataStatus(customer.getDataStatus());
                    customerBean.setDirectGuest(customer.getDirectGuest());//是否直客
                }
                creditMaster = iCustomerBizService.actSaveCustomer(customerBean).getD();
                //更新交易信息
                result = this.updateTransaction(demandSubmission);
                if (result.failed()) return result;
                transaction = (CustomerTransactionBean) result.getD();
                //修改客户信息
                //creditMaster = iCustomerBizService.actSubmitCustomer(demandSubmission.getCreditMaster()).getD();
            }

            //保存资质信息
            customerDemand = this.saveCustomerDemand(customerDemand, demandSubmission, creditMaster).getD();
            // 资质提交需要启动工作流同时完成提交操作
            result = this.startCustomerDemandFlow(customerDemand, demandSubmission.getComment());
            if (result.failed()) {
                return result;
            }
            customerDemand = (CustomerDemand) result.getD();
            /*String carValuationMsg = "";
            if (customerDemand.getBusinessTypeCode().equals("OC") && demandSubmission.getCustomerCar() != null) { //二手车
                if (demandSubmission.getCustomerCar() != null) {
                    String vin = demandSubmission.getCustomerCar().getVin();
                    if(vin != null){
                        CarValuationBean carValuationBean = iCarValuationBizService.actGetValuationByVin(vin).getD();
                        if(carValuationBean != null && carValuationBean.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                            carValuationMsg = messageService.getMessage("MSG_CUSTOMERDEMAND_CARVALUATIONMSG");
                        }
                    }
                }
                //购车资质保存成功后发送MQ消息
                try {
                    MsgRecordBean msgRecordBean = new MsgRecordBean("OC_A001_CustomerDemand_CarValuation", customerDemand.getCustomerTransactionId(), null, null, null);
                    iAmqpBizService.actSendMq("OC_A001_CustomerDemand_CarValuation", new Object[]{customerDemand.getId()}, msgRecordBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

            String carDealerMsg = messageService.getMessage("MSG_CUSTOMERDEMAND_CARDEALERMSG");
            if (customerDemand.getCarDealerId() != null) {
                CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(customerDemand.getCarDealerId()).getD();
                if (carDealerBean.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
                    carDealerMsg = "";
                }
            }
            logger.info(customerDemand.getBillTypeCode() + "." + customerDemand.getId() + messageService.getMessage(messageService.getMessage("MSG_CUSTOMERDEMAND_SUBMITTED")) + carDealerMsg);
            return ResultBean.getSucceed().setD(mappingService.map(customerDemand, CustomerDemandBean.class)).setM(messageService.getMessage("MSG_CUSTOMERDEMAND_SUBMITTED") + carDealerMsg);
        } catch (Exception e) {
            throw e;
        } finally {
            mutexService.unLockSaveObject(demandSubmission.getBusinessTypeCode(), demandSubmission.getLoginUserId());
        }

    }

    /**
     * 解析 客户信息中的 客户身份证号  获取客户年龄、出生日期、户籍所在地
     *
     * @param customerBean
     * @return
     */
    public ResultBean<CustomerBean> actParseCustomerIdentifyNo(CustomerBean customerBean) {
        String identifyNo = customerBean.getIdentifyNo();
        if (IdcardUtils.validateCard(identifyNo)) {
            customerBean.setAge(IdcardUtils.getAgeByIdCard(identifyNo));
            //      将 yyyyMMdd 格式 String类型的时间字符串 转换为    yyyy-MM-dd 格式的字符串
            customerBean.setBirthday(DateTimeUtils.StringPattern(IdcardUtils.getBirthByIdCard(identifyNo), "yyyyMMdd", "yyyy-MM-dd"));
            customerBean.setCensusRegisterCity(IdcardUtils.getProvinceByIdCard(identifyNo));
            //  解析 性别
            //customerBean.setGender("N".equals(IdcardUtils.getGenderByIdCard(identifyNo)) ? -1 : ("M".equals(IdcardUtils.getGenderByIdCard(identifyNo)) ? 0 : 1) );
        }
        return ResultBean.getSucceed().setD(customerBean);
    }

    /**
     * 创建评估单对象
     *
     * @param car
     * @param carDealerId
     * @param loginUserId
     * @return
     */
    /*private CarValuationBean createValuation(CustomerCarBean car, String carDealerId, String loginUserId) {
        CarValuationBean valuation = new CarValuationBean();
        valuation.setCarTypeId(car.getCarTypeId());
        valuation.setVin(car.getVin());
        valuation.setLicenceNumber(car.getLicenseNumber());
        valuation.setCarModelNumber(car.getCarModelNumber());
        valuation.setMileage(car.getMileage());
        valuation.setMaintenanceMileage(car.getMaintenanceMileage());
        // 9月４号再次确认　不保存页面提交的　评估来源
        // valuation.setOnlineEvaluateSourceCode(car.getEvaluateType());
        valuation.setInitialValuationPrice(car.getEvaluatePrice());
        valuation.setPrice(car.getEvaluatePrice());
        valuation.setFirstRegistryDate(car.getFirstRegistryDate());

        valuation.setFinishOrder(false);
        valuation.setColor(car.getCarColor());
        valuation.setDataStatus(DataStatus.SAVE);
        valuation.setCarDealerId(carDealerId);
        valuation.setLoginUserId(loginUserId);
        return valuation;
    }*/

    /**
     * 启动客户业务（判断该客户能否继续提交业务）
     *
     * @param customerId
     * @param submission
     * @return
     */
    private ResultBean<CustomerTransactionBean> startTransaction(String customerId, DemandSubmissionBean submission) {
        //从系统配置中读取限制
        Integer loanLimit = iParamBizService.actGetInteger("CUSTOMER_LOAN_LIMIT").getD();
        Integer transCount = iCustomerTransactionBizService.actCountTransactionsByCustomerId(customerId).getD();

        //检查业务数是否超限
        if (transCount > loanLimit) {
            return ResultBean.getFailed().addL(String.format(messageService.getMessage("TRANSACTION_REACH_LIMIT"), loanLimit));
        } else {
            CustomerTransactionBean transaction = new CustomerTransactionBean();

            //获取报单行
            CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(submission.getCarDealerId()).getD();
            if (carDealer != null) {
                transaction.setCashSourceId(carDealer.getCashSourceId());
            }
            transaction.setBusinessTypeCode(submission.getBusinessTypeCode());
            transaction.setCarDealerId(submission.getCarDealerId());
            transaction.setCustomerId(customerId);
            transaction.setLoginUserId(submission.getLoginUserId());

            EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(submission.getLoginUserId()).getD();
            if (employee != null) {
                transaction.setEmployeeId(employee.getId());
                transaction.setOrginfoId(employee.getOrgInfoId());
            }

            transaction.setStartTime(DateTimeUtils.getCreateTime());
            transaction.setStatus(CustomerTransactionBean.TRANSACTION_PROCESSING);
            transaction.setDataStatus(DataStatus.SAVE);

            transaction = iCustomerTransactionBizService.actSaveCustomerTransaction(transaction).getD();
            logger.info(transaction.getBillTypeCode() + ":" + transaction.getId() + messageService.getMessage("MSG_SUCESS_SAVE"));
            return ResultBean.getSucceed().setD(transaction);
        }
    }

    /**
     * 更新业务信息
     *
     * @param submission
     * @return
     */
    private ResultBean<CustomerTransactionBean> updateTransaction(DemandSubmissionBean submission) {
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindCustomerTransactionById(submission.getCustomerTransactionId()).getD();

        if (customerTransaction != null) {
            //检查业务数是否超限
            customerTransaction.setBusinessTypeCode(submission.getBusinessTypeCode());
            customerTransaction.setCarDealerId(submission.getCarDealerId());
            if(submission.getCreditMaster() != null ){
                customerTransaction.setCustomerId(submission.getCreditMaster().getId());
            }
            EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(submission.getLoginUserId()).getD();
            customerTransaction.setEmployeeId(employee.getId());
            customerTransaction.setOrginfoId(employee.getOrgInfoId());

            //更新报单行
            CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(submission.getCarDealerId()).getD();
            if(carDealerBean != null){
                customerTransaction.setCashSourceId(carDealerBean.getCashSourceId());
            }
            customerTransaction = iCustomerTransactionBizService.actSaveCustomerTransaction(customerTransaction).getD();
            return ResultBean.getSucceed().setD(customerTransaction);
        }
        return ResultBean.getFailed();

    }

    /**
     * 保存资质信息
     *
     * @param customerDemand
     * @param demandSubmission
     * @param creditMaster
     * @return
     */
    private ResultBean<CustomerDemand> saveCustomerDemand(CustomerDemand customerDemand, DemandSubmissionBean demandSubmission, CustomerBean creditMaster) {
        customerDemand.setCreditMasterId(creditMaster.getId());
        customerDemand.setCustomerId(creditMaster.getId());
        customerDemand.setRelation(demandSubmission.getRelation()); //指标人关系
        customerDemand.setDealerEmployeeId(demandSubmission.getDealerEmployeeId()); //4s店销售员
        customerDemand.setBusinessTypeCode(demandSubmission.getBusinessTypeCode()); //业务类型
        customerDemand.setLoginUserId(demandSubmission.getLoginUserId()); //分期经理用户ID
        customerDemand.setCarDealerId(demandSubmission.getCarDealerId()); //经销商

        EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(demandSubmission.getLoginUserId()).getD();
        if (employee != null) {
            customerDemand.setEmployeeId(employee.getId()); //分期经理员工ID
            customerDemand.setOrginfoId(employee.getOrgInfoId()); //部门ID
        }

        if (customerDemand.getBusinessTypeCode().equals(BusinessTypeBean.BUSINESSTYPE_OC)) {
            customerDemand.setFirstPurchaseDate(demandSubmission.getFirstPurchaseDate());//二手车首次购车日期
        }

        //保存配偶信息，如果有的话
        if (demandSubmission.getMateCustomer() != null) {
            CustomerBean mate = iCustomerBizService.actSubmitCustomer(demandSubmission.getMateCustomer()).getD();
            customerDemand.setMateCustomerId(mate.getId());
        } else {
            customerDemand.setMateCustomerId(null);
        }

        if(demandSubmission.getRelation() != null && demandSubmission.getRelation().equals("0") ){
            customerDemand.setPledgeCustomerId(customerDemand.getCreditMasterId());
        }else{
            //保存指标人信息
            if (demandSubmission.getPledgeCustomer() != null) {
                CustomerBean pledge = demandSubmission.getPledgeCustomer();
                if (pledge.getIdentifyNo().equals(creditMaster.getIdentifyNo())) { //与贷款主体身份证号相同
                    customerDemand.setPledgeCustomerId(customerDemand.getCreditMasterId());
                } else if (demandSubmission.getMateCustomer() != null && pledge.getIdentifyNo().equals(demandSubmission.getMateCustomer().getIdentifyNo())) { //与配偶身份证号相同
                    customerDemand.setPledgeCustomerId(customerDemand.getMateCustomerId());
                } else { //
                    customerDemand.setPledgeCustomerId(iCustomerBizService.actSubmitCustomer(demandSubmission.getPledgeCustomer()).getD().getId());
                }
            }
        }


        //处理档案资料
        iCustomerImageFileBizService.actSaveCustomerImages(customerDemand.getCustomerId(),
                customerDemand.getCustomerTransactionId(),
                demandSubmission.getCustomerImages()); //整体保存档案资料

        //保存车辆信息
        if (demandSubmission.getCustomerCar() != null) {
            CustomerCarBean car = mappingService.map(demandSubmission.getCustomerCar(), CustomerCarBean.class);
            car.setCustomerId(customerDemand.getCustomerId());
            car.setCustomerTransactionId(customerDemand.getCustomerTransactionId());
            car = iCustomerBizService.actSaveCustomerCar(car).getD();
            customerDemand.setCustomerCarId(car.getId());
        }

        //保存贷款数据
        CustomerLoanBean loan = mappingService.map(demandSubmission.getCustomerLoan(), CustomerLoanBean.class);
        RateType rateType = new RateType();
        rateType.setMonths(demandSubmission.getCustomerLoan().getMonths() == null ? 0 : demandSubmission.getCustomerLoan().getMonths());
        rateType.setRatio(demandSubmission.getCustomerLoan().getRatio() == null ? 0.0 : demandSubmission.getCustomerLoan().getRatio());
        loan.setRateType(rateType);
        loan.setCustomerId(customerDemand.getCustomerId());
        loan.setCustomerTransactionId(customerDemand.getCustomerTransactionId());
        // 计算分期申请车价 applyAmount
        Double min = (loan.getReceiptPrice() < loan.getRealPrice()) ? loan.getReceiptPrice() : loan.getRealPrice();
        if (demandSubmission.getBusinessTypeCode().equals("OC")) {
            if (demandSubmission.getCustomerCar() != null) {
                min = (min < demandSubmission.getCustomerCar().getEvaluatePrice()) ? min : demandSubmission.getCustomerCar().getEvaluatePrice();
            }
        }
        /*if (demandSubmission.getBusinessTypeCode().equals("OC")) {
            if (demandSubmission.getCustomerCar() != null) {
                String vin = demandSubmission.getCustomerCar().getVin();
                if(vin != null){
                    CarValuationBean carValuationBean = iCarValuationBizService.actGetValuationByVin(vin).getD();
                    if(carValuationBean != null && carValuationBean.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
                        min = carValuationBean.getPrice();
                    }else{

                    }
                }
            }
        }*/
        loan.setApplyAmount(min);//分期申请车价

        //  计算贴息方案
        Map<String, Object> map = iCustomerBizService.actCalculateCompensatoryWay(loan.getBankFeeAmount(), loan.getCompensatoryAmount(), loan.getRateType().getMonths()).getD();
        if (map != null && !"".equals((String) map.get("compensatoryWay"))) {
            loan.setCompensatoryWay((String) map.get("compensatoryWay"));
            loan.setCompensatoryMonth((Integer) map.get("compensatoryMonth"));
        }

        loan = iCustomerBizService.actSaveCustomerLoan(loan).getD();
        customerDemand.setCustomerLoanId(loan.getId());

        //保存资质信息
        customerDemand = iCustomerDemandService.save(customerDemand);
        return ResultBean.getSucceed().setD(customerDemand);
    }

    /**
     * 启动资质审查流程
     *
     * @param customerDemand
     * @param comment
     * @return
     */
    private ResultBean<CustomerDemand> startCustomerDemandFlow(CustomerDemand customerDemand, String comment) {
        SignInfo signInfo = new SignInfo(customerDemand.getLoginUserId(), customerDemand.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, comment);
        //启动工作流并进行提交操作
        String collectionName = null;
        try {
            collectionName = CustomerDemand.getMongoCollection(customerDemand);
        } catch (Exception e) {
            //// TODO: 2017/9/9
            e.printStackTrace();
        }
        ResultBean resultBean = iWorkflowBizService.actSubmit(customerDemand.getBusinessTypeCode(), customerDemand.getId(), customerDemand.getBillTypeCode(), signInfo, collectionName, null, customerDemand.getCustomerTransactionId());
        if (resultBean != null) {
            if (resultBean.isSucceed()) {
                WorkFlowBillBean workFlowBill = (WorkFlowBillBean) resultBean.getD();
                if (workFlowBill != null) {
                    //由于审核状态和保存状态在工作流中已更改，所以只需要查一下再返回即可
                    customerDemand = iCustomerDemandService.getOne(customerDemand.getId());
                } else {
                    return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWNULL_SUBMIT"));
                }
            } else if (resultBean.failed()) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_WORKFLOWFAILED_SUBMIT"));
            }

        }
        return ResultBean.getSucceed().setD(customerDemand);
    }

    @Override
    public ResultBean<DemandSubmissionBean> actRetrieveCustomerDemand(String transactionId) {
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerTransactionId(transactionId);
        if (customerDemand == null) {
            return ResultBean.getFailed().addL(messageService.getMessage("MSG_FAIL_NUll"));
        }
        DemandSubmissionBean demandSubmissionBean = mappingService.map(customerDemand, DemandSubmissionBean.class);
        //TODO 此处不应该再提供其它对象的查询，作为代码耦合太紧
        //贷款主体
        if (StringUtils.isNotBlank(customerDemand.getCreditMasterId()))
            demandSubmissionBean.setCreditMaster(iCustomerBizService.actGetCustomerById(customerDemand.getCreditMasterId()).getD());
        //配偶
        if (StringUtils.isNotBlank(customerDemand.getMateCustomerId()))
            demandSubmissionBean.setMateCustomer(iCustomerBizService.actGetCustomerById(customerDemand.getMateCustomerId()).getD());
        //指标人
        if (StringUtils.isNotBlank(customerDemand.getPledgeCustomerId()))
            demandSubmissionBean.setPledgeCustomer(iCustomerBizService.actGetCustomerById(customerDemand.getPledgeCustomerId()).getD());
        //车
        if (StringUtils.isNotBlank(customerDemand.getCustomerCarId())) {
            CustomerCarBean car = iCustomerBizService.actGetCustomerCarById(customerDemand.getCustomerCarId()).getD();
            if (car != null) {
                demandSubmissionBean.setCustomerCar(mappingService.map(car, PadCustomerCarBean.class));
            }
        }
        //钱
        if (StringUtils.isNotBlank(customerDemand.getCustomerLoanId())) {
            //处理 客户端 rateType 的转换
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(customerDemand.getCustomerLoanId()).getD();
            if (customerLoanBean != null) {
                LoanSubmissionBean loanSubmissionBean = mappingService.map(iCustomerBizService.actGetCustomerLoanById(customerDemand.getCustomerLoanId()).getD(), LoanSubmissionBean.class);
                loanSubmissionBean.setMonths(customerLoanBean.getRateType().getMonths());
                loanSubmissionBean.setRatio(customerLoanBean.getRateType().getRatio());
                demandSubmissionBean.setCustomerLoan(loanSubmissionBean);
                //不用将评估价格覆盖提交的评估价格
               /* if (StringHelper.isNotBlock(demandSubmissionBean.getCustomerCar().getVin())) {
                    CarValuationBean carValuationBean = this.iCarValuationBizService.actGetValuationByVin(demandSubmissionBean.getCustomerCar().getVin()).getD();
                    if (carValuationBean != null) {
                        demandSubmissionBean.getCustomerCar().setEvaluatePrice(carValuationBean.getInitialValuationPrice());
                    }
                }*/
            }
        }


//        //钱
//        if (StringUtils.isNotBlank(customerDemand.getCustomerLoanId())){
//            demandSubmissionBean.setCustomerLoan(iCustomerBizService.actGetCustomerLoanById(customerDemand.getCustomerLoanId()).getD());
//        }

        //获取单据类型
        BillTypeBean billType = iBaseDataBizService.actGetBillType(customerDemand.getBillTypeCode()).getD();
        //档案类型编码
        //TODO 根据档案类型和单据的ID，查询单据的PAD端显示档案，不应该在此进行查询
        List<String> imageTypeCodes = billType.getRequiredImageTypeCodes();

        //获取档案类型
        List<ImageTypeFileBean> imageTypeFiles = iCustomerImageFileBizService.actGetBillImageTypesWithFiles(customerDemand.getCustomerId(),
                customerDemand.getCustomerTransactionId(),
                customerDemand.getBusinessTypeCode(),
                customerDemand.getBillTypeCode()).getD();

        demandSubmissionBean.setCustomerImages(imageTypeFiles);

        return ResultBean.getSucceed().setD(demandSubmissionBean);
    }

    /**
     * 资质审核签批
     *
     * @param customerDemandId
     * @param signInfo
     * @return
     */
    @Override
    public ResultBean<CustomerDemandBean> actSignCustomerDemand(String customerDemandId, SignInfo signInfo) {

        CustomerDemand customerDemand = iCustomerDemandService.getOne(customerDemandId);;
        //提交审核任务
        try {
            ResultBean resultBean = null;
            WorkFlowBillBean workFlowBillBean = iWorkflowBizService.actGetBillWorkflow(customerDemandId).getD();
            if (workFlowBillBean == null ) {
                return ResultBean.getFailed();
            }
            if ("CustomerDemand_Complete".equals(workFlowBillBean.getCurrentTask())) {
                TEMSignInfo s = (TEMSignInfo) signInfo;
                //客户信息
                CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerDemand.getCustomerId()).getD();
                //借款信息
                CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(customerDemand.getCustomerLoanId()).getD();
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
                    return ResultBean.getFailed().setD(ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERDEMAND_ISFINALNOREJECT")));
                }
                approveVars.put("final", isFinal);
                s.setAuditStatus(isFinal);
                s.setApproveVars(approveVars);
                resultBean = iWorkflowBizService.actSignBill(customerDemandId, s, true);
            } else {
                resultBean = iWorkflowBizService.actSignBill(customerDemandId, signInfo);
            }
            if (resultBean.failed()) {
                return ResultBean.getFailed().setM(resultBean.getM());
            }
            //获取购车资质
            customerDemand = iCustomerDemandService.getOne(customerDemandId);
            //查询资质流程是否结束，最终状态为拒绝则废弃签约数据
            ResultBean<Boolean> booleanResultBean = iWorkflowBizService.actBusinessBillIsFinish(customerDemand.getBillTypeCode() + "." + customerDemand.getId());
            if (booleanResultBean.isSucceed()) {
                if (booleanResultBean.getD()) {//流程结束
                    //若资质拒绝，判断签约是否存在，存在则直接废弃
                    if (customerDemand.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
                        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerDemand.getCustomerTransactionId());
                        if (purchaseCarOrder != null) {
                            purchaseCarOrder = iOrderService.discard(purchaseCarOrder.getId());
                            if (purchaseCarOrder.getDataStatus() == DataStatus.DISCARD) {
                                logger.info(messageService.getMessage("MSG_CUSTOMERDEMAND_REJECT"));
                                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERDEMAND_REJECT"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }

        //查询当前单据任务是否完成
        return ResultBean.getSucceed().setD(mappingService.map(customerDemand, CustomerDemandBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
    }

//    /**
//     * 资质审核签批
//     * @param customerDemandId
//     * @param temSignInfo
//     * @return
//     */
//    @Override
//    public ResultBean<CustomerDemandBean> actSignCustomerDemand(String customerDemandId, TEMSignInfo temSignInfo) {
//        //四级审批功能
//        CustomerDemand customerDemand = iCustomerDemandService.getOne(customerDemandId);
//        //客户信息
//        CustomerBean customerBean = iCustomerBizService.actGetCustomerById(customerDemand.getCustomerId()).getD();
//        //借款信息
//        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(customerDemand.getCustomerLoanId()).getD();
//        //默认为初审
//
//
//        //参配项中获取审批权限数据
//        Map<?, ?> permission = iParamBizService.actGetMap("TEM_APPROVAL_PERMISSION").getD();
//        List<String> specialRoles = iAuthenticationBizService.actGetSpecialRoleByUserId(temSignInfo.getUserId()).getD();
//        Integer isFinal = 0; // 默认初审
//        for (String role : specialRoles) {
//            Map<String,Object> data = (Map<String, Object>) permission.get(role);
//            Double downPaymentRatio1 = (Double) data.get("downPaymentRatio");
//            Double creditAmount1 = (Double) data.get("creditAmount");
//            Integer isSelfEmployed1 = (Integer) data.get("isSelfEmployed");
//            if(downPaymentRatio < downPaymentRatio1 && creditAmount > creditAmount1 && isSelfEmployed.equals(isSelfEmployed1)){
//                isFinal = 1; // 终审
//                break;
//            }
//        }
//        //查询当前单据任务是否完成
//        return ResultBean.getSucceed().setD(mappingService.map(customerDemand, CustomerDemandBean.class)).setM(messageService.getMessage("MSG_SUCESS_OPERATION"));
//    }

    /**
     * 保存客户信息
     *
     * @param
     * @return
     *//*
    private CustomerBean saveCustomer(CustomerBean customer) {
        //通过身份证号查询客户
        CustomerBean existCustomer = iCustomerBizService.actGetCustomerByIdentifyNo(customer.getIdentifyNo()).getD();
        if (existCustomer != null) { //身份证号已存在

            //更新客户信息
            customer.setId(existCustomer.getId());
        }
        return iCustomerBizService.actSaveCustomer(customer).getD();
    }*/
    @Override
    public ResultBean<CustomerDemandBean> actFindCustomerDemandByMateCustomerId(String customerId) {
        CustomerDemand customerDemand = iCustomerDemandService.findCarDemandByMateCustomerId(customerId);
        if (customerDemand != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerDemand, CustomerDemandBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    /**
     * 通过ID获取购车资质详情
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<CustomerDemandBean> actFindCustomerDemandById(String id) {
        CustomerDemand customerDemand = iCustomerDemandService.getOne(id);
        if (customerDemand != null) {
            String code = customerDemand.getBillTypeCode();
            //通过编码获取单据类型
            BillTypeBean billType = iBaseDataBizService.actGetBillType(code).getD();
            CustomerDemandBean customerDemandBean = mappingService.map(customerDemand, CustomerDemandBean.class);
            customerDemandBean.setBillType(billType);
            return ResultBean.getSucceed().setD(customerDemandBean);

        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    /**
     * 通过客户ID获取购车资质详情
     *
     * @param customerId
     * @return
     */
    @Override
    public ResultBean<CustomerDemandBean> actFindCustomerDemandByCustomerId(String customerId) {
        CustomerDemand customerDemand = iCustomerDemandService.findByCustomerId(customerId);
        if (customerDemand != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerDemand, CustomerDemandBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
    }

    /**
     * 模糊查询 资质列表
     *
     * @param searchBean
     * @return
     */
    @Override
    public ResultBean<CustomerDemandBean> actSearchCustomerDemands(String userId, SearchBean searchBean) {
        Page<CustomerDemand> demands = iCustomerDemandService.findAllBySearchBean(CustomerDemand.class,searchBean,SearchBean.STAGE_DEMAND,userId);
        return ResultBean.getSucceed().setD(mappingService.map(demands, CustomerDemandBean.class));
    }

    /**
     * 保存  资质信息
     *
     * @param customerDemandBean
     * @return
     */
    @Override
    public ResultBean<CustomerDemandBean> actSaveCustomerDemand(CustomerDemandBean customerDemandBean) {
        CustomerDemand customerDemand = mappingService.map(customerDemandBean, CustomerDemand.class);
        customerDemand = iCustomerDemandService.save(customerDemand);
        return ResultBean.getSucceed().setD(mappingService.map(customerDemand, CustomerDemandBean.class)).setM(messageService.getMessage("MSG_SUCESS_SAVE"));
    }

    @Override
    public ResultBean<CustomerDemandBean> actGetByCustomerTransactionId(String customerTransactionId) {
        CustomerDemand bean = iCustomerDemandService.findByCustomerTransactionId(customerTransactionId);
        if (bean != null) {
            return ResultBean.getSucceed().setD(mappingService.map(bean, CustomerDemandBean.class));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
    }

    /**
     * 保存是否需要反担保人
     *
     * @param customerDemandBean
     * @return
     */
    @Override
    public ResultBean<CustomerDemandBean> actSaveCustomerDemandNeedCounterGuarantor(CustomerDemandBean customerDemandBean) {
        CustomerDemand customerDemand = iCustomerDemandService.getOne(customerDemandBean.getId());
        if (customerDemand != null) {
            customerDemand.setNeedCounterGuarantor(customerDemandBean.getNeedCounterGuarantor());
            customerDemand = iCustomerDemandService.save(customerDemand);
            return ResultBean.getSucceed().setD(mappingService.map(customerDemand, CustomerDemandBean.class));
        }
        return ResultBean.getFailed();
    }

    /**
     * 获取分期经理的分页信息
     *
     * @param isPass
     * @param loginUserId （分期经理的用户ID）
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ResultBean<DataPageBean<DemandListBean>> actGetCustomerDemands(Boolean isPass, String loginUserId, Integer pageIndex, Integer pageSize) {
        Page<CustomerDemand> customerDemands = null;
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORDER_LOGINUSERID_ID_NULL"), loginUserId));
        }

        List<String> tids = iCustomerTransactionBizService.actGetTransactionIds(loginUserId, isPass).getD();
        if (isPass) {
            customerDemands = this.iCustomerDemandService.findCompletedItemsByUser(CustomerDemand.class, loginUserId, tids, pageIndex, pageSize);
            if (customerDemands == null || customerDemands.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEMAND_LOGINUSERID_HISTORY_NULL"));
            }
        } else {
            customerDemands = this.iCustomerDemandService.findPendingItemsByUser(CustomerDemand.class, loginUserId, tids, pageIndex, pageSize);
            if (customerDemands == null || customerDemands.getTotalElements() <= 0) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEMAND_LOGINUSERID_NULL"));
            }
        }


        DataPageBean<DemandListBean> destination = new DataPageBean<DemandListBean>();
        destination.setPageSize(customerDemands.getSize());
        destination.setTotalCount(customerDemands.getTotalElements());
        destination.setTotalPages(customerDemands.getTotalPages());
        destination.setCurrentPage(customerDemands.getNumber());
        destination.setResult(this.getDemandList(customerDemands.getContent()));
        return ResultBean.getSucceed().setD(destination);
    }

    private List<DemandListBean> getDemandList(List<CustomerDemand> demands) {
        List<DemandListBean> result = new ArrayList<DemandListBean>();
        for (CustomerDemand demand : demands) {
            DemandListBean demandListBean = mappingService.map(demand, DemandListBean.class);

            CustomerTransactionBean ct = iCustomerTransactionBizService.actFindCustomerTransactionById(demand.getCustomerTransactionId()).getD();

            //客户信息
            CustomerBean customerBean = iCustomerBizService.actGetCustomerById(demand.getCustomerId()).getD();

            //车辆信息
            CustomerCarBean customerCarBean = iCustomerBizService.actGetCustomerCarById(demand.getCustomerCarId()).getD();

            //借款信息
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(demand.getCustomerLoanId()).getD();

            //经销商信息
            CarDealerBean carDealerBean = iCarDealerBizService.actGetOneCarDealer(demand.getCarDealerId()).getD();

            TransactionSummaryBean transactionSummary = iCarTransactionBizService.actGetTransactionSummary(mappingService.map(ct, CustomerTransactionBean.class),
                    customerBean,
                    customerCarBean,
                    customerLoanBean,
                    carDealerBean);

            transactionSummary.setApproveStatus(demand.getApproveStatus());
            demandListBean.setTransactionSummary(transactionSummary);

            result.add(demandListBean);
        }

        return result;
    }


    /**
     * 日报
     * @param date
     * @param t
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> getDailyReport(String orgId,String date, CustomerDemandBean t) {

        Map<Object, Object> dailyReport = iCustomerDemandService.getDailyReport(orgId, date, mappingService.map(t, CustomerDemand.class));
        if(dailyReport != null){
            return ResultBean.getSucceed().setD(dailyReport);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Map<Object, Object>> getEmployeeReport(String date, CustomerDemandBean t, String employeeId) {
        CustomerDemand customerDemand = mappingService.map(t, CustomerDemand.class);
        Map<Object, Object> report = iCustomerDemandService.getEmployeeReport(employeeId, date, customerDemand);
        if(report != null){
            return ResultBean.getSucceed().setD(report);
        }
        return ResultBean.getFailed();
}

    @Override
    public ResultBean<Map<Object, Object>> getChannelReport(String date, CustomerDemandBean t, String loginUserId) {
        String orgInfoId = iOrgBizService.actFindEmployeeByLoginUserId(loginUserId).getD().getOrgInfoId();
        CustomerDemand customerDemand = mappingService.map(t, CustomerDemand.class);
        Map<Object, Object> report = iCustomerDemandService.getCarDealerReport(orgInfoId ,date, customerDemand);
        if(report != null){
            return ResultBean.getSucceed().setD(report);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<Map>> getAllCustomerByApproveStatus(Integer as) {

        List<CustomerDemand> items = null;
        if (as == ApproveStatus.APPROVE_INIT) {
            items = iCustomerDemandService.findAllByTsDesc(CustomerDemand.getTsSort());
        } else {
            items = iCustomerDemandService.findAllByDataStatusAndApproveStatus(DataStatus.SAVE,as,CustomerDemand.getTsSort());
        }
        return convertCustomerMapList(items);
    }

    private ResultBean<List<Map>> convertCustomerMapList(List<CustomerDemand> items) {
        List<Map>   resultList = new ArrayList<Map>();
        for(CustomerDemand t:items){
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
            dataMap.put("employeeName",iOrgBizService.actGetEmployee(customerTransactionBean.getEmployeeId()).getD().getUsername()== null? "N/A": iOrgBizService.actGetEmployee(customerTransactionBean.getEmployeeId()).getD().getUsername());
            resultList.add(dataMap);
        }
        return ResultBean.getSucceed().setD(resultList);
    }

}