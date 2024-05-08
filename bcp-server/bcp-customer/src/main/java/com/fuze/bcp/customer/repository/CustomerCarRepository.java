package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerCar;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by sean on 2016/10/26.
 */
public interface CustomerCarRepository extends BaseRepository<CustomerCar, String> {
    /**
     * 根据 customerTransactionId 获取 customerCar
     * @param customerTransactionId
     * @return
     */
    CustomerCar findOneByCustomerTransactionId(String customerTransactionId);

    /**
     *
     * @param customerTransactionId
     * @return
     */
    CustomerCar findOneByCustomerTransactionIdOrderByTsDesc(String customerTransactionId);

    CustomerCar findOneByCustomerTransactionIdAndDataStatusOrderByIdDesc(String customerTransactionId, Integer ds);

    List<CustomerCar> findByCustomerTransactionIdAndDataStatusOrderByIdDesc(String customerTransactionId, Integer ds);

    /**
     *根据客户Id获取CustomerCar
     */
    CustomerCar findByCustomerId(String customerId);
}
