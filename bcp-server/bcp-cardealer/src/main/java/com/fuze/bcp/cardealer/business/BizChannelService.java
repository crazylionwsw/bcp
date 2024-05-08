package com.fuze.bcp.cardealer.business;

import com.fuze.bcp.api.cardealer.bean.ChannelBean;
import com.fuze.bcp.api.cardealer.service.IChannelBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.cardealer.domain.Channel;
import com.fuze.bcp.cardealer.service.IChannelService;
import com.fuze.bcp.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZQW on 2018/3/27.
 */
@Service
public class BizChannelService implements IChannelBizService{

    private static final Logger logger = LoggerFactory.getLogger(BizChannelService.class);

    @Autowired
    IChannelService iChannelService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<List<ChannelBean>> actChannels() {
        List<Channel> channels = iChannelService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(channels,ChannelBean.class));
    }
}
