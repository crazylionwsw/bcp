package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.BusinessEventType;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

/**
 * Created by CJ on 2017/8/10.
 */
public interface IBusinessEventTypeService extends IBaseDataService<BusinessEventType> {

    BusinessEventType getOneByBusinessType(String bussinessType);

    Page<BusinessEventType> getAllOrderByEventTypeCode(Integer currentPage);
}
