package com.fuze.bcp.api.workflow.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by admin on 2017/7/17.
 */
@Data
public class SignInfoBean implements Serializable {

    /**
     * 签批用户
     */
    private String userId;

    /**
     * 签批人
     */
    private String employeeId;

    /**
     * 签批人姓名
     */
    private String employeeName;

    /**
     * 签批人头像
     */
    private String employeeAvatar;

    /**
     * 签批状态
     */
    private Integer result = SignInfo.SIGN_INIT;

    /**
     *  标志状态
     */
    private Integer flag = SignInfo.FLAG_COMMENT;

    /**
     *  操作任务信息
     */
    private String taskInfo = null;

    /**
     * 时间戳
     */
    private String ts = null;

    /**
     * 说明
     */
    private String comment = null;

    /**
     * 审核状态
     * 0：初审
     * 1：终审
     */
    private Integer auditStatus;

}
