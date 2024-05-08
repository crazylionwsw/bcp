package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.CustomerContract;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by hecaifeng on 2017/2/18.
 */
public interface CustomerContractRepository extends BaseRepository<CustomerContract,String> {

    List<CustomerContract> findAllByCustomerId(String customerId);

    List<CustomerContract> findByCustomerIdAndCustomerTransactionId(String customerId, String customerTransactionId);

    CustomerContract findByCustomerIdAndCustomerTransactionIdAndDocumentIdAndDataStatus(String customerId, String customerTransactionId, String documentId, Integer ds);

    List<CustomerContract> findByCustomerIdAndCustomerTransactionIdAndDocumentIdInAndDataStatus(String customerId, String customerTransactionId, List<String> documentIdList, Integer ds);

    Integer countByDocumentIdAndTsStartingWith(String documentId, String today);
}
