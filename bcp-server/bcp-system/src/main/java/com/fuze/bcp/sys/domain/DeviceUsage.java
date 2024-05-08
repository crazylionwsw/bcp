package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 终端设备信息
 */
@Document(collection = "sys_terminaldevice_usage")
public class DeviceUsage extends MongoBaseEntity {

    /**
     * 领取
     */
    public final static Integer DEVICE_RECEIVE = 1;
    /**
     * 归还
     */
    public final static Integer DEVICE_RETURN = 0;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 使用人
     */
    private String employeeId;

    /**
     * 使用类型（领取/归还)
     */
    private Integer usageType;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getUsageType() {
        return usageType;
    }

    public void setUsageType(Integer usageType) {
        this.usageType = usageType;
    }
}
