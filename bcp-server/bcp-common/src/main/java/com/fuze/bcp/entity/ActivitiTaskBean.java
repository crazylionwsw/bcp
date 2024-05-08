package com.fuze.bcp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ActivitiTaskBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String vid;
    private String taskName;
    private String userName;
    private String createTime;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVid() {
        return vid;
    }
    public void setVid(String vid) {
        this.vid = vid;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public ActivitiTaskBean() {
        super();
        // TODO Auto-generated constructor stub
    }
    public ActivitiTaskBean(String id, String vid, String taskName, String userName, String createTime) {
        super();
        this.id = id;
        this.vid = vid;
        this.taskName = taskName;
        this.userName = userName;
        this.createTime = createTime;
    }
    @Override
    public String toString() {
        return "ActivitiTaskBean [id=" + id + ", vid=" + vid + ", taskName=" + taskName + ", userName=" + userName + ", createTime="
                + createTime + "]";
    }

}
