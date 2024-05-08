package com.fuze.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableCaching
@EnableMongoRepositories(basePackages = {"com.fuze.wechat.repository"})
public class WeChatBootStarter {
    public static void main(String[] args) {
        SpringApplication.run(WeChatBootStarter.class, args);
    }

}
