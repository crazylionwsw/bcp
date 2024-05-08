package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.CustomerSurveyResult;
import com.fuze.bcp.creditcar.repository.CustomerSurveyResultRepository;
import com.fuze.bcp.creditcar.service.ICustomerSurveyResultService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zqw on 2017/9/5.
 */
@Service
public class CustomerSurveyResultServiceImpl extends BaseDataServiceImpl<CustomerSurveyResult, CustomerSurveyResultRepository> implements ICustomerSurveyResultService {

    @Autowired
    CustomerSurveyResultRepository customerSurveyResultRepository;

    @Override
    public CustomerSurveyResult getByTransactionId(String transactionId) {
        return customerSurveyResultRepository.findOneByCustomerTransactionId(transactionId);
    }
}
