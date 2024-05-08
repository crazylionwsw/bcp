package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.paymentBill.PaymentBillBean;
import com.fuze.bcp.api.creditcar.service.IPaymentBillBizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ${Liu} on 2018/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class PaymentBillServiceTest {

    @Autowired
    IPaymentBillBizService iPaymentBillBizService;

    /*@Test
    public void test1(){
        PaymentBillBean paymentBillBean = iPaymentBillBizService.actCreatePaymentBillByDecompress("5aa7362ef98b5a01eccce173","5a28a814fed7671d74458d9a","").getD();
        System.out.println(paymentBillBean);
    }

    @Test
    public void test2(){
        Integer d = iPaymentBillBizService.actGetPaymentBillCount("5aae19fcf98b5a3080869d22").getD();
    }*/
}
