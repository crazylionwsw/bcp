package com.fuze.bcp.mq.starter.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by CJ on 2017/6/30.
 */
@Configuration
public class AmqpConfig {

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

    @Value("${spring.rabbitmq.host}")
    private String addresses;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses + ":" + port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean(name = "FzfqListenContainer")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }

    @Bean
    public Queue businessQueue() {
        return new Queue("fuze-business-queue", true); //队列持久
    }

    @Bean
    public Queue logQueue() {
        return new Queue("fuze-log-queue", true); //队列持久
    }

    @Bean
    public Queue messageQueue() {
        return new Queue("fuze-message-queue", true); //队列持久
    }

    @Bean
    public Queue web3jQueue() {
        return new Queue("fuze-web3j-queue", true); //队列持久
    }



    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding businessBinding(Queue businessQueue, DirectExchange defaultExchange) {
        return BindingBuilder.bind(businessQueue).to(defaultExchange).with(businessRoutingkey);
    }

    @Bean
    public Binding logBinding(Queue logQueue, DirectExchange defaultExchange) {
        return BindingBuilder.bind(logQueue).to(defaultExchange).with(logRoutingkey);
    }

    @Bean
    public Binding messageBinding(Queue messageQueue, DirectExchange defaultExchange) {
        return BindingBuilder.bind(messageQueue).to(defaultExchange).with(messageRoutingkey);
    }

    @Bean
    public Binding web3jBinding(Queue web3jQueue, DirectExchange defaultExchange) {
        return BindingBuilder.bind(web3jQueue).to(defaultExchange).with(web3jRoutingkey);
    }

    @Bean
    public Queue messageWebsocketQueue() {
        return new Queue("fuze-message-queue-websocket", true); //队列持久
    }

    @Bean
    public Binding messageWebsocketBinding(Queue messageWebsocketQueue, DirectExchange defaultExchange) {
        return BindingBuilder.bind(messageWebsocketQueue).to(defaultExchange).with("websocket-routingKey");
    }
}
