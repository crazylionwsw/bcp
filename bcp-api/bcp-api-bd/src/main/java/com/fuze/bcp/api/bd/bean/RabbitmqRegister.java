package com.fuze.bcp.api.bd.bean;

import lombok.Data;

/**
 * Rabbitmq监听者注册消息队列
 * Created by Lily on 2017/6/13.
 */
@Data
public class RabbitmqRegister {

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 监听者实体类
     */
    private String ListenerEntity;



}
