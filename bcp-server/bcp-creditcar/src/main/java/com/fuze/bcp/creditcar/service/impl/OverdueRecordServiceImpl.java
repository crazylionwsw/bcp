package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.OverdueRecord;
import com.fuze.bcp.creditcar.repository.OverdueRecordRepository;
import com.fuze.bcp.creditcar.service.IOverdueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/10.
 */
@Service
public class OverdueRecordServiceImpl extends BaseBillServiceImpl<OverdueRecord,OverdueRecordRepository>  implements IOverdueRecordService{


    @Autowired
    OverdueRecordRepository overdueRecordRepository;

    @Override
    public OverdueRecord getOverdueRecordByMonth(String transactionId, String month) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        query.addCriteria(Criteria.where("customerTransactionId").is(transactionId));
        if(!StringUtils.isEmpty(month)){
            query.addCriteria(Criteria.where("overdueTime").regex(String.format(month,"m")));
        }
        OverdueRecord overdueRecord = mongoTemplate.findOne(query, OverdueRecord.class);
        return overdueRecord;
    }

    @Override
    public List<OverdueRecord> findByCustomerTransactionIdAndDataStatus(String transactionId, Integer save) {
        return overdueRecordRepository.findByCustomerTransactionIdAndDataStatus(transactionId,save);
    }

    @Override
    public void delOverdueRecordByTransactionId(String transactionId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.TEMPSAVE).and("customerTransactionId").is(transactionId).and("overdueTime").is(null).and("overdueAmount").is(null));
        mongoTemplate.remove(query, OverdueRecord.class);
    }
}
