package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerExchangeLoan;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
public interface CustomerExchangeLoanRepository extends BaseRepository<CustomerExchangeLoan,String>{

    CustomerExchangeLoan findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(String customerTransactionId, Integer ds);

    List<CustomerExchangeLoan> findByCustomerTransactionId(String customerTransactionId);
}
