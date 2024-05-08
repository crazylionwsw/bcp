package com.fuze.bcp.pad;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class HttpUploadUtil {
    public static void main(String[] args) {
        //请求地址
        String urlStr = "http://192.168.0.37:8082/json/file/upload";
        //要上传的文件
        List<String[]> filesMap = new ArrayList<>();
        //文件名
        String[] filepath = new String[]{"file","test.mp4","video/mpeg4","/Volumes/SSD/Users/sean/Desktop/test.mp4"};
        filesMap.add(filepath);
        HttpFormFileRequest result = new HttpFormFileRequest(urlStr, null, filesMap);
        try {
            byte[] bytes = result.sendPost();
            System.out.println(new String(bytes));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
