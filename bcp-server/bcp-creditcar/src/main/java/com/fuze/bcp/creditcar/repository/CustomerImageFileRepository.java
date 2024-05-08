package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.CustomerImageFile;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by sean on 2016/10/26.
 */
public interface CustomerImageFileRepository extends BaseRepository<CustomerImageFile,String> {

    List<CustomerImageFile> findByCustomerId(String id);

    List<CustomerImageFile> findByCustomerIdAndCustomerImageTypeCode(String customerId, String customerImageTypeCode);

    List<CustomerImageFile> findByCustomerIdAndCustomerTransactionId(String customerId, String transactionId);

    List<CustomerImageFile> findByCustomerIdAndCustomerTransactionIdAndDataStatus(String customerId, String transactionId, Integer ds);

    List<CustomerImageFile> findByCustomerIdAndCustomerTransactionIdAndCustomerImageTypeCodeInAndDataStatus(String customerId, String transactionId, List<String> imageTypeCodes, Integer ds);

    List<CustomerImageFile> findByIdAndCustomerImageTypeCodeIn(String id,List<String> imageTypeCodes);

    CustomerImageFile findAllByCustomerTransactionIdAndCustomerImageTypeCodeAndDataStatus(String transactionId,String imgCode,Integer ds);

    CustomerImageFile findAllByCustomerIdAndCustomerTransactionIdAndCustomerImageTypeCodeAndDataStatus(String customerId, String transactionId,String imgCode,Integer ds);

}
