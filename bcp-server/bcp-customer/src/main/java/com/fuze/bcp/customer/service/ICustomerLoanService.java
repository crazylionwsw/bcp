package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
public interface ICustomerLoanService extends IBaseService<CustomerLoan> {

    CustomerLoan  findByCustomerTransactionId(String customerTransactionId);

    List<CustomerLoan> findAll();

}
