package com.fuze.bcp.push.service;

import com.fuze.bcp.push.domain.PushTokenBind;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by CJ on 2017/10/11.
 */
public interface IPushTokenBindService extends IBaseService<PushTokenBind> {

    PushTokenBind getByLoginUserName(String loginUserId);

    List<PushTokenBind> getByLoginUserName(List<String> loginUserNames);

    PushTokenBind getByUserToken(String userToken);

    List<PushTokenBind> getByUserTokenOrLoginUserName(String userToken, String loginUserName);
}
