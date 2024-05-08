package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.BusinessType;
import com.fuze.bcp.bd.repository.BusinessTypeRepository;
import com.fuze.bcp.bd.service.IBusinessTypeService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class BusinessTypeServiceImpl extends BaseDataServiceImpl<BusinessType, BusinessTypeRepository> implements IBusinessTypeService {

    @Autowired
    BusinessTypeRepository businessTypeRepository;

    @Override
    public List<BusinessType> getBusinessByCodes(List<String> codes) {
        return businessTypeRepository.findByDataStatusAndCodeIn(DataStatus.SAVE,codes);
    }
}
