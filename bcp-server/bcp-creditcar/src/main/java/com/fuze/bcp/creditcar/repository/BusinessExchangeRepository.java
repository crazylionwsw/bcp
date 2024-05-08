package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.BusinessExchange;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
public interface BusinessExchangeRepository extends BaseBillRepository<BusinessExchange,String>{

    List<BusinessExchange> findByCustomerTransactionIdAndApproveStatus(String transactionId,Integer approveStatus);
}
