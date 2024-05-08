package com.fuze.bcp.mqreceiver.service;

import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

/**
 * Created by CJ on 2017/7/7.
 */
@RabbitListener(containerFactory = "FzfqListenContainer", queues = "fuze-message-queue")
public interface IMessageReceiverService {

    @RabbitHandler
    void process(MsgRecordBean msgRecord) throws IOException;

}