package com.fuze.bcp.creditcar.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fuze.bcp.creditcar.domain.PaymentBill;
import com.fuze.bcp.creditcar.repository.PaymentBillRepository;
import com.fuze.bcp.creditcar.service.IPaymentBillService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ${Liu} on 2018/3/5.
 */
@Service
public class PaymentBillServiceImpl extends BaseBillServiceImpl<PaymentBill,PaymentBillRepository> implements IPaymentBillService{

    @Autowired
    PaymentBillRepository paymentBillRepository;

    @Override
    public List<PaymentBill> findByCustomerTransactionIdAndDataStatus(String transactionId, Integer save) {
        return paymentBillRepository.findByCustomerTransactionIdAndDataStatus(transactionId,save);
    }

    @Override
    public List<PaymentBill> getByCustomerTransactionId(String transactionId) {
        return paymentBillRepository.findByCustomerTransactionId(transactionId);
    }

    @Override
    public PaymentBill getPaymentBillByPayContentTypeAndCustomerTransactionId(String type, String transactionId) {
        return paymentBillRepository.findByPayContentTypeAndCustomerTransactionId(type,transactionId);
    }
}
