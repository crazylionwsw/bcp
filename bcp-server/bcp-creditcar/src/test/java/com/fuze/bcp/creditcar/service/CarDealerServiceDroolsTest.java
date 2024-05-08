package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.bd.bean.PayAccountBean;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.bean.CarDealerSubmissionBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
//渠道
public class CarDealerServiceDroolsTest {

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    @Test
    public void submitCardealer(){

        CarDealerSubmissionBean carDealerSubmissionBean = new CarDealerSubmissionBean();

        carDealerSubmissionBean.setBusinessType("NO");
        carDealerSubmissionBean.setDealerRegion("广东省->潮州市->饶平县");//经销商所属区域
        carDealerSubmissionBean.setName("经销商001");//经销商名称
        carDealerSubmissionBean.setCell("123"); //经销商联系方式
        //是否限定经营品牌BrandIsLimit不等于0
        //carDealerSubmissionBean.setBrandIsLimit(1);
        //账户信息
        PayAccountBean pay = new PayAccountBean();
        pay.setAccountName("邯郸银行");
        List<PayAccountBean> payAccountList = new ArrayList<PayAccountBean>();
        payAccountList.add(pay);
        //carDealerSubmissionBean.setPayAccounts(payAccountList);

        //carDealerSubmissionBean.setPaymentNewTime();

        ResultBean<CarDealerBean> carDealerBeanResultBean = iCarDealerBizService.actSaveCarDealerSubmission(carDealerSubmissionBean);
        System.out.println(carDealerBeanResultBean.getM());

    }
}
