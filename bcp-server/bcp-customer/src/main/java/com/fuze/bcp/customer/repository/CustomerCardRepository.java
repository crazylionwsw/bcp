package com.fuze.bcp.customer.repository;

import com.fuze.bcp.customer.domain.CustomerCard;
import com.fuze.bcp.repository.BaseRepository;

/**
 * Created by Lily on 2017/8/23.
 */
public interface CustomerCardRepository extends BaseRepository<CustomerCard,String> {

    CustomerCard findOneByCustomerTransactionId(String customerTransactionId);

    CustomerCard findByCustomerTransactionIdOrderByTsDesc(String customerTransactionId);
}
