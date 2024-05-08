package com.fuze.bcp.mqreceiver.service.impl;

import com.fuze.bcp.api.mq.bean.BusinessLogBean;
import com.fuze.bcp.mqreceiver.service.ILogReceiverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/7/3.
 */

@Service
public class FuzeLogReceiverImpl implements ILogReceiverService {

    private Logger log = LoggerFactory.getLogger(FuzeLogReceiverImpl.class);

    @Override
    public void process(BusinessLogBean message) {
        try {
            log.info("1th Log执行action完毕！");
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
