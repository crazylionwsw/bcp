package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.BankSettlementStandard;
import com.fuze.bcp.service.IBaseDataService;

/**
 * Created by zqw on 2017/8/11.
 */
public interface IBankSettlementStandardService extends IBaseDataService<BankSettlementStandard> {
    BankSettlementStandard getOneByChannelIdAndDeclarationId(String channelId, String declarationId);
}
