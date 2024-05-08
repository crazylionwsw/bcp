package com.fuze.wechat.service.impl;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.PublicUser;
import com.fuze.wechat.repository.PublicUserRepository;
import com.fuze.wechat.service.IPublicUserService;
import com.fuze.wechat.service.ISMSService;
import com.fuze.wechat.service.IWeChatPublicService;
import com.fuze.wechat.utils.DateTimeUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by ZQW on 2018/5/17.
 */
@Service
public class PublicUserServiceImpl implements IPublicUserService{

    Logger logger = LoggerFactory.getLogger(PublicUserServiceImpl.class);

    @Autowired
    PublicUserRepository publicUserRepository;

    @Autowired
    IWeChatPublicService iWeChatPublicService;

    @Autowired
    ISMSService ismsService;

    @Override
    public ResultBean actBindPublicUser(String openId, String cell, String verificationCode) {
        logger.info("绑定手机号 ==》 openId:" + openId + ";cell:" + cell + ";verificationCode:" + verificationCode);
        String code = ismsService.getCode(cell).getD();
        if (StringUtils.isEmpty(code)){
            return ResultBean.getFailed().setM("验证码错误");
        }
        if (!code.equals(verificationCode)){
            return ResultBean.getFailed().setM("验证码错误");
        }
        PublicUser publicUser = publicUserRepository.findOneByOpenid(openId);
        if (publicUser == null){
            publicUser = new PublicUser();
            publicUser.setOpenid(openId);
        }
        publicUser.setCell(cell);
        publicUser.setTs(DateTimeUtils.getCreateTime());
        publicUser = publicUserRepository.save(publicUser);
        if (publicUser != null){
            return ResultBean.getSucceed().setD(publicUser);
        }
        return ResultBean.getFailed().setM("绑定失败");
    }

    @Override
    public ResultBean<PublicUser> actLoginPublicUser(String cell, String verificationCode) {
        logger.info("手机号登录 ==》 cell:" + cell + ";verificationCode:" + verificationCode);
        String code = ismsService.getCode(cell).getD();
        if (StringUtils.isEmpty(code)){
            return ResultBean.getFailed().setM("验证码错误");
        }
        if (!code.equals(verificationCode)){
            return ResultBean.getFailed().setM("验证码错误");
        }
        PublicUser publicUser = publicUserRepository.findOneByCell(cell);
        if (publicUser == null){
            return ResultBean.getFailed().setM("对不起！登录失败，请先注册！");
        }
        return ResultBean.getSucceed().setD(publicUser);
    }

    @Override
    public ResultBean actWeixinLoginPublicUser(String openId) throws IOException {
        ResultBean<JSONObject> baseUserInfo = iWeChatPublicService.getBaseUserInfo(openId);
        if (baseUserInfo.isSucceed()){
            return this.actSavePublicUser(baseUserInfo.getD());
        }
        return ResultBean.getFailed();
    }


    @Override
    public ResultBean<PublicUser> actSavePublicUser(JSONObject jsonObject) {
        if (jsonObject.containsKey("openid")) {
            PublicUser publicUser = publicUserRepository.findOneByOpenid((String) jsonObject.get("openid"));
            if (publicUser == null){
                publicUser = new PublicUser();
                publicUser.setOpenid((String) jsonObject.get("openid"));
            }
            if (jsonObject.containsKey("nickname"))
                publicUser.setNickname( (String) jsonObject.get("nickname"));
            if (jsonObject.containsKey("headimgurl"))
                publicUser.setHeadimgurl( (String) jsonObject.get("headimgurl"));
            if (jsonObject.containsKey("subscribe"))
                publicUser.setSubscribe( (Integer) jsonObject.get("subscribe"));
            publicUser.setCreateType("WXL");
            publicUser.setTs(DateTimeUtils.getCreateTime());
            publicUser = publicUserRepository.save(publicUser);
            if (publicUser != null) {
                return ResultBean.getSucceed().setD(publicUser);
            }
            return ResultBean.getFailed();
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PublicUser> actRegisterPublicUser(String cell, String verificationCode) {
        logger.info("手机号注册 ==》 cell:" + cell + ";verificationCode:" + verificationCode);
        String code = ismsService.getCode(cell).getD();
        if (StringUtils.isEmpty(code)){
            return ResultBean.getFailed().setM("验证码错误");
        }
        if (!code.equals(verificationCode)){
            return ResultBean.getFailed().setM("验证码错误");
        }
        PublicUser publicUser = publicUserRepository.findOneByCell(cell);
        if (publicUser != null){
            return ResultBean.getFailed().setM("对不起！该手机号已注册，请登录！");
        }
        publicUser = new PublicUser();
        publicUser.setCell(cell);
        publicUser.setCreateType("CL");
        publicUser.setTs(DateTimeUtils.getCreateTime());
        publicUser = publicUserRepository.save(publicUser);
        return ResultBean.getSucceed().setD(publicUser);
    }
}
