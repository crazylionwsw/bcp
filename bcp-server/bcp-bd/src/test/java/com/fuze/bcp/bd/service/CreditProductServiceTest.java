package com.fuze.bcp.bd.service;

import com.fuze.bcp.api.bd.bean.CompensatoryPolicyBean;
import com.fuze.bcp.api.bd.bean.CreditProductBean;
import com.fuze.bcp.api.bd.service.IProductBizService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/6/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CreditProductServiceTest {

    @Autowired
    IProductBizService iProductService;


    @Test
    public void testSaveTest() {
        CompensatoryPolicyBean compensatoryPolicy = new CompensatoryPolicyBean();
        iProductService.actSaveCompensatoryPolicy(compensatoryPolicy);
    }


    @Test
    public void testGetCreditProducts() {
        ResultBean<DataPageBean<CreditProductBean>> datas = iProductService.actGetCreditProducts(0);

        System.out.println(datas);
    }

    @Test
    public void testLookupCreditProducts() {

        ResultBean<List<APILookupBean>> datas = iProductService.actLookupCreditProducts();

        System.out.println(datas);
    }

    @Test
    public void testSaveCreditProducts() {

        CreditProductBean creditProductBean = new CreditProductBean();
        creditProductBean.setName("test1");
        creditProductBean.setCashSourceId("dsd");
        ResultBean<CreditProductBean> datas = iProductService.actSaveCreditProduct(creditProductBean);
        System.out.println(datas);
    }

    @Test
    public void testDelCreditProducts() {
        CreditProductBean creditProductBean = new CreditProductBean();
        creditProductBean.setId("593e398463a6e828c44f4051");
        ResultBean<CreditProductBean> datas = iProductService.actDeleteCreditProduct(creditProductBean.getId());
        System.out.println(datas);

    }


}
