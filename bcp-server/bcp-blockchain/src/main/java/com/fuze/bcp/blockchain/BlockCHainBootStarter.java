package com.fuze.bcp.blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by sean on 2017/5/20.
 */
//@SpringBootApplication(scanBasePackages = {"com.fuze.bcp.*.starter,com.fuze.bcp.*.service,com.fuze.bcp.*.contract,com.fuze.bcp.*.domain,com.fuze.bcp.*.business,com.fuze.bcp.*.service.**"})
@EnableCaching
@SpringBootApplication
public class BlockCHainBootStarter {
    public static void main(String[] args) {
        SpringApplication.run(BlockCHainBootStarter.class, args);
    }

}
