package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.CustomerDemandBean;
import com.fuze.bcp.api.creditcar.bean.DemandSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarValuationBizService;
import com.fuze.bcp.api.creditcar.service.ICreditPhotographBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerDemandBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lily on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CustomerDemandServiceTest {

    @Autowired
    ICustomerDemandBizService bizCustomerDemandService;

    @Autowired
    ICarValuationBizService iCarValuationBizService;

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    ICreditPhotographBizService iCreditPhotographBizService;

    @Test
    public void doValuation() {
        //iCarValuationBizService.actBuyMaintenanceReport("LHGRC3884G8029447");
    }

    @Test
    public void commitCustomerDemand(){

        iCreditPhotographBizService.actFinishOneCustomer("5a0cfd38ba2a5d1e0067d7be");
    }

}
