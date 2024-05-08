package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.BusinessExchange;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/1.
 */
public interface IBusinessExchangeService extends IBaseBillService<BusinessExchange>{

    List<BusinessExchange> getByCustomerTransactionIdAndByApproveStatus(String transactionId, Integer approveStatus);
}
