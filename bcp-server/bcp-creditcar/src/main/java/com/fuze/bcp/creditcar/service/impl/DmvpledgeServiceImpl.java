package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.api.creditcar.bean.DMVPledgeBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.DMVPledge;
import com.fuze.bcp.creditcar.repository.DMVPledgeRepository;
import com.fuze.bcp.creditcar.service.IDmvpledgeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GQR on 2017/8/24.
 */
@Service
public class DmvpledgeServiceImpl extends BaseBillServiceImpl<DMVPledge, DMVPledgeRepository> implements IDmvpledgeService {

    @Autowired
    DMVPledgeRepository dmvPledgeRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<DMVPledge> findAllByStatusOrderByTsDesc(int currentPage, Integer status) {
        PageRequest pageRequest = new PageRequest(currentPage, 20);
        return dmvPledgeRepository.findAllByStatusOrderByTsDesc(status, pageRequest);
    }

    @Override
    public List<DMVPledge> findByCarDealerIdAndPledgeDateReceiveTimeBetween(String carDealerId, String startTime, String endTime) {
        return dmvPledgeRepository.findByDataStatusAndCarDealerIdAndPledgeDateReceiveTimeBetween(DataStatus.SAVE, carDealerId, startTime, endTime);
    }

    @Override
    public List<DMVPledge> findByPledgeDateReceiveTimeLike(String saleMonth) {
        return dmvPledgeRepository.findByDataStatusAndPledgeDateReceiveTimeLike(DataStatus.SAVE, saleMonth);
    }

    /**
     * 日报
     *
     * @param
     * @param
     * @return
     */
    @Override
    public Map<Object, Object> getDailyReport(String orgid, String date, DMVPledge dmvPledge) {
        return getBillDailyReport(orgid, null, null, date, dmvPledge);
    }

    public Map<Object,Object>  getEmployeeReport(String employeeId,String date, DMVPledge dmvPledge){
        return getBillDailyReport(null, employeeId, null, date, dmvPledge);
    }

    public Map<Object, Object> getBillDailyReport(String orginfoId, String employeeId, String cardealerId, String date, DMVPledge dmvPledge) {

        Map<Object, Object> resultMap = new HashMap<Object, Object>();
        SearchBean searchBean = new SearchBean();
        searchBean.setOrginfoId(orginfoId);
        searchBean.setBusinessManagerId(employeeId);
        searchBean.setCarDealerId(cardealerId);

        List<String> transactionId = new ArrayList<String>();
        String regex = String.format("^%s", date);
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));

        //  查询符合条件的transactionIds
        transactionId = getSearchTransactionIds(searchBean,SearchBean.STAGE_ORDER);

        if (!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("ts").regex(regex, "m"));
        }
        query.addCriteria(Criteria.where("customerTransactionId").in(transactionId));

        //业务总数
        long count = mongoTemplate.count(query, dmvPledge.getClass());
        resultMap.put("total", count);

        //登记证代收取
        query.addCriteria(Criteria.where("status").is(DMVPledgeBean.STATUS_EXPRESS));
        count = mongoTemplate.count(query, dmvPledge.getClass());
        resultMap.put("express", count);


        //登记证已经收取
        query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        // query.addCriteria(Criteria.where("status").is(DMVPledgeBean.STATUS_RECEIVE));
        if (!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("pledgeDateReceiveTime").regex(regex, "m"));
        } else {
            query.addCriteria(Criteria.where("pledgeDateReceiveTime").ne(null));
        }

        query.addCriteria(Criteria.where("customerTransactionId").in(transactionId));

        count = mongoTemplate.count(query, dmvPledge.getClass());
        resultMap.put("receive", count);


        //合同已经打印
        query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //query.addCriteria(Criteria.where("status").is(DMVPledgeBean.STATUS_CONTRACT));
        if (!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("contractStartTime").regex(regex, "m"));
        } else {
            query.addCriteria(Criteria.where("contractStartTime").ne(null));
        }

        query.addCriteria(Criteria.where("customerTransactionId").in(transactionId));

        count = mongoTemplate.count(query, dmvPledge.getClass());
        resultMap.put("contract", count);

        //合同已经盖章
        query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //query.addCriteria(Criteria.where("status").is(DMVPledgeBean.STATUS_TAKED));
        if (!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("takeContractTime").regex(regex, "m"));
        } else {
            query.addCriteria(Criteria.where("takeContractTime").ne(null));
        }

        query.addCriteria(Criteria.where("customerTransactionId").in(transactionId));

        count = mongoTemplate.count(query, dmvPledge.getClass());
        resultMap.put("taked", count);

        //车管所开始抵押
        query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //query.addCriteria(Criteria.where("status").is(DMVPledgeBean.STATUS_START));
        if (!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("pledgeStartTime").regex(regex, "m"));
        } else {
            query.addCriteria(Criteria.where("pledgeStartTime").ne(null));
        }

        query.addCriteria(Criteria.where("customerTransactionId").in(transactionId));

        count = mongoTemplate.count(query, dmvPledge.getClass());
        resultMap.put("start", count);

        //车管所抵押完成
        query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        // query.addCriteria(Criteria.where("status").is(DMVPledgeBean.STATUS_END));
        if (!StringUtils.isEmpty(date)) {
            query.addCriteria(Criteria.where("pledgeEndTime").regex(regex, "m"));
        } else {
            query.addCriteria(Criteria.where("pledgeEndTime").ne(null));
        }

        query.addCriteria(Criteria.where("customerTransactionId").in(transactionId));

        count = mongoTemplate.count(query, dmvPledge.getClass());
        resultMap.put("end", count);

        return resultMap;
    }

    /**
     * 获取抵押完成的transactionId
     *
     * @param date
     * @return
     */
    @Override
    public List<ObjectId> getDvmpFinishTransactionObjectIds(String date) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if (!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s", date);
            criteria.and("pledgeEndTime").regex(regex, "m");
        } else {
            criteria.and("pledgeEndTime").ne(null);
        }
        List<ObjectId> datas = getIdsList(Query.query(criteria), "so_dmvpledge", "customerTransactionId");
        return datas;
    }

    @Override
    public List<ObjectId> getReceiveFinishTransactionObjectIds(String date) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if (!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s", date);
            criteria.and("pledgeDateReceiveTime").regex(regex, "m");
        } else {
            criteria.and("pledgeDateReceiveTime").ne(null);
        }
        List<ObjectId> datas = getIdsList(Query.query(criteria), "so_dmvpledge", "customerTransactionId");
        return datas;
    }

    @Override
    public List<DMVPledge> findByCustomerTransactionIdInAndPledgeDateReceiveTimeLike(List<String> transactionIds, String saleMonth) {
        return dmvPledgeRepository.findByDataStatusAndCustomerTransactionIdInAndPledgeDateReceiveTimeLike(DataStatus.SAVE, transactionIds, saleMonth);
    }

}
