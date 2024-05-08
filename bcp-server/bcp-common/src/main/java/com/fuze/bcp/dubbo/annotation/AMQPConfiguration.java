package com.fuze.bcp.dubbo.annotation;

import java.lang.annotation.*;

/**
 * RabbitMQ消息注解
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AMQPConfiguration {
    /**
     * RabbitMQ消息编码
     * @return
     */
    String AMQPMsgCode() default "";
}
