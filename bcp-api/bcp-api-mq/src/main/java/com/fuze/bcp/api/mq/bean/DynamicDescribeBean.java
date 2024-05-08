package com.fuze.bcp.api.mq.bean;

import com.fuze.bcp.bean.APIBaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CJ on 2017/7/12.
 */
public class DynamicDescribeBean extends APIBaseBean implements Serializable {

    private String beanId;

    private String jarPath;

    private String className;

    private List<DynamicDescribeBean> neededResource;

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

    public List<DynamicDescribeBean> getNeededResource() {
        return neededResource;
    }

    public void setNeededResource(List<DynamicDescribeBean> neededResource) {
        this.neededResource = neededResource;
    }
}
