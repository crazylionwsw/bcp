package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.appointswipingcard.AppointSwipingCardBean;
import com.fuze.bcp.api.creditcar.service.IAppointSwipingCardBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by gqr on 2017/8/17.
 */

/**
 * 预约刷卡
 */

@RestController
@RequestMapping(value = "/json")
public class AppointSwipingCardController {

    @Autowired
    private IAppointSwipingCardBizService iAppointSwipingCardBizService;

    /**
     * 根据id回显
     */
    @RequestMapping(value = "/appointswipingcard/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<AppointSwipingCardBean> getOne(@PathVariable("id") String id) {
        return iAppointSwipingCardBizService.actGetAppointSwipingCard(id);
    }

    @RequestMapping(value = "/appointswipingcard/transaction/{transactionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<AppointSwipingCardBean> getOneByTransactionId(@PathVariable("transactionId") String transactionId) {
        return iAppointSwipingCardBizService.actGetAppointSwipingCardByCustomerTransactionId(transactionId);
    }


    @RequestMapping(value = "/appointswipingcard/status", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<AppointSwipingCardBean> saveStatus(@RequestParam(value = "id", required = false)  String id,
                                                     @RequestParam(value = "status", required = false)  Integer status) {
        return iAppointSwipingCardBizService.actUpdateStatus(id,status);
    }

    @RequestMapping(value = "/appointswipingcard/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<AppointSwipingCardBean> signAppointPayment(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iAppointSwipingCardBizService.actSignAppointSwipingCard(id,signInfo);
    }

    /**
     * 模糊查询(升级)
     * @return
     */
    @RequestMapping(value = "/appointswipingcards/search", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchAppointSwipingCards(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iAppointSwipingCardBizService.actSearchAppointSwipingCards(userId, searchBean);
    }

}
