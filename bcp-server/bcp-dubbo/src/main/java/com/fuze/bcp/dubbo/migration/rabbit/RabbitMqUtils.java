package com.fuze.bcp.dubbo.migration.rabbit;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.mq.domain.DynamicDescribe;
import com.fuze.bcp.mq.domain.TaskDescribe;
import com.fuze.bcp.mq.domain.TaskRecord;
import com.fuze.bcp.utils.DateTimeUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by CJ on 2017/11/20.
 */
public class RabbitMqUtils {

    private RabbitTemplate rabbitTemplate = null;

    public static RabbitTemplate getRabbitTemplate() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.16.2.14");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("fuzefenqi998");
        org.springframework.amqp.rabbit.connection.CachingConnectionFactory factory = new CachingConnectionFactory(connectionFactory);
        factory.setVirtualHost("/");
        factory.setPublisherConfirms(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    public RabbitMqUtils() throws IOException, TimeoutException {
        rabbitTemplate = getRabbitTemplate();
    }

    public void sendBusinessMsg(String taskType, Object[] params, MongoTemplate target) throws IOException, TimeoutException {
        Map<String, String> map = new HashMap<>();
        List<TaskDescribe> taskDescribes = getTaskDescribeByType(taskType, target);
        if (taskDescribes == null) {
            return;
        }
        for (TaskDescribe taskDescribe : taskDescribes) {
            TaskRecord taskRecord = new TaskRecord();
            taskRecord.setTaskId(taskDescribe.getId());
            taskRecord.setTaskType(taskDescribe.getType());
            DynamicDescribe dynamicDescribe = target.findOne(new Query(Criteria.where("_id").is(taskDescribe.getDynamicDescribeId())), DynamicDescribe.class);
            taskRecord.setClassMethodName(dynamicDescribe.getClassName() + "!" + taskDescribe.getMethodName());
            taskRecord.setParams(params);
            taskRecord.setResult("init");
            taskRecord.setStartDate(DateTimeUtils.getSimpleDateFormat().format(new Date(System.currentTimeMillis())));
            target.save(taskRecord);
            map.put(taskRecord.getId(), taskDescribe.getId());
        }
        if (!map.isEmpty()) {
            this.rabbitTemplate.convertAndSend("fuze-exchange", "business-routingKey", map);
        }
    }

    private List<TaskDescribe> getTaskDescribeByType(String type, MongoTemplate target) {
        List<TaskDescribe> taskDescribes = target.find(new Query(Criteria.where("type").is(type).and("dataStatus").is(DataStatus.SAVE)), TaskDescribe.class);
        if (taskDescribes == null || taskDescribes.size() == 0) {
            return new ArrayList<>();
        }
        return taskDescribes;
    }

}
