package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CustomerContract;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * 客户合同接口
 * Created by zqw on 2017/8/10.
 */
public interface ICustomerContractService extends IBaseService<CustomerContract>{

    List<CustomerContract> getCustomerTransactionContracts(String customerId, String customerTransactionId);

    CustomerContract getCustomerTransactionContract(String customerId, String customerTransactionId, String documentId);

    List<CustomerContract> getCustomerTransactionContracts(String customerId, String customerTransactionId, List<String> documentIdsList);

    Integer countByContractAndTsStartingWith(String documentId, String today);
}
