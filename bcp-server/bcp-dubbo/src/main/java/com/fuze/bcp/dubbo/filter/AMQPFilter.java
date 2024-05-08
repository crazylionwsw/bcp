package com.fuze.bcp.dubbo.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.fuze.bcp.bd.service.IBillTypeService;
import com.fuze.bcp.dubbo.annotation.AMQPConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Dubbo基于RabbitMQ业务耦合过滤器
 */
@Activate
public class AMQPFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AMQPFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result result = invoker.invoke(invocation);

            try {
                Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                if (method.isAnnotationPresent(AMQPConfiguration.class)) {
                    logger.info(invocation.getMethodName());
                    try {
                        // iterates all the annotations available in the method
                        for (Annotation annotation : method.getDeclaredAnnotations()) {
                            System.out.println("Annotation in Method '" + method + "' : " + annotation);
                        }
                        //取得Annotation
                        AMQPConfiguration amqpAnnotation = method.getAnnotation(AMQPConfiguration.class);
                        //取得AMQP消息编码
                        String msgCode = amqpAnnotation.AMQPMsgCode();

                        logger.info(msgCode);
                        //TODO:根据编码调用RabbitMQ发送消息



                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (NoSuchMethodException e) {
                return result;
            }

            // Do our business
            return result;
        } catch (RuntimeException e) {
            logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                    + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                    + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }
}
