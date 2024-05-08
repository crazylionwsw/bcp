package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.msg.domain.MessageLog;
import com.fuze.bcp.msg.repository.MessageLogRepository;
import com.fuze.bcp.msg.service.IMessageLogService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/7/27.
 */
@Service
public class MessageLogServiceImpl extends BaseServiceImpl<MessageLog, MessageLogRepository> implements IMessageLogService {

    @Autowired
    MessageLogRepository messageLogRepository;

}
