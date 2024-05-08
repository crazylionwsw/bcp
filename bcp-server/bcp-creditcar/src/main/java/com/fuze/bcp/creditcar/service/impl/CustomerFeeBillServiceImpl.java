package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.CustomerFeeBill;
import com.fuze.bcp.creditcar.repository.CustomerFeeBillRepository;
import com.fuze.bcp.creditcar.service.ICustomerFeeBillService;
import org.springframework.stereotype.Service;

/**
 * Created by Lily on 2017/7/19.
 */
@Service
public class CustomerFeeBillServiceImpl extends BaseBillServiceImpl<CustomerFeeBill, CustomerFeeBillRepository> implements ICustomerFeeBillService {

}
