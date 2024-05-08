package com.fuze.bcp.system.service;

import com.fuze.bcp.api.sys.bean.LoginLogBean;
import com.fuze.bcp.api.sys.service.ILogBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by user on 2017/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class LogServiceTest {

    @Autowired
    ILogBizService iLogBizService;

    @Test
    public void testGetLoginLogs(){
        ResultBean<DataPageBean<LoginLogBean>> datas = iLogBizService.actGetLoginLogs(30);
        System.out.println(datas);
    }

    @Test
    public void testSaveLoginLogs(){
        LoginLogBean loginLogBean = new LoginLogBean();
        loginLogBean.setLoginTime("");
        ResultBean<LoginLogBean> data = iLogBizService.actSaveLoginLogs(loginLogBean);
        System.out.println(data);
    }

    @Test
    public void testGetLoginLogss(){//

        ResultBean<List<LoginLogBean>> data = iLogBizService.actGetLoginLogs();
        System.out.println(data);

    }

    @Test
    public void testGetLoginLog(){//
        ResultBean<LoginLogBean> data = iLogBizService.actGetLoginLog("58f832cbe4b01add747fb342");
        System.out.println(data);
    }

    @Test
    public void testDeleteLoginLogs(){//
        LoginLogBean loginLogBean = new LoginLogBean();
        loginLogBean.setId("594b4f44e776ac1458b2f777");
        ResultBean<LoginLogBean> data = iLogBizService.actDeleteLoginLogs("594b4f44e776ac1458b2f777");
        System.out.println(data);
    }

    @Test
    public void testLookupLoginLogs(){
        ResultBean<List<LoginLogBean>> data = iLogBizService.actLookupLoginLogs();
        System.out.println(data);

    }


}
