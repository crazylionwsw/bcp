package com.fuze.wechat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.service.IWeChatPublicService;
import com.fuze.wechat.utils.HttpUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZQW on 2018/5/5.
 */
@Service
public class WeChatPublicServiceImpl implements IWeChatPublicService{

    Logger logger = LoggerFactory.getLogger(WeChatPublicServiceImpl.class);

    // 获取code
    public static final String CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    public static final String BASE_ACCESS_TOKER_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static final String ACCESS_TOKER_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

    public static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    public static final String BASE_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    public static final String ADD_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";

    //  通过access_token获取ticket
    public static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    //  检查TICKET是否过期
    public static final String CHECK_JSAPI_TICKET_EXPIRE_TIME = "https://mp.weixin.qq.com/debug/cgi-bin/ticket/check?ticket=TICKET";

    //  检验授权凭证（access_token）是否有效
    public static final String CHECK_ACCESS_TOKEN_IS_EXPIRE = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";

    @Autowired
    private JedisPool jedisPool;

    @Value("${fuze.wechat.public.appid}")
    private String appid;

    @Value("${fuze.wechat.public.secret}")
    private String secret;

    @Value("${fuze.wechat.believeIp}")
    private String believeIp;

    public ResultBean<JSONObject> addMaterialEver(File file, String type) throws Exception {
        try {
            logger.info("开始上传" + type + "永久素材---------------------");
            //开始获取证书
            ResultBean<JSONObject> resultBean = baseAccessToken(false);
            if (resultBean.failed() || resultBean.getD() == null) {
                logger.error(resultBean.getM());
                return resultBean;
            }
            //上传素材
            String path = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + resultBean.getD().get("access_token") + "&type=" + type;
            String result = HttpUtils.connectHttpsByPost(path, file);
            result = result.replaceAll("[\\\\]", "");
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (!jsonObject.isNullObject()) {
                if (jsonObject.has("media_id")) {
                    return ResultBean.getSucceed().setD(jsonObject);
                } else {
                    logger.error("上传" + type + "永久素材失败" + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
                    return ResultBean.getFailed().setM("上传" + type + "永久素材失败  " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
                }
            }
            return ResultBean.getFailed().setM("网络错误，未接收到微信返回信息");
        } catch (Exception e) {
            logger.error("程序异常", e);
            throw e;
        } finally {
            logger.info("结束上传" + type + "永久素材---------------------");
        }
    }

    @Override
    public ResultBean<JSONObject> addMedia(Map params) throws IOException {
        ResultBean<JSONObject> tokenMapResult = baseAccessToken(false);
        if (!tokenMapResult.failed()) {
            JSONObject tokenMap = tokenMapResult.getD();
            String add_media_url = ADD_MEDIA_URL.replace("ACCESS_TOKEN", tokenMap.getString("access_token"));
            ObjectMapper objectMapper = new ObjectMapper();
            String paramsJson = objectMapper.writeValueAsString(params);
            String add_media_result = HttpUtils.doPost(add_media_url, "utf-8", paramsJson);
            JSONObject jsonObject = JSONObject.fromObject(add_media_result);
            if (!jsonObject.isNullObject()) {
                if (jsonObject.has("media_id")) {
                    return ResultBean.getSucceed().setD(jsonObject);
                } else if (jsonObject.has("errcode") && 42001 == jsonObject.getInt("errcode")) {
                    tokenMapResult = baseAccessToken(true);
                    add_media_url = ADD_MEDIA_URL.replace("ACCESS_TOKEN", (String) tokenMapResult.getD().get("access_token"));
                    paramsJson = objectMapper.writeValueAsString(params);
                    add_media_result = HttpUtils.doPost(add_media_url, "utf-8", paramsJson);
                    jsonObject = JSONObject.fromObject(add_media_result);
                    return ResultBean.getSucceed().setD(jsonObject);
                }
                logger.error("上传失败   " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
                return ResultBean.getFailed().setM("上传失败   " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
            }
        }
        return tokenMapResult;
    }

    @Override
    public ResultBean<String> oAuth2Url(String redirect_uri, String scope) {
        try {
            redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        return ResultBean.getSucceed().setD(CODE.replace("APPID", getAppId()).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", scope));
    }

    public ResultBean<JSONObject> getBaseUserInfo(String openid) throws IOException {
        ResultBean<JSONObject> bean = baseAccessToken(false);
        if (bean.failed()) {
            logger.error("[getBaseUserInfo][baseAccessToken]失败");
            return bean;
        }
        String base_user_info_url = BASE_USER_INFO_URL.replace("ACCESS_TOKEN", bean.getD().getString("access_token")).replace("OPENID", openid);
        String base_user_info_json = HttpUtils.doGet(base_user_info_url);
        logger.info("[base_user_info_json] ===> " + base_user_info_json);
        JSONObject jsonObject = JSONObject.fromObject(base_user_info_json);
        if (jsonObject != null && !jsonObject.isNullObject()) {
            if (!jsonObject.has("errcode") || jsonObject.getInt("errcode") == 0) {
                return ResultBean.getSucceed().setD(jsonObject);
            } else if (jsonObject.getInt("errcode") == 42001) {
                bean = baseAccessToken(true);
                if (bean.failed()) {
                    return bean;
                }
                base_user_info_url = BASE_USER_INFO_URL.replace("ACCESS_TOKEN", bean.getD().getString("access_token")).replace("OPENID", openid);
                base_user_info_json = HttpUtils.doGet(base_user_info_url);
                jsonObject = JSONObject.fromObject(base_user_info_json);
                if (jsonObject != null && !jsonObject.isNullObject()) {
                    if (!jsonObject.has("errcode") || jsonObject.getInt("errcode") == 0) {
                        return ResultBean.getSucceed().setD(jsonObject);
                    }
                }
                logger.error("获取用户基本信息错误  " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
                return ResultBean.getFailed().setM("获取用户基本信息错误  " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
            }
            logger.error("获取用户基本信息错误  " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
            return ResultBean.getFailed().setM("获取用户基本信息错误  " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
        }
        return ResultBean.getFailed().setM("网络错误，未接收到微信返回信息");
    }

    /**
     * 网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息
     *
     * @param access_token
     * @param openid
     * @return
     * @throws IOException
     */
    @Override
    public ResultBean<JSONObject> getUserInfo(String access_token, String openid) throws IOException {
        String user_info_url = USER_INFO_URL.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        String user_info_json = HttpUtils.doGet(user_info_url);
        JSONObject jsonObject = JSONObject.fromObject(user_info_json);
        if (jsonObject != null && !jsonObject.isNullObject()) {
            if (jsonObject.has("errcode") || jsonObject.getInt("errcode") == 0) {
                return ResultBean.getSucceed().setD(jsonObject);
            } else {
                logger.error("获取用户信息失败   " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
                return ResultBean.getFailed().setM("获取用户信息失败   " + jsonObject.get("errcode").toString() + ":" + jsonObject.get("errmsg").toString());
            }
        }
        return ResultBean.getFailed().setM("网络错误，未接收到微信返回信息");
    }

    public ResultBean<JSONObject> baseAccessToken(Boolean flag) throws IOException {
        Jedis jedis = jedisPool.getResource();
        try {
            String tokenStr = null;
            if (!flag) {
                tokenStr = this.checkAccessTokenExpireTime(jedis.hget("base_access_token", secret));
            }
            if (tokenStr == null) {
                String baseAccessTokenUrl = BASE_ACCESS_TOKER_URL.replace("APPID", appid).replace("APPSECRET", secret);
                tokenStr = HttpUtils.doGet(baseAccessTokenUrl);
            }
            JSONObject jsonObject = JSONObject.fromObject(tokenStr);
            if (jsonObject != null) {
                if (!jsonObject.has("errcode") || jsonObject.getInt("errcode") == 0) {
                    jedis.hset("base_access_token", secret, tokenStr);
                    jedis.expire("base_access_token", jsonObject.getInt("expires_in") - 200);
                    return ResultBean.getSucceed().setD(jsonObject);
                } else {
                    logger.error("获取basetoken失败   " + jsonObject.get("errcode") + ":" + jsonObject.get("errmsg"));
                    return ResultBean.getFailed().setM("获取basetoken失败   " + jsonObject.get("errcode") + ":" + jsonObject.get("errmsg"));
                }
            }
            return ResultBean.getFailed().setM("网络错误，未接收到微信返回信息");
        } finally {
            jedis.close();
        }
    }

    public String checkAccessTokenExpireTime(String tokenStr){

        if (!StringUtils.isEmpty(tokenStr)){
            logger.info("判断redis中的access_token是否过期");
            JSONObject jsonObject = JSONObject.fromObject(tokenStr);
            String checkAccessTokenExpireTimeUrl = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", jsonObject.getString("access_token"));
            String check_access_token_expire_time_json = HttpUtils.doGet(checkAccessTokenExpireTimeUrl);
            JSONObject checkAccessTokenExpireTimeUrlObject = JSONObject.fromObject(check_access_token_expire_time_json);
            if (checkAccessTokenExpireTimeUrlObject.getInt("errcode") == 40001 && checkAccessTokenExpireTimeUrlObject.getString("errmsg").contains("invalid credential")){
                logger.info("过期");
                return null;
            } else if (checkAccessTokenExpireTimeUrlObject.getInt("errcode") == 0 && checkAccessTokenExpireTimeUrlObject.getString("errmsg").equals("ok")) {
                logger.info("没过期");
                return tokenStr;
            }
        }
        return null;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public ResultBean<JSONObject> oath2AccessToken(String code) throws IOException {
        JSONObject jsonObject = getOauth2Token(code);
        if (!jsonObject.isNullObject()) {
            if (!jsonObject.has("errcode") || 0 == jsonObject.getInt("errcode")) {
                return ResultBean.getSucceed().setD(jsonObject);
            } else {
                return ResultBean.getFailed().setM(jsonObject.getInt("errcode") + ":" + jsonObject.get("errmsg"));
            }
        }
        return ResultBean.getFailed().setM("accessToken获取失败");
    }

    private JSONObject refreshToken(String openId) throws IOException {
        Jedis jedis = jedisPool.getResource();
        try {
            String refresh_token = jedis.hget(openId + "_refresh_token", secret);
            String refresh_toker_url = REFRESH_TOKEN_URL.replace("APPID", appid).replace("refresh_token", refresh_token);
            String refresh_token_result = HttpUtils.doGet(refresh_toker_url);
            JSONObject jsonObject = JSONObject.fromObject(refresh_token_result);
            if (jsonObject.isNullObject()) {
                return null;
            }
            if (!jsonObject.has("errcode") || 0 == jsonObject.getInt("errcode")) {
                return jsonObject;
            }
            return null;
        } finally {
            jedis.close();
        }
    }

    private JSONObject getOauth2Token(String code) throws IOException {
        Jedis jedis = jedisPool.getResource();
        try {
            Map<Object, Object> params = new HashMap<>();
            params.put("appid", appid);
            params.put("secret", secret);
            params.put("code", code);
            params.put("grant_type", "authorization_code");
            String access_toker_url = ACCESS_TOKER_URL.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
            String access_token_json = HttpUtils.doGet(access_toker_url);
            logger.info("access_token_json:" + access_token_json);
            JSONObject jsonObject = JSONObject.fromObject(access_token_json);
            if (jsonObject.isNullObject()) {
                return null;
            }
            if (!jsonObject.has("errcode") || 0 == jsonObject.getInt("errcode")) {
                jedis.hset(jsonObject.getString("openid") + "_refresh_token", secret, jsonObject.getString("refresh_token"));
                jedis.expire(jsonObject.getString("openid") + "_refresh_token", 30 * 24 * 60 * 60);
                jedis.hset(jsonObject.getString("openid") + "_access_token", secret, jsonObject.getString("access_token"));
                jedis.expire(jsonObject.getString("openid") + "_access_token", 3600);
            }
            return jsonObject;
        } finally {
            jedis.close();
        }
    }

    public ResultBean<JSONObject> baseJsapiTicket(Boolean flag) throws IOException {
        Jedis jedis = jedisPool.getResource();
        try {
            String ticketStr = null;
            if (!flag) {
                ticketStr = this.checkTicketExpireTime(jedis.hget("ticket_" + appid, secret));
            }
            if (ticketStr == null) {
                ResultBean<JSONObject> result = baseAccessToken(false);
                if (result.getD() == null) {
                    logger.error(result.getM());
                    return result;
                }
                String access_token = result.getD().getString("access_token");
                logger.info("baseJsapiTicket----access_token:" + access_token);
                String baseTicketUrl = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", access_token);
                ticketStr = HttpUtils.doGet(baseTicketUrl);
            }
            logger.info("baseJsapiTicket----ticketStr:" + ticketStr);
            JSONObject jsonObject = JSONObject.fromObject(ticketStr);
            if (!jsonObject.isNullObject()) {
                if (!jsonObject.has("errcode") || jsonObject.getInt("errcode") == 0) {
                    logger.info("向redis中保存ticket:" + ticketStr);
                    jedis.hset("ticket_" + appid, secret, ticketStr);
                    jedis.expire("ticket_" + appid, jsonObject.getInt("expires_in") - 200);
                    return ResultBean.getSucceed().setD(jsonObject);
                } else {
                    logger.error("错误" + jsonObject.getInt("errcode") + ":" + jsonObject.getString("errmsg"));
                    return ResultBean.getFailed().setM(jsonObject.getInt("errcode") + ":" + jsonObject.getString("errmsg"));
                }
            }
            return ResultBean.getFailed().setM("网络错误，未接收到微信返回信息");
        } finally {
            jedis.close();
        }
    }

    public String checkTicketExpireTime(String ticketStr){

        if (!StringUtils.isEmpty(ticketStr)){
            logger.info("判断redis中的ticket是否过期");
            JSONObject jsonObject = JSONObject.fromObject(ticketStr);
            String checkJsApiTicketExpireTimeUrl = CHECK_JSAPI_TICKET_EXPIRE_TIME.replace("TICKET", jsonObject.get("ticket").toString());
            String check_js_api_ticket_expire_time_json = HttpUtils.doGet(checkJsApiTicketExpireTimeUrl);
            JSONObject checkJsApiTicketExpireTimeUrlObject = JSONObject.fromObject(check_js_api_ticket_expire_time_json);
            if (checkJsApiTicketExpireTimeUrlObject.getInt("errcode") == 1 && checkJsApiTicketExpireTimeUrlObject.getString("errmsg").equals("expire_time")){
                logger.info("过期");
                return null;
            } else if (checkJsApiTicketExpireTimeUrlObject.getInt("errcode") == 0 && checkJsApiTicketExpireTimeUrlObject.getString("errmsg").equals("ok")) {
                logger.info("没过期");
                return ticketStr;
            }
        }
        return null;
    }

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

}
