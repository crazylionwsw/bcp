package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.BusinessEventType;
import com.fuze.bcp.repository.BaseDataRepository;

/**
 * Created by CJ on 2017/8/10.
 */
public interface BusinessEventTypeRepository extends BaseDataRepository<BusinessEventType,String> {

    BusinessEventType findByEventTypeCode(String eventTypeCode);
}
