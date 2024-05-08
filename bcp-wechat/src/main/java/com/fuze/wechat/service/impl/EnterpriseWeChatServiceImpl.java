package com.fuze.wechat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.AccessToken;
import com.fuze.wechat.domain.LoanQuery;
import com.fuze.wechat.domain.WechatLoginResult;
import com.fuze.wechat.domain.WechatUserResult;
import com.fuze.wechat.service.IEnterpriseWeChatService;
import com.fuze.wechat.utils.HttpUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZQW on 2018/5/5.
 */
@Service
public class EnterpriseWeChatServiceImpl implements IEnterpriseWeChatService{

    Logger logger = LoggerFactory.getLogger(EnterpriseWeChatServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Value("${fuze.wechat.enterprise.corpid}")
    private String corpId;

    @Value("${fuze.wechat.enterprise.secret}")
    private String secret;

    @Value("${fuze.wechat.enterprise.secretW}")
    private String secretW;

    @Value("${fuze.wechat.enterprise.agentid}")
    private String agentId;

    @Value("${fuze.wechat.believeIp}")
    private String believeIp;

    // 获取access_token的url
    public static final String ACCESS_TOKER_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    // 根据code获取成员信息的url
    public static final String GET_OAUTH2_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE&agentid=AGENTID";

    // 获取code
    public static final String CODE = "https://open.work.weixin.qq.com/wwopen/sso/qrConnect?appid=CORPID&agentid=AGENTID&redirect_uri=REDIRECT_URI&state=login@fzfq";

    // 根据ID 获取USER
    public static final String USERREAD = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";

    public static final String SENDMESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";

    public static final String ANENTURL = "https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token=ACCESS_TOKEN&agentid=AGENTID";

    public ResultBean<AccessToken> accessToken() throws IOException {
        return accessToken(false);
    }

    /**
     * @param flag 为true 时不管缓存中的token 直接访问微信，false时先取缓存中数据
     * @return
     * @throws IOException
     */
    public ResultBean<AccessToken> accessToken(Boolean flag) throws IOException {
        AccessToken accessToken = getToken(flag, secret);
        if (accessToken != null) {
            return ResultBean.getSucceed().setD(accessToken);
        }
        return ResultBean.getFailed();
    }

    public ResultBean<AccessToken> accessTokenW() throws IOException {
        return accessTokenW(false);
    }

    /**
     * @param flag 为true 时不管缓存中的token 直接访问微信，false时先取缓存中数据
     * @return
     * @throws IOException
     */
    public ResultBean<AccessToken> accessTokenW(Boolean flag) throws IOException {
        AccessToken accessToken = getToken(flag, secretW);
        if (accessToken != null) {
            return ResultBean.getSucceed().setD(accessToken);
        }
        return ResultBean.getFailed();
    }

    private AccessToken getToken(Boolean flag, String secret) throws IOException {
        AccessToken accessToken = null;
        Jedis jedis = jedisPool.getResource();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String tokenStr = null;
            if (!flag) {
                tokenStr = jedis.hget(corpId, secret);
            }
            if (tokenStr == null) {
                Map<Object, Object> params = new HashMap<>();
                params.put("corpid", corpId);
                params.put("corpsecret", secret);
                String access_token = HttpUtils.doGet(ACCESS_TOKER_URL, "UTF-8", params);
                accessToken = objectMapper.readValue(access_token, AccessToken.class);
                if ("0".equals(accessToken.getErrcode())) {
                    jedis.hset(corpId, secret, access_token);
                    jedis.expire(corpId, accessToken.getExpires_in());
                }
            } else {
                accessToken = objectMapper.readValue(tokenStr, AccessToken.class);
            }
            if (accessToken != null) {
                return accessToken;
            }
            return null;
        }finally {
            jedis.close();
        }
    }

    /**
     * 调用接口获取企业微信UserID
     *
     * @param token
     * @param code
     * @return
     */
    public ResultBean<String> getMemberGuidByCode(String token, String code, String agentId) {
        WechatLoginResult result = oAuth2GetUserByCode(token, code, agentId).getD();
        if (result.getErrcode().equals("0")) {
            if (result.getUserId() != null && result.getUserId().length() > 0) {
                return ResultBean.getSucceed().setD(result.getUserId());
            }
        }
        return ResultBean.getFailed().setM(result.getErrmsg());
    }

