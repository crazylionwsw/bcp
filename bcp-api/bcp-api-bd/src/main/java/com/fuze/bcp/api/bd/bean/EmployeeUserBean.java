package com.fuze.bcp.api.bd.bean;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 员工基本信息
 */
@Data
public class EmployeeUserBean extends APIBaseBean {

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
     * 用户，数据交互时使用
     */
    private LoginUserBean loginUser;


    private String  wx_openid = null;



}
