package com.fuze.bcp.customer.service.impl;

import com.fuze.bcp.customer.domain.CustomerCheck;
import com.fuze.bcp.customer.repository.CustomerCheckRepository;
import com.fuze.bcp.customer.service.ICustomerCheckService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lily on 2017/8/3.
 */
@Service
public class CustomerCheckServiceImpl extends BaseServiceImpl<CustomerCheck, CustomerCheckRepository> implements ICustomerCheckService {

    @Autowired
    private CustomerCheckRepository customerCheckRepository;

    @Override
    public CustomerCheck findCustomerCheckByCustomerId(String customerId) {
        return customerCheckRepository.findOneByCustomerId(customerId);
    }

    @Override
    public Integer FindCustomerCheckCountByCustomerId(String customerId) {
        return customerCheckRepository.countByCustomerId(customerId);
    }
}
