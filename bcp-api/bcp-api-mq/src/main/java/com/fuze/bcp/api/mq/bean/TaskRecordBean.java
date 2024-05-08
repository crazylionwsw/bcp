package com.fuze.bcp.api.mq.bean;

import com.fuze.bcp.bean.APIBaseBean;

import java.io.Serializable;

/**
 * 任务执行记录
 * Created by CJ on 2017/7/13.
 */
public class TaskRecordBean extends APIBaseBean implements Serializable {

    private String taskId;

    private String taskType;

    private String result;

    private String startDate;

    private String classMethodName;

    private Object[] params;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getClassMethodName() {
        return classMethodName;
    }

    public void setClassMethodName(String classMethodName) {
        this.classMethodName = classMethodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
