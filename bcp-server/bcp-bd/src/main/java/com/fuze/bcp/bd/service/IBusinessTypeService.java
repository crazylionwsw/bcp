package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.BusinessType;
import com.fuze.bcp.service.IBaseDataService;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * Created by admin on 2017/5/24.
 */
public interface IBusinessTypeService extends IBaseDataService<BusinessType> {

    List<BusinessType> getBusinessByCodes(List<String> codes);

}
