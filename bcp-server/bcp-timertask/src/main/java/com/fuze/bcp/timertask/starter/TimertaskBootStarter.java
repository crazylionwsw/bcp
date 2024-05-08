package com.fuze.bcp.timertask.starter;

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
@EnableMongoRepositories(basePackages = {"com.fuze.bcp.*.repository"})
public class TimertaskBootStarter {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TimertaskBootStarter.class);
        app.setWebEnvironment(false);
        ApplicationContext applicationContext = app.run(args);
        DubboStarter.start("dubbo-providers.xml", applicationContext,"");
    }

}
