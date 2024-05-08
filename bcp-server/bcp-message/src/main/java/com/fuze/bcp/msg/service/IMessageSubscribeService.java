package com.fuze.bcp.msg.service;

import com.fuze.bcp.msg.domain.MessageSubScribe;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by CJ on 2017/7/21.
 */
public interface IMessageSubscribeService extends IBaseService<MessageSubScribe> {

    List<MessageSubScribe> getListByBusinessType(String business);

}
