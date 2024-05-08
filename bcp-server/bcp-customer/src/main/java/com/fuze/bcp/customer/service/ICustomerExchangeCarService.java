package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.CustomerExchangeCar;
import com.fuze.bcp.customer.domain.CustomerExchangeLoan;
import com.fuze.bcp.service.IBaseService;

/**
 * Created by ${Liu} on 2018/3/1.
 */
public interface ICustomerExchangeCarService extends IBaseService<CustomerExchangeCar>{

    CustomerExchangeCar findByCustomerTransactionId(String customerTransactionId);
}
