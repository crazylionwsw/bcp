package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.BusinessExcelBean;
import com.fuze.bcp.api.creditcar.bean.CompensatoryExcelBean;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by ${Liu} on 2018/1/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CustomerTransactionTest {

    @Autowired
    ICustomerTransactionBizService iCustomerTransactionBizService;

    @Test
    public void testActExport(){
        SearchBean searchBean = new SearchBean();
        searchBean.setCompensatory(true);
        searchBean.setBusiness(false);
        searchBean.setNc(false);
        searchBean.setOc(false);
        searchBean.setSwipingCardTime("2018-01");
        //ResultBean<List<CompensatoryExcelBean>> listResultBean = iCustomerTransactionBizService.actExportCompensatoryTransactions(searchBean);
        //System.out.println("result:"+listResultBean.getD());
    }

    @Test
    public void testBusiness(){
        SearchBean searchBean = new SearchBean();
        searchBean.setCompensatory(false);
        searchBean.setBusiness(true);
        searchBean.setNc(false);
        searchBean.setOc(false);
        searchBean.setSwipingCardTime("2018-01");
        //List<BusinessExcelBean> businessExcelBeen = iCustomerTransactionBizService.actExportBusinessTransactions(searchBean).getD();
        //System.out.println(businessExcelBeen);
    }
}
