package com.fuze.bcp.api.push.service;

import com.fuze.bcp.api.push.bean.PushDataBean;
import com.fuze.bcp.api.push.bean.PushTokenBindBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * Created by CJ on 2017/9/28.
 */
public interface IPushDataBizService {

    ResultBean<PushTokenBindBean> actGetPushTokenBind(String id);

    ResultBean<PushTokenBindBean> actSavePushTokenBind(PushTokenBindBean pushTokenBindBean);

    ResultBean<PushTokenBindBean> actGetToken(String loginUserId);

    ResultBean<PushTokenBindBean> actGetTokenByUserToken(String userToken);

    ResultBean<List<PushTokenBindBean>> actGetToken(List<String> loginUserIds);

    ResultBean<List<PushTokenBindBean>> actGetTokenByUserTokenOrLoginUserName(String userToken, String loginUserName);

    ResultBean actDeleteRealByIds(List<String> ids);

    ResultBean actPushData(String loginUserName, PushDataBean pushDataBean);

    ResultBean actPushDatas(List<String> loginUserNames, PushDataBean pushDataBean);

    ResultBean actPushAllData(PushDataBean pushDataBean);

}
