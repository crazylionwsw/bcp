package com.fuze.bcp.api.auth.service;

public interface ILoginCacheService  {
    public  static final String LOGIN_USER_HASH ="L:U:";
    public static final String LOGIN_TOKEN_HASH ="L:T:";
    public static final String TOKEN_TYPE="Token-";//登录的token的类型
    public static final String USER_ID="UserID";//用户ID
    public static final String CLIENT_TYPE="ClientType";//登录类型
    public static final int TOKEN_TIMEOUT= 300;//设置token过期时间为2分钟
    public static final int  USERINFO_TIMEOUT=604800;//设置用户过期时间为一星期


    /**
     *添加用户的token
     * @param userID//用户id
     * @param newToken//token
     * @param client_type//连接类型
     */
    void setLoginUserInfo(String userID, String newToken, String client_type) throws InterruptedException;

    /**
     * 获取用户的token
     * @param userID
     * @param client_type
     * @return
     */
    String[] getLoginUserInfo(String userID, String client_type);

    /**
     * 刷新用户的token
     * @param userID
     * @param newToken
     * @param client_type
     */
    void refreshLoginUserInfo(String userID, String newToken, String client_type);

    /**
     *设置token信息
     * @param userID //用户id
     * @param newToken//token
     * @param client_type//连接类型
     * @param DeviceID//app设备ip
     * @param WebID//网站客户端ip
     * @param OpenID//微信用户opendID
     */
    void  setTokenInfo(String userID, String newToken, String client_type, String DeviceID, String WebID, String OpenID) throws InterruptedException;

    /**
     *  获取token值
     * @param token
     * @param client_type
     * @returntoken 和 登录类型
     */
    String[] getTokenInfo(String token, String client_type);

    /**
     * 根据token信息，清除用户登录信息
     * @param token
     */
    void clearLoginUserByToken(String token);

    /**
     * 根据用户ID，清除用户登录信息
     * @param userid
     */
    void clearLoginByUserID(String userid);
}
