package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.overdueRecord.OverdueRecordBean;
import com.fuze.bcp.api.creditcar.service.IOverdueRecordBizService;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ${Liu} on 2018/3/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class OverdueRecordServiceTest {

    @Autowired
    IOverdueRecordBizService iOverdueRecordBizService;

    @Test
    public void test(){
        OverdueRecordBean overdueRecordBean = iOverdueRecordBizService.actSendRemindMsg("").getD();
        System.out.println(overdueRecordBean);
    }

    @Test
    public void test1(){
        OverdueRecordBean overdueRecord = new OverdueRecordBean();
        overdueRecord.setCustomerId("5a9cabaff98b5a0d787b7c23");
        overdueRecord.setBusinessTypeCode("OC");
        overdueRecord.setEmployeeId("5a24fba10d17f91af8734f75");
        overdueRecord.setLoginUserId("5a24fba10d17f91af8734f74");
        overdueRecord.setCustomerTransactionId("5a9cabaff98b5a0d787b7c24");
        overdueRecord.setApproveUserId("5a24fba10d17f91af8734f75");
        ResultBean<OverdueRecordBean> overdueRecordBeanResultBean = iOverdueRecordBizService.actSaveOverdueRecord(overdueRecord,"");
        System.out.println(overdueRecordBeanResultBean.getD());
    }

    @Test
    public void test2(){
        OverdueRecordBean d = iOverdueRecordBizService.actGetOverdueRecordByMonth("5ab35e9ff98b5a2f24f95c53", "2018-03-27").getD();
        System.out.println(d);
    }
}
