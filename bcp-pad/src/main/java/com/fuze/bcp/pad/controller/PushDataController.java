package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.push.bean.PushTokenBindBean;
import com.fuze.bcp.api.push.service.IPushDataBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/10/12.
 */
@RestController
@RequestMapping(value = "/json")
public class PushDataController {

    @Autowired
    IPushDataBizService iPushDataBizService;

    @RequestMapping(value = "/pushdata/addpushtoken", method = RequestMethod.POST)
    public ResultBean addPushToken(@RequestBody PushTokenBindBean pushTokenBindBean) { // loginUserName, userToken
        // 删除旧记录
        ResultBean<List<PushTokenBindBean>> resultBean = iPushDataBizService.actGetTokenByUserTokenOrLoginUserName(pushTokenBindBean.getUserToken(), pushTokenBindBean.getLoginUserName());
        if (resultBean.isSucceed()) {
            List ids = new ArrayList<>();
            for (PushTokenBindBean beans : resultBean.getD()) {
                ids.add(beans.getId());
            }
            iPushDataBizService.actDeleteRealByIds(ids);
        }
        return iPushDataBizService.actSavePushTokenBind(pushTokenBindBean);
    }



}
