package com.fuze.bcp.utils;

import java.io.File;

/**
 * Created by sean on 2017/6/24.
 * 系统环境变量
 */
public class Environment {

    private final static String ROOT = "fuze.erp.root";
    private final static String TMP = "fuze.erp.tmp";
    /**
     * 获取产品的默认持久存储路径
     * @return
     */
    public final static String getFuzeRootPath(){
        String str =System.getProperty(ROOT);
        if(str == null || "".equals(str)){
            String userHome = System.getProperty("user.home");
            File file = new File(userHome+"/fzerp/root");
            if(!file.exists()){
                file.mkdirs();
            }
            System.setProperty(ROOT,file.getAbsolutePath());
            str = System.getProperty(ROOT);
        }
        return str;
    }

    /**
     * 获取产品的默认临时路径
     * @return
     */
    public final static String getFuzeTmpPath(){
        String str =System.getProperty(TMP);
        if(str == null || "".equals(str)){
            String userHome = System.getProperty("user.home");
            File file = new File(userHome+"/fzerp/tmp");
            if(!file.exists()){
                file.mkdirs();
            }
            System.setProperty(TMP,file.getAbsolutePath());
            str = System.getProperty(TMP);
        }
        return str;
    }

    public static void main(String[] args){
        System.out.println(Environment.getFuzeRootPath());
        System.out.println(Environment.getFuzeTmpPath());
    }
}
