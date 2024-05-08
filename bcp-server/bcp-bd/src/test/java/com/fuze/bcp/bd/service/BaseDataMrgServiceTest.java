package com.fuze.bcp.bd.service;

import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.IValidateBizService;
import com.fuze.bcp.bean.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by CJ on 2017/6/9.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class BaseDataMrgServiceTest {

    @Autowired
    IBaseDataBizService iBaseDataMrgService;

    @Test
    public void testGetBillTypes() {


        ResultBean<List<CustomerImageTypeBean>> bills = iBaseDataMrgService.actGetCustomerImageTypes("NC", "A002");

        System.out.println(bills);
    }


    @Test
    public void testSaveBillType() {

        BillTypeBean billType = new BillTypeBean();


        billType.setName("测试数据");

        billType.setDataStatus(DataStatus.SAVE);

        ResultBean<BillTypeBean> bills = iBaseDataMrgService.actSaveBillType(billType);

        System.out.println(bills);
    }

    @Test
    public void testActDelBillTypes() {

        BillTypeBean billType = new BillTypeBean();

        billType.setId("593a710b63a6e81af83bdd63");

        ResultBean<BillTypeBean> bills = iBaseDataMrgService.actDeleteBillType("593a710b63a6e81af83bdd63");

        System.out.println(bills);
    }

    @Test
    public void testActLookupBillTypes() {

        ResultBean<List<APILookupBean>> bills = iBaseDataMrgService.actLookupBillTypes();

        System.out.println(bills);
    }

    @Test
    public void testSaveBusinessType() {
        BusinessTypeBean b = iBaseDataMrgService.actGetBusinessType("58c75860e4b0d35997f3d42f").getD();
        RateType r1 = new RateType();
        r1.setMonths(12);
        r1.setRatio(0.5);
        RateType r2 = new RateType();
        r2.setMonths(6);
        r2.setRatio(0.35);
        List<RateType> list = new ArrayList<>();
        list.add(r1);
        list.add(r2);
        Map<String, List<RateType>> map = new HashMap<>();
        map.put("key", list);
        b.setFeeItems(map);
        ResultBean<BusinessTypeBean> data = iBaseDataMrgService.actSaveBusinessType(b);
        System.out.println(data);
    }


    @Test
    public void testGetAllBusinessType() {

        ResultBean<BusinessTypeBean> data = iBaseDataMrgService.actGetBusinessType("597588f28e9af8142ce109b2");

        System.out.println(data);
    }

    @Test
    public void testGetAllBusinessTypes() {

        ResultBean<DataPageBean<BusinessTypeBean>> datas = iBaseDataMrgService.actGetBusinessTypes(0);

        System.out.println(datas);
    }

    @Test
    public void testGetLookup() {

        ResultBean<List<APILookupBean>> datas = iBaseDataMrgService.actLookupBusinessTypes();

        System.out.println(datas);

    }

    @Test
    public void testDelBusinessType() {

        BusinessTypeBean businessTypeBean = new BusinessTypeBean();
        businessTypeBean.setId("5954bbe963a6e81c1054f265");
        ResultBean<BusinessTypeBean> businessType = iBaseDataMrgService.actDeleteBusinessType("5954bbe963a6e81c1054f265");

    }

    @Test
    public void testDelGuaranteeWay() {

        GuaranteeWayBean guaranteeWayBean = new GuaranteeWayBean();
        guaranteeWayBean.setId("593e125b63a6e827183ba21e");
        ResultBean<GuaranteeWayBean> guaranteeWay = iBaseDataMrgService.actDeleteGuaranteeWay("593e125b63a6e827183ba21e");

    }


    @Test
    public void testGetAllGuaranteeWay() {

        ResultBean<DataPageBean<GuaranteeWayBean>> datas = iBaseDataMrgService.actGetGuaranteeWays(0);
        System.out.println(datas);
    }

    @Test
    public void testSaveGuaranteeWay() {

        GuaranteeWayBean guaranteeWay = new GuaranteeWayBean();
        guaranteeWay.setName("test");
        ResultBean<GuaranteeWayBean> data = iBaseDataMrgService.actSaveGuaranteeWay(guaranteeWay);
        System.out.println(data);
    }

    @Test
    public void testLookupGuaranteeWay() {

        ResultBean<List<APILookupBean>> datas = iBaseDataMrgService.actLookupGuaranteeWays();
        System.out.println(datas);
    }

    @Test
    public void testGetRepaymentWays() {
        ResultBean<DataPageBean<RepaymentWayBean>> datas = iBaseDataMrgService.actGetRepaymentWays(0);
        System.out.println(datas);
    }

    @Test
    public void testSaveRepaymentWays() {

        RepaymentWayBean repaymentWayBean = new RepaymentWayBean();

        repaymentWayBean.setName("test1");

        ResultBean<RepaymentWayBean> datas = iBaseDataMrgService.actSaveRepaymentWay(repaymentWayBean);

        System.out.println(datas);
    }

    @Test
    public void testDelRepaymentWays() {
        RepaymentWayBean repaymentWayBean = new RepaymentWayBean();
        repaymentWayBean.setId("593e2c1f63a6e82f486d60b4");
        ResultBean<RepaymentWayBean> datas = iBaseDataMrgService.actDeleteRepaymentWay("593e2c1f63a6e82f486d60b4");
        System.out.println(datas);
    }

    @Test
    public void testGetCustomerImageTypes() {
        ResultBean<DataPageBean<CustomerImageTypeBean>> datas = iBaseDataMrgService.actGetCustomerImageTypes(0);

        System.out.println(datas);
    }


    @Test
    public void testLookupCustomerImageTypes() {
        ResultBean<List<APILookupBean>> datas = iBaseDataMrgService.actLookupCustomerImageTypes();

        System.out.println(datas);
    }

    @Test
    public void testSaveCustomerImageType() {

        CustomerImageTypeBean customerImageTypeBean = new CustomerImageTypeBean();

        customerImageTypeBean.setName("test1");
        customerImageTypeBean.setCode("code");
        customerImageTypeBean.setName("name");
        ResultBean<CustomerImageTypeBean> datas = iBaseDataMrgService.actSaveCustomerImageType(customerImageTypeBean);

        System.out.println(datas);
    }

    @Test
    public void testDeleteCustomerImageType() {
        CustomerImageTypeBean customerImageTypeBean = new CustomerImageTypeBean();

        customerImageTypeBean.setId("593e320c63a6e82cac61d89e");

        ResultBean<CustomerImageTypeBean> datas = iBaseDataMrgService.actDeleteCustomerImageType("593e320c63a6e82cac61d89e");

        System.out.println(datas);
    }


    @Test
    public void testGetFeeItems() {
        ResultBean<DataPageBean<FeeItemBean>> datas = iBaseDataMrgService.actGetFeeItems(0);
        System.out.println(datas);
    }


    @Test
    public void testLookupFeeItems() {
        ResultBean<List<APILookupBean>> datas = iBaseDataMrgService.actLookupFeeItems();
        System.out.println(datas);
    }

    @Test
    public void testSaveFeeItem() {

        FeeItemBean feeItemBean = new FeeItemBean();

        feeItemBean.setFeeOwner(1);
        feeItemBean.setFee(300.00);
        feeItemBean.setFormula("x+y");
        feeItemBean.setDecimalPoint(2);

        ResultBean<FeeItemBean> datas = iBaseDataMrgService.actSaveFeeItem(feeItemBean);

        System.out.println(datas);
    }

    @Test
    public void testDeleteFeeItem() {

        //FeeItemBean feeItemBean = new FeeItemBean();
        //feeItemBean.setId("594202ec4c9ea2014ce3b40b");
        String tid = "594202ec4c9ea2014ce3b40b";
        ResultBean<FeeItemBean> datas = iBaseDataMrgService.actDeleteFeeItem("594202ec4c9ea2014ce3b40b");
        System.out.println(datas);
    }

    @Test
    public void testGetProvinces() {
        ResultBean<DataPageBean<ProvinceBean>> datas = iBaseDataMrgService.actGetProvinces(0);
        System.out.println(datas);
    }

    @Test
    public void testGetProvince() {
        ResultBean<List<ProvinceBean>> datas = iBaseDataMrgService.actGetProvinces();
        System.out.println(datas);
    }

    @Test
    public void testGetChildProvinces() {
        ProvinceBean provinceBean = new ProvinceBean();
        provinceBean.setId("589a93bb9fa4528a2d25e63b");
        ResultBean<List<ProvinceBean>> datas = iBaseDataMrgService.actGetChildProvinces("589a93bb9fa4528a2d25e63b");
        System.out.println(datas);
    }

    @Test
    public void testLookupProvinces() {
        ResultBean<List<APITreeLookupBean>> datas = iBaseDataMrgService.actLookupProvinces();
        System.out.println(datas);
    }

    @Test
    public void testSaveProvince() {
        ProvinceBean provinceBean = new ProvinceBean();
        provinceBean.setParentId("0");
        ResultBean<ProvinceBean> datas = iBaseDataMrgService.actSaveProvince(provinceBean);
        System.out.println(datas);
    }

    @Test
    public void testDeleteProvince() {

        ProvinceBean provinceBean = new ProvinceBean();
        provinceBean.setId("5942583ee776ac3ff899b7e8");
        ResultBean<ProvinceBean> datas = iBaseDataMrgService.actDeleteProvince("5942583ee776ac3ff899b7e8");
        System.out.println(datas);
    }

    @Test
    public void testBusinessTypeRate() {
        ResultBean<List<BusinessTypeRateLookupBean>> datas = iBaseDataMrgService.actLookupBusinessRates();
        System.out.println(datas);
    }

    @Test
    public void testSaveBusinessEventType() {
        BusinessEventTypeBean b1 = new BusinessEventTypeBean();
        b1.setEventTypeCode("NC_A016_UploadCreditreport");
        iBaseDataMrgService.actSaveBusinessEventType(b1);
        BusinessEventTypeBean b2 = new BusinessEventTypeBean();
        b2.setEventTypeCode("NC_A001_SubmitCardemand");
        iBaseDataMrgService.actSaveBusinessEventType(b2);
        BusinessEventTypeBean b3 = new BusinessEventTypeBean();
        b3.setEventTypeCode("NC_A001_QueryCreditReport_Passed");
        iBaseDataMrgService.actSaveBusinessEventType(b3);
        BusinessEventTypeBean b4 = new BusinessEventTypeBean();
        b4.setEventTypeCode("NC_A001_QueryCreditReport_Reapply");
        iBaseDataMrgService.actSaveBusinessEventType(b4);
        BusinessEventTypeBean b5 = new BusinessEventTypeBean();
        b5.setEventTypeCode("NC_A001_QueryCreditReport_Reject");
        iBaseDataMrgService.actSaveBusinessEventType(b5);
        BusinessEventTypeBean b6 = new BusinessEventTypeBean();
        b6.setEventTypeCode("NC_A001_CheckReview_Passed");
        iBaseDataMrgService.actSaveBusinessEventType(b6);
        BusinessEventTypeBean b7 = new BusinessEventTypeBean();
        b7.setEventTypeCode("NC_A001_CheckReview_Reapply");
        iBaseDataMrgService.actSaveBusinessEventType(b7);
        BusinessEventTypeBean b8 = new BusinessEventTypeBean();
        b8.setEventTypeCode("NC_A001_CheckReview_Reject");
        iBaseDataMrgService.actSaveBusinessEventType(b8);
        BusinessEventTypeBean b9 = new BusinessEventTypeBean();
        b9.setEventTypeCode("NC_A001_CompleteCardemand");
        iBaseDataMrgService.actSaveBusinessEventType(b9);
    }

    @Test
    public void testGetBusinessEventTypes() {
        ResultBean data = iBaseDataMrgService.actGetBusinessEventTypes(0);
        System.out.println(data);
    }

    @Test
    public void testlookups() {
        ResultBean data = iBaseDataMrgService.actLookupBusinessEventTypes();
        System.out.println(data);
    }

    @Test
    public void testGetBusinessEventType() {
        ResultBean data = iBaseDataMrgService.actGetBusinessEventType("598c39a15832de3b14ffb993");
        System.out.println(data);
    }

    @Test
    public void testDeleteBusinessEventType() {
        ResultBean data = iBaseDataMrgService.actDeleteBusinessEventType("598c39a15832de3b14ffb993");
        System.out.println(data);
    }

    @Autowired
    IValidateBizService iValidateBizService;

    @Test
    public void testValidate() {
        BusinessEventTypeBean b = new BusinessEventTypeBean();
        String boo = iValidateBizService.actCheckUnique(b, "eventTypeCode", "NC_A016_UploadCreditreport").getD();
        System.out.println(boo);
    }

}
