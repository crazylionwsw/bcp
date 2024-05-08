package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarTransferBizService;
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
* 转移过户
* */
public class CarTransferServiceDroolsTest {

    @Autowired
    ICarTransferBizService iCarTransferBizService;

    @Test
    public void submitCarTransfer(){

        CarTransferSubmissionBean carTransferSubmissionBean = new CarTransferSubmissionBean();
        carTransferSubmissionBean.setId("123456");
        carTransferSubmissionBean.setVin("123");
        iCarTransferBizService.actSaveCarTransfer(carTransferSubmissionBean);
        System.out.println();

    }
}
