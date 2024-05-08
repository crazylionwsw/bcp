package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.CustomerExchangeLoan;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
public interface ICustomerExchangeLoanService extends IBaseService<CustomerExchangeLoan>{

    CustomerExchangeLoan  findByCustomerTransactionId(String customerTransactionId);

    List<CustomerExchangeLoan> getByCustomerTransactionId(String customerTransactionId);
}
