package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.creditcar.bean.customerfeebill.CustomerFeeBillBean;
import com.fuze.bcp.api.creditcar.service.ICustomerFeeBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.CustomerFeeBill;
import com.fuze.bcp.creditcar.service.ICustomerFeeBillService;
import com.fuze.bcp.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ${Liu} on 2017/9/27.
 */
@Service
public class BizCustomerFeeBillService implements ICustomerFeeBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerFeeBillService.class);

    @Autowired
    MappingService mappingService;

    @Autowired
    ICustomerFeeBillService iCustomerFeeBillService;

    @Override
    public ResultBean<CustomerFeeBillBean> actGetCustomerFeeBill(String customerTransactionId) {
        CustomerFeeBill customerFeeBill = iCustomerFeeBillService.findByCustomerTransactionId(customerTransactionId);
        if(customerFeeBill != null){
            return ResultBean.getSucceed().setD(mappingService.map(customerFeeBill,CustomerFeeBill.class));
        }
        return ResultBean.getFailed();
    }
}
