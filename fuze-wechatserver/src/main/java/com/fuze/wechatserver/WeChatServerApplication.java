package com.fuze.wechatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by CJ on 2018/4/20.
 */

@SpringBootApplication
@EnableCaching
public class WeChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChatServerApplication.class, args);
    }

}
