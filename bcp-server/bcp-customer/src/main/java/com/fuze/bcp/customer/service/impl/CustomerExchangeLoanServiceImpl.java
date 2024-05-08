package com.fuze.bcp.customer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.customer.domain.CustomerExchangeLoan;
import com.fuze.bcp.customer.repository.CustomerExchangeLoanRepository;
import com.fuze.bcp.customer.service.ICustomerExchangeLoanService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
@Service
public class CustomerExchangeLoanServiceImpl extends BaseServiceImpl<CustomerExchangeLoan,CustomerExchangeLoanRepository> implements ICustomerExchangeLoanService{

    @Autowired
    CustomerExchangeLoanRepository customerExchangeLoanRepository;


    @Override
    public CustomerExchangeLoan findByCustomerTransactionId(String customerTransactionId) {
        return customerExchangeLoanRepository.findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(customerTransactionId, DataStatus.SAVE);
    }

    @Override
    public List<CustomerExchangeLoan> getByCustomerTransactionId(String customerTransactionId) {
        return customerExchangeLoanRepository.findByCustomerTransactionId(customerTransactionId);
    }
}
