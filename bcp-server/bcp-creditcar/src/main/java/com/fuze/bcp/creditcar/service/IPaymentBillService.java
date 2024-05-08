package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.PaymentBill;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/5.
 */
public interface IPaymentBillService extends IBaseBillService<PaymentBill>{

    List<PaymentBill> findByCustomerTransactionIdAndDataStatus(String transactionId,Integer save);

    List<PaymentBill> getByCustomerTransactionId(String transactionId);

    PaymentBill getPaymentBillByPayContentTypeAndCustomerTransactionId(String type,String transactionId);
}
