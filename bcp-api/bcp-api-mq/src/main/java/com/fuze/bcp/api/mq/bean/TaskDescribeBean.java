package com.fuze.bcp.api.mq.bean;

import com.fuze.bcp.annotation.MongoEntity;
import com.fuze.bcp.bean.APIBaseDataBean;

import java.io.Serializable;

/**
 * 任务描述类, 用于定义特定任务对应特定执行
 */
@MongoEntity(entityName = "mq_taskdescribe")
public class TaskDescribeBean extends APIBaseDataBean implements Serializable {

    public static final String BUSINESS = "business";

    public static final String LOG = "log";

    public static final String MESSAGE = "message";

    /**
     * 相关角色
     */
    private String roleID;

    /**
     * 类型，business、log、message
     */
    private String type;

    /**
     * 该任务指定的执行对象
     */
    DynamicDescribeBean dynamicDescribeBean;

    /**
     * 该任务指定的执行方法
     */
    private String methodName;

//    /**
//     * 方法参数值
//     */
//    private Object[] paramValues;
//
//    /**
//     * 方法参数类型
//     */
//    private Class[] paramClass;
//    public Class[] getParamClass() {
//        return paramClass;
//    }
//    public void setParamClass(Class[] paramClass) {
//        this.paramClass = paramClass;
//    }
//    public Object[] getParamValues() {
//        return paramValues;
//    }
//    public void setParamValues(Object[] paramValues) {
//        this.paramValues = paramValues;
//    }

    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public DynamicDescribeBean getDynamicDescribeBean() {
        return dynamicDescribeBean;
    }

    public void setDynamicDescribeBean(DynamicDescribeBean dynamicDescribeBean) {
        this.dynamicDescribeBean = dynamicDescribeBean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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