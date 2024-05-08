package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/8/21.
 */
@RestController
@RequestMapping(value = "/json")
public class BankCardApplyController extends BaseController {

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    @RequestMapping(value = "/bankcards/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BankCardApplyBean> searchCarDemands(@RequestBody SearchBean searchBean) {
        return iBankCardApplyBizService.actSearchBankCardApply(searchBean);
    }

    @RequestMapping(value = "/bankcard/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BankCardApplyBean> getBankCardApply(@PathVariable("id") String id){
        return iBankCardApplyBizService.actFindBankCardApplyById(id);
    }

    @RequestMapping(value = "/bankcard/transaction/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BankCardApplyBean> getBankCardApplyByTransactionId(@PathVariable("id") String id){
        return iBankCardApplyBizService.actFindBankCardApplyByTransactionId(id);
    }

    /**
     * 完成当前任务
     * @param bankCardApplyBean
     * @param start
     * @returnbank
     */
    @RequestMapping(value = "/bankcard/sign",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BankCardApplyBean> approvedBankCardApply(@RequestBody BankCardApplyBean bankCardApplyBean,
                                           @RequestParam(value = "approveStatus" ,required = false , defaultValue = "") Integer approveStatus,
                                           @RequestParam(value = "start" ,required = false , defaultValue = "") Integer start){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        String loginUserId = jwtUser.getId();
        return iBankCardApplyBizService.actApprovedBankCardApply(bankCardApplyBean,approveStatus,start,loginUserId);
    }

    /**
     *      根据交易ID查询卡业务处理业务状态
     * @param transactionId
     * @return
     */
    @RequestMapping(value = "/bankcard/status/{transactionId}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<Integer> getBankCardApplyStatusByTransactionId(@PathVariable("transactionId") String transactionId){
        return iBankCardApplyBizService.actCheckBankCardApplyStatus(transactionId);
    }

    /**
     * 重新制卡
     * @param bankcardId
     * @param comment
     * @return
     */
    @RequestMapping(value = "/bankcard/remakecard",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BankCardApplyBean> reMakeCard(@RequestParam(value = "bankcardId",required = false,defaultValue = "")String bankcardId,
                                                    @RequestParam(value = "comment",required = false,defaultValue = "")String comment){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        String loginUserId = jwtUser.getId();
        return iBankCardApplyBizService.actReMakeCard(bankcardId,comment,loginUserId);
    }

    /**
     * 领卡人的保存
     * @param customerTransactionId
     * @param receiveCardName
     * @return
     */
    @RequestMapping(value = "/bankcard", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BankCardApplyBean> saveReceiveCardName(
            @RequestParam(value = "customerTransactionId") String customerTransactionId,
            @RequestParam(value = "receiveCardName") String receiveCardName) {
        return iBankCardApplyBizService.actsaveReceiveCardName(customerTransactionId,receiveCardName);
    }

    @RequestMapping(value = "/clear/report",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean clearReport(@RequestBody BankCardApplyBean bankCardApplyBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        String loginUserId = jwtUser.getId();
        return iBankCardApplyBizService.actDeleteReport(bankCardApplyBean,loginUserId);
    }

}

