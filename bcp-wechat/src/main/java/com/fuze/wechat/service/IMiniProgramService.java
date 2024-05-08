package com.fuze.wechat.service;

import com.fuze.wechat.base.ResultBean;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by ZQW on 2018/5/14.
 */
public interface IMiniProgramService {

    String getAppId();

    String getSecret();

    String getBelieveIp();

    /**
     *  使用 临时登录凭证code，从微信服务器中获取 session_key 和 openid
     * @param code  临时登录凭证
     * @return      openid
     */
    ResultBean<JSONObject> actGetSessionByCode(String code);

    /**
     *  数据签名校验  返回用户数据的安全性
     *
     *  https://developers.weixin.qq.com/miniprogram/dev/api/open.html
     *
     * @param rawData  不包括敏感信息的原始数据字符串，用于计算签名。
     * @param signature 使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息，
     * @param openId  用户的openid
     * @return      用户数据是否安全
     */
    ResultBean<Boolean> actCheckSignature(JSONObject rawData , String signature, String openId) throws IOException;

    /**
     *     解密敏感数据
     *
     *  https://developers.weixin.qq.com/miniprogram/dev/api/open.html
     *
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv        加密算法的初始向量，
     * @param openId    用户的openid
     * @return
     */
    ResultBean<JSONObject> actDecrypt(String encryptedData, String iv, String openId) throws IOException ;

}
