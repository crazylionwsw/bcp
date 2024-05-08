package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.ValuationListBean;
import com.fuze.bcp.api.creditcar.service.ICarValuationBizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by jinglu on 2017/11/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CarValuationServiceTest {

    @Autowired
    ICarValuationBizService iCarValuationBizService;

    @Test
    public void actFindCarValuationsByCarDealerId(){
        List<ValuationListBean> d = iCarValuationBizService.actFindCarValuationsByCarDealerId("59fa96c7ba2a5d6476b459e3").getD();
    }
}
