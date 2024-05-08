package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.PaymentBill;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/5.
 */
public interface PaymentBillRepository extends BaseBillRepository<PaymentBill,String>{

    List<PaymentBill> findByCustomerTransactionIdAndDataStatus(String transactionId,Integer save);

    List<PaymentBill> findByCustomerTransactionId(String transactionId);

    PaymentBill findByPayContentTypeAndCustomerTransactionId(String type,String transactionId);
}
