package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeBean;
import com.fuze.bcp.api.creditcar.service.IBusinessExchangeBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.api.workflow.bean.TEMSignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by ${Liu} on 2018/3/4.
 */
@RestController
@RequestMapping(value = "/json")
public class BusinessExchangeController {

    @Autowired
    IBusinessExchangeBizService iBusinessExchangeBizService;

    /**
     *获取业务调整列表(含查询)
     */
    @RequestMapping(value = "/businessexchanges/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BusinessExchangeBean> searchBusinessExchange(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        ResultBean<BusinessExchangeBean> businessExchangeBeanResultBean = iBusinessExchangeBizService.actSearchBusinessExchange(userId, searchBean);
        return businessExchangeBeanResultBean;
    }

    /**
     *获取单条业务调整数据
     */
    @RequestMapping(value = "/businessexchange/{businessExchangeId}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BusinessExchangeBean> getBusinessExchange(@PathVariable("businessExchangeId") String businessExchangeId){
        return iBusinessExchangeBizService.actGetBusinessExchange(businessExchangeId);
    }

    /**
     *调整单与签约对比
     */
    @RequestMapping(value = "/businessexchange/compare/{propname}/{transactionId}/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean compareDataInfo(@PathVariable("propname") String propname, @PathVariable("transactionId") String transactionId,@PathVariable("id") String businessExchangeId){
        return iBusinessExchangeBizService.actCompareData(propname, transactionId,businessExchangeId);
    }

    /**
     * 通过交易id获取交易信息
     */
    @RequestMapping(value = "/businessexchange/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BusinessExchangeBean> getBusinessExchangeByTransaction(@PathVariable("id") String id){
        return iBusinessExchangeBizService.actGetBusinessExchangeByTransaction(id);
    }

    /**
     * 审批
     */
    @RequestMapping(value = "/businessexchange/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BusinessExchangeBean> sign(@PathVariable("id") String id,@RequestBody TEMSignInfo temSignInfo) {
        return iBusinessExchangeBizService.actSignBusinessExchange(id, temSignInfo);
    }
}
