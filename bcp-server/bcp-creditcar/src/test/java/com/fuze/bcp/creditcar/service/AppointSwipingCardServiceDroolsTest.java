package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IAppointSwipingCardBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by admin on 2017/10/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class AppointSwipingCardServiceDroolsTest {

    @Autowired
    IAppointSwipingCardBizService iAppointSwipingCardBizService;

    @Test
    public void submitAppointSwipingCard(){
        /*AppointSwipingCardSubmissionBean appointSwipingCardSubmissionBean = new AppointSwipingCardSubmissionBean();
        appointSwipingCardSubmissionBean.setId("123456789999");
        appointSwipingCardSubmissionBean.setAppointTakeTime("2017年10月21日"); //预计领卡时间
        appointSwipingCardSubmissionBean.setAppointPayTime("2017年10月23日");  //预计刷卡时间
        //appointSwipingCardSubmissionBean.setPickupDate("2017年10月24日");  //预计提车时间

        ResultBean resultBean = iAppointSwipingCardBizService.actSaveAppointSwipingCard(appointSwipingCardSubmissionBean);
        System.out.println(resultBean);*/
        iAppointSwipingCardBizService.actCreateAppointSwipingCard("5a56fe8d617db11f9840a6ff");
    }
}
