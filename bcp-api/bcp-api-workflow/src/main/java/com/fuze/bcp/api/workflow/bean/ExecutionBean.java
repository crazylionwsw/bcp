package com.fuze.bcp.api.workflow.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by CJ on 2018/1/3.
 */
@Data
public class ExecutionBean implements Serializable{

    private String activityId;

    private String processDefinitionId;

    private String processInstanceId;

    private String businessKey;

}
