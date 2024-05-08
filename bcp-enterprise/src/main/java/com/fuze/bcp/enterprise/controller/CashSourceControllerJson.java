package com.fuze.bcp.enterprise.controller;

import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  新增客户资质的对象接口
 * Created by LB on 2016-10-27.
 */
@RestController
@RequestMapping(value="/json",method = {RequestMethod.GET, RequestMethod.POST})
public class CashSourceControllerJson {

    private static final Logger logger = LoggerFactory.getLogger(CashSourceControllerJson.class);

    @Autowired
    private ICashSourceBizService iCashSourceMrgService;

    //分页单位
    private static final Integer pageSize = 10;

    /**
     *  获取可以合作的支行
     * @return
     */
    @RequestMapping(value="/cashsources",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CashSourceBean>> getCashSources (){
       return iCashSourceMrgService.actFindChildBank("YH-GSYH-BJ");
    }

}