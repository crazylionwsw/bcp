package com.fuze.bcp.push.service.impl;

import com.fuze.bcp.push.domain.PushTokenBind;
import com.fuze.bcp.push.repository.PushTokenBindRepository;
import com.fuze.bcp.push.service.IPushTokenBindService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/10/11.
 */
@Service
public class PushTokenBindServiceImpl extends BaseServiceImpl<PushTokenBind, PushTokenBindRepository> implements IPushTokenBindService {
    @Override
    public PushTokenBind getByLoginUserName(String loginUserName) {
        return baseRepository.findByLoginUserName(loginUserName);
    }

    @Override
    public List<PushTokenBind> getByLoginUserName(List<String> loginUserNames) {
        return baseRepository.findByLoginUserNameIn(loginUserNames);
    }

    @Override
    public PushTokenBind getByUserToken(String userToken) {
        return baseRepository.findByUserToken(userToken);
    }

    @Override
    public List<PushTokenBind> getByUserTokenOrLoginUserName(String userToken, String loginUserName) {
        return baseRepository.findByUserTokenOrLoginUserName(userToken, loginUserName);
    }
}
