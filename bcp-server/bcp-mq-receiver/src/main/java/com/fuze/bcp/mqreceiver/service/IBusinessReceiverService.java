package com.fuze.bcp.mqreceiver.service;

import com.fuze.bcp.api.mq.bean.TaskDescribeBean;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;


@RabbitListener(containerFactory = "FzfqListenContainer", queues = "fuze-business-queue")
public interface IBusinessReceiverService {

    @RabbitHandler
    public void process(Map<String, String> map) throws Exception;

}