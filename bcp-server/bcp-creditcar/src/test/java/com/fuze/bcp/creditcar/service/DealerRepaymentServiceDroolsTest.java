package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IDealerRepaymentBizService;
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
public class DealerRepaymentServiceDroolsTest {

    @Autowired
    IDealerRepaymentBizService iDealerRepaymentBizService;

    @Test
    public void submitDealerRepayment(){

        /*DealerRepaymentSubmissionBean dealerRepaymentSubmissionBean = new DealerRepaymentSubmissionBean();
        //dealerRepaymentSubmissionBean.setRepaymentTime("2017年10月25日");
        dealerRepaymentSubmissionBean.setTradeWay("支付宝");
        ResultBean resultBean = iDealerRepaymentBizService.actSaveDealerRepayment(dealerRepaymentSubmissionBean);
        System.out.println(resultBean);*/
        iDealerRepaymentBizService.actCreateDealerRepayment("5a4b4a90617db13d9c7c3727");

    }

}
