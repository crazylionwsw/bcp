package com.fuze.bcp.mq.domain;

import com.fuze.bcp.api.mq.bean.TaskDescribeBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by CJ on 2017/7/13.
 */
@Document(collection = "mq_taskrecord")
@Data
public class TaskRecord extends MongoBaseEntity {

    public TaskRecord() {
    }

    public TaskRecord(TaskDescribeBean taskDescribeBean, Object[] params) {
        this.taskId = taskDescribeBean.getId();
        this.taskType = taskDescribeBean.getType();
        this.result = "";
        this.startDate = "";
        this.classMethodName = taskDescribeBean.getDynamicDescribeBean().getClassName() + "!" + taskDescribeBean.getMethodName();
        this.params = params;
    }

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
