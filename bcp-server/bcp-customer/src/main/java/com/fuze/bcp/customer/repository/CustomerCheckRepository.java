package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerCheck;
import com.fuze.bcp.repository.BaseRepository;

/**
 * Created by Lily on 2017/8/3.
 */
public interface CustomerCheckRepository extends BaseRepository<CustomerCheck,String> {
    CustomerCheck findOneByCustomerId(String customerId);

    Integer countByCustomerId(String customerId);
}
