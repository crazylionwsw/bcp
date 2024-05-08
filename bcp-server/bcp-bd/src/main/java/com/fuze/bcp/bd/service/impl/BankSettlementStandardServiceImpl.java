package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.BankSettlementStandard;
import com.fuze.bcp.bd.repository.BankSettlementStandardRepository;
import com.fuze.bcp.bd.service.IBankSettlementStandardService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zqw on 2017/8/11.
 */
@Service
public class BankSettlementStandardServiceImpl extends BaseDataServiceImpl<BankSettlementStandard,BankSettlementStandardRepository> implements IBankSettlementStandardService{

    @Autowired
    private BankSettlementStandardRepository bankSettlementStandardRepository;

    @Override
    public BankSettlementStandard getOneByChannelIdAndDeclarationId(String channelId, String declarationId) {
        return bankSettlementStandardRepository.getOneByChannelIdAndDeclarationId(channelId,declarationId);
    }
}
