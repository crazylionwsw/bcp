package com.fuze.wechat.controller;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.service.IPublicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by ZQW on 2018/5/17.
 */
@RestController
@RequestMapping("/json")
public class PublicUserController {

    @Autowired
    IPublicUserService iPublicUserService;

    @RequestMapping(value = "/publicuser/bind",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean bindPublicUser(@RequestParam(value = "openId") String openId, @RequestParam(value = "cell") String cell, @RequestParam(value = "verificationCode") String verificationCode){
        return iPublicUserService.actBindPublicUser(openId, cell, verificationCode);
    }

    @RequestMapping(value = "/publicuser/login",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean publicUserLogin(@RequestParam(value = "cell") String cell, @RequestParam(value = "verificationCode") String verificationCode){
        return iPublicUserService.actLoginPublicUser(cell, verificationCode);
    }
    @RequestMapping(value = "/publicuser/xlogin",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean publicUserWeixinLogin(@RequestParam(value = "openId") String openId) throws IOException {
        return iPublicUserService.actWeixinLoginPublicUser(openId);
    }
    @RequestMapping(value = "/publicuser/reg",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean regPublicUser(@RequestParam(value = "cell") String cell, @RequestParam(value = "verificationCode") String verificationCode){
        return iPublicUserService.actRegisterPublicUser(cell, verificationCode);
    }
}
