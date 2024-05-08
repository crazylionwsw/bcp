package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentExcelBean;
import com.fuze.bcp.api.creditcar.service.IAppointPaymentBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by admin on 2017/10/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
//预约垫资
public class AppointPaymentServiceDroolsTest {

    @Autowired
    IAppointPaymentBizService iAppointPaymentBizService;

    @Test
    public void submitAppointPayment(){
        iAppointPaymentBizService.actSubmitAppointPayment("5a27827ecd7d576dbf4628e6", "重新提交");

    }

    @Test
    public void createAppointPayment(){
        iAppointPaymentBizService.actCreateAppointPaymentByswipingCardId("5a03f334ba2a5d6f1dd97735");

    }

    public void test1(){
        iAppointPaymentBizService.actSendAppointPaymentVoucher("");
    }

    @Test
    public void testexport(){
        ResultBean<List<AppointPaymentExcelBean>> listResultBean = iAppointPaymentBizService.actExportAppointPayBusinessBook("2018-01-24");
        System.out.println(listResultBean.getD());

    }
}
