package com.fuze.wechat.service;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.PublicUser;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by ZQW on 2018/5/17.
 */
public interface IPublicUserService {

    ResultBean actBindPublicUser(String openId, String cell, String verificationCode);

    ResultBean<PublicUser> actSavePublicUser(JSONObject jsonObject);

    ResultBean<PublicUser> actLoginPublicUser(String cell, String verificationCode);

    ResultBean actWeixinLoginPublicUser(String openId) throws IOException;

    ResultBean<PublicUser> actRegisterPublicUser(String cell, String verificationCode);

}
