package com.fuze.bcp.system.service;

import com.fuze.bcp.api.sys.bean.APKReleaseBean;
import com.fuze.bcp.api.sys.bean.APKUpgradeLogBean;
import com.fuze.bcp.api.sys.service.IAPKBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by CJ on 2017/6/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class APKServiceTest {

    @Autowired
    IAPKBizService iapkService;

    @Test
    public void testGetCreditProducts() {
        ResultBean<DataPageBean<APKReleaseBean>> data = iapkService.actGetAPKReleases(0, 2, "ts", false);
        System.out.println(data);
    }

    @Test
    public void testSaveAPKRelease() {
        APKReleaseBean apkReleaseBean = new APKReleaseBean();
        apkReleaseBean.setAppName("test1");
        ResultBean<APKReleaseBean> data = iapkService.actSaveAPKRelease(apkReleaseBean);
        System.out.println(data);

        APKReleaseBean apkReleaseBean1 = iapkService.actGetLatestAPKRelease().getD();

        System.out.println(data);
    }

    @Test
    public void testDeleteAPKRelease() {
        APKReleaseBean apkReleaseBean = new APKReleaseBean();
        apkReleaseBean.setId("593f9c9563a6e8262c3f96c9");
        ResultBean<APKReleaseBean> data = iapkService.actDeleteAPKRelease("593f9c9563a6e8262c3f96c9");
        System.out.println(data);
    }

    @Test
    public void testGetAPKUpgradeLogs() {
        ResultBean<DataPageBean<APKUpgradeLogBean>> data = iapkService.actGetAPKUpgradeLogs(0);
        System.out.println(data);
    }

    @Test
    public void testSaveAPKUpgradeLog() {
        APKUpgradeLogBean apkUpgradeLogBean = new APKUpgradeLogBean();
        apkUpgradeLogBean.setPackageName("testPackage");
        apkUpgradeLogBean.setAppName("testApp");
        apkUpgradeLogBean.setVersionCode(10);
        apkUpgradeLogBean.setVersionName("testVersion");
        apkUpgradeLogBean.setTerminalBindId("dswesaq");
        apkUpgradeLogBean.setLoginUserId("testLoginId");
        ResultBean<APKUpgradeLogBean> data = iapkService.actSaveAPKUpgradeLog(apkUpgradeLogBean);
        System.out.println(data);
    }

    @Test
    public void testDeleteAPKUpgradeLogBean() {
        APKUpgradeLogBean apkUpgradeLogBean = new APKUpgradeLogBean();
        apkUpgradeLogBean.setId("593fa6c363a6e82e2c2fd088");
        ResultBean<APKUpgradeLogBean> data = iapkService.actDeleteAPKUpgradeLog("593f9c9563a6e8262c3f96c9");
        System.out.println(data);
    }

}
