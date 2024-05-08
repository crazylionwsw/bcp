package com.fuze.bcp.drools.starter;

import com.fuze.bcp.dubbo.DubboStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by sean on 2017/5/20.
 */
@SpringBootApplication
@EnableCaching
@ImportResource(locations = {"dubbo-consumers.xml"})
//@ImportResource(locations = {"dubbo-consumers.xml", "spring-drools.xml"})
@EnableMongoRepositories(basePackages = {"com.fuze.bcp.*.repository"})
public class DroolsBootStarter {
    public static void main(String[] args) {
        System.setProperty("kie.maven.settings.custom", args[0]);
        SpringApplication app = new SpringApplication(DroolsBootStarter.class);
        app.setWebEnvironment(false);
        ApplicationContext applicationContext = app.run(args);
        DubboStarter.start("dubbo-providers.xml", applicationContext, DroolsBootStarter.class.getSimpleName());
    }

}
