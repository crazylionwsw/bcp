package com.fuze.bcp.api.sys.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 设备使用记录
 */
@Data
public class DeviceUsageBean extends APIBaseBean {

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

}
