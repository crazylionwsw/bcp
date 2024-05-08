package com.fuze.bcp.mqreceiver.service;

import com.fuze.bcp.api.mq.bean.BusinessLogBean;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


@RabbitListener(containerFactory = "FzfqListenContainer", queues = "fuze-log-queue")
public interface ILogReceiverService {

    @RabbitHandler
    public void process(BusinessLogBean message);

}