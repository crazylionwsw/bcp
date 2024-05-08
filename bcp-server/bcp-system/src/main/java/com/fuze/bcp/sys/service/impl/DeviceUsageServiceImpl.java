package com.fuze.bcp.sys.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.sys.domain.DeviceUsage;
import com.fuze.bcp.sys.repository.DeviceUsageRepository;
import com.fuze.bcp.sys.service.IDeviceUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class DeviceUsageServiceImpl extends BaseServiceImpl<DeviceUsage, DeviceUsageRepository> implements IDeviceUsageService{

    @Autowired
    DeviceUsageRepository deviceUsageRepository;

    @Override
    public DeviceUsage getOneByDataStatusAndDeviceIdAndEmployeeId(String employeeId, String deviceId) {
        return deviceUsageRepository.findOneByDataStatusAndDeviceIdAndEmployeeId(DataStatus.SAVE, employeeId, deviceId);
    }
}
