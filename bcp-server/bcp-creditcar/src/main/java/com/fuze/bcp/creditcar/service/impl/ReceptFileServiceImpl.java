package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.ReceptFile;
import com.fuze.bcp.creditcar.repository.ReceptFileRepository;
import com.fuze.bcp.creditcar.service.IReceptFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ${Liu} on 2017/9/19.
 */
@Service
public class ReceptFileServiceImpl extends BaseBillServiceImpl<ReceptFile,ReceptFileRepository> implements IReceptFileService{

    @Autowired
    ReceptFileRepository receptFileRepository;

    @Override
    public ReceptFile getOneByCustomerTransactionId(String customerTransactionId) {
        return receptFileRepository.findOneByCustomerTransactionId(customerTransactionId);
    }
}
