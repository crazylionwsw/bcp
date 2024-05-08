package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.BankCardApply;
import com.fuze.bcp.creditcar.repository.BankCardApplyRepository;
import com.fuze.bcp.creditcar.service.IBankCardApplyService;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/8/21.
 */
@Service
public class BankCardApplyServiceImpl extends BaseBillServiceImpl<BankCardApply,BankCardApplyRepository> implements IBankCardApplyService {

    @Autowired
    BankCardApplyRepository bankCardApplyRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<BankCardApply> findByDataStatusAndStatusOrderByTsDesc(Integer currentPage, Integer save, Integer status) {
        PageRequest page = new PageRequest(currentPage, 20);
        return bankCardApplyRepository.findByDataStatusAndStatusOrderByTsDesc(page,save,status);
    }

    @Override
    public Page<BankCardApply> findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(List<String> customerIds, Integer save, int status, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return bankCardApplyRepository.findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(page,customerIds,save,status);
    }

    @Override
    public void deleteBankCard(BankCardApplyBean bankCardApply) {
        bankCardApplyRepository.deleteById(bankCardApply.getId());
    }

    @Override
    public Page<BankCardApply> findByCashSourceIdOrderByTsDesc(String cashSourceId, Integer pageindex,Integer pagesize) {
        PageRequest page = new PageRequest(pageindex, pagesize);
        return bankCardApplyRepository.findByCashSourceIdOrderByTsDesc(page,cashSourceId);
    }

    @Override
    public Page<BankCardApply> findByCashSourceIdAndStatusOrderByTsAsc(String cashSourceId, Integer pageindex,Integer pagesize, Integer status) {
        PageRequest page = new PageRequest(pageindex, pagesize);
        return bankCardApplyRepository.findByCashSourceIdAndStatusOrderByTsAsc(page,cashSourceId,status);
    }

    @Override
    public BankCardApply findByIdAndCashSourceIdOrderByTsDesc(String key,String cashSourceId) {
        return bankCardApplyRepository.findByIdAndCashSourceIdOrderByTsDesc(key,cashSourceId);
    }

    @Override
    public Page<BankCardApply> findByLoginUserIdAndApproveStatusIn(String loginUserId, List<Integer> as, Integer pageIndex, Integer pageSize) {
        PageRequest page = new PageRequest(pageIndex, pageSize,BankCardApply.getTsSort());
        return bankCardApplyRepository.findByLoginUserIdAndApproveStatusIn(loginUserId,as,page);
    }

    public Map<Object,Object>  getEmployeeReport(String date, BankCardApply bankCardApply ,String employeeId){
        Map<Object,Object>  resultMap = new HashMap<Object,Object>();
        String regex = String.format("^%s",date);
        Query query =new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if(!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("ts").regex(regex, "m"));
        }else{
            query.addCriteria(Criteria.where("ts").ne(null));
        }
        if(!StringUtils.isEmpty(employeeId)) {
            query.addCriteria(Criteria.where("employeeId").is(employeeId));
        }
        //业务总数
        long count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("total",count);

        //当天待处理的数量
        query.addCriteria(Criteria.where("status").is(BankCardApplyBean.APPROVE_INIT));
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("init",count);


        //已制卡
        Criteria madeCri = Criteria.where("dataStatus").is(DataStatus.SAVE);
        //madeCri = madeCri.and("status").is(BankCardApplyBean.BKSTATUS_APPLY);
        if(!StringUtils.isEmpty(date)) {
            madeCri = madeCri.and("applyTime").regex(regex, "m");
        }else{
            madeCri = madeCri.and("applyTime").ne(null);
        }
        if(!StringUtils.isEmpty(employeeId)) {
            madeCri = madeCri.and("employeeId").is(employeeId);
        }
        count = mongoTemplate.count(new Query(madeCri),bankCardApply.getClass());
        resultMap.put("made",count);


        //已取卡
        query =new Query();
        Criteria takedCri = Criteria.where("dataStatus").is(DataStatus.SAVE);
        //takedCri = takedCri.and("status").is(BankCardApplyBean.BKSTATUS_TAKE);
        if(!StringUtils.isEmpty(date)) {
            takedCri = takedCri.and("takeTime").regex(regex, "m");
        }else{
            takedCri = takedCri.and("takeTime").ne(null);
        }
        if(!StringUtils.isEmpty(employeeId)) {
            takedCri = takedCri.and("employeeId").is(employeeId);
        }
        count = mongoTemplate.count(new Query(takedCri),bankCardApply.getClass());
        resultMap.put("taked",count);

