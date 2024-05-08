package com.fuze.bcp.customer.domain;

import com.fuze.bcp.api.customer.bean.CustomerJobBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户基本信息
 * Created by sean on 16/10/10.
 */
@Document(collection = "so_customer")
@Data
public class Customer extends MongoBaseEntity {

    /**
     * 客户姓名
     */
    private String name = "";

    /**
     * 性别： -1：未标识，0：男性，1：女性
     */
    private Integer gender = 0;

    /**
     * 身份证号码
     */
    private String identifyNo;

    /**
     * 身份证地址(户籍)
     */
    private String address;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 民族
     */
    private String nationality;

    /**
     * 发证机关
     */
    private String authorizeBy;

    /**
     * 身份证有效期
     */
    private String identifyValid;

    /**
     * 身份证是否长期有效
     * 1:是  0:否
     */
    private int longEffective;

    /**
     * 联系地址
     */
    private String contactAddress;

    /**
     * 客户的婚姻状况
     */
    private String maritalStatus;

    /**
     * 是否为直客（默认 否）
     * 0不是  1是
     */
    private Integer directGuest = 0;

    /**
     * 是否为自雇人士
     * 0不是  1是
     */
    private Integer isSelfEmployed = null;

    /**
     * 联系电话
     */
    private List<String> cells;

    /**
     * 是否有北京牌照
     */
    private String isCarBrand;

    /**
     * 是否有北京房产
     */
    private String isHouseBrand;

    /**
     * 是否有北京社保
     */
    private String isSocialInsurance;

    /**
     * 是否有北京户籍
     */
    private String isBJCensusRegister;

    //TODO 根据发证机关判断用户籍贯，精确到省份
    //     比如: 北京市海淀区莫某派出所
    //           河南省某某市
    /**
     * 户籍所在地
     */
    private String censusRegisterCity;

    /**
     * 社保期数
     */
    private String socialInsuranceDate;

    /**
     * 客户工作信息
     */
    private CustomerJobBean customerJob = null;

    /**
     * 微信ID
     */
    private String wx_openid = null;

    /**
     *  房屋地址
     *  格式
     *  0 ："北京市"
     *  1 ："直辖市"
     *  2 ："朝阳区"
     */
    private List<String> houseAddress = new ArrayList<String>();

    /**
     *  房屋详细地址
     */
    private String houseDetailAddress;

}
