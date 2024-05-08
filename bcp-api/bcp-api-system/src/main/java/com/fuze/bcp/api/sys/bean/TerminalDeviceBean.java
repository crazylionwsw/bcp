package com.fuze.bcp.api.sys.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 终端设备信息
 * Created by sean on 16/10/10.
 */
@Data
@MongoEntity(entityName = "sys_terminaldevice")
public class TerminalDeviceBean extends APIBaseBean {

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

}
