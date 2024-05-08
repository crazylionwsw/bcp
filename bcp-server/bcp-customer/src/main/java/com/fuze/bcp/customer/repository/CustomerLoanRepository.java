package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerLoan;
import com.fuze.bcp.repository.BaseRepository;

/**
 * Created by sean on 2016/10/26.
 */
public interface CustomerLoanRepository extends BaseRepository<CustomerLoan,String> {


    CustomerLoan findByCustomerTransactionId(String customerTransactionId);


    CustomerLoan findByCustomerTransactionIdOrderByTsDesc(String customerTransactionId);

    CustomerLoan findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(String customerTransactionId, Integer ds);
}
