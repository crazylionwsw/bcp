package com.fuze.bcp.sys.business;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.sys.bean.DeviceUsageBean;
import com.fuze.bcp.api.sys.bean.TerminalBindBean;
import com.fuze.bcp.api.sys.bean.TerminalDeviceBean;
import com.fuze.bcp.api.sys.service.IDeviceBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.sys.domain.DeviceUsage;
import com.fuze.bcp.sys.domain.TerminalBind;
import com.fuze.bcp.sys.domain.TerminalDevice;
import com.fuze.bcp.sys.service.IDeviceUsageService;
import com.fuze.bcp.sys.service.ITerminalBindService;
import com.fuze.bcp.sys.service.ITerminalDeviceService;
import com.fuze.bcp.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class BizDeviceService implements IDeviceBizService {
    private static final Logger logger = LoggerFactory.getLogger(BizDeviceService.class);
    @Autowired
    MappingService mappingService;

    @Autowired
    ITerminalDeviceService iTerminalDeviceService;

    @Autowired
    ITerminalBindService iTerminalBindService;

    @Autowired
    IDeviceUsageService iDeviceUsageService;
    @Autowired

    IAuthenticationBizService iAuthenticationBizService;

    @Override
    public ResultBean<TerminalDeviceBean> actGetTerminalDevice(String deviceId) {
        TerminalDevice terminalDevice = iTerminalDeviceService.getOne(deviceId);
        if (terminalDevice != null) {
            return ResultBean.getSucceed().setD(mappingService.map(terminalDevice, TerminalDeviceBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<TerminalDeviceBean> actSaveTerminalDevice(TerminalDeviceBean terminalDevice) {
        TerminalDevice terminalDevice1 = iTerminalDeviceService.save(mappingService.map(terminalDevice, TerminalDevice.class));
        if (terminalDevice1 != null) {
            return ResultBean.getSucceed().setD(mappingService.map(terminalDevice1, TerminalDeviceBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<TerminalDeviceBean>> actGetTerminalDevices(Integer currentPage) {
        Page<TerminalDevice> terminalDevices = iTerminalDeviceService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(terminalDevices, TerminalDeviceBean.class));
    }

    @Override
    public ResultBean<TerminalDeviceBean> actDeleteTerminalDevice(String deviceId) {
        TerminalDevice terminalDevice = iTerminalDeviceService.getOne(deviceId);
        if (terminalDevice != null) {
            terminalDevice = iTerminalDeviceService.delete(deviceId);
            return ResultBean.getSucceed().setD(mappingService.map(terminalDevice, TerminalDeviceBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actBindTerminalDevice(String userId, String deviceId) {
        TerminalDevice terminalDevice = iTerminalDeviceService.getOne(deviceId);
        if (terminalDevice == null) {
            return ResultBean.getFailed().setM("SYS_DEVICE_NOT_EXIST");
        }

        TerminalBind terminalBind = iTerminalBindService.getOneByDataStatusAndLoginUserId(userId);
        if (terminalBind != null) { // 用户已绑定
            return ResultBean.getFailed().setM("SYS_USER_BOUND");
        }
        terminalBind = iTerminalBindService.getOneByDataStatusAndTerminalDevice(deviceId);
        if (terminalBind != null) { // 设备已绑定
            return ResultBean.getFailed().setM("SYS_DEVICE_BOUND");
        }
        terminalBind = new TerminalBind();
        terminalBind.setTerminalDeviceId(deviceId);
        terminalBind.setLoginUserId(userId);
        terminalBind.setDataStatus(DataStatus.SAVE);
        terminalBind.setBindtime(DateTimeUtils.getCreateTime());

        terminalBind = iTerminalBindService.save(terminalBind);

        if (terminalBind != null) {
            return ResultBean.getSucceed().setD(mappingService.map(terminalBind, TerminalBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actBindTerminalDevice(@NotNull String userId, TerminalDeviceBean terminalDevice) {
        //查看设备是否保存
        TerminalDevice exist = iTerminalDeviceService.getOneByDataStatusAndIdentify(terminalDevice.getIdentify());
        TerminalDevice terminalDevice1;

        if (exist == null) {
            //保存设备
            terminalDevice1 = mappingService.map(terminalDevice, TerminalDevice.class);
            terminalDevice1 = iTerminalDeviceService.save(terminalDevice1);
        } else {
            terminalDevice1 = exist;
        }

        //绑定设备
        return actBindTerminalDevice(userId, terminalDevice1.getId());
    }

    @Override
    public ResultBean actGetTerminalBind(String identify) {

        TerminalDevice device = iTerminalDeviceService.getOneByDataStatusAndIdentify(identify);
        if (device == null) {
            return ResultBean.getSucceed().setD(null);
        } else {
            TerminalBind terminalBind = iTerminalBindService.getOneByDataStatusAndTerminalDevice(device.getId());
            if (terminalBind == null) {
                return ResultBean.getSucceed().setD(null);
            } else {
                return ResultBean.getSucceed().setD(mappingService.map(terminalBind, TerminalBindBean.class));
            }
        }
    }

    @Override
    public ResultBean<TerminalBindBean> actUnbindTerminalDevice(String bindId) {
        TerminalBind terminalBind = iTerminalBindService.getOne(bindId);
        if (terminalBind != null) {
            terminalBind = iTerminalBindService.delete(bindId);
            if (terminalBind != null) {
                return ResultBean.getSucceed().setD(mappingService.map(terminalBind, TerminalBindBean.class));
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actReceiveTerminalDevice(DeviceUsageBean deviceUsage) {
        TerminalDevice terminalDevice = iTerminalDeviceService.getOne(deviceUsage.getDeviceId());
        if (deviceUsage.getUsageType() == 1) {
            terminalDevice.setEmployeeId(deviceUsage.getEmployeeId());
        } else {
            terminalDevice.setEmployeeId(null);
        }
        iTerminalDeviceService.save(terminalDevice);
        if (deviceUsage != null) {
            return ResultBean.getSucceed().setD(mappingService.map(deviceUsage, DeviceUsageBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actReturnTerminalDevice(String employeeId, String deviceId) {
        DeviceUsage deviceUsage = iDeviceUsageService.getOneByDataStatusAndDeviceIdAndEmployeeId(deviceId, employeeId);
        if (deviceUsage != null) {
            if (DeviceUsage.DEVICE_RECEIVE == deviceUsage.getUsageType()) {
                deviceUsage.setUsageType(DeviceUsage.DEVICE_RETURN);
                deviceUsage = iDeviceUsageService.save(deviceUsage);
                if (deviceUsage != null) {
                    return ResultBean.getSucceed().setD(mappingService.map(deviceUsage, DeviceUsageBean.class));
                }
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actLockTerminalBind(String bindId) {

        TerminalBind terminalBind = iTerminalBindService.getOne(bindId);
        if (terminalBind != null) {
            terminalBind.setDataStatus(DataStatus.LOCK);
            terminalBind = iTerminalBindService.save(terminalBind);
            if (terminalBind != null) {
                return ResultBean.getSucceed().setD(mappingService.map(terminalBind, TerminalBindBean.class));
            }
        }

        return ResultBean.getFailed();
    }

    @Override
    public ResultBean actLockTerminalBind(TerminalBindBean terminalBind) {
        TerminalBind terminalBind1 = iTerminalBindService.save(mappingService.map(terminalBind, TerminalBind.class));
        if (terminalBind1 != null) {
            return ResultBean.getSucceed().setD(mappingService.map(terminalBind1, TerminalBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<TerminalBindBean>> actGetTerminalBinds(Integer currentPage) {
        Page<TerminalBind> terminalBinds = iTerminalBindService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(terminalBinds, TerminalBindBean.class));
    }

    @Override
    public ResultBean<DataPageBean<TerminalBindBean>> actSearchTerminalBinds(Integer currentPage, String username) {
        List<LoginUserBean> loginUsers = iAuthenticationBizService.actSearchByUsername(username).getD();
        List<String> userIds = new ArrayList<String>();
        for (LoginUserBean u : loginUsers) {
            userIds.add(u.getId());
        }

        Page<TerminalBind> terminalBinds = iTerminalBindService.getTerminalBindByIds(currentPage, userIds);
        return ResultBean.getSucceed().setD(mappingService.map(terminalBinds, TerminalBindBean.class));
    }

    @Override
    public ResultBean<List<TerminalBindBean>> actGetTerminalBinds() {
        List<TerminalBind> terminalBinds = iTerminalBindService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(terminalBinds, TerminalBindBean.class));
    }

    @Override
    public ResultBean<TerminalBindBean> actSaveTerminalBind(TerminalBindBean terminalBind) {
        TerminalBind terminalBind1 = iTerminalBindService.save(mappingService.map(terminalBind, TerminalBind.class));
        if (terminalBind1 != null) {
            return ResultBean.getSucceed().setD(mappingService.map(terminalBind1, TerminalBindBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<TerminalBindBean> actDeleteTerminalBind(String id) {
        TerminalBind terminalBind = iTerminalBindService.getOne(id);
        if (terminalBind != null) {
            terminalBind = iTerminalBindService.delete(id);
            if (terminalBind != null) {
                return ResultBean.getSucceed().setD(mappingService.map(terminalBind, TerminalBindBean.class));
            }
        }
        return ResultBean.getFailed();
    }
}
