package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.CancelOrder;
import com.fuze.bcp.creditcar.repository.CancelOrderRepository;
import com.fuze.bcp.creditcar.service.ICancelOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by GQR on 2017/8/19.
 */
@Service
public class CancelOrderServiceImpl extends BaseBillServiceImpl<CancelOrder, CancelOrderRepository> implements ICancelOrderService {

    @Autowired
    CancelOrderRepository cancelOrderRepository;

    @Override
    public List<CancelOrder> getCancelOrdersByCustomerTransactionId(String customerTransactionId) {
        return cancelOrderRepository.findAllByDataStatusAndCustomerTransactionId(DataStatus.SAVE,customerTransactionId);
    }
}
