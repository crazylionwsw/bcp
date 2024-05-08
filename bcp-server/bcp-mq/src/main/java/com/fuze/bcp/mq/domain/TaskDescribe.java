package com.fuze.bcp.mq.domain;

import com.fuze.bcp.api.mq.bean.TaskDescribeBean;
import com.fuze.bcp.domain.BaseDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by CJ on 2017/7/12.
 */

@Document(collection = "mq_taskdescribe")
@Data
public class TaskDescribe extends BaseDataEntity {

    public TaskDescribe() {
    }

    public TaskDescribe(TaskDescribeBean taskDescribeBean) {
        this.setId(taskDescribeBean.getId());
        this.setDataStatus(taskDescribeBean.getDataStatus());
        this.setTs(taskDescribeBean.getTs());
        this.setMethodName(taskDescribeBean.getMethodName());
        this.setRoleID(taskDescribeBean.getRoleID());
        this.setType(taskDescribeBean.getType());
        this.setCode(taskDescribeBean.getCode());
        this.setName(taskDescribeBean.getName());
    }

    public TaskDescribe(TaskDescribeBean taskDescribeBean, String dynamicDescribeBeanId) {
        this(taskDescribeBean);
        this.setDynamicDescribeId(dynamicDescribeBeanId);
    }



    private String roleID;

    private String type;

    /****************************
     *       动态加载bean
     ***************************/
    private String dynamicDescribeId;

    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDynamicDescribeId() {
        return dynamicDescribeId;
    }

    public void setDynamicDescribeId(String dynamicDescribeId) {
        this.dynamicDescribeId = dynamicDescribeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

}