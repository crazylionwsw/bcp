package com.fuze.bcp.customer.service.impl;

import com.fuze.bcp.customer.domain.CustomerRepayment;
import com.fuze.bcp.customer.repository.CustomerRepaymentRepository;
import com.fuze.bcp.customer.service.ICustomerRepaymentService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lily on 2017/9/23.
 */
@Service
public class CustomerRepaymentServiceImpl extends BaseServiceImpl<CustomerRepayment,CustomerRepaymentRepository> implements ICustomerRepaymentService {


    @Autowired
    CustomerRepaymentRepository customerRepaymentRepository;

    @Override
    public CustomerRepayment findByCustomerTransactionId(String customerTransactionId) {
        return customerRepaymentRepository.findByCustomerTransactionId(customerTransactionId);
    }
}
