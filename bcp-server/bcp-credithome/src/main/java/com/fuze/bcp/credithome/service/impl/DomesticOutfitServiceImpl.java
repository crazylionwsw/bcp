package com.fuze.bcp.credithome.service.impl;

import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.credithome.domain.DomesticOutfit;
import com.fuze.bcp.credithome.repository.DomesticOutfitRepository;
import com.fuze.bcp.credithome.service.IDomesticOutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQW on 2018/3/19.
 */
@Service
public class DomesticOutfitServiceImpl extends HomeBillServiceImpl<DomesticOutfit, DomesticOutfitRepository> implements IDomesticOutfitService {

    @Autowired
    private DomesticOutfitRepository domesticOutfitRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public DomesticOutfit findByCustomerTransactionId(String id) {
        return domesticOutfitRepository.findOneByCustomerTransactionId(id);
    }

    @Override
    public Page<DomesticOutfit> findAllByLoginUser(String loginUserId, Integer currentPage, Integer pageSize, Boolean isPass) {
        PageRequest page = new PageRequest(currentPage, pageSize, DomesticOutfit.getTsSort());
        Query query = new Query();
        Criteria c = Criteria.where("dataStatus").ne(DataStatus.DISCARD).and("loginUserId").is(loginUserId);
        query.addCriteria(c);

        List<Integer> status = new ArrayList<Integer>();
        if (isPass){
            status.add(ApproveStatus.APPROVE_PASSED);
            status.add(ApproveStatus.APPROVE_REJECT);
            query.addCriteria(Criteria.where("approveStatus").in(status));
        } else {
            status.add(ApproveStatus.APPROVE_ONGOING);
            status.add(ApproveStatus.APPROVE_REAPPLY);
            query.addCriteria(Criteria.where("approveStatus").in(status));
        }
        query.with(page);

        long total = mongoTemplate.count(query, DomesticOutfit.class);
        List items = mongoTemplate.find(query, DomesticOutfit.class);
        return new PageImpl(items, page, total);
    }
}
