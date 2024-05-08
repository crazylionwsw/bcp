package com.fuze.bcp.api.ocr.bean;

import com.fuze.bcp.bean.APIBaseBean;

/**
 * Created by sean on 2017/6/8.
 * 身份证识别结果信息
 */
public class IdentifyCardInfo extends APIBaseBean {

    /**
     * 姓名
     */
    private String name = null;

    /**
     * 身份证号码
     */
    private String indetifyNo = null;

    /**
     * 性别
     */
    private String gender = null;

    /**
     * 民族
     */
    private String nation = null;

    /**
     * 地址
     */
    private String address = null;

    /**
     * 生日
     */
    private String birthday = null;

    /**
     * 发证机关
     */
    private String issueUnit = null;

    /**
     * 开始有效期
     */
    private String startDate = null;

    /**
     * 结束有效期
     */
    private String endDate = null;


    /**
     * 头像信息
     */
    private byte[] avatar = null;


    /**
     * 省
     */
    private String province = null;

    /**
     * 市
     */
    private String city = null;

    /**
     * 区
     */
    private String area = null;
}
