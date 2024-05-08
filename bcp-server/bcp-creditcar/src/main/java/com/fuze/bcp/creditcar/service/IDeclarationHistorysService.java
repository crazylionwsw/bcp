package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.DeclarationHistorys;
import com.fuze.bcp.service.IBaseService;

/**
 * Created by Lily on 2017/9/23.
 */
public interface IDeclarationHistorysService extends IBaseService<DeclarationHistorys> {


    DeclarationHistorys findOneByCustomerTransactionId(String transactionId);
}
