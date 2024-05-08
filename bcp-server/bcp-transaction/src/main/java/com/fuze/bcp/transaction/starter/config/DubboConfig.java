package com.fuze.bcp.transaction.starter.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by CJ on 2017/8/11.
 */
@Configuration
public class DubboConfig {

    /**
     * 自动扫描地址
     *
     * @return
     */
    @Bean
    public static AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage("com.fuze.bcp.service.**,com.fuze.bcp.business.**,com.fuze.bcp.*.business,com.fuze.bcp.*.service.**");
        return annotationBean;
    }

    /**
     * dubbo:application 上下文
     *
     * @return
     */
    @Bean
    public ApplicationConfig application() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("fuze-bcp-server-provider");
        applicationConfig.setOwner("developer");
        applicationConfig.setOrganization("fuze");
        return applicationConfig;
    }

    /**
     * dubbo:registry 注入地址
     *
     * @return
     */
    @Bean
    public RegistryConfig registry(@Value("${dubbo.zookeeper.registry.address}") String address, @Value("${dubbo.zookeeper.registry.protocol}") String protocol) {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(address);
        registryConfig.setProtocol(protocol);
        return registryConfig;
    }

    @Bean
    public ProtocolConfig creditcarDubbo() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20895);
        protocolConfig.setSerialization("kryo");
//        protocolConfig.setOptimizer("com.fuze.bcp.api.transaction.SerializationOptimizerImpl");
        return protocolConfig;
    }

    @Bean
    public ProviderConfig provider(@Value("${dubbo.timeout}") Integer timeout) {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(timeout);
        return providerConfig;
    }

}
