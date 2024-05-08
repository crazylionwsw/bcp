package com.fuze.bcp.enterprise.controller;

import com.fuze.bcp.api.cardealer.bean.ChannelBean;
import com.fuze.bcp.api.cardealer.service.IChannelBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/json",method = {RequestMethod.GET, RequestMethod.POST})
public class ChannelController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    IChannelBizService iChannelBizService;

    /**
     *  获取可以合作的支行
     * @return
     */
    @RequestMapping(value="/channels",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<ChannelBean>> getChannels (){
       return iChannelBizService.actChannels();
    }

}