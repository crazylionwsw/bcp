package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by user on 2017/7/10.
 */
@RestController
@RequestMapping(value = "/json")
public class IndexController extends BaseController {

    @Autowired
    IParamBizService iParamBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    /**
     * 【PAD API】-- 主页
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean index() {
        return ResultBean.getSucceed().setD(iParamBizService.actGetWebServerUrl().getD() + "/#/pad/index");
    }

    /**
     * 【pad】主页统计
     * @return
     */
    @RequestMapping(value = "/charts/{mode}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean chart(@PathVariable("mode") Integer mode) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResultBean.getSucceed().setD(iParamBizService.actGetWebServerUrl().getD() + "/#/charts/" + jwtUser.getId() + "/" + mode);
    }


    /**
     * 【PAD API】-- zhuye
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean about() {
        return ResultBean.getSucceed().setD(iParamBizService.actGetWebServerUrl().getD() + "/#/pad/about");
    }

    @RequestMapping(value = "/loginuser/orgs", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getLoginUserOrgs() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        return iOrgBizService.actGetOrgsByLoginUser(jwtUser.getId());
    }

}
