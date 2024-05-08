package com.fuze.bcp.customer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.customer.domain.CustomerExchangeCar;
import com.fuze.bcp.customer.repository.CustomerExchangeCarRepository;
import com.fuze.bcp.customer.service.ICustomerExchangeCarService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Service
public class CustomerExchangeCarServiceImpl extends BaseServiceImpl<CustomerExchangeCar,CustomerExchangeCarRepository> implements ICustomerExchangeCarService{

    @Autowired
    CustomerExchangeCarRepository customerExchangeCarRepository;

    @Override
    public CustomerExchangeCar findByCustomerTransactionId(String customerTransactionId) {
        return customerExchangeCarRepository.findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(customerTransactionId, DataStatus.SAVE);
    }
}
