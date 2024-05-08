package com.fuze.bcp.web.websocket.receiver.impl;

import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.mq.service.IAmqpBizService;
import com.fuze.bcp.api.msg.bean.Channels;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.web.websocket.WebSocketServer;
import com.fuze.bcp.web.websocket.receiver.IMessageReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by CJ on 2017/7/7.
 */

@Service
public class MessageReceiverServiceImpl implements IMessageReceiverService {

    @Autowired
    IAmqpBizService iAmqpService;

    @Autowired
    IAuthenticationBizService iAuthenticationService;

    @Autowired
    IMessageBizService iMessageBizService;

    @Override
    public void process(Map<String, String> mqMap) throws IOException {
        WebSocketServer.sendInfo(mqMap.get("content"), mqMap.get("userId"));
    }
}
