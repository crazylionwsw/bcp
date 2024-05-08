package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.msg.domain.MessageSubScribe;
import com.fuze.bcp.msg.repository.MessageSubscribeRepository;
import com.fuze.bcp.msg.service.IMessageSubscribeService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/7/21.
 */
@Service
public class MessageSubscribeServiceImpl extends BaseServiceImpl<MessageSubScribe, MessageSubscribeRepository> implements IMessageSubscribeService {

    @Autowired
    MessageSubscribeRepository messageSubscribeRepository;

    @Override
    public List<MessageSubScribe> getListByBusinessType(String business) {
        return messageSubscribeRepository.findByDataStatusAndBusinessType(DataStatus.SAVE, business);
    }

}
