package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.OrderSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by admin on 2017/10/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
/*
* 客户签约
* */
public class OrderServiceDroolsTest {

    @Autowired
    IOrderBizService iOrderBizService;

    @Test
    public void submitOrder(){

        OrderSubmissionBean orderSubmissionBean = new OrderSubmissionBean();

    }
}
