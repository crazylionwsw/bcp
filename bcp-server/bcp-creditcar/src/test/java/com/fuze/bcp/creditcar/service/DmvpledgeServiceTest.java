package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.api.creditcar.service.IDmvpledgeBizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lily on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class DmvpledgeServiceTest {

    @Autowired
    IDmvpledgeBizService iDmvpledgeBizService;

    @Test
    public void commitBanCardApply(){

        iDmvpledgeBizService.actCreateDMVPledge("5a0eacb87cc57e47c4c4cbd7");
    }

}
