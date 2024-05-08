package com.fuze.bcp.creditcar.repository;


import com.fuze.bcp.creditcar.domain.CarPaymentBill;

public interface CarPaymentBillRepository extends BaseBillRepository<CarPaymentBill,String> {

    CarPaymentBill findByCustomerId(String customerId);
}