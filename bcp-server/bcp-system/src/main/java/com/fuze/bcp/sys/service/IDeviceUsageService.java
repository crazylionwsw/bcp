package com.fuze.bcp.sys.service;

import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.sys.domain.APKRelease;
import com.fuze.bcp.sys.domain.DeviceUsage;

/**
 * Created by lenovo on 2017-06-13.
 */
public interface IDeviceUsageService extends IBaseService<DeviceUsage>{
    DeviceUsage getOneByDataStatusAndDeviceIdAndEmployeeId(String employeeId, String deviceId);
}
