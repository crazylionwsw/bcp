package com.fuze.bcp.api.auth.service;

/**
 * Created by lenovo on 2017-05-30.
 */
public interface ILoginTokenBizService {

    public static final String LOGIN_USER_HASH = "L:U:";
    public static final String LOGIN_TOKEN_HASH = "L:T:";
    public static final String LOGIN_VERIFYCODE_HASH = "L:V:";
    public static final String TOKEN_TYPE = "Token-";//登录的token的类型
    public static final String USER_ID = "UserID";//用户ID
    public static final String CLIENT_TYPE = "ClientType";//登录类型
    public static final String VERIFYCODE_TYPE = "VerifyCode-";//VerifyCode类型
    public static final int TOKEN_TIMEOUT = 300;//设置token过期时间为2分钟
    public static final int  USERINFO_TIMEOUT = 604800
            ;//设置用户过期时间为一星期
    public static final int  VERIFYCODE_TIMEOUT = 3000;//设置VerifyCode过期时间为5分钟

    /**
     *添加用户的token
     * @param username//用户名
     * @param clientType//连接类型
     */
    String setLoginTokenInfo(String username, String clientType) throws InterruptedException;

    /**
     * 获取用户的token
     * @param username
     * @param clientType
     * @return
     */
    String getLoginTokenInfo(String username, String clientType);

    /**
     * 刷新用户的token
     * @param username
     * @param clientType
     */
    String refreshLoginTokenInfo(String username, String clientType);


    /**
     * 根据token信息，清除用户登录信息
     * @param token
     */
    void clearLoginToken(String token);

   /* *//**
     *设置token信息
     * @param username //用户id
     * @param token//token
     * @param clientType//连接类型
     * @param deviceId//app设备ip
     * @param webId//网站客户端ip
     * @param openId//微信用户opendID
     *//*
    void  setUserInfo(String username, String token, String clientType, String deviceId, String webId, String openId) throws InterruptedException;

    *//**
     * 这个方法实际是根据token值获取登录的用户ID和客户端类型
     * @param token
     * @param clientType
     * @returntoken 和 登录类型
     *//*
    String[] getUserInfo(String token, String clientType);


    *//**
     * 根据用户的token值和客户端类型，获取登录的用户ID
     * @param token
     * @param clientType
     * @return
     *//*
    String getLoginUserIdByToken(String token,String clientType);

    *//**
     * 根据token信息，清除用户登录信息
     * @param token
     *//*
    void clearLoginUserByToken(String token);

    *//**
     * 根据用户ID，清除用户登录信息
     * @param userid
     *//*
    void clearLoginByUserID(String username);*/

}
