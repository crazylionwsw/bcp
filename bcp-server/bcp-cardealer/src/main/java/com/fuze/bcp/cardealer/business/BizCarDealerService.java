package com.fuze.bcp.cardealer.business;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysRoleBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.*;
import com.fuze.bcp.api.cardealer.bean.*;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.WorkFlowBillBean;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.*;
import com.fuze.bcp.cardealer.domain.*;
import com.fuze.bcp.cardealer.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.SimpleUtils;
import com.fuze.bcp.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.lang.StringUtils;

/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class BizCarDealerService implements ICarDealerBizService {

    @Autowired
    ICarDealerService iCarDealerService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    ICashSourceBizService iCashSourceBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    IValidateBizService iValidateBizService;

    @Autowired
    IDealerSharingRatioService iDealerSharingRatioService;

    @Autowired
    IDealerGroupService iDealerGroupService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResultBean<CarDealerBean> actSaveCarDealer(CarDealerBean carDealerBean) {
        CarDealer carDealer = mappingService.map(carDealerBean, CarDealer.class);
        carDealer.setDealerRateTypes(carDealerBean.getDealerRateTypes());
        carDealer.setPayAccounts(carDealerBean.getPayAccounts());
        carDealer = iCarDealerService.save(carDealer);
        if (carDealer != null) {
            return ResultBean.getSucceed().setD(mappingService.map(carDealer, carDealerBean.getClass()));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CarDealerBean> actGetCarDealer(String id) {
        CarDealer carDealer = iCarDealerService.getAvailableOne(id);
        if (carDealer == null) {
            return ResultBean.getFailed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(carDealer, CarDealerBean.class));
    }

    @Override
    public ResultBean<List<CarDealerListBean>> actGetCarDealerQuery(String loginUserId, String inputStr) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("name").regex(".*?\\" + inputStr + ".*"), Criteria.where("manager").regex(".*?\\" + inputStr + ".*")
                , Criteria.where("cell").regex(".*?\\" + inputStr + ".*"), Criteria.where("address").regex(".*?\\" + inputStr + ".*"));
        criteria.and("loginUserId").is(loginUserId);
        List<CarDealer> carDealers = mongoTemplate.find(new Query(criteria), CarDealer.class);
        return ResultBean.getSucceed().setD(mappingService.map(carDealers, CarDealerListBean.class));
    }

    /**
     * 提交经销商信息，进入审批流
     *
     * @param carDealerSubmissionBean
     * @return
     */
    public ResultBean<CarDealerBean> actSaveCarDealerSubmission(CarDealerSubmissionBean carDealerSubmissionBean) {

        //根据id判断是否新建
        if (StringHelper.isBlank(carDealerSubmissionBean.getId())) {
            carDealerSubmissionBean.setId(null);
        }

        String isUnique = iValidateBizService.actCheckUnique(carDealerSubmissionBean, "name", carDealerSubmissionBean.getName()).getD();
        if (isUnique.equals("false")) {
            return ResultBean.getFailed().setM("该经销商已存在，请勿重复提交！");
        }

        //判断新增还是编辑
        CarDealer oldCarDealer = null;
        if (carDealerSubmissionBean.getId() != null) {
            oldCarDealer = this.iCarDealerService.getOne(carDealerSubmissionBean.getId());
            //审核中和审核完成的数据不允许编辑
            if (oldCarDealer != null && oldCarDealer.getApproveStatus() == ApproveStatus.APPROVE_ONGOING) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_NOEDIT"));
            }
            if (oldCarDealer != null && oldCarDealer.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_REJECTNOEDIT"));
            }
        }

        CarDealer carDealer = mappingService.map(carDealerSubmissionBean, CarDealer.class);

        //解析业务类型字符串为数组说   "NC:OC"
        List<String> busList = new ArrayList<>();
        String[] strs = carDealerSubmissionBean.getBusinessType().split(":");
        for (int i = 0; i < strs.length; i++) {
            if (!"null".equals(strs[i])) {
                busList.add(strs[i]);
            }
        }
        carDealer.setBusinessTypeCodes(busList);

        EmployeeLookupBean employee = null;
        if (carDealerSubmissionBean.getLoginUserId() != null) {
            employee = iOrgBizService.actGetEmployeeByLogin(carDealerSubmissionBean.getLoginUserId()).getD();
            if (employee != null && employee.getId() != null) {
                carDealer.setEmployeeId(employee.getId());
                //保存组织机构id
                carDealer.setOrginfoId(employee.getOrgInfoId());
            }
        }

        List<SalesRate> tmplist = new ArrayList<>();
        SalesRate salesRate;
        BusinessRate businessRate;
        List<DealerRate> subList = carDealerSubmissionBean.getDealerRateTypes();
        DealerRate dr = null;
        for (int i = 0; i < subList.size(); i++) {
            dr = subList.get(i);
            if (dr != null) {
                businessRate = new BusinessRate();
                businessRate.setSourceRateId(BusinessRate.DEF_SOURCE_RATE_ID);
                businessRate.setRateTypeList(dr.getSalePoliceRatioList());
                List<BusinessRate> businessRates = new ArrayList<>();
                businessRates.add(businessRate);
                salesRate = new SalesRate();
                salesRate.setRateTypeList(businessRates);
                salesRate.setBusinessTypeCode(dr.getBusinessType());
                tmplist.add(salesRate);
            }
        }
        carDealer.setDealerRateTypes(tmplist);
        //  TODO:解析渠道账户--并进行保存
        List<PayAccount> payAccounts = new ArrayList<PayAccount>();
        List<PayAccountBean> payAccountBeans = carDealerSubmissionBean.getPayAccounts();
        for (PayAccountBean payAccountBean : payAccountBeans) {
            PayAccount payAccount = mappingService.map(payAccountBean, PayAccount.class);
            String accountNature = payAccountBean.getAccountNature();
            switch (accountNature) {
                case "0:1":
                    //付款账户
                    payAccount.setAccountWay(PayAccount.ACCOUNTWAY_PAYMENT);
                    break;
                case "1:0":
                    //收款账户
                    payAccount.setAccountWay(PayAccount.ACCOUNTWAY_RECIEVED);
                    break;
                case "1:1":
                    //收付款账户
                    payAccount.setAccountWay(PayAccount.ACCOUTNWAY_ALL);
                    break;
            }
            payAccounts.add(payAccount);
        }
        carDealer.setPayAccounts(payAccounts);

        if (oldCarDealer != null) {
            //更新时，需将web端独有的字段同步过来
            if (oldCarDealer.getStartDate() != null)
                carDealer.setStartDate(oldCarDealer.getStartDate());
            if (oldCarDealer.getPayPeriod() != null)
                carDealer.setPayPeriod(oldCarDealer.getPayPeriod());
        }
        carDealer.setApproveStatus(ApproveStatus.APPROVE_ONGOING);
        carDealer = iCarDealerService.save(carDealer);

        //保存经销商员工
        if (carDealerSubmissionBean.getId() == null) {
            if (carDealerSubmissionBean.getEmployeeInfos() != null) {
                if (carDealerSubmissionBean.getEmployeeInfos().size() > 0) {
                    List<DealerEmployeeBean> dealerEmployees = carDealerSubmissionBean.getEmployeeInfos();
                    //设置渠道员工的渠道ID
                    for (int i = 0; i < dealerEmployees.size(); i++) {
                        dealerEmployees.get(i).setCarDealerId(carDealer.getId());
                    }
                    dealerEmployees = iBaseDataBizService.actSaveDealerEmployee(dealerEmployees).getD();
                }
            }
        }


        if (carDealer != null) {
            //提交进工作流
            String collectionName = null;
            try {
                collectionName = CarDealer.getMongoCollection(carDealer);
            } catch (Exception e) {
                // TODO: 2017/9/9
                e.printStackTrace();
            }
            SignInfo signInfo = new SignInfo(carDealer.getLoginUserId(), carDealer.getEmployeeId(), SignInfo.SIGN_PASS, SignInfo.FLAG_COMMIT, carDealer.getCommentInfo());
            iWorkflowBizService.actSubmit(carDealer.getBusinessTypeCodes().toString(), carDealer.getId(), carDealer.getBillTypeCode(), signInfo, collectionName, null, null).getD();
            return ResultBean.getSucceed().setD(mappingService.map(carDealer, CarDealerBean.class));
        }
        return ResultBean.getFailed();
    }


    /**
     * 提交审批
     *
     * @return
     */
    public ResultBean<CarDealerBean> actSignCarDealer(String carDealerId, SignInfo signInfo) {
        //提交审核任务
        try {
            ResultBean<WorkFlowBillBean> resultBean = iWorkflowBizService.actSignBill(carDealerId, signInfo);
            if (resultBean.failed()) {
                return ResultBean.getFailed().setM(resultBean.getM());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAILED_SIGN"));
        }
        //获取渠道信息
        CarDealer carDealer = iCarDealerService.getOne(carDealerId);

        // 签批
        if (carDealer != null) {
            if (carDealer.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {//审核通过
                carDealer.setStatus(CarDealerBean.STATUS_ONGOING);
                carDealer.setStartDate(DateTimeUtils.getCreateTime());
            } else if (carDealer.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {//审核拒绝
                carDealer.setStatus(CarDealerBean.STATUS_STOP);
            }
        }
        carDealer = iCarDealerService.save(carDealer);
        return ResultBean.getSucceed().setD(mappingService.map(carDealer, CarDealerBean.class));
    }



    @Override
    public ResultBean<CarDealerBean> actDeleteCarDealer(String id) {
        CarDealer carDealer = iCarDealerService.getOne(id);
        if (carDealer != null) {
            carDealer = iCarDealerService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(carDealer, CarDealerBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CarDealerBean> actGetOneCarDealer(String id) {
        CarDealer carDealer = iCarDealerService.getOne(id);
        if (carDealer == null) {
            return ResultBean.getFailed().setM("获取渠道信息错误！");
        }
        CarDealerBean carDealerBean = mappingService.map(carDealer, CarDealerBean.class);
        carDealerBean.setDealerRateTypes(carDealer.getDealerRateTypes());
        return ResultBean.getSucceed().setD(carDealerBean);
    }

    /**
     * 检查经销商是否经营某品牌
     *
     * @param carDealerId
     * @param carTypeId
     * @return
     */
    public ResultBean<Integer> actCheckDealerCarType(String carDealerId, String carTypeId) {
        ResultBean result = this.actGetOneCarDealer(carDealerId);
        if (result.failed()) return result;

        CarDealerBean carDealer = (CarDealerBean) result.getD();

        //通过车型获取品牌
        CarTypeBean carType = iCarTypeBizService.actGetCarTypeById(carTypeId).getD();
        String carBrandId = carType.getCarBrandId();

        //未限定品牌
        if (carDealer.getCarBrandIds() == null || carDealer.getCarBrandIds().size() == 0)
            return ResultBean.getSucceed().setD(1);

        if (carDealer.getCarBrandIds().contains(carBrandId))
            return ResultBean.getSucceed().setD(1);

        return ResultBean.getSucceed().setD(0);
    }

    @Override
    public ResultBean<CashSourceBean> actFindCashSource(@NotNull String id) {
        CarDealer carDealer = iCarDealerService.getOne(id);
        if (carDealer == null) {
            return ResultBean.getFailed().setM("4S店经销商信息不存在!");
        } else {
            if (carDealer.getCashSourceId() == null) {
                return ResultBean.getFailed().setM("该4S店未分配报单支行！");
            }
            return iCashSourceBizService.actGetCashSource(carDealer.getCashSourceId());
        }
    }

    @Override
    public ResultBean<CashSourceBean> actFindCooperationCashSource(@NotNull String id) {
        CarDealer carDealer = iCarDealerService.getOne(id);
        if (carDealer == null) {
            return ResultBean.getFailed().setM("4S店经销商信息不存在!");
        } else {
            if (carDealer.getCooperationCashSourceId() == null) {
                return ResultBean.getFailed().setM("该4S店未分配渠道合作支行！");
            }
            return iCashSourceBizService.actGetCashSource(carDealer.getCooperationCashSourceId());
        }
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCarDealer() {
        List<CarDealer> carDealers = iCarDealerService.getAvaliableAll();
        if (carDealers == null)
            return ResultBean.getSucceed();

        return ResultBean.getSucceed().setD(mappingService.map(carDealers, APILookupBean.class));
    }

    @Override
    public ResultBean<List<CarDealerBean>> actCarDealers() {
        List<CarDealer> carDealers = iCarDealerService.getAll();
        List<CarDealerBean> cRD = new ArrayList<>();
        for (CarDealer cd : carDealers) {
            CarDealerBean carDealerBean = new CarDealerBean();
            carDealerBean.setDealerRateTypes(cd.getDealerRateTypes());
            carDealerBean.setStartDate(cd.getStartDate());
            carDealerBean.setCode(cd.getCode());
            carDealerBean.setName(cd.getName());
            carDealerBean.setDataStatus(cd.getDataStatus());
            carDealerBean.setLoginUserId(cd.getLoginUserId());
            carDealerBean.setActivitiId(cd.getActivitiId());
            carDealerBean.setAddress(cd.getAddress());
            List<String> bMan = new ArrayList<String>();
            for (String businessMan : cd.getBusinessManIds()) {
                bMan.add(businessMan);
            }
            carDealerBean.setBusinessManIds(bMan);
            carDealerBean.setBusinessTypeCodes(cd.getBusinessTypeCodes());
            carDealerBean.setCarBrandIds(cd.getCarBrandIds());
            carDealerBean.setCashSourceId(cd.getCashSourceId());
            carDealerBean.setCell(cd.getCell());
            carDealerBean.setCompensatoryRatio(cd.getCompensatoryRatio());
            carDealerBean.setCooperationCashSourceId(cd.getCooperationCashSourceId());
            carDealerBean.setCustomerNumber(cd.getCustomerNumber());
            carDealerBean.setEmployeeId(cd.getEmployeeId());
            carDealerBean.setLeaderId(cd.getLeaderId());
            carDealerBean.setManager(cd.getManager());
            carDealerBean.setPayPeriod(cd.getPayPeriod());
            carDealerBean.setOrginfoId(cd.getOrginfoId());
            carDealerBean.setTelephone(cd.getTelephone());
            carDealerBean.setPayAccounts(cd.getPayAccounts());
            carDealerBean.setSourceDeals(cd.getSourceDeals());
            carDealerBean.setStatus(cd.getStatus());
            carDealerBean.setSumLoan(cd.getSumLoan());
            carDealerBean.setTurnover(cd.getTurnover());
            carDealerBean.setId(cd.getId());
            carDealerBean.setTs(cd.getTs());
            cRD.add(carDealerBean);
        }
        return ResultBean.getSucceed().setD(cRD);
    }

    @Override
    public ResultBean<DataPageBean<CarDealerBean>> actGetCarDealers(Integer currentPage) {
        Page<CarDealer> carDealers = iCarDealerService.getAllOrderByTs(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(carDealers, CarDealerBean.class));
    }

    @Override
    public ResultBean<CarDealerBean> actSearchCarDealers(Integer currentPage, CarDealerBean carDealerBean) {

        Page<CarDealer> carDealers = iCarDealerService.searchCarDealers(currentPage, mappingService.map(carDealerBean, CarDealer.class));
        return ResultBean.getSucceed().setD(mappingService.map(carDealers, CarDealerBean.class));
    }

    @Override
    public ResultBean<List<CarDealerBean>> actGetCarDealers() {
        List<CarDealer> carDealers = iCarDealerService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(carDealers, CarDealerBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CarDealerListBean>> actGetCarDealersPageByLoginUserId(Integer currentPage, Integer currentSize, String loginUserId) {

        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_NULL"), loginUserId));
        }

        EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
        if (employee == null)
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_NULL"), loginUserId));


        List<String> role = this.getRoleByLoginUserId(loginUserId);
        Page<CarDealer> carDealers;
        if (role.contains("CHANNEL_MANAGER")) {
            carDealers = iCarDealerService.getChannelCarDealers(employee.getId(), currentPage, currentSize);
        } else {
            carDealers = iCarDealerService.getSalesCarDealers(employee.getId(), currentPage, currentSize);
        }

        if (carDealers != null) {
            List<CarDealer> carDealerList = carDealers.getContent();
            List<CarDealerListBean> carDealerListBeans = new ArrayList<CarDealerListBean>();

            for (CarDealer carDealer : carDealers) {
                CarDealerListBean carDealerListBean = getCarDealerListBean(carDealer);
                List<String> bList = new ArrayList<String>();
                List<String> bName = new ArrayList<String>();
                if (role.contains("SALES_MANAGER") && !role.contains("CHANNEL_MANAGER")) {
                    for (String bId : carDealerListBean.getBusinessManIds()) {
                        if (bId.equals(employee.getId())) {
                            bList.add(bId);
                            bName.add(employee.getUsername());
                        }
                    }
                    carDealerListBean.setBusinessManIds(bList);
                    carDealerListBean.setBusinessMans(bName);
                }

                carDealerListBeans.add(carDealerListBean);
            }

            DataPageBean<CarDealerListBean> destination = new DataPageBean<CarDealerListBean>();
            destination.setPageSize(carDealers.getSize());
            destination.setTotalCount(carDealers.getTotalElements());
            destination.setTotalPages(carDealers.getTotalPages());
            destination.setCurrentPage(carDealers.getNumber());
            destination.setResult(carDealerListBeans);
            return ResultBean.getSucceed().setD(destination);
        } else {
            return ResultBean.getSucceed();
        }
    }

    private List<String> getRoleByLoginUserId(String loginUserId) {
        List<String> rCode = new ArrayList<String>();
        ResultBean<LoginUserBean> loginUserBean = iAuthenticationBizService.actGetLoginUser(loginUserId);
        String role = null;
        if (loginUserBean.getD() != null) {
            for (int i = 0; i < loginUserBean.getD().getUserRoleIds().size(); i++) {
                String roleId = loginUserBean.getD().getUserRoleIds().get(i);
                ResultBean<SysRoleBean> sysRoleBean = iAuthenticationBizService.actGetAvailableSysRole(roleId);
                if (sysRoleBean.getD() != null) {
                    role = sysRoleBean.getD().getCode();
                    rCode.add(role);
                }
            }
        }
        return rCode;
    }

    private CarDealerListBean getCarDealerListBean(CarDealer carDealer) {
        CarDealerListBean carDealerListBean = mappingService.map(carDealer, CarDealerListBean.class);
        //获取员工数
        carDealerListBean.setCountOfEmployees(iBaseDataBizService.actCountDealerEmployees(carDealer.getId()).getD());
        List<String> bName = new ArrayList<String>();
        if (carDealer.getBusinessManIds() != null)
            for (String businessManId : carDealer.getBusinessManIds()) {
                if (StringUtils.isEmpty(businessManId)) {
                    continue;
                }
                bName.add(this.getBusinessManName(businessManId));
                carDealerListBean.setPaymentPolicy(carDealer.getPaymentPolicy());
            }
        carDealerListBean.setBusinessMans(bName);

        carDealerListBean.setBusinessTypes(this.getBusinessTypeNames(carDealer.getBusinessTypeCodes()));
        return carDealerListBean;
    }

    private String getBusinessManName(String businessManId) {

        EmployeeBean employee = iOrgBizService.actGetEmployee(businessManId).getD();
        if (employee != null)
            return employee.getUsername();
        else
            return "";
    }

    private List<String> getBusinessTypeNames(List<String> businessTypeCodes) {
        List<BusinessTypeBean> bts = iBaseDataBizService.actGetAvaliableListByCodes(businessTypeCodes).getD();
        List<String> names = new ArrayList<String>();
        for (BusinessTypeBean bt : bts) {
            names.add(bt.getName());
        }
        return names;
    }


    @Override
    public ResultBean<List<CarDealerListBean>> actGetCarDealersByLoginUserId(String loginUserId, String ctype) {
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_NULL"), loginUserId));
        }
        if (StringHelper.isBlock(ctype)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BUSINESSMAN_TYPE_NULL"), ctype));
        }
        List<CarDealer> carDealers = iCarDealerService.getCarDealersByLoginUserId(loginUserId);
        List<CarDealerListBean> carDealerListBeans = new ArrayList<CarDealerListBean>();
        for (CarDealer carDealer : carDealers) {
            if (carDealer != null && carDealer.getBusinessTypeCodes().contains(ctype)) {
                CarDealerListBean carDealerListBean = getCarDealerListBean(carDealer);

                carDealerListBeans.add(carDealerListBean);
            }
        }
        return ResultBean.getSucceed().setD(carDealerListBeans);

    }

    public ResultBean<List<CarDealerBean>> actGetCarDealersByEmployeeIds(List<String> employeeIds) {
        List<CarDealer> carDealers = iCarDealerService.getCarDealersByEmployeeIds(employeeIds);

        return ResultBean.getSucceed().setD(mappingService.map(carDealers, CarDealerBean.class));
    }

    @Override
    public ResultBean<List<CarDealerBean>> actGetCarDealersByGroup(String groupId) {
        List<CarDealer> carDealers = iCarDealerService.getCarDealersByGroupId(groupId);
        return ResultBean.getSucceed().setD(mappingService.map(carDealers, CarDealerBean.class));
    }

    @Override
    public ResultBean<List<CarDealerListBean>> actGetMyCarDealers(String loginUserId) {
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_NULL"), loginUserId));
        }


        EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
        if (employee == null)
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_NULL"), loginUserId));


        List<EmployeeBean> childEmployees = iOrgBizService.actGetChildEmployees(employee.getId()).getD();
        List<String> employeeIds = new ArrayList<String>();
        employeeIds.add(employee.getId());
        for (EmployeeBean e : childEmployees) {
            employeeIds.add(e.getId());
        }
        List<CarDealer> carDealers = iCarDealerService.getMyCarDealers(employeeIds);
        List<CarDealerListBean> carDealerListBeans = new ArrayList<CarDealerListBean>();
        for (CarDealer carDealer : carDealers) {
            if (carDealer.getStatus() != CarDealerBean.STATUS_STOP) {
                CarDealerListBean carDealerListBean = getCarDealerListBean(carDealer);

                carDealerListBeans.add(carDealerListBean);
            }
        }
        return ResultBean.getSucceed().setD(carDealerListBeans);

    }

    @Override
    public ResultBean<List<CarDealerListBean>> actGetMyCarDealers(String loginUserId, String ctype) {
        if (StringHelper.isBlock(loginUserId)) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_NULL"), loginUserId));
        }

        EmployeeLookupBean employee = iOrgBizService.actGetEmployeeByLogin(loginUserId).getD();
        if (employee == null)
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_LOGINUSERID_NULL"), loginUserId));


        List<EmployeeBean> childEmployees = iOrgBizService.actGetChildEmployees(employee.getId()).getD();
        List<String> employeeIds = new ArrayList<String>();
        employeeIds.add(employee.getId());
        for (EmployeeBean e : childEmployees) {
            employeeIds.add(e.getId());
        }
        List<CarDealer> carDealers = iCarDealerService.getMyCarDealers(employeeIds);
        List<CarDealerListBean> carDealerListBeans = new ArrayList<CarDealerListBean>();
        if (carDealers != null && carDealers.size() > 0) {
            for (CarDealer carDealer : carDealers) {
                if (carDealer.getStatus() != CarDealerBean.STATUS_STOP && carDealer.getBusinessTypeCodes().contains(ctype)) {
                    CarDealerListBean carDealerListBean = getCarDealerListBean(carDealer);
                    carDealerListBeans.add(carDealerListBean);
                }
            }
            return ResultBean.getSucceed().setD(carDealerListBeans);
        } else {
            return ResultBean.getFailed();
        }


    }

    @Override
    public ResultBean<DealerEmployeeBean> actSaveDealerEmployee(DealerEmployeeBean dealerEmployeeBean) {
        return iBaseDataBizService.actSaveDealerEmployee(dealerEmployeeBean);
    }

    @Override
    public ResultBean<DealerEmployeeBean> actDeleteDealerEmployee(String id) {
        return iBaseDataBizService.actDeleteDealerEmployee(id);
    }


    @Override
    public ResultBean<DealerEmployeeBean> actGetOneDealerEmployee(String id) {
        return iBaseDataBizService.actGetOneDealerEmployee(id);

    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupDealerEmployees() {
        return iBaseDataBizService.actLookupDealerEmployees();
    }

    @Override
    public ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(Integer currentPage) {
        return iBaseDataBizService.actGetDealerEmployees(currentPage);
    }

    @Override
    public ResultBean<Integer> actCountDealerEmployees(String carDealerId) {
        return iBaseDataBizService.actCountDealerEmployees(carDealerId);
    }

    @Override
    public ResultBean<DataPageBean<DealerEmployeeBean>> actGetDealerEmployees(Integer currentPage, String carDealerId) {
        return iBaseDataBizService.actGetDealerEmployees(currentPage, carDealerId);
    }

    @Override
    public ResultBean<List<DealerEmployeeBean>> actGetAllDealerEmployees(String carDealerId) {
        return iBaseDataBizService.actGetAllDealerEmployees(carDealerId);
    }

    @Override
    public ResultBean<PayAccount> actSaveCarDealerPayAccount(String carDealerId, Integer pindex, PayAccountBean payAccountBean) {
        CarDealer bean = (CarDealer) this.iCarDealerService.getOne(carDealerId);
        if (bean == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PAYACCOUNT_NOCARDEALER"), carDealerId));
        }
        //审核中和审核完成的数据不允许编辑
        if (bean.getApproveStatus() == ApproveStatus.APPROVE_ONGOING) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_NOEDIT"));
        }
//        if (bean.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
//            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_PASSEDNOEDIT"));
//        }
        if (bean.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_REJECTNOEDIT"));
        }
        List<PayAccount> payAccounts = bean.getPayAccounts();
        for (PayAccount payAccount : payAccounts) {
            if (payAccount != null) {
                if (payAccountBean.getAccountNumber().equals(payAccount.getAccountNumber())) {//渠道账户不能重复
                    if(payAccountBean.getDefaultAccount() == payAccount.getDefaultAccount()){
                        return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_PAYACCOUNTNOREPEAT"));
                    }else {
                        if(payAccount.getDefaultAccount() == 0){
                            for(PayAccount account : bean.getPayAccounts()){
                                if(account.getDefaultAccount() == 0){
                                    account.setDefaultAccount(1);
                                }
                            }
                        }
                    }
                }
            }
        }
        PayAccount payAccount = mappingService.map(payAccountBean, PayAccount.class);
        String accountNature = payAccountBean.getAccountNature();
        switch (accountNature) {
            case "0:1":
                //付款账户
                payAccount.setAccountWay(PayAccount.ACCOUNTWAY_PAYMENT);
                break;
            case "1:0":
                //收款账户
                payAccount.setAccountWay(PayAccount.ACCOUNTWAY_RECIEVED);
                break;
            case "1:1":
                //收付款账户
                payAccount.setAccountWay(PayAccount.ACCOUTNWAY_ALL);
                break;
        }

        List<PayAccount> list = bean.getPayAccounts();
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
            list.add(payAccount);
        } else {
            if (payAccount.getDefaultAccount() == PayAccount.ACCOUTCHECK_ALL) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setDefaultAccount(PayAccount.ACCOUTCHECK_NALL);
                }
            }
            if (pindex == null || pindex == -1 || pindex > list.size()) {
                list.add(payAccount);
            } else {
                list.set(pindex, payAccount);
            }
        }
        bean.setPayAccounts(list);
        bean = iCarDealerService.save(bean);
        if (bean != null) {
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_SUCESS_SAVE"));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_SAVE"));
        }
    }

    @Override
    public ResultBean<CarDealerBean> actSetCarDealerBusinessMan(String carDealerId, String businessManId) {
        CarDealer bean = (CarDealer) this.iCarDealerService.getOne(carDealerId);
        EmployeeBean employee = this.iOrgBizService.actGetEmployee(businessManId).getD();
        //TODO 完善返回信息
        if (bean == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PAYACCOUNT_NOCARDEALER"), carDealerId));
        }
        if (employee == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BUSINESSMAN_NOTFIND"), businessManId));
        }
        List<String> businessManIds = new ArrayList<String>();
        businessManIds.add(businessManId);
        bean.setBusinessManIds(businessManIds);
        bean = this.iCarDealerService.save(bean);
        if (bean == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_BUSINESSMAN_SAVE_ERROR"));
        } else {
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_BUSINESSMAN_SAVE_SUCCESS"));
        }
    }

    @Override
    public ResultBean<CarDealerBean> actSetCarDealerBusinessMans(String carDealerId, List<String> businessManIds) {
        CarDealer bean = (CarDealer) this.iCarDealerService.getOne(carDealerId);
        for (String businessManId : businessManIds) {
            EmployeeBean employee = this.iOrgBizService.actGetEmployee(businessManId).getD();
            if (employee == null) {
                return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_BUSINESSMAN_NOTFIND"), businessManId));
            }
        }

        //TODO 完善返回信息
        if (bean == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PAYACCOUNT_NOCARDEALER"), carDealerId));
        }
        bean.setBusinessManIds(businessManIds);
        bean = this.iCarDealerService.save(bean);
        if (bean == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_BUSINESSMAN_SAVE_ERROR"));
        } else {
            return ResultBean.getSucceed().setM(messageService.getMessage("MSG_BUSINESSMAN_SAVE_SUCCESS"));
        }
    }


    @Override
    public ResultBean<CarDealerBean> actDeleteCarDealerPayAccount(String carDealerId, Integer pindex) {
        CarDealer bean = (CarDealer) this.iCarDealerService.getOne(carDealerId);
        if (bean == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ERROR_NOCARDEALER"), carDealerId));
        }
        if (pindex == null || pindex < 0) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_DATA_ERROR"), pindex));
        }
        //审核中和审核完成的数据不允许编辑
        if (bean.getApproveStatus() == ApproveStatus.APPROVE_ONGOING) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_NOEDIT"));
        }
