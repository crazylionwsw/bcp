package com.fuze.bcp.sys.repository;


import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.sys.domain.DeviceUsage;

/**
 */
public interface DeviceUsageRepository extends BaseRepository<DeviceUsage,String> {

    DeviceUsage findOneByDataStatusAndDeviceIdAndEmployeeId(Integer save, String employeeId, String deviceId);
}
