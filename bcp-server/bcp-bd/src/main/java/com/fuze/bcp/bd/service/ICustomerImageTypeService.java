package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CustomerImageType;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CJ on 2017/6/12.
 */
public interface ICustomerImageTypeService extends IBaseDataService<CustomerImageType> {
    CustomerImageType getCustomerImageTypeByCode(String code);

    CustomerImageType FindCustomerImageTypeById(String id);

    List<CustomerImageType> findCustomerImageTypesByIds(List<String> ids);

    List<CustomerImageType> findCustomerImageTypesByCodes(List<String> codes);

    List<CustomerImageType> getAllOrderByCode();

    Page<CustomerImageType> getCustomerImageTypesByCodes(List<String> businessTypeImageTypeCodes, Integer pageIndex, Integer pageSize);
}
