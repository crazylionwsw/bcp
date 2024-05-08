package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.msg.service.ISMSService;
import com.fuze.bcp.utils.EncryUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/7/20.
 */
@Service
public class SMSServiceImpl implements ISMSService {

    Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);

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

            return result;

        } catch (Exception e) {
            logger.error("发送短息失败", e);
        }
        return "";
    }

}
