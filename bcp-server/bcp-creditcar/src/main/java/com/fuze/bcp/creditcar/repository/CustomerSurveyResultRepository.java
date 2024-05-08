package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.CustomerSurveyResult;
import com.fuze.bcp.repository.BaseDataRepository;

/**
 * Created by zqw on 2017/9/5.
 */
public interface CustomerSurveyResultRepository extends BaseDataRepository<CustomerSurveyResult,String> {
    CustomerSurveyResult findOneByCustomerTransactionId(String transactionId);
}
