package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.PoundageSettlementBean;
import com.fuze.bcp.api.creditcar.service.IPoundageSettlementBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/11/17.
 */
@RestController
@RequestMapping(value = "/json")
public class BankDailyStatementController {

    @Autowired
    private IPoundageSettlementBizService iPoundageSettlementBizService;

    /**
     * 查询       报单行的 每日手续费分成
     * @param poundageSettlementBean
     * @return
     */
    @RequestMapping(value = "/bankdailystatement/searchdeclaration",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<PoundageSettlementBean>> calculateDailyDeclaration(@RequestBody PoundageSettlementBean poundageSettlementBean){

        return iPoundageSettlementBizService.actCalculateDailyDeclaration(poundageSettlementBean);
    }

    /**
     * 查询       渠道行的 每日手续费分成
     * @param poundageSettlementBean
     * @return
     */
    @RequestMapping(value = "/bankdailystatement/searchchannel",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<PoundageSettlementBean>> calculateDailyChannel(@RequestBody PoundageSettlementBean poundageSettlementBean){
        return iPoundageSettlementBizService.actCalculateDailyChannel(poundageSettlementBean);
    }


    /**
     * 查询  截止某个日期累计发生的数据总和
     * @param poundageSettlementBean
     * @return
     */
    @RequestMapping(value = "/bankdailystatement/summary/{chargewaycode}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<Map<String,Object>>> getSummation(@PathVariable("chargewaycode") String chargeWayCode, @RequestBody PoundageSettlementBean poundageSettlementBean){
        poundageSettlementBean.setChargePaymentWayCode(chargeWayCode);
        return iPoundageSettlementBizService.actGetSummation(poundageSettlementBean);
    }
}