//        if (bean.getApproveStatus() == ApproveStatus.APPROVE_PASSED) {
//            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_PASSEDNOEDIT"));
//        }
        if (bean.getApproveStatus() == ApproveStatus.APPROVE_REJECT) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CARDEALER_REJECTNOEDIT"));
        }
        List<PayAccount> list = bean.getPayAccounts();
        if (list != null && list.size() > 0 && pindex < list.size()) {
            list.remove((int) pindex);
            bean.setPayAccounts(list);
            bean = (CarDealer) this.iCarDealerService.save(bean);
            if (bean != null) {
                return ResultBean.getSucceed().setM(messageService.getMessage("MSG_PAYACCOUNT_DELETE_SUCCESS")).setD(mappingService.map(bean, CarDealerBean.class));
            }
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_PAYACCOUNT_DELETE_ERROR"));
    }

    @Override
    public ResultBean<Boolean> actCheckAuditPermission(String orgId, String employeeId) {
        if (employeeId.equals("-1"))
            return ResultBean.getSucceed().setD(true);

        OrgBean orginfo = iOrgBizService.actFindSuperOrgByOrgId(orgId).getD();
        if (orginfo != null && orginfo.getLeaderId() != null) {
            if (orginfo.getLeaderId().equals(employeeId))
                return ResultBean.getSucceed().setD(true);
        }
        return ResultBean.getSucceed().setD(false);
    }

    @Override
    public ResultBean<DataPageBean<CarDealerListBean>> actSearchCarDealers(String name, String loginUserId, int pageIndex, int pageSize) {
        Page<CarDealer> carDealerPage = iCarDealerService.searchCarDealers(name, loginUserId, pageIndex, pageSize);
        return ResultBean.getSucceed().setD(mappingService.map(carDealerPage, CarDealerListBean.class));
    }

    /**
     * 保存返佣比例
     *
     * @param dealerSharingRatio
     * @return
     */
    public ResultBean<DealerSharingRatioBean> actSaveDealerSharingRatio(DealerSharingRatioBean dealerSharingRatio) {

        DealerSharingRatio dealerSharingRatio1 = mappingService.map(dealerSharingRatio, DealerSharingRatio.class);
        dealerSharingRatio1 = iDealerSharingRatioService.save(dealerSharingRatio1);

        return ResultBean.getSucceed().setD(mappingService.map(dealerSharingRatio1, DealerSharingRatio.class));
    }

    /**
     * 获取渠道返佣比例
     *
     * @param carDealerId
     * @return
     */
    public ResultBean<DealerSharingRatioBean> actGetDealerSharingRatio(String carDealerId) {
        DealerSharingRatio ratio = iDealerSharingRatioService.getCarDealerRatio(carDealerId);

        if (ratio != null)
            return ResultBean.getSucceed().setD(mappingService.map(ratio, DealerSharingRatioBean.class));
        else
            return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<DealerGroupBean> actSaveDealerGroup(DealerGroupBean dealerGroupBean) {
        DealerGroup group = iDealerGroupService.save(mappingService.map(dealerGroupBean, DealerGroup.class));
        if (group == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEALEREMPLOYEE_SAVE_ERROR"));
        } else
            return ResultBean.getSucceed().setD(mappingService.map(group, DealerGroupBean.class)).setM(messageService.getMessage("MSG_DEALEREMPLOYEE_SAVE_SUCCESS"));
    }

    @Override
    public ResultBean<DealerGroupBean> actGetOneDealerGroup(@NotNull String id) {
        DealerGroup dealerGroup = iDealerGroupService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(dealerGroup, DealerGroupBean.class));
    }

    @Override
    public ResultBean<DealerGroupBean> actDeleteDealerGroup(String id) {
        DealerGroup group = iDealerGroupService.getOne(id);
        if (group != null) {
            group = iDealerGroupService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(group, DealerGroupBean.class)).setM(messageService.getMessage("MSG_DEALEREMPLOYEE_DELETE_SUCCESS"));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_DEALEREMPLOYEE_DELETE_ERROR"));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupDealerGroup() {
        List<DealerGroup> groups = iDealerGroupService.getAvaliableAll();
        if (groups == null)
            return ResultBean.getSucceed();

        return ResultBean.getSucceed().setD(mappingService.map(groups, APILookupBean.class));
    }

    @Override
    public ResultBean<DataPageBean<DealerGroupBean>> actGetDealerGroups(Integer currentPage) {
        Page<DealerGroup> groups = iDealerGroupService.getAllOrderByTs(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(groups, DealerGroupBean.class));
    }

    public ResultBean<DealerGroupBean> actGetDealerGroup(String id) {
        DealerGroup dealerGroup = iDealerGroupService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(dealerGroup, DealerGroupBean.class));
    }

    @Override
    public ResultBean<DealerEmployeeBean> actGetAvaliableDealerEmployee() {
        return iBaseDataBizService.actGetAvaliableDealerEmployee();
    }

    @Override
    public ResultBean<DealerEmployeeBean> actGetOneDealerEmployeeById(String id) {
        return iBaseDataBizService.actGetOneDealerEmployeeById(id);
    }

    /**
     * 获取渠道经理的渠道数量
     *
     * @param date
     * @param orginfoid
     * @return
     */
    @Override
    public ResultBean<Map<Object, Object>> actGetChannelCount(String date, String orginfoid, String employeeId) {
        Map<Object, Object> dataMap = new HashMap<>();
        Query query = new Query();
        String year = null;
        if (date == null) {
            String createTime = SimpleUtils.getCreateTime();
            year = createTime.substring(0, 7);
        } else {
            year = date.substring(0, 7);
        }
        query.addCriteria(Criteria.where("ts").regex(year, "m"));
        if (!StringUtils.isEmpty(orginfoid)) {
            List<String> orgIds = iOrgBizService.getAllChildOrginfoIds(orginfoid).getD();
            if (orgIds.size() == 0) orgIds.add(orginfoid);
            query.addCriteria(Criteria.where("orginfoId").in(orgIds));
        }
        if (!StringUtils.isEmpty(employeeId)) {
            query.addCriteria(Criteria.where("employeeId").is(employeeId));
        }

        List<CarDealer> result = mongoTemplate.find(query, CarDealer.class);

        dataMap.put("channelCount", result.size());
        if (dataMap != null) {
            return ResultBean.getSucceed().setD(dataMap);
        }
        return ResultBean.getFailed();
    }


    @Override
    public ResultBean<List<CarDealerBean>> actGetCarDealer(List<String> ids) {
        List<CarDealer> carDealers = iCarDealerService.getCardealerMore(ids);
        if (carDealers != null) {
            return ResultBean.getSucceed().setD(mappingService.map(carDealers, CarDealerBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CarDealerBean>> actGetCarDealerByOrgIds(List<String> orgIds) {
        List<CarDealer> carDealerBeen = iCarDealerService.getCarDealerByOrgIds(orgIds);
        return ResultBean.getSucceed().setD(mappingService.map(carDealerBeen, CarDealerBean.class));
    }

    public void actDeleteCarDealerByIds(List<String> ids) {
        iCarDealerService.deleteRealByIds(ids);
    }

    @Override
    public ResultBean<DealerEmployeeBean> actGetDealerEmployeeById(String employeeId) {
        DealerEmployeeBean dealerEmployee = iBaseDataBizService.actGetOneDealerEmployee(employeeId).getD();
        if (dealerEmployee != null) {
            CarDealer carDealer = iCarDealerService.getOne(dealerEmployee.getCarDealerId());
            if (carDealer != null) {
                dealerEmployee.setCarDealerName(carDealer.getName());
                dealerEmployee.setCarDealerAddress(carDealer.getAddress());
            } else {
                dealerEmployee.setCarDealerName("未知渠道");
            }
            return ResultBean.getSucceed().setD(dealerEmployee);
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_DELEREMPLOYEE_NOTFIND"), employeeId));
    }


    @Override
    public ResultBean<CarBrandBean> actGetCarBrandByCardealer(String carDealerId) {
        CarDealer carDealer = iCarDealerService.getOne(carDealerId);
        List list = new ArrayList();
        if (carDealer != null && carDealer.getCarBrandIds() != null && !carDealer.getCarBrandIds().isEmpty()) {
            list = iCarTypeBizService.actGetCarBrandList(carDealer.getCarBrandIds()).getD();
        } else {
            list = iCarTypeBizService.actGetCarBrandAll().getD();
        }
        return ResultBean.getSucceed().setD(mappingService.map(list, CarBrandBean.class));
    }

}
