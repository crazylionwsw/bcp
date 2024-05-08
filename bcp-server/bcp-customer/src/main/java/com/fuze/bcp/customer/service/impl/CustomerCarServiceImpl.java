package com.fuze.bcp.customer.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.customer.domain.CustomerCar;
import com.fuze.bcp.customer.repository.CustomerCarRepository;
import com.fuze.bcp.customer.service.ICustomerCarService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lily on 2017/7/31.
 */
@Service
public class CustomerCarServiceImpl extends BaseServiceImpl<CustomerCar, CustomerCarRepository> implements ICustomerCarService {

    @Override
    public CustomerCar findByCustomerTransactionId(String customerTransactionId) {
        return baseRepository.findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(customerTransactionId, DataStatus.SAVE);
    }

    @Override
    public List<CustomerCar> findAllByCustomerTransactionId(String customerTransactionId) {
        return baseRepository.findByCustomerTransactionIdAndDataStatusOrderByIdDesc(customerTransactionId, DataStatus.SAVE);
    }

    @Override
    public CustomerCar findByCustomerId(String customerId) {
        return baseRepository.findByCustomerId(customerId);
    }
}
