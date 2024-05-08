package com.fuze.wechat.service.impl;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.service.IMiniProgramService;
import com.fuze.wechat.utils.HttpUtils;
import com.fuze.wechat.utils.SHA1;
import com.fuze.wechat.utils.WXCore;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by ZQW on 2018/5/14.
 */
@Service
public class MiniProgramServiceImpl implements IMiniProgramService{

    Logger logger = LoggerFactory.getLogger(MiniProgramServiceImpl.class);

    public static final String CODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    @Autowired
    private JedisPool jedisPool;

    @Value("${fuze.wechat.miniprogram.appid}")
    private String appid;

    @Value("${fuze.wechat.miniprogram.secret}")
    private String secret;

    @Value("${fuze.wechat.believeIp}")
    private String believeIp;

    @Override
    public String getAppId() {
        return appid;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public String getBelieveIp() {
        return believeIp;
    }

    @Override
    public ResultBean<JSONObject> actGetSessionByCode(String code) {
        JSONObject sessionByCode = getSessionByCode(code);
        if (!sessionByCode.isNullObject()) {
            if (!sessionByCode.has("errcode")){
                sessionByCode.discard("session_key");
                sessionByCode.put("expires_in", 2 * 60 * 60);
                return ResultBean.getSucceed().setD(sessionByCode);
            } else {
                logger.error(sessionByCode.getInt("errcode") + ":" + sessionByCode.get("errmsg"));
                return ResultBean.getFailed().setM(sessionByCode.getInt("errcode") + ":" + sessionByCode.get("errmsg"));
            }
        }
        return ResultBean.getFailed().setM("微信服务器异常");
    }

    private JSONObject getSessionByCode(String code){
        Jedis jedis = jedisPool.getResource();
        try {
            String code2session_url = CODE_2_SESSION.replace("APPID", appid).replace("SECRET", secret).replace("JSCODE", code);
            String code2session_json = HttpUtils.doGet(code2session_url);
            logger.info("code2session_json:" + code2session_json);
            JSONObject jsonObject = JSONObject.fromObject(code2session_json);
            if (jsonObject.isNullObject()) {
                return null;
            }
            if (!jsonObject.has("errcode")) {
                jedis.hset(jsonObject.getString("openid") + "_session_key", secret, code2session_json);
                jedis.expire(jsonObject.getString("openid") + "_session_key", 2 * 60 * 60);
            }
            return jsonObject;
        } finally {
            jedis.close();
        }
    }

    @Override
    public ResultBean<Boolean> actCheckSignature(JSONObject rawData, String signature, String openId){
        ResultBean<JSONObject> sessionKeyResult = getSessionKey(openId);
        String calculateSignature = "";
        if (sessionKeyResult.isSucceed()){
            calculateSignature = SHA1.encode(rawData.toString()+sessionKeyResult.getD().getString("session_key"));
            if (signature.equals(calculateSignature)){
                return ResultBean.getSucceed().setD(true);
            }
        }
        return ResultBean.getFailed().setM(sessionKeyResult.getM());
    }

    private ResultBean<JSONObject> getSessionKey(String openId){
        Jedis jedis = jedisPool.getResource();
        try {
            String sessionStr = jedis.hget(openId + "_session_key", secret);
            if (sessionStr == null){
                logger.error("从redis中未获取到session_key信息");
                return ResultBean.getFailed().setM("从redis中未获取到session_key信息");
            }
            JSONObject jsonObject = JSONObject.fromObject(sessionStr);
            return ResultBean.getSucceed().setD(jsonObject);
        } finally {
            jedis.close();
        }
    }

    @Override
    public ResultBean<JSONObject> actDecrypt(String encryptedData, String iv, String openId) {
        ResultBean<JSONObject> sessionKeyResult = getSessionKey(openId);
        if (sessionKeyResult.isSucceed()){
            String securityUserInfoJson = WXCore.decrypt(appid, encryptedData, sessionKeyResult.getD().getString("session_key"), iv);
            JSONObject jsonObject = JSONObject.fromObject(securityUserInfoJson);
            return ResultBean.getSucceed().setD(jsonObject);
        }
        return ResultBean.getFailed().setM(sessionKeyResult.getM());
    }
}
