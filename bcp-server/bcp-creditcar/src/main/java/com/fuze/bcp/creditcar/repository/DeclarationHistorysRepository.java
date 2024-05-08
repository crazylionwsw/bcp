package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.DeclarationHistorys;
import com.fuze.bcp.repository.BaseRepository;

public interface DeclarationHistorysRepository extends BaseRepository<DeclarationHistorys,String> {

    DeclarationHistorys findOneByCustomerTransactionId(String transactionId);
}
