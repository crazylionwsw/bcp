package com.fuze.bcp.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by sean on 2017/5/20.
 */
@SpringBootApplication
@EnableCaching
@ImportResource(locations = {"dubbo-providers.xml"})
//@ImportResource(locations = {"dubbo-providers.xml", "spring-drools.xml"})
@EnableMongoRepositories(basePackages = {"com.fuze.bcp.*.repository"})
public class DubboApplication {
    public static void main(String[] args) {
//        System.setProperty("kie.maven.settings.custom", args[0]);
        SpringApplication.run(DubboApplication.class, args);
    }
}
