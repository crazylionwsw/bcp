package com.fuze.bcp.pad.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by CJ on 2017/8/11.
 */
@Configuration
public class DubboConfig {

    @Bean
    public ApplicationConfig application() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("fuze-bcp-pad");
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
    public RegistryConfig padRegistry(@Value("${dubbo.zookeeper.registry.address}") String address, @Value("${dubbo.zookeeper.registry.protocol}") String protocol) {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(address);
        registryConfig.setProtocol(protocol);
        registryConfig.setFile("/root/.dubbo/dubbo-registry/pad/dubbo-registry.properties");
        return registryConfig;
    }

    @Bean
    public ProtocolConfig dubbo() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
//        protocolConfig.setSerialization("kryo");
        return protocolConfig;
    }

    @Bean
    public ProviderConfig provider(RegistryConfig padRegistry, ProtocolConfig dubbo) {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setRegistry(padRegistry);
        providerConfig.setProtocol(dubbo);
        return providerConfig;
    }

}