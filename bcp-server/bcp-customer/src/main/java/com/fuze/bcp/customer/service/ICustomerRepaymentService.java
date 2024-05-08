package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.CustomerRepayment;
import com.fuze.bcp.service.IBaseService;

/**
 * Created by Lily on 2017/9/23.
 */
public interface ICustomerRepaymentService extends IBaseService<CustomerRepayment> {

    CustomerRepayment findByCustomerTransactionId(String customerTransactionId);

}