        //启卡或者代启卡
        query =new Query();
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        // query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_ACTIVATE));
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("replaceActivateTime").regex(regex, "m"),(Criteria.where("activateTime").regex(regex, "m")));
        }else{
            criteria.orOperator(Criteria.where("replaceActivateTime").ne(null),(Criteria.where("activateTime").ne(null)));
        }
        if(!StringUtils.isEmpty(employeeId)) {
            criteria.and("employeeId").is(employeeId);
        }
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("activated",count);

        //调额
        query =new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_CHANGEAMOUN));
        if(!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("changeAmountTime").regex(regex, "m"));
        }else{
            query.addCriteria(Criteria.where("changeAmountTime").ne(null));
        }
        if(!StringUtils.isEmpty(employeeId)) {
            query.addCriteria(Criteria.where("employeeId").is(employeeId));
        }
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("asjusted",count);


        //领卡
        query =new Query();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        //query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_GET));
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("receiveTrusteeTime").regex(regex, "m"),Criteria.where("receiveDiscountTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("receiveTrusteeTime").ne(null),Criteria.where("receiveDiscountTime").ne(null));
        }
        if(!StringUtils.isEmpty(employeeId)) {
            criteria.and("employeeId").is(employeeId);
        }
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("received",count);

        //刷卡
        query =new Query();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        //query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_SWIPING));
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("swipingTrusteeTime").regex(regex, "m"),Criteria.where("swipingShopTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("swipingTrusteeTime").ne(null),Criteria.where("swipingShopTime").ne(null));
        }
        if(!StringUtils.isEmpty(employeeId)) {
            criteria.and("employeeId").is(employeeId);
        }
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("swing",count);

        //销卡
        query =new Query();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_CANCEL));
        if(!StringUtils.isEmpty(employeeId)) {
            query.addCriteria(Criteria.where("employeeId").is(employeeId));
        }
        /*if(!StringUtils.isEmpty(date)) {
            criteria.and("cancelCardTime").regex(regex, "m");
        }else{
            criteria.and("cancelCardTime").ne(null);
        }*/
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("cancelCard",count);
        //POS刷卡或者店内刷卡
        List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("swipingTrusteeTime").regex(regex, "m"),Criteria.where("swipingShopTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("swipingTrusteeTime").ne(null),Criteria.where("swipingShopTime").ne(null));
        }
        if(!StringUtils.isEmpty(employeeId)) {
            criteria=criteria.and("employeeId").is(employeeId);
        }
        aos.add(Aggregation.match(criteria));
        aos.add(Aggregation.group("dataStatus").count().as("totalCount").sum("swipingMoney").as("totalAmount"));
        AggregationResults<BasicDBObject> wholeResults = mongoTemplate.aggregate( Aggregation.newAggregation(aos), BankCardApply.class, BasicDBObject.class);
        List<BasicDBObject> result = wholeResults.getMappedResults();
        if(result.size()>0) {
            resultMap.put("totalAmount",result.get(0).get("totalAmount"));
        }else{
            resultMap.put("totalAmount",0.0);
        }
        return resultMap;
    }

    ;

    /**
     * 获取日报列表
     * @param date
     * @param bankCardApply
     * @return
     */
    @Override
    public Map<Object, Object> getDailyReport(String orgid,String date, BankCardApply bankCardApply) {
        //获取符合条件的transactionId
        List<String>    transactionIds = queryTransactionIds(orgid,null,null,null,null,null,null);
        Map<Object,Object>  resultMap = new HashMap<Object,Object>();
        String regex = String.format("^%s",date);
        Query query =new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if(!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("ts").regex(regex, "m"));
        }else{
            query.addCriteria(Criteria.where("ts").ne(null));
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            query.addCriteria(Criteria.where("customerTransactionId").in(transactionIds));
        }
        //业务总数
        long count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("total",count);

        //当天待处理的数量
        query.addCriteria(Criteria.where("status").is(BankCardApplyBean.APPROVE_INIT));
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("init",count);


        //已制卡
        query =new Query();
        Criteria madeCri = Criteria.where("dataStatus").is(DataStatus.SAVE);
        //madeCri = madeCri.and("status").is(BankCardApplyBean.BKSTATUS_APPLY);
        if(!StringUtils.isEmpty(date)) {
            madeCri = madeCri.and("applyTime").regex(regex, "m");
        }else{
            madeCri = madeCri.and("applyTime").ne(null);
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            madeCri = madeCri.and("customerTransactionId").in(transactionIds);
        }
        count = mongoTemplate.count(new Query(madeCri),bankCardApply.getClass());
        resultMap.put("made",count);


        //已取卡
        query =new Query();
       Criteria takedCri = Criteria.where("dataStatus").is(DataStatus.SAVE);
        //takedCri = takedCri.and("status").is(BankCardApplyBean.BKSTATUS_TAKE);
        if(!StringUtils.isEmpty(date)) {
            takedCri = takedCri.and("takeTime").regex(regex, "m");
        }else{
            takedCri = takedCri.and("takeTime").ne(null);
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            takedCri = takedCri.and("customerTransactionId").in(transactionIds);
        }
        count = mongoTemplate.count(new Query(takedCri),bankCardApply.getClass());
        resultMap.put("taked",count);

        //启卡或者代启卡
        query =new Query();
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
       // query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_ACTIVATE));
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("replaceActivateTime").regex(regex, "m"),(Criteria.where("activateTime").regex(regex, "m")));
        }else{
            criteria.orOperator(Criteria.where("replaceActivateTime").ne(null),(Criteria.where("activateTime").ne(null)));
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            criteria = criteria.and("customerTransactionId").in(transactionIds);
        }
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("activated",count);

        //调额
        query =new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_CHANGEAMOUN));
        if(!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("changeAmountTime").regex(regex, "m"));
        }else{
            query.addCriteria(Criteria.where("changeAmountTime").ne(null));
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            query.addCriteria(Criteria.where("customerTransactionId").in(transactionIds));
        }
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("asjusted",count);


        //领卡
        query =new Query();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
       //query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_GET));
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("receiveTrusteeTime").regex(regex, "m"),Criteria.where("receiveDiscountTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("receiveTrusteeTime").ne(null),Criteria.where("receiveDiscountTime").ne(null));
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            criteria = criteria.and("customerTransactionId").in(transactionIds);
        }
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("received",count);

        //刷卡
        query =new Query();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        //query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_SWIPING));
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("swipingTrusteeTime").regex(regex, "m"),Criteria.where("swipingShopTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("swipingTrusteeTime").ne(null),Criteria.where("swipingShopTime").ne(null));
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            criteria = criteria.and("customerTransactionId").in(transactionIds);
        }
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("swing",count);

        //销卡
        query =new Query();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        query.addCriteria(Criteria.where("status").is(BankCardApplyBean.BKSTATUS_CANCEL));
        if(!StringUtils.isEmpty(transactionIds)) {
            query.addCriteria(Criteria.where("customerTransactionId").in(transactionIds));
        }
        query.addCriteria(criteria);
        count = mongoTemplate.count(query,bankCardApply.getClass());
        resultMap.put("cancelCard",count);

        //POS刷卡或者店内刷卡
        List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
        criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if(!StringUtils.isEmpty(date)) {
            criteria.orOperator(Criteria.where("swipingTrusteeTime").regex(regex, "m"),Criteria.where("swipingShopTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("swipingTrusteeTime").ne(null),Criteria.where("swipingShopTime").ne(null));
        }
        if(!StringUtils.isEmpty(transactionIds)) {
            criteria = criteria.and("customerTransactionId").in(transactionIds);
        }
        aos.add(Aggregation.match(criteria));
        aos.add(Aggregation.group("dataStatus").count().as("totalCount").sum("swipingMoney").as("totalAmount"));
        AggregationResults<BasicDBObject> wholeResults = mongoTemplate.aggregate( Aggregation.newAggregation(aos), BankCardApply.class, BasicDBObject.class);
        List<BasicDBObject> result = wholeResults.getMappedResults();
        if(result.size()>0) {
            resultMap.put("totalAmount",result.get(0).get("totalAmount"));
        }else{
            resultMap.put("totalAmount",0.0);
        }
        return resultMap;
    }

    @Override
    public List<ObjectId> getDailySwipingMoneyTransactionObjectIds(String date) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if(!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s",date);
            criteria.orOperator(Criteria.where("swipingTrusteeTime").regex(regex, "m"),Criteria.where("swipingShopTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("swipingTrusteeTime").ne(null),Criteria.where("swipingShopTime").ne(null));
        }
        List<ObjectId>  datas = getIdsList(Query.query(criteria),"so_bankcard_apply","customerTransactionId");
        return datas;
    }

    @Override
    public List<String> getDailySwipingMoneyTransactionIds(String date) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if(!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s",date);
            criteria.orOperator(Criteria.where("swipingTrusteeTime").regex(regex, "m"),Criteria.where("swipingShopTime").regex(regex, "m"));
        }else{
            criteria.orOperator(Criteria.where("swipingTrusteeTime").ne(null),Criteria.where("swipingShopTime").ne(null));
        }
        List<String>  datas = getIdStringList(Query.query(criteria),"so_bankcard_apply","customerTransactionId");
        return datas;
    }

    @Override
    public List<BankCardApply> getByStatus(List<Integer> ds) {
        return bankCardApplyRepository.findByStatusInOrderByTsAsc(ds);
    }
}
