package com.fuze.bcp.customer.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.customer.repository.CustomerLoanRepository;
import com.fuze.bcp.customer.service.ICustomerLoanService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lily on 2017/7/31.
 */
@Service
public class CustomerLoanServiceImpl extends BaseServiceImpl<CustomerLoan, CustomerLoanRepository> implements ICustomerLoanService {


    @Autowired
    CustomerLoanRepository customerLoanRepository;

    @Override
    public CustomerLoan findByCustomerTransactionId(String customerTransactionId) {
        return customerLoanRepository.findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(customerTransactionId, DataStatus.SAVE);
    }

    @Override
    public List<CustomerLoan> findAll() {
        return customerLoanRepository.findAll();
    }

}


