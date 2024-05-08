package com.fuze.bcp.api.cardealer.service;

import com.fuze.bcp.api.cardealer.bean.ChannelBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * Created by ZQW on 2018/3/27.
 */
public interface IChannelBizService {

    ResultBean<List<ChannelBean>> actChannels();
}
