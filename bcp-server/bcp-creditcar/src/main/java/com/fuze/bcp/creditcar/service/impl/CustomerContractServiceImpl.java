package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.CustomerContract;
import com.fuze.bcp.creditcar.repository.CustomerContractRepository;
import com.fuze.bcp.creditcar.service.ICustomerContractService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zqw on 2017/8/10.
 */
@Service
public class CustomerContractServiceImpl extends BaseServiceImpl<CustomerContract,CustomerContractRepository> implements ICustomerContractService {

    @Autowired
    private CustomerContractRepository customerContractRepository;

    @Override
    public List<CustomerContract> getCustomerTransactionContracts(String customerId, String customerTransactionId) {
        return customerContractRepository.findByCustomerIdAndCustomerTransactionId(customerId, customerTransactionId);
    }

    @Override
    public CustomerContract getCustomerTransactionContract(String customerId, String customerTransactionId, String documentId) {
        return customerContractRepository.findByCustomerIdAndCustomerTransactionIdAndDocumentIdAndDataStatus(customerId, customerTransactionId, documentId, DataStatus.SAVE);
    }

    @Override
    public List<CustomerContract> getCustomerTransactionContracts(String customerId, String customerTransactionId, List<String> documentIdsList) {
        return customerContractRepository.findByCustomerIdAndCustomerTransactionIdAndDocumentIdInAndDataStatus(customerId, customerTransactionId, documentIdsList, DataStatus.SAVE);
    }

    @Override
    public Integer countByContractAndTsStartingWith(String documentId, String today) {
        return customerContractRepository.countByDocumentIdAndTsStartingWith(documentId,today);
    }
}
