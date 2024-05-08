package com.fuze.bcp.statistics;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;

/**
 * Created by CJ on 2018/3/9.
 */
public class Test {


    public static void main(String[] args) throws Exception {

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("fuze-bcp-server-provider");
        applicationConfig.setOwner("developer");
        applicationConfig.setOrganization("fuze");
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1:2181");
        registryConfig.setProtocol("zookeeper");
        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setApplication(applicationConfig);
        referenceBean.setRegistry(registryConfig);
        referenceBean.setInterface("com.fuze.bcp.api.creditcar.service.IOrderBizService");
        referenceBean.setProtocol("zookeeper");
        referenceBean.setTimeout(15000);
        referenceBean.afterPropertiesSet();
        Object object = referenceBean.get();
    }

}
