package com.fuze.bcp.customer.service.impl;

import com.fuze.bcp.customer.domain.CustomerCard;
import com.fuze.bcp.customer.repository.CustomerCardRepository;
import com.fuze.bcp.customer.repository.CustomerRepository;
import com.fuze.bcp.customer.service.ICustomerCardService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lily on 2017/8/23.
 */
@Service
public class CustomerCardServiceImpl extends BaseServiceImpl<CustomerCard,CustomerCardRepository> implements ICustomerCardService {

    @Autowired
    CustomerCardRepository customerCardRepository;

    @Override
    public CustomerCard findByCustomerTransactionId(String customerTransactionId) {
        return customerCardRepository.findByCustomerTransactionIdOrderByTsDesc(customerTransactionId);
    }
}
