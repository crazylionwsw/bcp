package com.fuze.bcp.transaction.service.impl;

import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.transaction.repository.CustomerTransactionRepository;
import com.fuze.bcp.transaction.service.ICustomerTransactionService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/7/31.
 */
@Service
public class CustomerTransactionServiceImpl extends BaseServiceImpl<CustomerTransaction, CustomerTransactionRepository> implements ICustomerTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerTransactionServiceImpl.class);

    @Autowired
    CustomerTransactionRepository customerTransactionRepository;

    @Autowired
    MessageService messageService;

    public List<CustomerTransaction> getListsBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, String sortName, Boolean sortDesc) {

        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (customerIds.size()>0){
            query.addCriteria(Criteria.where("customerId").in(customerIds));
        }
        if (employeeIds.size()>0){
            query.addCriteria(Criteria.where("employeeId").in(employeeIds));
        }
        if (!StringUtils.isEmpty(loginUserId)){
            query.addCriteria(Criteria.where("loginUserId").is(loginUserId));
        }
        if(!StringUtils.isEmpty(selectTime)){
            query.addCriteria(Criteria.where("ts").regex(String.format(selectTime,"m")));
        }
        if (carDealerIds.size() > 0){
            query.addCriteria(Criteria.where("carDealerId").in(carDealerIds));
        }
        if (statusList.size() > 0){
            query.addCriteria(Criteria.where("status").in(statusList));
        }
        if (sortDesc){
            query.with(CustomerTransaction.getSortDESC(sortName));
        } else {
            query.with(CustomerTransaction.getSortASC(sortName));
        }
        List list = mongoTemplate.find(query,CustomerTransaction.class);
        return list;
    }

    public Page<CustomerTransaction> getPagesBySomeConditions(String loginUserId, String selectTime, List<String> customerIds, List<String> employeeIds, List<String> carDealerIds, List<Integer> statusList, Integer pageIndex, Integer pageSize, String sortName, Boolean sortDesc) {

        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (customerIds.size()>0){
            query.addCriteria(Criteria.where("customerId").in(customerIds));
        }
        if (employeeIds.size()>0){
            query.addCriteria(Criteria.where("employeeId").in(employeeIds));
        }
        if (!StringUtils.isEmpty(loginUserId)){
            query.addCriteria(Criteria.where("loginUserId").is(loginUserId));
        }
        if(!StringUtils.isEmpty(selectTime)){
            query.addCriteria(Criteria.where("ts").regex(String.format(selectTime,"m")));
        }
        if (carDealerIds.size() > 0){
            query.addCriteria(Criteria.where("carDealerId").in(carDealerIds));
        }
        if (statusList.size() > 0){
            query.addCriteria(Criteria.where("status").in(statusList));
        }
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        if (sortDesc){
            query.with(CustomerTransaction.getSortDESC(sortName)).with(pageable);
        } else {
            query.with(CustomerTransaction.getSortASC(sortName)).with(pageable);
        }
        List list = mongoTemplate.find(query,CustomerTransaction.class);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,CustomerTransaction.class));
        return page;
    }

    public Page<CustomerTransaction> findAllPagesBySearchBean(SearchBean searchBean){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if ("ts".equals(searchBean.getTimeName()) && !StringUtils.isEmpty(searchBean.getStartTime()) && !StringUtils.isEmpty(searchBean.getEndTime())){
            query.addCriteria(Criteria.where(searchBean.getTimeName()).gt(searchBean.getStartTime()).lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndTime(), 1)));
        } else if ("ts".equals(searchBean.getTimeName()) && !StringUtils.isEmpty(searchBean.getStartTime()) && StringUtils.isEmpty(searchBean.getEndTime())) {
            query.addCriteria(Criteria.where(searchBean.getTimeName()).gt(searchBean.getStartTime()));
        } else if ("ts".equals(searchBean.getTimeName()) && StringUtils.isEmpty(searchBean.getStartTime()) && !StringUtils.isEmpty(searchBean.getEndTime())) {
            query.addCriteria(Criteria.where(searchBean.getTimeName()).lt(searchBean.getEndTime()));
        }
        if ("status".equals(searchBean.getStatusName()) && searchBean.getStatusValue() != null && searchBean.getStatusValue() != -1 ){
            query.addCriteria(Criteria.where("status").is(searchBean.getStatusValue()));
        }
        query.addCriteria(Criteria.where("_id").in(getSearchTransactionIds(searchBean,SearchBean.STAGE_TRANSACTION)));

        Pageable pageable = new PageRequest(searchBean.getCurrentPage(),searchBean.getPageSize(), CustomerTransaction.getTsSort());
        query.with(CustomerTransaction.getSortDESC("ts")).with(pageable);
        List list = mongoTemplate.find(query,CustomerTransaction.class);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,CustomerTransaction.class));
        return page;
    }

    @Override
    public List<CustomerTransaction> getAllBySearchBean(SearchBean searchBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if ("ts".equals(searchBean.getTimeName()) && !StringUtils.isEmpty(searchBean.getStartTime()) && !StringUtils.isEmpty(searchBean.getEndTime())){
            query.addCriteria(Criteria.where(searchBean.getTimeName()).gt(searchBean.getStartTime()).lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndTime(), 1)));
        } else if ("ts".equals(searchBean.getTimeName()) && !StringUtils.isEmpty(searchBean.getStartTime()) && StringUtils.isEmpty(searchBean.getEndTime())) {
            query.addCriteria(Criteria.where(searchBean.getTimeName()).gt(searchBean.getStartTime()));
        } else if ("ts".equals(searchBean.getTimeName()) && StringUtils.isEmpty(searchBean.getStartTime()) && !StringUtils.isEmpty(searchBean.getEndTime())) {
            query.addCriteria(Criteria.where(searchBean.getTimeName()).lt(searchBean.getEndTime()));
        }
        if ("status".equals(searchBean.getStatusName()) && searchBean.getStatusValue() != null && searchBean.getStatusValue() != -1 ){
            query.addCriteria(Criteria.where("status").is(searchBean.getStatusValue()));
        }
        query.addCriteria(Criteria.where("_id").in(getSearchTransactionIds(searchBean,SearchBean.STAGE_TRANSACTION)));
        query.with(CustomerTransaction.getSortDESC(searchBean.getSortName()));
        List list = mongoTemplate.find(query,CustomerTransaction.class);
        return list;
    }

    @Override
    public Integer countTransactionsByCustomerId(String customerId) {
        List<Integer> status = new ArrayList<Integer>();
        status.add(CustomerTransactionBean.TRANSACTION_INIT); //业务开始
        status.add(CustomerTransactionBean.TRANSACTION_PROCESSING); //业务正在处理

        return this.customerTransactionRepository.countByCustomerIdAndStatusIn(customerId, status);
    }

    @Override
    public List<CustomerTransaction> getPendingTransactionsByLoginUserId(String loginUserId) {
        List<Integer> ss = new ArrayList<>();
        ss.add(CustomerTransactionBean.TRANSACTION_INIT);
        ss.add(CustomerTransactionBean.TRANSACTION_PROCESSING);
        ss.add(CustomerTransactionBean.TRANSACTION_CANCELLING);

        return customerTransactionRepository.findByDataStatusAndStatusInAndLoginUserId(DataStatus.SAVE, ss, loginUserId);
    }


    public List<CustomerTransaction> getPendingTransactionsByLoginUserIdOnDecompress(String loginUserId) {
        List<Integer> ss = new ArrayList<>();
        ss.add(CustomerTransactionBean.TRANSACTION_INIT);
        ss.add(CustomerTransactionBean.TRANSACTION_PROCESSING);
        ss.add(CustomerTransactionBean.TRANSACTION_CANCELLING);
        ss.add(CustomerTransactionBean.TRANSACTION_FINISH);

        return customerTransactionRepository.findByDataStatusAndStatusInAndLoginUserId(DataStatus.SAVE, ss, loginUserId);
    }



    @Override
    public List<CustomerTransaction> getAllTransactionsByLoginUserId(String loginUserId) {
        List<Integer> ss = new ArrayList<>();
        ss.add(CustomerTransactionBean.TRANSACTION_INIT);
        ss.add(CustomerTransactionBean.TRANSACTION_PROCESSING);
        ss.add(CustomerTransactionBean.TRANSACTION_CANCELLING);
        ss.add(CustomerTransactionBean.TRANSACTION_CANCELLED);
        ss.add(CustomerTransactionBean.TRANSACTION_FINISH);
        ss.add(CustomerTransactionBean.TRANSACTION_STOP);

        return customerTransactionRepository.findByDataStatusAndStatusInAndLoginUserId(DataStatus.SAVE, ss, loginUserId);
    }

    public List<CustomerTransaction> getCancelledTransactionsByLoginUserId(String loginUserId) {
        List<Integer> ss = new ArrayList<>();
        ss.add(CustomerTransactionBean.TRANSACTION_CANCELLED);

        return customerTransactionRepository.findByDataStatusAndStatusInAndLoginUserId(DataStatus.SAVE, ss, loginUserId);
    }

    @Override
    public ResultBean<CustomerTransaction> getEditableTransaction(String tid) {

        CustomerTransaction transaction = this.getOne(tid);
        if (transaction == null)
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_NONULL"));

        //需要判断单据状态是否取消或正在取消
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONCANCELLING"));
        }
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONCANCELLED"));
        }
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_STOP) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONSTOP"));
        }
        if (transaction.getStatus() == CustomerTransactionBean.TRANSACTION_FINISH) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMERTRANSCTION_TRANSACTIONSTOP"));
        }

        return ResultBean.getSucceed().setD(transaction);
    }

    /**
     * 创建客户的档案编号
     *
     * @return
     */
    public String createCustomerNumber(String ts, String businessTypeCode) {
        //档案编号
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String time = ts;
        try {
            time = f.format(DateTimeUtils.getSimpleDateFormat().parse(ts));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Integer codeNum = this.getTodayCountByContract(ts);
        codeNum += 1;
        String s = codeNum.toString();
        Integer codeLen = s.length();
        for (int j = codeLen; j < 4; j++) {
            s = "0" + s;
        }
        String fileNumber = String.format("%s-%s-%s", businessTypeCode, time, s);
        return fileNumber;
    }

    private Integer getTodayCountByContract(String ts) {
        String today = ts.substring(0, 10);
        return customerTransactionRepository.countByTsStartingWithAndFileNumberIsNotNull(today);
    }

    @Override
    public List<String> getTransactionIds(String userId, Boolean status) {
        List<CustomerTransaction> ts = new ArrayList<CustomerTransaction>();
        if (status){
            ts = this.getCancelledTransactionsByLoginUserId(userId);
        } else {
            ts = this.getPendingTransactionsByLoginUserId(userId);
        }

        List<String> ids = new ArrayList<>();
        if (ts != null) {
            for(CustomerTransaction t : ts) {
                ids.add(t.getId());
            }
        }

        return ids;
    }

    @Override
    public List<String> getTransactionIdsOnDecompress(String userId, Boolean status) {
        List<CustomerTransaction> ts = new ArrayList<CustomerTransaction>();
        if (status){
            ts = this.getCancelledTransactionsByLoginUserId(userId);
        } else {
            ts = this.getPendingTransactionsByLoginUserIdOnDecompress(userId);
        }

        List<String> ids = new ArrayList<>();
        if (ts != null) {
            for(CustomerTransaction t : ts) {
                ids.add(t.getId());
            }
        }

        return ids;
    }

    @Override
    public List<String> getAllUnFinishTransactionIds() {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        criteria.and("status").in(getOngoingStatus());
        return (List<String>) getAllFieldList(Query.query(criteria),"cus_transaction","_id");
    }

    private List<Integer>   getOngoingStatus(){
        //获取正在进行的业务
        List<Integer> status = new ArrayList<Integer>();
        status.add(CustomerTransactionBean.TRANSACTION_INIT);
        status.add(CustomerTransactionBean.TRANSACTION_PROCESSING);
        status.add(CustomerTransactionBean.TRANSACTION_WARNING);
        return status;
    }

}
