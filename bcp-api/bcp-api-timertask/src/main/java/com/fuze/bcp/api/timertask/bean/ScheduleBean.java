package com.fuze.bcp.api.timertask.bean;

import com.fuze.bcp.bean.APIBaseDataBean;

/**
 * 定时任务记录
 * Created by CJ on 2017/7/3.
 */
public class ScheduleBean extends APIBaseDataBean {

    /**
     * 执行类的beanName
     */
    private String beanName;

    /**
     * 执行类的类路径
     */
    private String classpath;

    /**
     * 要执行的方法名称
     */
    private String methodName;

    /**
     * 执行时间表达式
     */
    private String cronExpression;
}
