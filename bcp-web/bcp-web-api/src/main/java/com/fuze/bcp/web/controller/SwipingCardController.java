package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.swipingcard.SwipingCardBean;
import com.fuze.bcp.api.creditcar.service.ISwipingCardBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2017/9/22.
 * 渠道刷卡
 */
@RestController
@RequestMapping(value = "/json")
public class SwipingCardController {

    @Autowired
    private ISwipingCardBizService iSwipingCardBizService;

    /**
     * 根据Id回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/swipingcard/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSwipingCard(@PathVariable("id") String id){
        return iSwipingCardBizService.actGetSwipingCard(id);
    }

    @RequestMapping(value = "/swipingcard/transaction/{transactionId}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSwipingCardByTransactionId(@PathVariable("transactionId") String transactionId){
        return iSwipingCardBizService.actGetSwipingCardByTransactionId(transactionId);
    }

    /**
     * 保存渠道刷卡信息
     * @param swipingCardBean
     * @return
     */
    @RequestMapping(value = "/swipingcard",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveSwipingCard(@RequestBody SwipingCardBean swipingCardBean){
        return iSwipingCardBizService.actSaveSwipingcard(swipingCardBean);
    }

    @RequestMapping(value = "/swipingcards/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchSwipingCards(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iSwipingCardBizService.actSearchSwipingCards(userId, searchBean);
    }

    @RequestMapping(value = "/swipingcard/status", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<SwipingCardBean> saveStatus(@RequestParam(value = "id", required = false)  String id,
                                                  @RequestParam(value = "status", required = false)  Integer status) {
        return iSwipingCardBizService.actUpdateStatus(id,status);
    }

    @RequestMapping(value = "/swipingcard/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<SwipingCardBean> sign(@PathVariable("id") String id,@RequestBody SignInfo signInfo) {
        return iSwipingCardBizService.actSignSwipingCard(id, signInfo);
    }
}
