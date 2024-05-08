package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CustomerSurveyResult;
import com.fuze.bcp.service.IBaseDataService;

/**
 * Created by zqw on 2017/9/5.
 */
public interface ICustomerSurveyResultService extends IBaseDataService<CustomerSurveyResult> {

    CustomerSurveyResult getByTransactionId(String transactionId);

}
