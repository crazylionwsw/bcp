package com.fuze.bcp.web.websocket.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.util.Map;

/**
 * Created by CJ on 2017/7/7.
 */
@RabbitListener(containerFactory = "FzfqListenContainer", queues = "fuze-message-queue-websocket")
public interface IMessageReceiverService {

    @RabbitHandler
    void process(Map<String, String> mqMap) throws IOException;

}