package com.fuze.bcp.entity;

import java.io.Serializable;
/**
 * Created by sean on 16/10/10.
 * 时间线
 */
public class ActionTimeLine implements Serializable {

    /**
     * 备注
     */
    private String comment = null;

    /**
     * action对象需要单独定义
     */
    private String action = null;


    /**
     * 签批用户
     */

    private String userId;

    /**
     * 签批人
     */
    private String employeeId;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
