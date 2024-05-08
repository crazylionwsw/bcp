package com.fuze.bcp.api.workflow.bean;

import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

/**
 * 定时任务记录
 * Created by CJ on 2017/7/3.
 */
@Data
public class TaskBean extends APIBaseBean {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务key
     */
    private String taskDefinitionKey;

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
