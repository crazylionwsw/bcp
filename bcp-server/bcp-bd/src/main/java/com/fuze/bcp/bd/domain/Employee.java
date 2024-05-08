package com.fuze.bcp.bd.domain;

import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工基本信息
 */
//@Document(collection = "bd_employee_wx")
@Document(collection = "bd_employee")
@Data
public class Employee extends BaseDataEntity {

    /**
     * 姓名
     */
    private String username = null;

    /**
     * 性别： -1：未标识，0：男性，1：女性
     */
    private Integer gender = -1;

    /**
     * 生日
     */
    private String  birthday  = null;

    /**
     * 身份证号码
     */
    private String identifyNo = null;

    /**
     * 员工编号
     */
    private String employeeNo = null;

    /**
     * 邮箱
     */
    private String email = null;

    /**
     * 手机号码
     */
    private String cell = null;


    /**
     * 组织机构ID
     */
    private String orgInfoId = null;

    /**
     * 员工头像
     */
    private String avatarFileId = null;

    /**
     * 员工登录信息，可以绑定多个账户，使用不同的登录信息
     */
    private String loginUserId;

    /**
     * 微信号openId
     */
    private String  wxOpenid = null;

    /**
     * 企业微信用户userId
     */
    private String wxUserId;

    /**
     * 员工角色
     */
    private List<String> employeeRoles  = new ArrayList<String>();

}
