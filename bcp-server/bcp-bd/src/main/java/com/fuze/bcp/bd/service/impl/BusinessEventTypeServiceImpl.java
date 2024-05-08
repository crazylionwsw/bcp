package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.BusinessEventType;
import com.fuze.bcp.bd.repository.BusinessEventTypeRepository;
import com.fuze.bcp.bd.service.IBusinessEventTypeService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/8/10.
 */
@Service
public class BusinessEventTypeServiceImpl extends BaseDataServiceImpl<BusinessEventType, BusinessEventTypeRepository> implements IBusinessEventTypeService {

    @Autowired
    BusinessEventTypeRepository businessEventTypeRepository;

    @Override
    public BusinessEventType getOneByBusinessType(String eventTypeCode) {
        return businessEventTypeRepository.findByEventTypeCode(eventTypeCode);
    }

    @Override
    public Page<BusinessEventType> getAllOrderByEventTypeCode(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20, new Sort(Sort.Direction.ASC, "eventTypeCode"));
        return businessEventTypeRepository.findAll(pr);
    }
}
