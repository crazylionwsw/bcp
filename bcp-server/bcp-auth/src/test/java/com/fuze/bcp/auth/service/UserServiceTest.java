package com.fuze.bcp.auth.service;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by sean on 2017/5/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations ="classpath*:/dubbo-consumer.xml")
public class UserServiceTest {

    @Autowired
    private IAuthenticationBizService iAuthenticationBizService;

    @Test
    public void getList(){
        ResultBean<DataPageBean<LoginUserBean>> users = iAuthenticationBizService.actGetLoginUsers(1);
        String s = "";
    }

}
