package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardListBean;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Lily on 2017/12/25.
 */
@RestController
@RequestMapping(value = "/json/app", method = {RequestMethod.GET, RequestMethod.POST})
public class BankCardApplyController {

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    /**
     * 获取卡业务列表页面
     * @param searchBean
     * @return
     */
    @RequestMapping(value = "/bankcards", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DataPageBean<BankCardListBean>> getAll(@RequestBody SearchBean searchBean) {
        return iBankCardApplyBizService.actGetBankCardApplyListByCashSourceId(searchBean);
    }

    /**
     * 获取卡业务列表页面
     * @param key
     * @return
     */
    @RequestMapping(value = "/bankcards/{cashsourceid}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<BankCardListBean>> getAll(@PathVariable("cashsourceid") String cashsourceid,@RequestParam String key) {
        return iBankCardApplyBizService.actGetBankCardApplyByTaskDefinitionKey(key,cashsourceid);
    }



    /**
     * 查看详情数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/bankcard/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BankCardBean> getBankCardApply(@PathVariable("id") String id){
        return iBankCardApplyBizService.actGetBankCardApplyById(id);
    }

    /**
     * 完成当前任务
     * @param bankCardBean
     * @param start
     * @returnbank
     */
    @RequestMapping(value = "/bankcard/sign",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<BankCardBean> approvedBankCardApply(@RequestBody BankCardBean bankCardBean,
                                                               @RequestParam(value = "approveStatus" ,required = false , defaultValue = "") Integer approveStatus,
                                                               @RequestParam(value = "start" ,required = false , defaultValue = "") Integer start){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        String loginUserId = jwtUser.getId();
        return iBankCardApplyBizService.actApprovedBankCardApply(bankCardBean,approveStatus,start,loginUserId);
    }

    /**
     * 重新制卡
     * @param bankcardId
     * @param comment
     * @return
     */
    @RequestMapping(value = "/bankcard/remakecard/{bankcardId}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BankCardApplyBean> reMakeCard(@PathVariable("bankcardId")String bankcardId,
                                                    @RequestParam(value = "comment",required = false,defaultValue = "")String comment){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        String loginUserId = jwtUser.getId();
        return iBankCardApplyBizService.actReMakeCard(bankcardId,comment,loginUserId);
    }



}
