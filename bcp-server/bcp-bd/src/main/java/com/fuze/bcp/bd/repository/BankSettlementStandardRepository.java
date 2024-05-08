package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.BankSettlementStandard;
import com.fuze.bcp.repository.BaseDataRepository;

/**
 * Created by zqw on 2017/8/11.
 */
public interface BankSettlementStandardRepository extends BaseDataRepository<BankSettlementStandard,String> {

    BankSettlementStandard getOneByChannelIdAndDeclarationId(String channelId, String declarationId);

}
