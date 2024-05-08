package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeSubmitBean;
import com.fuze.bcp.api.creditcar.service.IBusinessExchangeBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2018/3/2.
 */
@RestController
@RequestMapping(value = "/json")
public class BusinessExchangeController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(BusinessExchangeController.class);

    @Autowired
    IBusinessExchangeBizService iBusinessExchangeBizService;


    /**
     *提交业务调整单
     */
    @RequestMapping(value = "/businessexchange",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean postBusinessExchange(@RequestBody BusinessExchangeSubmitBean businessExchangeSubmitBean){
        return iBusinessExchangeBizService.actSubmitBusinessExchange(businessExchangeSubmitBean);
    }

    /**
     *业务调整单列表(分页)
     */
    @RequestMapping(value = "/businessexchanges", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBusinessExchangeByLoginUserId(@RequestParam(value = "pageindex", defaultValue = "0", required = false) Integer pageIndex, @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iBusinessExchangeBizService.actGetBusinessExchanges(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }
    

    /**
     *业务调整单详情
     */
    @RequestMapping(value = "/businessexchange/{businessExchangeId}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBusinessExchange(@PathVariable("businessExchangeId") String businessExchangeId){
        return iBusinessExchangeBizService.actGetBusinessExchangeInfo(businessExchangeId);
    }

    /**
     * 获取组装所有缴费项
     */
    @RequestMapping(value = "/businessexchange/{id}/fees",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getAllFeesOnBusinessExchange(@PathVariable("id") String transactionId){
        return iBusinessExchangeBizService.actGetALLFeesOnBusinessExchange(transactionId);
    }

}
