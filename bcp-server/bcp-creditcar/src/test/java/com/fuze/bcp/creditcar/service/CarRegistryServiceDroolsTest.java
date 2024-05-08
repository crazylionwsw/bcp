package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.carregistry.CarRegistrySubmissionBean;
import com.fuze.bcp.api.creditcar.service.ICarRegistryBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 车辆上牌drools验证
 * Created by ${Liu} on 2017/10/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CarRegistryServiceDroolsTest {

    @Autowired
    ICarRegistryBizService iCarRegistryBizService;

    @Test
    public void actSaveCarRegistry(){
        SignInfo signInfo = new SignInfo("598acc2238edd4cb482ac373", "58ce4da5e4b0ad25ee41bb0f", 8, "bh");

        iCarRegistryBizService.actSignCarRegistry("5a093faeba2a5d7c2b00e5cf", signInfo);
        /*CarRegistrySubmissionBean carRegistrySubmissionBean = new CarRegistrySubmissionBean();
        carRegistrySubmissionBean.setId("123456");
        carRegistrySubmissionBean.setCarModelNumber("豪车123");
//        carRegistrySubmissionBean.setLicenseNumber("");
//        carRegistrySubmissionBean.setMotorNumber("");
//        carRegistrySubmissionBean.setPickDate("");
        carRegistrySubmissionBean.setRegistryDate("");
//        carRegistrySubmissionBean.setVin("");
        carRegistrySubmissionBean.setRegistryNumber("");
        ResultBean resultBean = iCarRegistryBizService.actSaveCarRegistry(carRegistrySubmissionBean);
        System.out.println(resultBean+"................");*/
    }

}
