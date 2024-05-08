package com.fuze.bcp.api.sys.service;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.sys.bean.DeviceUsageBean;
import com.fuze.bcp.api.sys.bean.TerminalBindBean;
import com.fuze.bcp.api.sys.bean.TerminalDeviceBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设备管理
 */
public interface IDeviceBizService {
    /**
     * 获取单个设备信息
     *
     * @param deviceId
     * @return
     */
    ResultBean<TerminalDeviceBean> actGetTerminalDevice(@NotNull String deviceId);

    /**
     * 保存终端设备
     *
     * @param terminalDevice
     * @return
     */
    ResultBean<TerminalDeviceBean> actSaveTerminalDevice(TerminalDeviceBean terminalDevice);

    /**
     * 获取终端设备列表（有分页）
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<TerminalDeviceBean>> actGetTerminalDevices(@NotNull @Min(0) Integer currentPage);

    /**
     * 删除终端设备
     *
     * @return
     */
    ResultBean<TerminalDeviceBean> actDeleteTerminalDevice(@NotNull String deviceId);

    /**
     * 绑定终端设备
     *
     * @param userId
     * @param deviceId
     * @return
     */
    ResultBean actBindTerminalDevice(@NotNull String userId, @NotNull String deviceId);

    /**
     * 保存设备并绑定到用户
     * @param userId
     * @param terminalDevice
     * @return
     */
    ResultBean actBindTerminalDevice(@NotNull String userId, TerminalDeviceBean terminalDevice);

    /**
     * 根据设备识别号查找设备绑定信息
     * @param identify
     * @return
     */
    ResultBean<TerminalBindBean> actGetTerminalBind(String identify);

    /**
     * 解绑
     *
     * @param bindId
     * @return
     */
    ResultBean actUnbindTerminalDevice(@NotNull String bindId);

    /**
     * 领取设备
     * @param deviceUsage
     * @return
     */
    ResultBean actReceiveTerminalDevice(DeviceUsageBean deviceUsage);


    /**
     * 归还设备
     *
     * @param employeeId
     * @param deviceId
     * @return
     */
    ResultBean actReturnTerminalDevice(@NotNull String employeeId, @NotNull String deviceId);

    /**
     * 锁定绑定记录
     *
     * @param bindId
     * @return
     */
    ResultBean actLockTerminalBind(@NotNull String bindId);

    /**
     * 锁定绑定记录(前台赋值)
     * @param terminalBind
     * @return
     */
    ResultBean actLockTerminalBind(TerminalBindBean terminalBind);

    /**
     * 获取绑定记录(分页)
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<TerminalBindBean>> actGetTerminalBinds(@NotNull @Min(0) Integer currentPage);

    /**
     * 模糊查询绑定记录(带分页)
     * @param currentPage
     * @param username
     * @return
     */
    ResultBean<DataPageBean<TerminalBindBean>> actSearchTerminalBinds(@NotNull @Min(0) Integer currentPage,String username);

    /**
     * 获取绑定记录(所有数据)
     * @return
     */
    ResultBean<List<TerminalBindBean>> actGetTerminalBinds();


    /**
     * 保存绑定记录
     * @param
     * @return
     */
    ResultBean<TerminalBindBean> actSaveTerminalBind(TerminalBindBean terminalBind);

    /**
     * 删除绑定记录
     * @param id
     * @return
     */
    ResultBean<TerminalBindBean> actDeleteTerminalBind(String id);
}
