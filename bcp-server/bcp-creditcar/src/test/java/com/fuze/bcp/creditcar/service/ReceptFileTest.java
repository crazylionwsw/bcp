package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.ReceptFileBean;
import com.fuze.bcp.api.creditcar.service.IReceptFileBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.ReceptFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Liu} on 2017/9/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class ReceptFileTest {

    @Autowired
    IReceptFileBizService iReceptFileBizService;

    @Test
    public void testReceptFile(){
        ReceptFileBean r = new ReceptFileBean();
        List<String> fa = new ArrayList<String>();
        fa.add("身份证");
        fa.add("户口呢么");
        r.setFileNames(fa);
        r.setCustomerTransactionId("123456789");

        ResultBean<ReceptFileBean> R  = iReceptFileBizService.actSaveReceptFile(r);
    }
}