    /**
     * 获取企业微信用户WechatUser
     *
     * @param userId
     * @param token
     * @return
     */
    @Override
    public ResultBean<WechatUserResult> getWcUser(String userId, String token) {
        WechatUserResult wechatUserResult = new WechatUserResult();
        String userUrl = USERREAD.replace("ACCESS_TOKEN", token).replace("USERID", userId);
        String userinfo = HttpUtils.doGet(userUrl);
        if (userinfo != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                wechatUserResult = objectMapper.readValue(userinfo, WechatUserResult.class);
                System.out.println(wechatUserResult);
            } catch (Exception e) {
                wechatUserResult.setErrmsg(e.getMessage());
                wechatUserResult.setErrcode(42001);
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ResultBean<Map> sendCardMessage(String userId, LoanQuery loanQueryBean) {
        String genderType = null;
        if(loanQueryBean.getGender() != null && loanQueryBean.getGender() == 1){
            genderType = "先生";
        } else if(loanQueryBean.getGender() != null && loanQueryBean.getGender() == 0) {
            genderType = "女士";
        }
        if(loanQueryBean.getExpectedLoanAmount() == null || loanQueryBean.getExpectedLoanAmount() == 0.0){
            loanQueryBean.setExpectedLoanAmount(loanQueryBean.getLoanAmount());
        }
        Map textcard = new HashMap<>();
        textcard.put("title", "消息通知");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        //具体业务描述
        textcard.put("description", "<div class=\"gray\">" + sf.format(date) + "</div> <div class=\"normal\">尊敬的分期经理:"+loanQueryBean.getCustomerName()+genderType==null?"":genderType+"在您推荐的富择金服中提交了金额为"+loanQueryBean.getExpectedLoanAmount()+"元贷款服务需求.</div><div class=\"highlight\">请尽快查看</div>");
        //TODO: url为企业微信页面的具体地址   待正式微信号，上线，修改URL
        textcard.put("url", "http://" + this.believeIp + "/?id=" + loanQueryBean.getId() + "#/creditloan/detail");
        textcard.put("btntxt", "详情");
        Map body = new HashMap<>();
        body.put("touser", userId);
        body.put("msgtype", "textcard");
        body.put("agentid", agentId);
        body.put("textcard", textcard);
        return sendMessage(body);
    }

    private ResultBean<Map> sendMessage(Map params) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AccessToken accessToken = getToken(false, secret);
            if (accessToken != null) {
                String send_message_url = SENDMESSAGE.replace("ACCESS_TOKEN", accessToken.getAccess_token());
                String paramsJson = objectMapper.writeValueAsString(params);
                System.out.println(paramsJson);
                String send_message_result = HttpUtils.doPost(send_message_url, "utf-8", paramsJson);

                Map map = objectMapper.readValue(send_message_result, HashMap.class);
                if (map != null ) {
                    if (!map.containsKey("errcode") || "0".equals(map.get("errcode"))) {
                        return ResultBean.getSucceed().setD(map);
                    } else {
                        return ResultBean.getFailed().setM(map.get("errcode") + ":" + map.get("errmsg"));
                    }
                } else {
                    return ResultBean.getFailed().setM("返回结果未知");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultBean.getFailed().setM("获取token失败");
    }

    public ResultBean<WechatLoginResult> oAuth2GetUserByCode(String token, String code, String agentId) {
        WechatLoginResult result = new WechatLoginResult();
        String menuUrl = GET_OAUTH2_URL.replace("ACCESS_TOKEN", token).replace("CODE", code).replace("AGENTID", agentId + "");
        String logininfo = HttpUtils.doGet(menuUrl);
        JSONObject jsonObject = null;
        if (logininfo != null) {
            try {
                jsonObject = JSONObject.fromObject(logininfo);
                logger.info("jsonObject: " + jsonObject);
                if (jsonObject.getString("UserId") != null && jsonObject.getString("UserId").length() > 0) {
                    result.setErrmsg(jsonObject.getString("errmsg"));
                    result.setErrcode(jsonObject.getString("errcode"));
                    result.setUserId(jsonObject.getString("UserId"));
                } else {
                    result.setErrmsg(jsonObject.getString("errmsg"));
                    result.setErrcode(jsonObject.getString("errcode"));
                }
            } catch (Exception e) {
                result.setErrmsg(e.getMessage());
                result.setErrcode("42001");
            }
        }
        return ResultBean.getSucceed().setD(result);
    }

    public ResultBean<String> oAuth2Url(String redirect_uri) {
        try {
            redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        return ResultBean.getSucceed().setD(CODE.replace("CORPID", getCorpId()).replace("AGENTID", getAgentId()).replace("REDIRECT_URI", redirect_uri));
    }

    public String getCorpId() {
        return corpId;
    }

    public String getSecret() {
        return secret;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getBelieveIp() {
        return believeIp;
    }

    public static final String SAVEORG = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=ACCESS_TOKEN";

    public static final String UPDATEORG = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=ACCESS_TOKEN";

    public static final String DELETEORG = "https://qyapi.weixin.qq.com/cgi-bin/department/delete";

    public static final String CREATEEMPLOYEE = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN";

    public static final String GETEMPLOYEE = "https://qyapi.weixin.qq.com/cgi-bin/user/get";

    public static final String DELETEEMPLOYEE = "https://qyapi.weixin.qq.com/cgi-bin/user/delete";

    public static final String DELETEEMPLOYEES = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=ACCESS_TOKEN";

    public static final String GETEMPLOYEEBYORG = "https://qyapi.weixin.qq.com/cgi-bin/user/list";

    public static final String UPDATEEMPLOYEE = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN";



    public ResultBean getEmployee(String userId) throws Exception {
        Map<Object, Object> params = new HashMap<>();
        AccessToken accessToken = this.accessToken().getD();
        params.put("access_token", accessToken.getAccess_token());
        if (userId != null) {
            params.put("userid", userId);
        }
        String employee = HttpUtils.doGet(GETEMPLOYEE, "UTF-8", params);
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(employee, Map.class);
        if (0 == (int) map.get("errcode")) {
            return ResultBean.getSucceed().setD(map);
        }
        if (40014 == (int) map.get("errcode")) {
            Map<Object, Object> params2 = new HashMap<>();
            AccessToken token2 = this.accessToken().getD();
            params2.put("access_token", token2.getAccess_token());
            if (userId != null) {
                params2.put("id", userId);
            }
            String employee2 = HttpUtils.doGet(GETEMPLOYEE, "UTF-8", params2);
            Map map2 = objectMapper.readValue(employee2, Map.class);
            if (0 == (int) map2.get("errcode")) {
                return ResultBean.getSucceed().setD(map2);
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean getAngent() throws Exception {
        AccessToken accessTokenW = this.accessTokenW().getD();
        String GETANENTURL = ANENTURL.replace("ACCESS_TOKEN", accessTokenW.getAccess_token()).replace("AGENTID",this.agentId);
        String angentInfo = HttpUtils.doGet(GETANENTURL);
        System.out.println(angentInfo);
        return null;
    }
}
