package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.PoundageSettlement;
import com.fuze.bcp.creditcar.repository.PoundageSettlementRepository;
import com.fuze.bcp.creditcar.service.IPoundageSettlementService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.utils.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by zqw on 2017/8/29.
 */
@Service
public class PoundageSettlementServiceImpl extends BaseServiceImpl<PoundageSettlement,PoundageSettlementRepository> implements IPoundageSettlementService{

    @Autowired
    private PoundageSettlementRepository poundageSettlementRepository;

    @Override
    public Page<PoundageSettlement> getAllByOrderByOrderTimeDesc(Integer currentPage) {
        PageRequest page = new PageRequest(currentPage,20);
        return poundageSettlementRepository.getAllByOrderByOrderTimeDesc(page);
    }

    @Override
    public Page<PoundageSettlement> findAllByCustomerIdsAndStartTimeAndEndTimeAndPoundageSettlement(List<String> customerIds, String startTime, String endTime, PoundageSettlement poundageSettlement,int currentPage) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (!StringUtils.isEmpty(poundageSettlement.getChargePaymentWayCode()))
            query.addCriteria(Criteria.where("chargePaymentWayCode").is(poundageSettlement.getChargePaymentWayCode()));
        if (poundageSettlement.getDeclarationCashSourceId() != null && !"".equals(poundageSettlement.getDeclarationCashSourceId()))
            query.addCriteria(Criteria.where("declarationCashSourceId").is(poundageSettlement.getDeclarationCashSourceId()));
        if (poundageSettlement.getChannelCashSourceId() != null && !"".equals(poundageSettlement.getChannelCashSourceId()))
            query.addCriteria(Criteria.where("channelCashSourceId").is(poundageSettlement.getChannelCashSourceId()));
        if (customerIds.size() > 0 ) {
            query.addCriteria(Criteria.where("customerId").in(customerIds));
        }
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && !"undefined".equals(startTime) && !"undefined".equals(endTime))
            query.addCriteria(Criteria.where("orderTime").gte(startTime).lt(SimpleUtils.getOffsetDaysTodayStrOf(endTime, 1)));
        if (!StringUtils.isEmpty(startTime) && !"undefined".equals(startTime))
            query.addCriteria(Criteria.where("orderTime").gte(startTime));
        if (!StringUtils.isEmpty(endTime) && !"undefined".equals(endTime))
            query.addCriteria(Criteria.where("orderTime").lt(SimpleUtils.getOffsetDaysTodayStrOf(endTime, 1)));

        //查询根据orderTime倒序
        Pageable pageable = new PageRequest(currentPage,20);
        query.with(PoundageSettlement.getSortASC("orderTime")).with(pageable);
        List list = mongoTemplate.find(query,PoundageSettlement.class);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,PoundageSettlement.class));
        return page;
    }

    @Override
    public PoundageSettlement getOneByCustomerTransactionId(String customerTransactionId) {
        return poundageSettlementRepository.findOneByCustomerTransactionId(customerTransactionId);
    }

    @Override
    public PoundageSettlement getOneByCustomerTransactionId(String customerTransactionId, int save) {
        return poundageSettlementRepository.findOneByCustomerTransactionIdAndDataStatus(customerTransactionId,save);
    }

    @Override
    public Page<PoundageSettlement> findAllByStartTimeAndEndTimeAndPoundageSettlementOrderByOrderTimeDesc(String startTime, String endTime, PoundageSettlement poundageSettlement,int currentPage) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (!StringUtils.isEmpty(poundageSettlement.getChargePaymentWayCode()))
            query.addCriteria(Criteria.where("chargePaymentWayCode").is(poundageSettlement.getChargePaymentWayCode()));
        if (poundageSettlement.getDeclarationCashSourceId() != null && !"".equals(poundageSettlement.getDeclarationCashSourceId()))
            query.addCriteria(Criteria.where("declarationCashSourceId").is(poundageSettlement.getDeclarationCashSourceId()));
        if (poundageSettlement.getChannelCashSourceId() != null && !"".equals(poundageSettlement.getChannelCashSourceId()))
            query.addCriteria(Criteria.where("channelCashSourceId").is(poundageSettlement.getChannelCashSourceId()));

        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && !"undefined".equals(startTime) && !"undefined".equals(endTime))
            query.addCriteria(Criteria.where("orderTime").gte(startTime).lt(SimpleUtils.getOffsetDaysTodayStrOf(endTime, 1)));
        if (!StringUtils.isEmpty(startTime) && !"undefined".equals(startTime))
            query.addCriteria(Criteria.where("orderTime").gte(startTime));
        if (!StringUtils.isEmpty(endTime) && !"undefined".equals(endTime))
            query.addCriteria(Criteria.where("orderTime").lt(SimpleUtils.getOffsetDaysTodayStrOf(endTime, 1)));

        Pageable pageable = new PageRequest(currentPage,20);
        List list = mongoTemplate.find(query,PoundageSettlement.class);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,PoundageSettlement.class));
        return page;
    }

}