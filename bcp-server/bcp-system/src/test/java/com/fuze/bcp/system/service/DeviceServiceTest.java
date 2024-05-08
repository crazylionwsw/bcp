package com.fuze.bcp.system.service;

import com.fuze.bcp.api.sys.bean.TerminalBindBean;
import com.fuze.bcp.api.sys.bean.TerminalDeviceBean;
import com.fuze.bcp.api.sys.service.IDeviceBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by CJ on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class DeviceServiceTest {

    @Autowired
    IDeviceBizService iDeviceService;

    @Test
    public void testSaveTerminalDevice() {
        TerminalDeviceBean terminalDevice = new TerminalDeviceBean();
        terminalDevice.setDeviceName("testname");
        terminalDevice.setDeviceType("testtype");
        terminalDevice.setEmployeeId("testemp");
        terminalDevice.setIdentify("ide");
        terminalDevice.setSerialNum("serial");
        terminalDevice.setMac("mac");
        ResultBean<TerminalDeviceBean> data = iDeviceService.actSaveTerminalDevice(terminalDevice);
        System.out.println(data);
    }

//    @Test
//    public void testDeleteTerminalDevice() {
//        iDeviceService.actDeleteTerminalDevice()
//    }

    @Test
    public void testGetTerminalDevice() {
        ResultBean<TerminalDeviceBean> data = iDeviceService.actGetTerminalDevice("593fab6663a6e830a8520b2e");
        System.out.println(data);
    }

    @Test
    public void testGetTerminalDevices() {
        ResultBean<DataPageBean<TerminalDeviceBean>> data = iDeviceService.actGetTerminalDevices(0);
        System.out.println(data);
    }

    @Test
    public void testBindTerminalDevice() {
        TerminalDeviceBean data = iDeviceService.actGetTerminalDevice("5959fbfe6ba1c421fc8d44d1").getD();
        ResultBean data1 = iDeviceService.actBindTerminalDevice("591fadc4e4b0ecb022b3b9d4", data.getId());
        System.out.println(data1);
    }

    @Test
    public void testUnbindTerminalDevice() {
        ResultBean<TerminalBindBean> data = iDeviceService.actUnbindTerminalDevice("593fb69363a6e806347b11db");
        System.out.println(data);
    }

    @Test
    public void testLockTerminalBind() {
        ResultBean<TerminalBindBean> data = iDeviceService.actLockTerminalBind("593fb7aa63a6e81480b74daa");
        System.out.println(data);
    }

    @Test
    public void testReceiveTerminalDevice() {
        //ResultBean data = iDeviceService.actReceiveTerminalDevice("employeeId", "deviceId");
        //System.out.println(data);
    }

    @Test
    public void test() {
        ResultBean data = iDeviceService.actReturnTerminalDevice("employeeId", "deviceId");
        System.out.println(data);
    }
}
