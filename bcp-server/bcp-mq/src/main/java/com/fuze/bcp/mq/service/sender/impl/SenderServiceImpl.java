package com.fuze.bcp.mq.service.sender.impl;

import com.fuze.bcp.api.mq.bean.BusinessLogBean;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import com.fuze.bcp.mq.domain.TaskRecord;
import com.fuze.bcp.mq.service.ITaskRecordService;
import com.fuze.bcp.mq.service.sender.ISenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SenderServiceImpl implements ISenderService {

    @Autowired
    private ITaskRecordService iTaskRecordService;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingKey-b}")
    private String businessRoutingkey;

    @Value("${spring.rabbitmq.routingKey-l}")
    private String logRoutingkey;

    @Value("${spring.rabbitmq.routingKey-m}")
    private String messageRoutingkey;

    @Value("${spring.rabbitmq.routingKey-web3j}")
    private String web3jRoutingkey;

    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入
     */
    @Autowired
    public SenderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (correlationData == null) {
            return;
        }
        System.out.println(" 回调id:" + correlationData);
        if (ack) {
            System.out.println("消息成功消费");
            TaskRecord taskRecord = iTaskRecordService.getOne(correlationData.getId());
            taskRecord.setResult("start");
            iTaskRecordService.save(taskRecord);
        } else {
            System.out.println("消息消费失败:" + cause);
            TaskRecord taskRecord = iTaskRecordService.getOne(correlationData.getId());
            taskRecord.setResult("faild:" + cause);
            iTaskRecordService.save(taskRecord);
        }
    }

    @Override
    public void sendBusinessMsg(Map<String, String> map) {
        this.rabbitTemplate.convertAndSend(exchange, businessRoutingkey, map);
    }

    @Override
    public void sendLogMsg(BusinessLogBean content, String recordId) {
        CorrelationData correlationData = new CorrelationData(recordId);
        this.rabbitTemplate.convertAndSend(exchange, logRoutingkey, content, correlationData);
    }

    @Override
    public void sendMessageMsg(MsgRecordBean msgRecord) {
        this.rabbitTemplate.convertAndSend(exchange, messageRoutingkey, msgRecord);
    }

    public void sendWeb3jMsg(Map<String,Object> map) {
        this.rabbitTemplate.convertAndSend(exchange, web3jRoutingkey, map);
    }

    /**
     * 此方法非框架实现业务方法，仅当server内需要发送websocket时提供的一种实现
     */
    @Override
    public void sendWebsocketMsg(String userId, String content) {
        Map map = new HashMap<>();
        map.put("userId", userId);
        map.put("content", content);
        this.rabbitTemplate.convertAndSend(exchange, "websocket-routingKey", map);
    }

}