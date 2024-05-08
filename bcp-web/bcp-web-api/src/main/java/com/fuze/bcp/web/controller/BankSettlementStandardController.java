package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.bd.bean.BankSettlementStandardBean;
import com.fuze.bcp.api.bd.service.IBankSettlementStandardBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zqw on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/json")
public class BankSettlementStandardController {

    private static final Logger logger = LoggerFactory.getLogger(BankSettlementStandardController.class);

    @Autowired
    private IBankSettlementStandardBizService iBankSettlementStandardBizService;

    /**
     *      获取  手续费支行结算表分页
     * @return
     */
    @RequestMapping(value = "/bankSettlementStandards", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<BankSettlementStandardBean>> getPoundageSettlements(){
        return iBankSettlementStandardBizService.actFindBankSettlementStandards();
    }

    /**
     *          根据  ID      回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/bankSettlementStandard/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BankSettlementStandardBean> getOne(@PathVariable("id") String id){
        return iBankSettlementStandardBizService.actFindBankSettlementStandardById(id);
    }

    @RequestMapping(value = "/bankSettlementStandard/{channelId}/{declarationId}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BankSettlementStandardBean> getOneByChannelIdAndDeclarationId(@PathVariable("channelId") String channelId,@PathVariable("declarationId") String declarationId){
        return iBankSettlementStandardBizService.actFindBankSettlementStandardByChannelIdAndDeclarationId(channelId,declarationId);
    }

    /**
     *      保存  手续费分成
     * @param bankSettlementStandardBean
     * @return
     */
    @RequestMapping(value = "/bankSettlementStandard", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BankSettlementStandardBean> save(@RequestBody BankSettlementStandardBean bankSettlementStandardBean){
        return iBankSettlementStandardBizService.actSaveBankSettlementStandard(bankSettlementStandardBean);
    }

    /**
     *      删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/bankSettlementStandard/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean<BankSettlementStandardBean> delete(@PathVariable("id") String id){
        return iBankSettlementStandardBizService.actDeleteBankSettlementStandard(id);
    }

    @RequestMapping(value = "/bankSettlementStandard/check/{channelId}/{declarationId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<Boolean> checkOneByChannelIdAndDeclarationId(@PathVariable("channelId") String channelId,@PathVariable("declarationId") String declarationId){
        return iBankSettlementStandardBizService.actCheckBankSettlementStandardByChannelIdAndDeclarationId(channelId,declarationId);
    }
}
