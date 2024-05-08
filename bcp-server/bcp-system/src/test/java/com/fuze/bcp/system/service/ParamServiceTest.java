package com.fuze.bcp.system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.api.sys.bean.SysParamBean;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import static org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.data;

/**
 * Created by CJ on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class ParamServiceTest {

    @Autowired
    IParamBizService iParamService;

    @Test
    public void testSaveSysParam() {
        SysParamBean sysParamBean = new SysParamBean();
        sysParamBean.setCanExtends(false);
        sysParamBean.setParameterValue("value1");
        ResultBean<SysParamBean> data = iParamService.actSaveSysParam(sysParamBean);
        System.out.println(data);
    }

    @Test
    public void testDeleteSysParam() {
        SysParamBean sysParamBean = new SysParamBean();
        sysParamBean.setId("593fbd6363a6e824c421274d");
        ResultBean<SysParamBean> data = iParamService.actDeleteSysParam("593fbd6363a6e824c421274d");
        System.out.println(data);
    }

    @Test
    public void testGetSysParam() {
        ResultBean<SysParamBean> data = iParamService.actGetSysParam("593fbd6363a6e824c421274d");
        System.out.println(data);
    }

    @Test
    public void testGetSysParams() {
        ResultBean<DataPageBean<SysParamBean>> data = iParamService.actGetSysParams(0);
        System.out.println(data);
    }

    @Test
    public void testLookupParams() {
        ResultBean<List<APILookupBean>> data = iParamService.actLookupParams();
        System.out.println(data);
    }

    @Test
    public void testAccount() throws Exception{
        List<String> d = iParamService.actGetParamByCode("DECOMPRESS_TYPE").getD();
        System.out.println(d);
    }

    @Test
    public void testAccount5() throws Exception{
        Map<?, ?> decompress_type = iParamService.actGetMap("DECOMPRESS_TYPE").getD();
        System.out.println(decompress_type);
    }


    @Test
    public void testAccount333() throws Exception{
        String decompress_type = iParamService.actGetString("BANK_CHARGE_PAYMENTWAY").getD();
        System.out.println(decompress_type);
    }

    @Test
    public void testAccount4() throws Exception{
        String type = iParamService.actGetString("DECOMPRESS_TYPE").getD();
        List<String> strsToList1= Arrays.asList(type);
        System.out.println("type:"+type);
        System.out.println(strsToList1);
    }


}
