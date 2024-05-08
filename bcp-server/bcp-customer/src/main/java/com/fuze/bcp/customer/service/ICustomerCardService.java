package com.fuze.bcp.customer.service;

import com.fuze.bcp.customer.domain.CustomerCard;
import com.fuze.bcp.service.IBaseService;

/**
 * Created by Lily on 2017/8/23.
 */
public interface ICustomerCardService extends IBaseService<CustomerCard> {

    CustomerCard findByCustomerTransactionId(String customerTransactionId);
}
