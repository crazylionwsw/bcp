package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.api.creditcar.bean.PurchaseCarOrderBean;
import com.fuze.bcp.api.creditcar.service.IOrderBizService;
import com.fuze.bcp.api.msg.bean.FeedbackBean;
import com.fuze.bcp.api.msg.bean.NoticeLookupBean;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class MessageController extends BaseController {

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    IAuthenticationBizService iAuthenticationBizService;

    @Autowired
    IOrderBizService iOrderBizService;

    /**
     * 【PAD API】-- 提交反馈意见
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/message/feedback", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<FeedbackBean> saveMessage(@RequestBody FeedbackBean feedbackBean) {
        feedbackBean.setTitle("用户反馈");
        return iMessageBizService.actSaveFeedBack(feedbackBean);
    }

    @RequestMapping(value = "/message/mynotice", method = RequestMethod.GET)
    public ResultBean<DataPageBean<NoticeLookupBean>> getMyNotice(@RequestParam("pageindex") Integer currentPage, @RequestParam("pagesize") Integer pageSize, String type) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUserBean loginUserBean = iAuthenticationBizService.actGetLoginUser(jwtUser.getId()).getD();
        return iMessageBizService.actGetMyNoticeForPad(currentPage, pageSize, type, "pad", loginUserBean.getUsername());
    }

}
