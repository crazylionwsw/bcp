package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CancelOrder;

import java.util.List;

/**
 * Created by GQR on 2017/8/19.
 */
public interface ICancelOrderService extends IBaseBillService<CancelOrder> {

    List<CancelOrder> getCancelOrdersByCustomerTransactionId(String customerTransactionId);

}
