package com.fuze.bcp.customer.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.customer.domain.Customer;
import com.fuze.bcp.customer.repository.CustomerRepository;
import com.fuze.bcp.customer.service.ICustomerService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/6/14.
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, CustomerRepository> implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer getByIdentifyNo(String identifyNo) {
        return this.customerRepository.findOneByDataStatusAndIdentifyNo(DataStatus.SAVE, identifyNo);
    }

    @Override
    public Page<Customer> getAllOrderByTs(int currentPage) {
        PageRequest pageRequest = new PageRequest(currentPage,20);
        return this.customerRepository.findAllByOrderByTsDesc(pageRequest);
    }

}


