package com.fuze.bcp.blockchain.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(containerFactory = "FzfqListenContainer", queues = "fuze-web3j-queue")
public interface IWeb3jReceiverService {

    @RabbitHandler
    public void process(Map<String,Object> map);

}