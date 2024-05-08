package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.DeclarationHistorys;
import com.fuze.bcp.creditcar.repository.DeclarationHistorysRepository;
import com.fuze.bcp.creditcar.service.IDeclarationHistorysService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zqw on 2017/9/23.
 */
@Service
public class DeclarationHistorysImpl extends BaseServiceImpl<DeclarationHistorys,DeclarationHistorysRepository> implements IDeclarationHistorysService {

    @Autowired
    DeclarationHistorysRepository declarationHistorysRepository;

    @Override
    public DeclarationHistorys findOneByCustomerTransactionId(String transactionId) {
        return declarationHistorysRepository.findOneByCustomerTransactionId(transactionId);
    }
}
