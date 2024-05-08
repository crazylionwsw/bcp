package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerRepayment;
import com.fuze.bcp.repository.BaseRepository;

/**
 * Created by Lily on 2017/9/23.
 * */
import com.fuze.bcp.repository.BaseRepository;

public interface CustomerRepaymentRepository extends BaseRepository<CustomerRepayment,String> {

    CustomerRepayment findByCustomerTransactionId(String customerTransactionId);

}
