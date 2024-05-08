package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.businessbook.BusinessBookBean;
import com.fuze.bcp.api.creditcar.service.IBusinessBookBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by ${Liu} on 2017/12/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class BusinessBookTest {

    @Autowired
    IBusinessBookBizService iBusinessBookBizService;


//    @Test
//    public void Test(){
//        String startTime = "2017-12-18";
//        String endTime = "2017-12-18";
//        String month = "2017-12";
//        iBusinessBookBizService.actGetBusinessBooks(month, startTime, endTime)
//    }


    @Test
    public void getiBusinessBookBizService() {
        String selectTime = "2017-12-19";
        ResultBean<List<BusinessBookBean>> listResultBean = iBusinessBookBizService.actGetBusinessBooks(selectTime);
        System.out.println(listResultBean.getD());
    }

}
