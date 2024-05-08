package com.fuze.bcp.customer.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zqw on 2017/9/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CustomerImageFileTest {

/*    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Test
    public void test(){
        iCustomerImageFileBizService.actGetCustomerImagesByTransactionId("5a3a22eaebf1eb3bd87be7cd",0,60);
    }*/

}
