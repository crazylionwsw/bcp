package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.SharingDetails;
import com.fuze.bcp.creditcar.repository.SharingDetailsRepository;
import com.fuze.bcp.creditcar.service.IDealerSharingDetailsService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lily on 2017/9/23.
 */
@Service
public class DealerSharingDetailsServiceImpl extends BaseServiceImpl<SharingDetails, SharingDetailsRepository> implements IDealerSharingDetailsService {

    @Override
    public SharingDetails getByTransactionIdAndType(String transactionId, Integer type) {
        return baseRepository.findOneByTransactionIdAndSharingType(transactionId, type);
    }

    @Override
    public List<SharingDetails> getByTransactionIdInAndType(List<String> transactionIds, Integer type) {
        return baseRepository.findOneByTransactionIdInAndSharingType(transactionIds, type);
    }

    @Override
    public List findDistinctStatusByTransactionIdInAndSharingTypeAndPledgeDateReceiveTimeLike(List<String> transactionIds, Integer type, String month) {
        return mongoTemplate.getCollection(mongoTemplate.getCollectionName(SharingDetails.class)).distinct("status", new Query(Criteria.where("transactionId").in(transactionIds).and("sharingType").is(type).and("pledgeDateReceiveTime").regex(month)).getQueryObject());
    }

    @Override
    public List<SharingDetails> getAllByIdInAndType(List<String> sharingDetailIds, Integer sharingType) {
        return baseRepository.findAllByDataStatusAndIdInAndSharingType(DataStatus.SAVE, sharingDetailIds, sharingType);
    }


    //    @Override
//    public List<DealerSharingDetails> getByDealerSharingIds(List<String> dealerSharingIds) {
//        return baseRepository.findByDealerSharingIdIn(dealerSharingIds);
//    }

//    @Override
//    public Page<DealerSharingDetails> getByDealerSharingIds(List<String> dealerSharingIds, Integer page) {
//        PageRequest pr = new PageRequest(page, 20, DealerSharingDetails.getSortDESC("dealerSharingId"));
//        return baseRepository.findByDealerSharingIdIn(dealerSharingIds, pr);
//    }

//    @Override
//    public List<DealerSharingDetails> getByDealerSharingId(String dealerSharingId) {
//        return baseRepository.findByDealerSharingId(dealerSharingId);
//    }
}
