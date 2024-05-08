package com.fuze.bcp.workflow.starter;

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
@EnableMongoRepositories(basePackages = {"com.fuze.bcp.*.repository"})
public class WorkflowBootStarter {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WorkflowBootStarter.class);
        app.setWebEnvironment(false);
        ApplicationContext applicationContext = app.run(args);
        DubboStarter.start("dubbo-providers.xml", applicationContext, WorkflowBootStarter.class.getSimpleName());
    }

}
