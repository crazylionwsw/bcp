package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.PoundageSettlementBean;
import com.fuze.bcp.api.creditcar.service.IPoundageSettlementBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zxp on 2017/7/3.
 */
/*
* 支行结算
* */
@RestController
@RequestMapping(value = "/json")
public class PoundageSettlementController {

    private static final Logger logger = LoggerFactory.getLogger(PoundageSettlementController.class);

    @Autowired
    private IPoundageSettlementBizService iPoundageSettlementBizService;

    /**
     *      获取  手续费支行结算表分页
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/poundageSettlements", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PoundageSettlementBean> getPoundageSettlements(@RequestParam(value = "currentPage", required = false, defaultValue = "0")int currentPage){
        return iPoundageSettlementBizService.actGetPoundageSettlements(currentPage);
    }

    /**
     *      获取  手续费支行结算表分页
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/poundageSettlements/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PoundageSettlementBean> search(@RequestParam(value = "currentPage", required = false, defaultValue = "0")int currentPage,
                                                                      @RequestParam(value = "userName", required = false, defaultValue = "")String userName,
                                                                      @RequestParam(value = "startTime", required = false, defaultValue = "")String startTime,
                                                                      @RequestParam(value = "endTime", required = false, defaultValue = "")String endTime,
                                                      @RequestBody PoundageSettlementBean poundageSettlementBean){
        return iPoundageSettlementBizService.actSearchPoundageSettlements(currentPage,userName,startTime,endTime,poundageSettlementBean);
    }

    /**
     *      根据ID 回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/poundageSettlement/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PoundageSettlementBean> getPoundageSettlement(@PathVariable("id") String id){
        return iPoundageSettlementBizService.actGetOne(id);
    }

    /**
     *      根据     客户交易ID    回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/poundageSettlement/transaction/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<PoundageSettlementBean> getPoundageSettlementByCustomerTransaction(@PathVariable("id") String id){
        return iPoundageSettlementBizService.actGetOneByCustomerTransactionId(id);
    }

    /**
     *      根据ID 回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/poundageSettlement/marketingCode/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PoundageSettlementBean> getMarketingCodeByCustomerTransactionId(@PathVariable("id") String id){
        return iPoundageSettlementBizService.actGetMarketingCodeByCustomerTransactionId(id);
    }

    /**
     *    更新订单的手续费分成信息
     * @param orderId  客户签约ID
     * @return
     */
    @RequestMapping(value = "/poundageSettlement/refresh/{id}/order", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PoundageSettlementBean> refreshOrder(@PathVariable("id") String orderId){
        return iPoundageSettlementBizService.actCalculateFeeSharing(orderId);
    }

    /**
     *    更新订单的手续费分成信息
     * @param transactionId   交易ID
     * @return
     */
    @RequestMapping(value = "/poundageSettlement/refresh/{id}/transaction", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<PoundageSettlementBean> refreshPoundageSettlement(@PathVariable("id") String transactionId){
        return iPoundageSettlementBizService.actCalculateFeeSharingByTransactionId(transactionId);
    }

}
