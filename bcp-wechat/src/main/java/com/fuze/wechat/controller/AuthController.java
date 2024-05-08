package com.fuze.wechat.controller;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.PublicUser;
import com.fuze.wechat.service.IMiniProgramService;
import com.fuze.wechat.service.IPublicUserService;
import com.fuze.wechat.service.IWeChatPublicService;
import com.fuze.wechat.utils.WxUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by CJ on 2018/4/23.
 */
@RestController
@RequestMapping("/json")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String token = "abc";

    @Autowired
    private IWeChatPublicService iWeChatPublicService;

    @Autowired
    private IMiniProgramService iMiniProgramService;

    @Autowired
    IPublicUserService iPublicUserService;

    @RequestMapping(value = "/oauth2")
    public ResultBean<String> Oauth2API(HttpServletRequest request, @RequestParam(value = "url", required = false) String url, @RequestParam(value = "sab", required = false, defaultValue = "snsapi_base") String sab) {
        String resultUrl = iWeChatPublicService.getBelieveIp();
        String redirectUrl = "";
        if (resultUrl != null) {
            String backUrl = "http://" + resultUrl;
            logger.info("param url= " + url);
            if (!StringUtils.isEmpty(url)) backUrl += url;
            logger.info("backUrl= " + backUrl);
            redirectUrl = iWeChatPublicService.oAuth2Url(backUrl, sab).getD();
        }
        logger.info("redirectUrl: " + redirectUrl);
        return ResultBean.getSucceed().setD(redirectUrl);
    }

    @RequestMapping(value = "/oauth2url/{code}", method = RequestMethod.GET)
    public ResultBean<PublicUser> Oauth2MeUrl(@PathVariable("code") String code) throws IOException {
        logger.info("code:" + code);
        ResultBean<JSONObject> resultBean = iWeChatPublicService.oath2AccessToken(code);
        if (resultBean.failed()) {
            return ResultBean.getFailed().setM(resultBean.getM());
        }
        if (resultBean.getD() != null && !StringUtils.isEmpty(resultBean.getD().getString("openid"))) {
            JSONObject accessTokenObj = resultBean.getD();
            ResultBean<JSONObject> userResultBean = iWeChatPublicService.getBaseUserInfo(accessTokenObj.getString("openid"));
            if (userResultBean.getD() != null){
                logger.info("userResultBean d :" + userResultBean.getD().toString());
                ResultBean<PublicUser> publicUserResultBean = iPublicUserService.actSavePublicUser(userResultBean.getD());
                if (publicUserResultBean.isSucceed()){
                    return publicUserResultBean;
                }
                return ResultBean.getFailed().setM("保存信息失败");
            }
            return ResultBean.getFailed().setM("获取用户信息失败");
        }
        return ResultBean.getFailed().setM("获取token失败");
    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public ResultBean<JSONObject> getUserInfo(@RequestParam(value = "openId", required = true) String openId) throws IOException {
        logger.info("openId:" + openId);
        return iWeChatPublicService.getBaseUserInfo(openId);
    }


    @RequestMapping("/checktoken")
    public String checktoken(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        logger.info("signature:" + signature);
        logger.info("timestamp:" + timestamp);
        logger.info("nonce:" + nonce);
        logger.info("echostr:" + echostr);
        return echostr;
    }

    @RequestMapping(value = "/getSignatureInfo", method = RequestMethod.GET)
    public ResultBean<JSONObject> getSignatureInfo(HttpServletRequest request, @RequestParam(value = "url", required = true) String url) throws IOException {
        logger.info("调用获取签名信息的方法，传入的参数URL：" + url);
        ResultBean<JSONObject> resultBean = iWeChatPublicService.baseJsapiTicket(false);
        if (resultBean.isSucceed()) {
            logger.info("获取JS_API_TICKET成功：" + resultBean.getD().get("ticket"));
            Map<String, String> signatureInfo = WxUtils.sign((String) resultBean.getD().get("ticket"), url);
            signatureInfo.put("appId", iWeChatPublicService.getAppId());
            logger.info("signatureInfo:" + signatureInfo.toString());
            return ResultBean.getSucceed().setD(JSONObject.fromObject(signatureInfo));
        }
        return resultBean;
    }

    @RequestMapping(value = "/jscode2session", method = RequestMethod.GET)
    public ResultBean<JSONObject> getSession(HttpServletRequest request, @RequestParam(value = "code", required = true) String code) throws IOException {
        logger.info("微信小程序通过Code获取用户的openid，传入的参数code：" + code);
        return iMiniProgramService.actGetSessionByCode(code);
    }

}
