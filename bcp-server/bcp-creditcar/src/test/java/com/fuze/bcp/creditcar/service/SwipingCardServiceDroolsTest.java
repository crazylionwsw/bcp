package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardSubmissionBean;
import com.fuze.bcp.api.creditcar.service.ISwipingCardBizService;
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
public class SwipingCardServiceDroolsTest {

    @Autowired
    ISwipingCardBizService iSwipingCardBizService;

//    渠道刷卡
    @Test
    public void submitSwipingCard(){

        SwipingCardSubmissionBean swipingCardSubmissionBean = new SwipingCardSubmissionBean();
        //刷卡时间
        swipingCardSubmissionBean.setPayTime("2017年10月23日");
        //首期还款日期firstRepaymentDate
        //swipingCardSubmissionBean.setFirstRepaymentDate("2017年10月23日");
        //还款日repayment
        //swipingCardSubmissionBean.setRepayment(10);
        ResultBean resultBean = iSwipingCardBizService.actSaveSwipingCard(swipingCardSubmissionBean);
        System.out.println(resultBean);
    }
}
