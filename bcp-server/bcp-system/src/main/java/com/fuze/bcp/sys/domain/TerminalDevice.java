package com.fuze.bcp.sys.domain;

import com.fuze.bcp.domain.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 终端设备信息
 */
@Document(collection = "sys_terminaldevice")
public class TerminalDevice extends MongoBaseEntity {

    /**
     * Android PAD信息
     */
    public final static String DEVICE_TYPE_PAD = "ANDROID_PAD";

    /**
     * Apple iPAD
     */
    public final static String DEVICE_TYPE_IPAD = "iPAD";

    /**
     * Android手机
     */
    public final static String DEVICE_TYPE_ANDROID = "ANDROID";

    /**
     * iPhone手机
     */
    public final static String DEVICE_TYPE_IPHONE = "iPHONE";

    /**
     * WEB浏览器
     */
    public final static String DEVICE_TYPE_WEB = "WEB";

    /**
     * 终端设备类型
     */
    private String deviceType = null;

    /**
     * 设备名称(自定义)
     */
    private String deviceName = null;

    /**
     * 设备唯一识别码
     */
    private String identify = null;

    /**
     * 设备序号
     */
    private String serialNum = null;

    /**
     * MAC地址
     */
    private String mac = null;

    /**
     * 当前使用人
     */
    private String employeeId;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
