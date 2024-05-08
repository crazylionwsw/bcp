package com.fuze.bcp.mq.domain;

import com.fuze.bcp.api.mq.bean.DynamicDescribeBean;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
@Document(collection = "mq_dynamicdescribe")
@Data
public class DynamicDescribe extends BaseDataEntity {

    private String beanId;

    private String jarPath;

    private String className;

    private List<String> neededResourceId;

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getNeededResourceId() {
        return neededResourceId;
    }

    public void setNeededResourceId(List<String> neededResourceId) {
        this.neededResourceId = neededResourceId;
    }
}