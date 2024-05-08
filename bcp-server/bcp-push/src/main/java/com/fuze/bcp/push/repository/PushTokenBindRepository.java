package com.fuze.bcp.push.repository;

import com.fuze.bcp.push.domain.PushTokenBind;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by CJ on 2017/10/11.
 */
public interface PushTokenBindRepository extends BaseRepository<PushTokenBind, String> {

    PushTokenBind findByLoginUserName(String loginUserId);

    List<PushTokenBind> findByLoginUserNameIn(List<String> loginUserIds);

    PushTokenBind findByUserToken(String userToken);

    List<PushTokenBind> findByUserTokenOrLoginUserName(String userToken, String loginUserName);
}
