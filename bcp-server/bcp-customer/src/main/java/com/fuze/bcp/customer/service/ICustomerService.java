package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.Customer;
import com.fuze.bcp.service.IBaseService;
import org.springframework.data.domain.Page;

/**
 * Created by admin on 2017/6/2.
 */

public interface ICustomerService extends IBaseService<Customer> {


    Customer getByIdentifyNo(String identifyNo);

    Page<Customer> getAllOrderByTs(int currentPage);

}

