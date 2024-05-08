package com.fuze.bcp.creditcar.business;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.bean.DealerGroupBean;
import com.fuze.bcp.api.cardealer.bean.DealerSharingRatioBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.DealerSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.GroupSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.SharingDetailsBean;
import com.fuze.bcp.api.creditcar.service.IDealerSharingBizService;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.DMVPledge;
import com.fuze.bcp.creditcar.domain.DealerSharing;
import com.fuze.bcp.creditcar.domain.GroupSharing;
import com.fuze.bcp.creditcar.domain.SharingDetails;
import com.fuze.bcp.creditcar.service.IDealerSharingDetailsService;
import com.fuze.bcp.creditcar.service.IDealerSharingService;
import com.fuze.bcp.creditcar.service.IDmvpledgeService;
import com.fuze.bcp.creditcar.service.IGroupSharingService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MutexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


/**
 * 渠道分成服务实现
 */
@Service
public class BizDealerSharingService implements IDealerSharingBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizDealerSharingService.class);

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Autowired
    IDealerSharingService iDealerSharingService;

    @Autowired
    IGroupSharingService iGroupSharingService;

    @Autowired
    IDealerSharingDetailsService iDealerSharingDetailsService;

    @Autowired
    IDmvpledgeService iDmvpledgeService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    MappingService mappingService;

    private SharingDetails newSharingDetails(SharingDetails sharingDetails, CustomerTransactionBean transactionBean, CustomerLoanBean customerLoan) {
        if (sharingDetails == null) {
            sharingDetails = new SharingDetails();
        }
        sharingDetails.setTransactionId(transactionBean.getId());
        sharingDetails.setMonths(customerLoan.getRateType().getMonths());
        sharingDetails.setCreditAmount(customerLoan.getCreditAmount());
        sharingDetails.setChargePaymentWay(customerLoan.getChargePaymentWay());
        sharingDetails.setCustomerId(transactionBean.getCustomerId());
        sharingDetails.setStatus(0);
        return sharingDetails;
    }


    /**
     * 根据transactionId 生成分成信息
     *
     * @param customerTransactionId
     * @return
     */
    public ResultBean<SharingDetailsBean> actSaveSharingDetails(String customerTransactionId, String date, Integer scope) {
        CustomerTransactionBean customerTransactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(customerTransactionId).getD();
        PurchaseCarOrderBean order = iOrderBizService.actGetOrderByTransactionId(customerTransactionId).getD();
        CustomerLoanBean customerLoan = iCustomerBizService.actGetCustomerLoanById(order.getCustomerLoanId()).getD();
        CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(customerTransactionBean.getCarDealerId()).getD();


        // 渠道分成比例
        DealerSharingRatioBean sharingRatio = iCarDealerBizService.actGetDealerSharingRatio(customerTransactionBean.getCarDealerId()).getD();
        if ((scope == 0 || scope == 1) && sharingRatio != null && sharingRatio.getSharingRatio() != null) {
            SharingDetails sharingDetails = iDealerSharingDetailsService.getByTransactionIdAndType(customerTransactionId, SharingDetailsBean.DEALERDETAIL);
            sharingDetails = newSharingDetails(sharingDetails, customerTransactionBean, customerLoan);
            sharingDetails.setMainPartType(null);
            sharingDetails.setSharingType(SharingDetailsBean.DEALERDETAIL);
            sharingDetails.setPledgeDateReceiveTime(date);
            Map<Integer, Double> ratioMap = sharingRatio.getSharingRatio();
            Double ratio = ratioMap.get(sharingDetails.getMonths());
            if (ratio != null && ratio > 0) {
                sharingDetails.setSharingRatio(ratio);
                BigDecimal bg = new BigDecimal(ratio * customerLoan.getCreditAmount());
                double creditAmount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                sharingDetails.setSharingAmount(creditAmount);
            }
            if (customerLoan.getCompensatoryAmount() > 0) {
                sharingDetails.setCompensatoryFlag(true);
            } else {
                sharingDetails.setCompensatoryFlag(false);
            }
            sharingDetails.setEmployeeId(customerTransactionBean.getEmployeeId());
            sharingDetails.setBankFeeAmount(customerLoan.getBankFeeAmount());
            sharingDetails.setLoanServiceFee(customerLoan.getLoanServiceFee());
            iDealerSharingDetailsService.save(sharingDetails);
        }
        if ((scope == 0 || scope == 2) && carDealerBean != null) {
            if (carDealerBean.getDealerGroupId() != null) {
                DealerGroupBean dealerGroupBean = iCarDealerBizService.actGetDealerGroup(carDealerBean.getDealerGroupId()).getD();
                if (dealerGroupBean != null && dealerGroupBean.getSharingRatio() != null) {
                    SharingDetails sharingDetails = iDealerSharingDetailsService.getByTransactionIdAndType(customerTransactionId, SharingDetailsBean.GROUPDETAIL);
                    sharingDetails = newSharingDetails(sharingDetails, customerTransactionBean, customerLoan);
                    sharingDetails.setSharingType(SharingDetailsBean.GROUPDETAIL);
                    sharingDetails.setMainPartType(1);
                    sharingDetails.setPledgeDateReceiveTime(date);
                    Map<Integer, Double> ratioMap = dealerGroupBean.getSharingRatio();
                    Double ratio = ratioMap.get(sharingDetails.getMonths());
                    if (ratio != null && ratio > 0) {
                        sharingDetails.setSharingRatio(ratio);
                        BigDecimal bg = new BigDecimal(ratio * customerLoan.getCreditAmount());
                        double creditAmount1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        sharingDetails.setSharingAmount(creditAmount1);
                    }
                    if (customerLoan.getCompensatoryAmount() > 0) {
                        sharingDetails.setCompensatoryFlag(true);
                    } else {
                        sharingDetails.setCompensatoryFlag(false);
                    }
                    sharingDetails.setEmployeeId(customerTransactionBean.getEmployeeId());
                    sharingDetails.setBankFeeAmount(customerLoan.getBankFeeAmount());
                    sharingDetails.setLoanServiceFee(customerLoan.getLoanServiceFee());
                    iDealerSharingDetailsService.save(sharingDetails);
                }
            }
        }

        return ResultBean.getSucceed();
    }

    public ResultBean actResetDealerSharingDetails(String saleMonth, String carDealerId) {
        List<DMVPledge> dmvPledges = iDmvpledgeService.findByPledgeDateReceiveTimeLike(saleMonth);
        for (DMVPledge dmvPledge : dmvPledges) {
            CustomerTransactionBean transactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(dmvPledge.getCustomerTransactionId()).getD();
            if (carDealerId.equals(transactionBean.getCarDealerId())) {
                this.actSaveSharingDetails(dmvPledge.getCustomerTransactionId(), dmvPledge.getPledgeDateReceiveTime(), 1);
            }
        }
        return ResultBean.getSucceed();
    }

    /**
     * 重新生成分成数据
     *
     * @param saleMonth
     * @return
     */
    public ResultBean actResetSharingDetails(String saleMonth, Integer scope, String groupId, String orgId) {
        List<String> employeeIds = new ArrayList<>();
        List<String> carDealerIds = new ArrayList<>();
        if (!"-1".equals(orgId)) {
            List<EmployeeBean> employees = iOrgBizService.actGetOrgEmployees(orgId).getD();
            for (EmployeeBean employee : employees) {
                employeeIds.add(employee.getId());
            }
        }
        List<CarDealerBean> carDealerBeen = iCarDealerBizService.actGetCarDealersByGroup(groupId).getD();
        for (CarDealerBean carDealerBean : carDealerBeen) {
            carDealerIds.add(carDealerBean.getId());
        }
        // 客户抵押资料签收时间存在的单据
        List<DMVPledge> dmvPledges = iDmvpledgeService.findByPledgeDateReceiveTimeLike(saleMonth);
        for (DMVPledge dmvPledge : dmvPledges) {
            if (scope == 2) { // 计算集团分成
                CustomerTransactionBean transactionBean = iCustomerTransactionBizService.actFindCustomerTransactionById(dmvPledge.getCustomerTransactionId()).getD();
                if (carDealerIds.contains(transactionBean.getCarDealerId())) {
                    this.actSaveSharingDetails(dmvPledge.getCustomerTransactionId(), dmvPledge.getPledgeDateReceiveTime(), scope);
                }
            } else if (scope == 1) { // 计算渠道分成
                if (("-1".equals(orgId) || employeeIds.contains(dmvPledge.getEmployeeId()))) {
                    this.actSaveSharingDetails(dmvPledge.getCustomerTransactionId(), dmvPledge.getPledgeDateReceiveTime(), scope);
                }
            }
        }
        return ResultBean.getSucceed();
    }

    public ResultBean<GroupSharingBean> actCreateGroupSharing(String groupId, String saleMonth) {
        GroupSharing groupSharing = iGroupSharingService.getByMonthAndGroup(saleMonth, groupId);
        DealerGroupBean dealerGroupBean = iCarDealerBizService.actGetDealerGroup(groupId).getD();
        if (groupSharing == null) {
            groupSharing = new GroupSharing();
        }
        groupSharing.setStatuses(new HashMap<>());
        groupSharing.setTotalCount(0);
        groupSharing.setTotalCredit(0.0);
        groupSharing.setTotalSharing(0.0);
        groupSharing.setStatus(0);
        groupSharing.setMonth(saleMonth);
        groupSharing.setDealerGroupId(groupId);
        if (dealerGroupBean != null) {
            groupSharing.setGroupName(dealerGroupBean.getName());
        }
        List<CarDealerBean> carDealers = iCarDealerBizService.actGetCarDealersByGroup(groupId).getD();
        Map<String, List<String>> groupSharingDetails = new HashMap<>();
        for (CarDealerBean carDealer : carDealers) {
            Map<String, Integer> map = groupSharing.getStatuses();
            List<String> carDealerIds = new ArrayList<>();
            carDealerIds.add(carDealer.getId());
            List<CustomerTransactionBean> transactions = iCustomerTransactionBizService.actGetListsBySomeConditions(null,null,new ArrayList<String>(),new ArrayList<String>(),carDealerIds,new ArrayList<Integer>(),"ts",true).getD();
            List<String> transactionIds = new ArrayList<>();
            for (CustomerTransactionBean transactionBean : transactions) {
                transactionIds.add(transactionBean.getId());
            }
            List<DMVPledge> dmvPledges = iDmvpledgeService.findByCustomerTransactionIdInAndPledgeDateReceiveTimeLike(transactionIds, saleMonth);
            transactionIds.clear();
            for (DMVPledge dmvPledge : dmvPledges) {
                transactionIds.add(dmvPledge.getCustomerTransactionId());
            }
            List<String> detailsIds = new ArrayList<>();
            List<SharingDetails> sharingDetailses = iDealerSharingDetailsService.getByTransactionIdInAndType(transactionIds, SharingDetailsBean.GROUPDETAIL);
            for (SharingDetails sharingDetails : sharingDetailses) {
                groupSharing.setTotalCount(groupSharing.getTotalCount() + 1);
                groupSharing.setTotalSharing(groupSharing.getTotalSharing() + sharingDetails.getSharingAmount());
                groupSharing.setTotalCredit(groupSharing.getTotalCredit() + sharingDetails.getCreditAmount());
                detailsIds.add(sharingDetails.getId());
            }
            if (detailsIds.size() > 0) {
                map.put(carDealer.getId(), 0);
                groupSharingDetails.put(carDealer.getId(), detailsIds);
            }
            groupSharing.setStatuses(map);
        }
        groupSharing.setGroupSharingDetails(groupSharingDetails);
        iGroupSharingService.save(groupSharing);
        GroupSharingBean g = new GroupSharingBean(groupSharing.getId(), groupSharing.getTs(), 0, groupSharing.getDataStatus(), groupSharing.getGroupSharingDetails(), groupSharing.getStatuses(), groupSharing.getTotalSharing(), groupSharing.getTotalCount(), groupSharing.getTotalCredit(), groupSharing.getMonth(), groupSharing.getDealerGroupId(), groupSharing.getGroupName());
        return ResultBean.getSucceed().setD(g);
    }

    public ResultBean actCreateDealerSharings(String saleMonth, String orgId) {
        if ("-1".equals(orgId)) {
            List<CarDealerBean> carDealerBeen = iCarDealerBizService.actGetCarDealers().getD();
            if (carDealerBeen != null) {
                for (CarDealerBean carDealerBean : carDealerBeen) {
                    DealerSharingRatioBean ratio = iCarDealerBizService.actGetDealerSharingRatio(carDealerBean.getId()).getD();
                    if (carDealerBean.getDataStatus() == DataStatus.SAVE && ratio != null) {
                        this.actCreateDealerSharing(carDealerBean.getId(), saleMonth);
                    }
                }
                return ResultBean.getSucceed();
            }
            return ResultBean.getFailed();
        } else {
            List<EmployeeBean> employeeBeanList = iOrgBizService.actGetOrgEmployees(orgId).getD();
            if (employeeBeanList != null) {
                List<String> eIds = new ArrayList<String>();
                for (EmployeeBean em : employeeBeanList) {
                    eIds.add(em.getId());
                }
                List<CarDealerBean> carDealers = iCarDealerBizService.actGetCarDealersByEmployeeIds(eIds).getD();
                Set<String> carDealerIds = new HashSet<>();
                if (carDealers != null) {
                    for (CarDealerBean carDealerBean : carDealers) {
                        if (carDealerBean.getDataStatus() == DataStatus.SAVE) {
                            carDealerIds.add(carDealerBean.getId());
                        }
                    }
                    for (String carDealerId : carDealerIds) {
                        DealerSharingRatioBean ratio = iCarDealerBizService.actGetDealerSharingRatio(carDealerId).getD();
                        if (ratio != null) {
                            this.actCreateDealerSharing(carDealerId, saleMonth);
                        }
                    }
                    return ResultBean.getSucceed();
                }
            }
            return ResultBean.getFailed();
        }
    }

    @Override
    public ResultBean<DealerSharingBean> actCreateDealerSharing(String dealerId, String saleMonth) {
        CarDealerBean carDealerBean = iCarDealerBizService.actGetCarDealer(dealerId).getD();
        DealerSharing dealerSharing = iDealerSharingService.getByMonthAndCarDealer(saleMonth, dealerId);
        if (dealerSharing == null) {
            dealerSharing = new DealerSharing();
        }
        dealerSharing.setMonth(saleMonth);
        dealerSharing.setCarDealerId(dealerId);
        dealerSharing.setDealerName(carDealerBean.getName());
        dealerSharing.setChannelId(carDealerBean.getEmployeeId());
        dealerSharing.setStatus(0);
        dealerSharing.setTotalSharing(0.0);
        dealerSharing.setTotalCredit(0.0);
        dealerSharing.setTotalCount(0);
        List<String> carDealerIds = new ArrayList<>();
        carDealerIds.add(dealerId);
        List<String> transactionIds = new ArrayList<>();
        List<CustomerTransactionBean> transactions = iCustomerTransactionBizService.actGetListsBySomeConditions(null,null,new ArrayList<String>(),new ArrayList<String>(),carDealerIds,new ArrayList<Integer>(),"ts",true).getD();
        for (CustomerTransactionBean transactionBean : transactions) {
            transactionIds.add(transactionBean.getId());
        }
        List<DMVPledge> dmvPledges = iDmvpledgeService.findByCustomerTransactionIdInAndPledgeDateReceiveTimeLike(transactionIds, saleMonth);
        transactionIds.clear();
        for (DMVPledge dmvPledge : dmvPledges) {
            transactionIds.add(dmvPledge.getCustomerTransactionId());
        }
        List<String> detailsIds = new ArrayList<>();
        List<SharingDetails> sharingDetailses = iDealerSharingDetailsService.getByTransactionIdInAndType(transactionIds, SharingDetailsBean.DEALERDETAIL);
        for (SharingDetails sharingDetails : sharingDetailses) {
            dealerSharing.setTotalSharing(dealerSharing.getTotalSharing() + sharingDetails.getSharingAmount());
            dealerSharing.setTotalCredit(dealerSharing.getTotalCredit() + sharingDetails.getCreditAmount());
            dealerSharing.setTotalCount(dealerSharing.getTotalCount() + 1);
            detailsIds.add(sharingDetails.getId());
        }
        dealerSharing.setSharingDetailIds(detailsIds);
        iDealerSharingService.save(dealerSharing);
        return ResultBean.getSucceed().setD(mappingService.map(dealerSharing, DealerSharingBean.class));
    }

    private boolean checkStatus(Integer oldStatus, Integer newStatus) {
        boolean flag = false;
        if (newStatus == 1 && oldStatus != 4) { // 不等于结算状态的状态都可以重置到修正
            return true;
        }
        if (newStatus - oldStatus == 1) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    private void saveDatas(List<SharingDetails> list) throws Exception {
        for (SharingDetails sharingDetails : list) {
            if (sharingDetails.getStatus() == 3) {
                sharingDetails.setStatus(4);
                iDealerSharingDetailsService.save(sharingDetails);
            } else {
                throw new Exception("计算失败，存在状态无效的分成数据");
            }
        }
    }


    /**
     * 结算分成
     *
     * @return
     */
    public ResultBean actDoBalanceSharing(Integer type, String id) throws Exception {
        if (!mutexService.lockSaveObject("groupSharing&dealerSharing", "admin", "sharingConfirmStatus")) {
            return ResultBean.getFailed().setM("已有数据在确认，请勿频繁操作");
        }
        try {
            if (SharingDetailsBean.DEALERDETAIL.equals(type)) {
                DealerSharing dealerSharing = iDealerSharingService.getAvailableOne(id);
                Integer status = dealerSharing.getStatus();
                if (status == 3) { // 复核状态
                    dealerSharing.setStatus(4);
                    List<String> sharingDetailIds = dealerSharing.getSharingDetailIds();
                    List<SharingDetails> list = iDealerSharingDetailsService.getAllByIdInAndType(sharingDetailIds, SharingDetailsBean.DEALERDETAIL);
                    saveDatas(list);
                    iDealerSharingService.save(dealerSharing);
                }
                return ResultBean.getSucceed();
            } else if (SharingDetailsBean.GROUPDETAIL.equals(type)) {
                GroupSharing groupSharing = iGroupSharingService.getAvailableOne(id);
                Integer status = groupSharing.getStatus();
                if (status == 3) { // 复核状态
                    groupSharing.setStatus(4);
                    Map<String, List<String>> groupSharingDetails = groupSharing.getGroupSharingDetails();
                    Map<String, Integer> statuses = groupSharing.getStatuses();
                    for (String carDealerId : statuses.keySet()) {
                        if (statuses.get(carDealerId) == 3) { // 复核状态
                            statuses.put(carDealerId, 4);
                        } else {
                            throw new Exception("计算失败，存在状态无效的分成数据");
                        }
                    }
                    groupSharing.setStatuses(statuses);
                    for (String carDealerId : groupSharingDetails.keySet()) {
                        List<String> sharingDetailIds = groupSharingDetails.get(carDealerId);
                        List<SharingDetails> list = iDealerSharingDetailsService.getAllByIdInAndType(sharingDetailIds, SharingDetailsBean.GROUPDETAIL);
                        saveDatas(list);
                    }
                    iGroupSharingService.save(groupSharing);
                }
                return ResultBean.getSucceed();
            }
            throw new Exception("计算失败，参数错误");
        } finally {
            mutexService.unLockSaveObject("groupSharing&dealerSharing", "admin");
        }

    }

    @Autowired
    MutexService mutexService;

    /**
     * 更改状态
     *
     * @param sharingDetailId
     * @param status
     * @return
     */
    @Override
    public ResultBean<SharingDetailsBean> actConfirmStatus(String id, String carDealerId, String sharingDetailId, Integer status, Double sharingRatio, Double sharingAmount, Integer mainPartType) throws Exception {
        if (!mutexService.lockSaveObject("groupSharing&dealerSharing", "admin", "sharingConfirmStatus")) {
            return ResultBean.getFailed().setM("已有数据在确认，请勿频繁操作");
        }
        try {
            List<String> carDealerIds = new ArrayList<>();
            carDealerIds.add(carDealerId);
            List<CustomerTransactionBean> transactions = iCustomerTransactionBizService.actGetListsBySomeConditions(null,null,new ArrayList<String>(),new ArrayList<String>(),carDealerIds,new ArrayList<Integer>(),"ts",true).getD();
            List<String> transactionIds = new ArrayList<>();
            for (CustomerTransactionBean customerTransactionBean : transactions) {
                transactionIds.add(customerTransactionBean.getId());
            }
            SharingDetails sharingDetails = iDealerSharingDetailsService.getAvailableOne(sharingDetailId);
            Double _SharingRatio = sharingRatio - sharingDetails.getSharingRatio(); // 算差值
            Double _SharingAmount = sharingAmount - sharingDetails.getSharingAmount(); // 算差值
            if (sharingDetails.getStatus() != 0 && _SharingRatio != 0 && _SharingAmount != 0) {
                throw new Exception("非法参数");
            }
            if (sharingDetails != null && checkStatus(sharingDetails.getStatus(), status)) {
                if (sharingDetails.getStatus() == 0) {
                    sharingDetails.setSharingRatio(sharingRatio);
                    sharingDetails.setSharingAmount(sharingAmount);
                    sharingDetails.setMainPartType(mainPartType);
                }
                sharingDetails.setStatus(status);
                sharingDetails = iDealerSharingDetailsService.save(sharingDetails);

                if (SharingDetailsBean.GROUPDETAIL.equals(sharingDetails.getSharingType())) { // 集团分成
                    GroupSharing groupSharing = iGroupSharingService.getAvailableOne(id);
                    List<Integer> list = iDealerSharingDetailsService.findDistinctStatusByTransactionIdInAndSharingTypeAndPledgeDateReceiveTimeLike(transactionIds, SharingDetailsBean.GROUPDETAIL, groupSharing.getMonth());
                    list.sort((Integer o1, Integer o2) -> {
                        return o1 - o2;
                    });
                    groupSharing.setTotalSharing(groupSharing.getTotalSharing() + _SharingAmount);
                    Map<String, Integer> map = groupSharing.getStatuses();
                    map.put(carDealerId, list.get(0));
                    Integer groupStatus = 5;
                    for (String key : map.keySet()) {
                        if (groupStatus > map.get(key)) {
                            groupStatus = map.get(key);
                        }
                    }
                    groupSharing.setStatus(groupStatus);
                    groupSharing.setStatuses(map);
                    iGroupSharingService.save(groupSharing);
                } else { // 渠道分成
                    DealerSharing dealerSharing = iDealerSharingService.getAvailableOne(id);
                    List<Integer> list = iDealerSharingDetailsService.findDistinctStatusByTransactionIdInAndSharingTypeAndPledgeDateReceiveTimeLike(transactionIds, SharingDetailsBean.DEALERDETAIL, dealerSharing.getMonth());
                    list.sort((Integer o1, Integer o2) -> {
                        return o1 - o2;
                    });
                    dealerSharing.setTotalSharing(dealerSharing.getTotalSharing());
                    dealerSharing.setStatus(list.get(0));
                    iDealerSharingService.save(dealerSharing);
                }
                return ResultBean.getSucceed().setD(mappingService.map(sharingDetails, SharingDetailsBean.class));
            }
            return ResultBean.getFailed().setM("未找到相关详情信息");
        } catch (Exception e) {
            logger.error("确认分成状态操作异常", e);
            return ResultBean.getFailed().setM("确认分成状态操作异常");
        } finally {
            mutexService.unLockSaveObject("groupSharing&dealerSharing", "admin");
        }

    }

    @Override
    public ResultBean<DataPageBean<SharingDetailsBean>> actGetSharingDetails(Integer currentPage) {
        Page<SharingDetails> page = iDealerSharingDetailsService.getAvaliableAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(page, SharingDetailsBean.class));
    }

    @Override
    public ResultBean<DataPageBean<DealerSharingBean>> actGetDealerSharings(Integer currentPage, String month, String orgId) {
        List carDealerIds = new ArrayList<>();
        if (!"-1".equals(orgId)) {
            List<OrgBean> orgs = iOrgBizService.actGetOrgs(orgId).getD();
            List<String> orgIds = new ArrayList<>();
            for (OrgBean org : orgs) {
                orgIds.add(org.getId());
            }
            orgIds.add(orgId);
            List<CarDealerBean> carDealerBeen = iCarDealerBizService.actGetCarDealerByOrgIds(orgIds).getD();
            for (CarDealerBean carDealerBean : carDealerBeen) {
                carDealerIds.add(carDealerBean.getId());
            }
        }
        Page<DealerSharing> page = iDealerSharingService.getAvaliablePageData(currentPage, month, carDealerIds);
        return ResultBean.getSucceed().setD(mappingService.map(page, DealerSharingBean.class));
    }

    @Override
    public ResultBean<DataPageBean<GroupSharingBean>> actGetGroupSharings(Integer currentPage, String month, String groupId) {
        Page<GroupSharing> page = iGroupSharingService.getAvaliablePageData(currentPage, month, groupId);
        DataPageBean<GroupSharingBean> destination = new DataPageBean<>();
        destination.setPageSize(page.getSize());
        destination.setTotalCount(page.getTotalElements());
        destination.setTotalPages(page.getTotalPages());
        destination.setCurrentPage(page.getNumber());
        for (GroupSharing groupSharing : page.getContent()) {
            GroupSharingBean g = new GroupSharingBean(groupSharing.getId(), groupSharing.getTs(), groupSharing.getStatus(), groupSharing.getDataStatus(), groupSharing.getGroupSharingDetails(), groupSharing.getStatuses(), groupSharing.getTotalSharing(), groupSharing.getTotalCount(), groupSharing.getTotalCredit(), groupSharing.getMonth(), groupSharing.getDealerGroupId(), groupSharing.getGroupName());
            destination.getResult().add(g);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<List<SharingDetailsBean>> actLookupDealerDetail(String carDealerSharingId) {
        List<DealerSharing> dealerSharings = new ArrayList<>();
        DealerSharing dealerSharing = iDealerSharingService.getAvailableOne(carDealerSharingId);
        if (dealerSharing != null) {
            List sharingDetailIds = dealerSharing.getSharingDetailIds();
            List<SharingDetails> detailses = iDealerSharingDetailsService.getAllByIdInAndType(sharingDetailIds, SharingDetailsBean.DEALERDETAIL);
            if (detailses != null) {
                List<SharingDetailsBean> sharingDetailsBeans = mappingService.map(detailses, SharingDetailsBean.class);
                return ResultBean.getSucceed().setD(sharingDetailsBeans);
            }
            return ResultBean.getFailed();
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Map<String, SharingDetailsBean>> actLookupGroupDetail(String groupSharingId) {
        GroupSharing groupSharing = iGroupSharingService.getAvailableOne(groupSharingId);
        if (groupSharing != null && groupSharing.getGroupSharingDetails() != null && groupSharing.getGroupSharingDetails().size() > 0) {
            Map<String, List<String>> groupSharingDetails = groupSharing.getGroupSharingDetails();
            Map<String, List<SharingDetailsBean>> showMap = new HashMap<>();
            for (String carDealerId : groupSharingDetails.keySet()) {
                List<SharingDetails> detailses = iDealerSharingDetailsService.getAllByIdInAndType(groupSharingDetails.get(carDealerId), SharingDetailsBean.GROUPDETAIL);
                if (detailses != null) {
                    List<SharingDetailsBean> sharingDetailsBeans = mappingService.map(detailses, SharingDetailsBean.class);
                    showMap.put(carDealerId, sharingDetailsBeans);
                }
            }
            return ResultBean.getSucceed().setD(showMap);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<GroupSharingBean> actGetGroupDetail(String groupId, String saleMonth) {
        GroupSharing groupSharing = iGroupSharingService.getGroupSharingByGroupId(groupId, saleMonth);
        if (groupSharing != null) {
            return ResultBean.getSucceed().setD(mappingService.map(groupSharing, GroupSharingBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DealerSharingBean> actGetDealerSharing(String saleMonth, String carDealerId) {
        DealerSharing dealerSharing = iDealerSharingService.getByMonthAndCarDealer(saleMonth, carDealerId);
        if (dealerSharing != null) {
            return ResultBean.getSucceed().setD(mappingService.map(dealerSharing, DealerSharingBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DealerSharingBean> actGetDealerSharing(String dealerSharingId) {
        DealerSharing dealerSharing = iDealerSharingService.getAvailableOne(dealerSharingId);
        if (dealerSharing != null) {
            return ResultBean.getSucceed().setD(mappingService.map(dealerSharing, DealerSharingBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<GroupSharingBean> actGetGroupSharing(String groupSharingId) {
        GroupSharing groupSharing = iGroupSharingService.getAvailableOne(groupSharingId);
        GroupSharingBean g = new GroupSharingBean(groupSharing.getId(), groupSharing.getTs(), groupSharing.getStatus(), groupSharing.getDataStatus(), groupSharing.getGroupSharingDetails(), groupSharing.getStatuses(), groupSharing.getTotalSharing(), groupSharing.getTotalCount(), groupSharing.getTotalCredit(), groupSharing.getMonth(), groupSharing.getDealerGroupId(), groupSharing.getGroupName());
        return ResultBean.getSucceed().setD(g);
    }
}
