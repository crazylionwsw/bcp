package com.fuze.bcp.mqreceiver.service.impl;

import com.fuze.bcp.api.mq.bean.DynamicDescribeBean;
import com.fuze.bcp.api.mq.bean.TaskDescribeBean;
import com.fuze.bcp.api.mq.bean.TaskRecordBean;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.mqreceiver.service.IBusinessReceiverService;
import com.fuze.bcp.mqreceiver.service.SpringDynamicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLClassLoader;
import java.util.Map;

/**
 * Created by CJ on 2017/7/1.
 */

@Service
public class FuzeFirstBusinessReceiverImpl implements IBusinessReceiverService {

    private Logger log = LoggerFactory.getLogger(FuzeFirstBusinessReceiverImpl.class);

    @Autowired
    IAmqpBizService iAmqpBizService;

    @Value("${dubbo.zookeeper.registry.address}")
    String address;

    @Value("${dubbo.zookeeper.registry.protocol}")
    String protocol;

    public void process(Map<String, String> map) {
        map.forEach((recordId, describeId) -> {
            TaskDescribeBean message = iAmqpBizService.actGetTaskDescribe(describeId).getD();
            DynamicDescribeBean dynamicDescribeBean = message.getDynamicDescribeBean();
            TaskRecordBean taskRecord = iAmqpBizService.actGetTaskRecord(recordId).getD();
            if (taskRecord != null) {
                try {
                    log.info("Business开始执行action！");
//                    Class clazz = SpringDynamicService.loadJarToClass(dynamicDescribeBean.getJarPath(), dynamicDescribeBean.getClassName(), (URLClassLoader) ClassLoader.getSystemClassLoader());
                    String methodName = message.getMethodName();
                    Object[] param = taskRecord.getParams();
                    Class[] clazzs = putClass(param);
                    SpringDynamicService.doDubboMethod(dynamicDescribeBean.getClassName(), methodName, clazzs, param, address, protocol);
                    taskRecord.setResult("finish");
                    log.info("Business执行action完毕！");
                } catch (Exception e) {
                    taskRecord.setResult("failed：" + e.getMessage());
                    log.error("Business执行action失败！action:" + dynamicDescribeBean.getClassName() + "." + message.getMethodName(), e);
                } finally {
                    iAmqpBizService.actSaveTaskRecord(taskRecord);
                }
            } else
                log.error("Business执行action失败！无效的taskRecord记录，ID：" + recordId);
        });
    }

    private Class[] putClass(Object[] value) {
        Class[] clazz = new Class[value.length];
        for (int i = 0; i < value.length; i++) {
            clazz[i] = value[i].getClass();
        }
        return clazz;
    }

}
