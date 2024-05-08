package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyListBean;
import com.fuze.bcp.api.creditcar.service.IBankCardApplyBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/10/23.
 */
@RestController
@RequestMapping(value = "/json")
public class BankCardApplyController extends BaseController{

    @Autowired
    IBankCardApplyBizService iBankCardApplyBizService;

    /**
     * 【PAD-API】 分页获取loginUserId 的卡业务处理列表
     * @param pageIndex
     * @param pageSize
     * @param isPassed
     * @return
     */
    @RequestMapping(value = "/bankCards", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<BankCardApplyListBean>> getBankCardApplyPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize, @RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iBankCardApplyBizService.actGetBankCards(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 【PAD-API】 卡业务代启卡失败时进行自启卡
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/bankCard/{id}/sign", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<BankCardApplyListBean> getAppointPaymentsPage(@PathVariable("id") String id,@RequestParam(value = "approveStatus", required = false, defaultValue = "2") Integer approveStatus,@RequestParam(value = "initPassword", required = false, defaultValue = "") String initPassword) {
        String loginUserId = super.getOperatorId();
        return iBankCardApplyBizService.actSignBankCardByReplaceActivate(id,approveStatus,initPassword,loginUserId);
    }

    /**
     *获取卡业务的审核状态,判断是否可以进行业务调整
     */
    @RequestMapping(value = "/bankCard/{id}/approvestatus", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getBankCardApplyApproveStatus(@PathVariable("id") String transactionId){
        return iBankCardApplyBizService.actGetBankCardApplyApproveStatus(transactionId);
    }
}
