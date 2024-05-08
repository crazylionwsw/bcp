package com.fuze.bcp.bd.service;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.bean.EmployeeUserBean;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class OrgMrgServiceTest {

    @Autowired
    IOrgBizService iOrgMrgService;


    @Test
    public void actSaveEmployee() { //5941019e1639be09a44a4f3d
        EmployeeUserBean employeeBean = new EmployeeUserBean();
        employeeBean.setUsername("testName");
        employeeBean.setIdentifyNo("idNo");
        employeeBean.setOrgInfoId("orgId");
        employeeBean.setLoginUserId("loginId");
        employeeBean.setAvatarFileId("avatar");
       // ResultBean<EmployeeUserBean> data = iOrgMrgService.actSaveEmployee(employeeBean);
       // System.out.println(data);
    }

    @Test
    public void actDeleteEmployee() {
        ResultBean<EmployeeBean> data = iOrgMrgService.actDeleteEmployee("5941019e1639be09a44a4f3d");
        System.out.println(data);
    }

    @Test
    public void actGetOneEmployee() {
        ResultBean<EmployeeBean> data = iOrgMrgService.actGetEmployee("58abf593722eb95f603005e7");
        System.out.println(data);
    }

    @Test
    public void actLookupEmployees() {
        ResultBean<List<EmployeeLookupBean>> data = iOrgMrgService.actLookupEmployees();
        System.out.println(data);
    }

    @Test
    public void actGetEmployees() {
        ResultBean<DataPageBean<EmployeeBean>> data = iOrgMrgService.actGetEmployees(0);
        System.out.println(data);
    }

    @Test
    public void testGetEmployees() {
        ResultBean<DataPageBean<EmployeeBean>> data = iOrgMrgService.actGetEmployees(0, "58a6b358e4b06e6e32453417");
        System.out.println(data);
    }

    @Test
    public void actSaveOrgBean() { //5941035a1639be09a44a4f3e
        OrgBean orgBean = new OrgBean();
        orgBean.setVirtual(false);
        ResultBean<OrgBean> data = iOrgMrgService.actSaveOrg(orgBean);
        System.out.println(data);
    }

    @Test
    public void actDeleteOrgBean() {
        ResultBean<OrgBean> data = iOrgMrgService.actDeleteOrg("5941035a1639be09a44a4f3e");
        System.out.println(data);
    }

    @Test
    public void actGetOneOrgInfo() {
        ResultBean<OrgBean> data = iOrgMrgService.actGetOrg("58a6b358e4b06e6e32453417");
        System.out.println(data);
    }

    @Test
    public void actLookupOrgs() {
        ResultBean<List<APITreeLookupBean>> data = iOrgMrgService.actLookupOrgs();
        System.out.println(data);
    }

    @Test
    public void actGetOrgs() {
        ResultBean<DataPageBean<OrgBean>> datas = iOrgMrgService.actGetOrgs(0);
        System.out.println(datas);
    }

    @Test
    public void testGetOrgs() {
        ResultBean<List<OrgBean>> data = iOrgMrgService.actGetOrgs("58a6b358e4b06e6e32453417");
        System.out.println(data);
    }

    @Test
    public void testGetEmployees1(){
        ResultBean<List<EmployeeBean>> listResultBean = iOrgMrgService.actGetOrgEmployees("589d8298e4b043db09873dc8");
        System.out.println(listResultBean);
    }

    @Test
    public void tesGetEmployeManager(){
        ResultBean<List<EmployeeBean>> listResultBean = iOrgMrgService.actGetEmployeeManager("58ad18dee4b000431c11e921");
        System.out.println(listResultBean.getD());
    }

    @Test
    public void testTime(){
         double d = 114.0;
         DecimalFormat df = new DecimalFormat("#");
         String str = df.format(d);
         System.out.println(str);
    }

    @Test
    public void testTime1(){
        String money = "4200.456";
        BigDecimal bd = new BigDecimal(money);
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        System.out.println("*************"+bd.toString());
    }




    @Test
    public void getTimeTrans(){
        String data = "2017-12-12 17:17:15.226";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = f.format(data);
        System.out.println(dateString);
    }

    @Test
    public void strToDateLong() {
        String strDate = "2017-12-12 17:17:15.226";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataString = formatter1.format(strtodate);
        System.out.println("*-*-*-*-*-*-*-*-*--*-*-*"+dataString);
    }

    @Test
    public void test(){
        EmployeeBean employeeBean = iOrgMrgService.actGetEmployeeByWXOpenId("").getD();
        System.out.println(employeeBean);
    }


}
