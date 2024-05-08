package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.service.IDeclarationBizService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by CJ on 2017/9/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class DeclarationServiceTest {

    @Autowired
    IDeclarationBizService iDeclarationBizService;

    @Test
    public void testActSubmitDeclaration(){

        iDeclarationBizService.actSubmitDeclaration("59c0dba60820154d08707581", "");

    }

}
