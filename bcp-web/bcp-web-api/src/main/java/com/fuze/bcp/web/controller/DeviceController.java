package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.sys.bean.DeviceUsageBean;
import com.fuze.bcp.api.sys.bean.TerminalBindBean;
import com.fuze.bcp.api.sys.bean.TerminalDeviceBean;
import com.fuze.bcp.api.sys.service.IDeviceBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/1.
 */
@RestController
@RequestMapping(value = "/json")
public class DeviceController {

    @Autowired
    private IDeviceBizService iDeviceBizService;

    /**
     * 获取终端设备列表(带分页)
     *
     * @param currentPage
     * @return terminaldevices
     */
    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTerminalDevices(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage) {
        return iDeviceBizService.actGetTerminalDevices(currentPage);
    }

    /**
     * 保存终端设备
     *
     * @param terminalDevice
     * @return
     */
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveTerminalDevice(@RequestBody TerminalDeviceBean terminalDevice) {
        return iDeviceBizService.actSaveTerminalDevice(terminalDevice);
    }

    /**
     * 获取单个设备
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/device/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTerminalDevice(@PathVariable("id") String id) {
        return iDeviceBizService.actGetTerminalDevice(id);
    }

    /**
     * 删除终端设备
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/device/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteTerminalDevice(@PathVariable("id") String id) {
        return iDeviceBizService.actDeleteTerminalDevice(id);
    }

    /**
     * 绑定终端设备
     *
     * @param deviceId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/device/{deviceId}/bind/{userId}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean bindTerminalDevice(@PathVariable("deviceId") String deviceId, @PathVariable("userId") String userId) {
        return iDeviceBizService.actBindTerminalDevice(deviceId, userId);
    }

    /**
     * 解除绑定
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/device/unbind/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean unbindTerminalDevice(@PathVariable("id") String id) {
        return iDeviceBizService.actUnbindTerminalDevice(id);
    }

    /**
     * 领取/归还设备(service层通过状态值区分)
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/device/usage", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actReceiveTerminalDevice(@RequestBody DeviceUsageBean deviceUsage) {
        return iDeviceBizService.actReceiveTerminalDevice(deviceUsage);
    }


    /**
     * 锁定或解除绑定记录(前台赋值)
     *
     * @param terminalBindBean
     * @return
     */
    @RequestMapping(value = "/device/bind", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actLockTerminalBind(@RequestBody TerminalBindBean terminalBindBean) {
        return iDeviceBizService.actSaveTerminalBind(terminalBindBean);
    }


    /**
     * 获取设备绑定记录列表(带分页)
     *
     * @param currentPage
     * @return terminaldevices
     */
    @RequestMapping(value = "/devices/binds", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTerminalBinds(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage) {
        return iDeviceBizService.actGetTerminalBinds(currentPage);
    }

    /**
     * 绑定记录模糊查询
     *
     * @param
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/devices/binds/search/{username}", method = RequestMethod.GET)
    public ResultBean searchTerminalBinds(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage, @PathVariable("username") String username) {
        return iDeviceBizService.actSearchTerminalBinds(currentPage, username);
    }


}
