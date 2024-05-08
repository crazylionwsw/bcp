package com.fuze.bcp.mq.service.sender;

import com.fuze.bcp.api.mq.bean.BusinessLogBean;
import com.fuze.bcp.api.mq.bean.MsgRecordBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

/**
 * Created by CJ on 2017/7/3.
 */
public interface ISenderService extends RabbitTemplate.ConfirmCallback {

    void sendBusinessMsg(Map<String, String> map);

    void sendLogMsg(BusinessLogBean content, String recordId);

    void sendMessageMsg(MsgRecordBean msgRecord);

    void sendWeb3jMsg(Map<String,Object> map);

    /**
     * 此方法非框架实现业务方法，仅当server内需要发送websocket时提供的一种实现
     */
    void sendWebsocketMsg(String userId, String content);
}
