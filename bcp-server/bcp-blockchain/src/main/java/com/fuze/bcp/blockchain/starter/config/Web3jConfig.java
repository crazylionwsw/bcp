package com.fuze.bcp.blockchain.starter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * Created by Lily on 2018/4/23.
 */
@Configuration
@Data
public class Web3jConfig {

    @Value("${web3j.ip}")
    private  String ip ;

    @Value("${web3j.password}")
    private String password;

    @Value("${web3j.file}")
    private String file;

    @Value("${web3j.address}")
    private String address;

    private volatile static Web3j web3j;

    public  Web3j getClient(){
        if(web3j==null){
            synchronized (Web3jConfig.class){
                if(web3j==null){
                    web3j = Web3j.build(new HttpService(ip));
                }
            }
        }
        return web3j;
    }

}
