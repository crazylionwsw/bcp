package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerExchangeCar;
import com.fuze.bcp.repository.BaseRepository;

/**
 * Created by ${Liu} on 2018/3/1.
 */
public interface CustomerExchangeCarRepository extends BaseRepository<CustomerExchangeCar, String> {

    CustomerExchangeCar findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(String transactionId,Integer ds);


}
