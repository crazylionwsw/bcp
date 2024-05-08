package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.CustomerCar;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
public interface ICustomerCarService extends IBaseService<CustomerCar> {

    CustomerCar findByCustomerTransactionId(String customerTransactionId);

    List<CustomerCar> findAllByCustomerTransactionId(String customerTransactionId);

    CustomerCar findByCustomerId(String customerId);
}
