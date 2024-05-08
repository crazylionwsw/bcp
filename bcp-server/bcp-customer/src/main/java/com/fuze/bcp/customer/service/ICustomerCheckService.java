package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.CustomerCheck;
import com.fuze.bcp.service.IBaseService;

/**
 * Created by Lily on 2017/8/3.
 */
public interface ICustomerCheckService extends IBaseService<CustomerCheck> {
    CustomerCheck findCustomerCheckByCustomerId(String customerId);

    Integer FindCustomerCheckCountByCustomerId(String customerId);
}
