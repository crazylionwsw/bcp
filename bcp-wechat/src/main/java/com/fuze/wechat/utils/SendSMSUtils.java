package com.fuze.wechat.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class SendSMSUtils {

    private static class Holder {
        private static SendSMSUtils singleton = new SendSMSUtils();
    }

    private SendSMSUtils() {
    }

    public static SendSMSUtils getSingleton() {
        return Holder.singleton;
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
        String result = "";
        try {
            String contentUrlEncode = URLEncoder.encode(content + " 【富择】", encode);
            buffer.append("http://m.5c.com.cn/api/send/index.php?username=" + username + "&password_md5=" + password_md5 + "&mobile=" + mobile + "&apikey=" + apikey + "&content=" + contentUrlEncode + "&encode=" + encode);
            URL url = new URL(buffer.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            result = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return  result;
        }
    }

    public String isms(String cell, String content) {
        if (StringUtils.isNoneBlank(cell) && StringUtils.isNoneBlank(content)) {
            List<String> cells  = new ArrayList<String>();
            cells.add(cell);
            return  isms(cells, content);
        } else {
            return "";
        }
    }
}
