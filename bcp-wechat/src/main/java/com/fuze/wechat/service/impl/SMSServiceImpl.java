package com.fuze.wechat.service.impl;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.service.ISMSService;
import com.fuze.wechat.utils.EncryUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by CJ on 2017/7/20.
 */
@Service
public class SMSServiceImpl implements ISMSService {

    Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    public ResultBean<Boolean> sendCode(String cell){
        Jedis jedis = jedisPool.getResource();

        String code = null;
        try {
            ResultBean<String> resultBean = this.getCode(cell);
            if (resultBean.isSucceed() && !StringUtils.isEmpty(resultBean.getD())) {
                code = resultBean.getD();
            }
            if (StringUtils.isEmpty(code)) {
                code = String.valueOf(new Random().nextInt(899999) + 100000);
                jedis.hset("VC_" + cell, "VerificationCode", code);
                jedis.expire("VC_" + cell, 60 * 10);
            }

            logger.info("手机号为：" + cell +"验证码为：" + code);
            List list= new ArrayList();
            list.add(cell);
            String result = this.send(list, "【富择】 " + code +" (富择微信公众号验证码，十分钟内有效)");
            if (!StringUtils.isEmpty(result) && result.contains("success")){
                return ResultBean.getSucceed().setD(true);
            } else if (!StringUtils.isEmpty(result) && result.contains("error")) {
                return ResultBean.getFailed().setM("短信发送失败，请检查手机号是否正确！");
            }
            return ResultBean.getFailed().setM("短信发送失败，请检查手机号是否正确！");
        } finally {
            jedis.close();
        }
    }

    public ResultBean<String> getCode(String cell){
        Jedis jedis = jedisPool.getResource();
        try {
            String code = jedis.hget("VC_" + cell, "VerificationCode");
            logger.info("从redis中获取手机号为：" + cell +"验证码为：" + code);
            return ResultBean.getSucceed().setD(code);
        } finally {
            jedis.close();
        }
    }

    public String send(String cells, String content) {
        List cell = new ArrayList();
        if (cells != null && cells != "") {
            String[] split = cells.split(",");
            for (String ce : split) {
                cell.add(ce);
            }
        }
        String result = this.isms(cell, content);
        return result;
    }

    public String send(List<String> cells, String content) {
        String result = this.isms(cells, content);
        return result;
    }

    public String isms(List<String> cells, String content) {

        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        StringBuffer buffer = new StringBuffer();

        String encode = "UTF-8";

        String username = "fuze";
        String password_md5 = EncryUtil.MD5("998Pa88word");
        String mobile = StringUtils.join(cells, ",");
        String apikey = "3fb346881ab55659b86d98e35b368973";

        try {
            String contentUrlEncode = URLEncoder.encode(content + " 【富择】", encode);

            buffer.append("http://m.5c.com.cn/api/send/index.php?username=" + username + "&password_md5=" + password_md5 + "&mobile=" + mobile + "&apikey=" + apikey + "&content=" + contentUrlEncode + "&encode=" + encode);

            URL url = new URL(buffer.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String result = reader.readLine();
            logger.info("短信发送结果：" + result);

            return result;

        } catch (Exception e) {
            logger.error("发送短息失败", e);
        }
        return "";
    }

}
