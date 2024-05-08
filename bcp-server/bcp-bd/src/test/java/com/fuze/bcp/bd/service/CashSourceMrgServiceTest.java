package com.fuze.bcp.bd.service;

import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.CashSourceEmployeeBean;
import com.fuze.bcp.api.bd.bean.SourceRateBean;
import com.fuze.bcp.api.bd.bean.SourceRateLookupBean;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.bean.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CashSourceMrgServiceTest {

    @Autowired
    ICashSourceBizService iCashSourceMrgService;


    @Test
    public void testActSaveCashSource() {

        CashSourceBean cashSourceBean = new CashSourceBean();
        cashSourceBean.setName("test1");
        cashSourceBean.setAddress("address");
        cashSourceBean.setCell("cell");
        cashSourceBean.setContacts("contact");
        cashSourceBean.setSourceType(2);
        cashSourceBean.setParentId("589ad090acd23edf388a0cf8");
        List list = new ArrayList<>();
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        //cashSourceBean.setPackageImageTypeIds(list);
        ResultBean<CashSourceBean> data = iCashSourceMrgService.actSaveCashSource(cashSourceBean);
        System.out.println(data);
    }

    @Test
    public void actDeleteCashSource() {
        CashSourceBean cashSourceBean = new CashSourceBean();
        cashSourceBean.setId("5940b4b71639be1e346ffa47");
        ResultBean<CashSourceBean> cashSourceBeanResultBean = iCashSourceMrgService.actDeleteCashSource(cashSourceBean.getId());
        System.out.println(cashSourceBeanResultBean);

    }

    @Test
    public void actLookupCashSource() {
        ResultBean<List<APITreeLookupBean>> cashSourceBeanResultBean = iCashSourceMrgService.actLookupCashSources();
        System.out.println(cashSourceBeanResultBean);
    }

    @Test
    public void actGetCashSources() {
        ResultBean<DataPageBean<CashSourceBean>> data = iCashSourceMrgService.actGetCashSources(0);
        System.out.println(data);
    }

    @Test
    public void testGetCashSources() {
        ResultBean<List<CashSourceBean>> cashSources = iCashSourceMrgService.actGetCashSources("589ad090acd23edf388a0cf8");
        System.out.println(cashSources);
    }

    @Test
    public void actGetOneCashSource() {
        ResultBean<CashSourceBean> data = iCashSourceMrgService.actGetCashSource("5940b4b71639be1e346ffa47");
        System.out.println(data);
    }

    @Test
    public void actSaveCashSourceEmployee() {
        CashSourceEmployeeBean cashSourceEmployeeBean = new CashSourceEmployeeBean();
        cashSourceEmployeeBean.setCashSourceId("589ad05bacd23edf388a0cf6");
        cashSourceEmployeeBean.setCell("cell");
        cashSourceEmployeeBean.setAvatarFileId("avatar");
        cashSourceEmployeeBean.setBirthday("birthday");
        cashSourceEmployeeBean.setEmail("email");
        cashSourceEmployeeBean.setEmployeeNo("010");
        cashSourceEmployeeBean.setIdentifyNo("idNo");
        cashSourceEmployeeBean.setUsername("userName");
        cashSourceEmployeeBean.setWxOpenid("openId");
        cashSourceEmployeeBean.setLoginUserId("loginId");
        cashSourceEmployeeBean.setOrgInfoId("orgId");
        ResultBean<CashSourceEmployeeBean> data = iCashSourceMrgService.actSaveCashSourceEmployee(cashSourceEmployeeBean);
        System.out.println(data);
    }

    @Test
    public void actDeleteCashSourceEmployee() {
        CashSourceEmployeeBean cashSourceEmployeeBean = new CashSourceEmployeeBean();
        cashSourceEmployeeBean.setId("5940c74e1639be18ccf43261");
        ResultBean<CashSourceEmployeeBean> data = iCashSourceMrgService.actDeleteCashSourceEmployee(cashSourceEmployeeBean.getId());
        System.out.println(data);
    }

    @Test
    public void actLookupCashSourceEmployee() {
        ResultBean<List<APILookupBean>> data = iCashSourceMrgService.actLookupCashSourceEmployees();
        System.out.println(data);
    }

    @Test
    public void actGetCashSourceEmployeeBeans() {
        ResultBean<DataPageBean<CashSourceEmployeeBean>> data = iCashSourceMrgService.actGetCashSourceEmployees(0);
        System.out.println(data);
    }

    @Test
    public void actGetOneCashSourceEmployee() {
        ResultBean<CashSourceEmployeeBean> data = iCashSourceMrgService.actGetCashSourceEmployee("5940c74e1639be18ccf43261");
        System.out.println(data);
    }

    @Test
    public void tesGetCashSourceEmployees() {
        ResultBean<List<CashSourceEmployeeBean>> data = iCashSourceMrgService.actGetCashSourceEmployees("5940c74e1639be18ccf43261");
        System.out.println(data);
    }

    @Test
    public void actSaveSourceRate() {
        SourceRateBean sourceRate = new SourceRateBean();
        sourceRate.setName("name");
        sourceRate.setAmount(12.0);
        sourceRate.setCashSourceId("5940b4b71639be1e346ffa47");
        sourceRate.setEndDate("endTime");
        List list = new ArrayList<>();
        list.add("abc");
        list.add("abc");
        sourceRate.setLimits(list);
        List list2 = new ArrayList<>();
        list2.add("ccc");
        list2.add("ccc");
        sourceRate.setRepaymentWayIds(list2);
        PayAccount account = new PayAccount();
        account.setAccountName("test");
        sourceRate.setPayAccount(account);
        List list3 = new ArrayList<>();
        RateType rateType = new RateType();
        rateType.setMonths(3);
        RateType rateType2 = new RateType();
        rateType.setMonths(4);
        list3.add(rateType);
        list3.add(rateType2);
        sourceRate.setRateTypes(list3);
        sourceRate.setStartDate("startDate");
        ResultBean<SourceRateBean> data = iCashSourceMrgService.actSaveSourceRate(sourceRate);
        System.out.println(data);

    }

    @Test
    public void actDeleteSourceRate() {
        SourceRateBean sourceRateBean = new SourceRateBean();
        sourceRateBean.setId("5940cbca1639be2b90e64f9b");
        ResultBean<SourceRateBean> data = iCashSourceMrgService.actDeleteSourceRate(sourceRateBean.getId());
        System.out.println(data);
    }

    @Test
    public void actLookupSourceRates() {
        ResultBean<List<SourceRateLookupBean>> data = iCashSourceMrgService.actLookupSourceRates();
        System.out.println(data);
    }

    @Test
    public void actGetSourceRates() {
        ResultBean<DataPageBean<SourceRateBean>> data = iCashSourceMrgService.actGetSourceRates(0);
        System.out.println(data);
    }

    @Test
    public void actGetOneSourceRate() {
        ResultBean<SourceRateBean> data = iCashSourceMrgService.actGetSourceRate("5940cbca1639be2b90e64f9b");
        System.out.println(data);
    }

    @Test
    public void testGetSourceRates() {
        ResultBean<List<SourceRateBean>> data = iCashSourceMrgService.actGetSourceRates("58ac71bc2d36325a48db6fa3");
        System.out.println(data);
    }

}
