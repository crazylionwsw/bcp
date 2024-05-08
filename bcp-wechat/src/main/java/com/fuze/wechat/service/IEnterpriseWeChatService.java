package com.fuze.wechat.service;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.AccessToken;
import com.fuze.wechat.domain.LoanQuery;
import com.fuze.wechat.domain.WechatLoginResult;
import com.fuze.wechat.domain.WechatUserResult;

import java.io.IOException;
import java.util.Map;

/**
 * Created by ZQW on 2018/5/5.
 */
public interface IEnterpriseWeChatService {

    ResultBean<AccessToken> accessToken() throws IOException;

    ResultBean<AccessToken> accessToken(Boolean flag) throws IOException;

    ResultBean<AccessToken> accessTokenW() throws IOException;

    ResultBean<AccessToken> accessTokenW(Boolean flag) throws IOException;

    ResultBean<WechatLoginResult> oAuth2GetUserByCode(String token, String code, String agentId);

    ResultBean<String> oAuth2Url(String redirect_uri);

    ResultBean<String> getMemberGuidByCode(String token, String code, String agentId);

    ResultBean<WechatUserResult> getWcUser(String userId, String token);

    ResultBean<Map> sendCardMessage(String userId, LoanQuery loanQueryBean);

    String getCorpId();

    String getSecret();

    String getAgentId();

    String getBelieveIp();

    ResultBean getAngent()throws Exception;
}
