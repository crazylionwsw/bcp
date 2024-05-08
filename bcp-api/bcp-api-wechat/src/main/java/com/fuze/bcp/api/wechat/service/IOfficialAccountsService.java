package com.fuze.bcp.api.wechat.service;

import com.fuze.bcp.bean.ResultBean;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by CJ on 2018/4/23.
 */
public interface IOfficialAccountsService {

    ResultBean<JSONObject> oath2AccessToken(String code) throws IOException;

    ResultBean<JSONObject> addMaterialEver(File file, String type) throws Exception;

    ResultBean<JSONObject> addMedia(Map params) throws IOException;

    ResultBean<String> oAuth2Url(String redirect_uri, String scope);

    ResultBean<JSONObject> getBaseUserInfo(String openid) throws IOException;

    ResultBean<JSONObject> getUserInfo(String access_token, String openid) throws IOException;

    ResultBean<JSONObject> baseAccessToken(Boolean flag) throws IOException;

    ResultBean<JSONObject> baseJsapiTicket(Boolean flag) throws IOException;

    String getAppId();

    String getSecret();

    String getBelieveIp();
}
