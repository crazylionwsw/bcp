package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.RejectCustomer;
import com.fuze.bcp.creditcar.repository.RejectCustomerRepository;
import com.fuze.bcp.creditcar.service.IRejectCustomerService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/6/14.
 */
@Service
public class RejectCustomerServiceImpl extends BaseServiceImpl<RejectCustomer, RejectCustomerRepository> implements IRejectCustomerService {

    @Autowired
    RejectCustomerRepository rejectCustomerRepository;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public Page<RejectCustomer> findAllBySearchBean(SearchBean searchBean) {

        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //  查询customerId
        query.addCriteria(Criteria.where("customerId").in(this.getCustomerIdsBySearchBean(searchBean,0)));
        Pageable pageable = new PageRequest(searchBean.getCurrentPage(),searchBean.getPageSize(), RejectCustomer.getTsSort());
        query.with(RejectCustomer.getSortDESC("ts")).with(pageable);
        List list = mongoTemplate.find(query,RejectCustomer.class);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,RejectCustomer.class));
        return page;
    }
}


