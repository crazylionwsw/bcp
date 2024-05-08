package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.dealerrepayment.DealerRepaymentBean;
import com.fuze.bcp.api.creditcar.service.IDealerRepaymentBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/** 渠道还款
 * Created by ${Liu} on 2017/9/25.
 */
@RestController
@RequestMapping(value = "/json")
public class DealerRepaymentController {

    @Autowired
    IDealerRepaymentBizService iDealerRepaymentBizService;

    /**
     * 根据ID回显信息
     */
    @RequestMapping(value = "/dealerrepayment/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDealerRepayment(@PathVariable("id") String id){
        return iDealerRepaymentBizService.actGetDealerRepayment(id);
    }

    /**
     * 保存渠道还款信息
     */
    @RequestMapping(value = "/dealerrepayment",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveDealerRepayment(@RequestBody DealerRepaymentBean dealerRepaymentBean){
        return iDealerRepaymentBizService.actSaveDealerRepayment(dealerRepaymentBean);
    }

    /**
     * 模糊查询渠道还款信息
     */
    @RequestMapping(value = "/dealerrepayments/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchDealerRepayment(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iDealerRepaymentBizService.actSearchDealerRepayment(userId, searchBean);
    }


    @RequestMapping(value = "/dealerrepayment/status", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DealerRepaymentBean> saveStatus(@RequestParam(value = "id", required = false)  String id,
                                                     @RequestParam(value = "status", required = false)  Integer status) {
        return iDealerRepaymentBizService.actUpdateStatus(id,status);
    }

    @RequestMapping(value = "/dealerrepayment/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DealerRepaymentBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iDealerRepaymentBizService.actSignDealerRepayment(id, signInfo);
    }

}
