package com.fuze.bcp.auth.business;

import com.fuze.bcp.api.auth.jwt.JwtTokenUtil;
import com.fuze.bcp.api.auth.service.ILoginTokenBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by lenovo on 2017-05-30.
 */
@Service
public class BizLoginTokenService implements ILoginTokenBizService {


    @Autowired
    private JedisPool jedisPool;

    public JwtTokenUtil getJwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Override
    public String setLoginTokenInfo(String username, String clientType) throws InterruptedException {
        Jedis jedis = jedisPool.getResource();
        try {
            String token = this.getJwtTokenUtil().generateToken(username, clientType);
            //保存到Redis
            String key = LOGIN_USER_HASH + username;
            jedis.hset(key, TOKEN_TYPE + clientType, token);
            jedis.expire(key, USERINFO_TIMEOUT);

            return token;
        } finally {
            jedis.close();
        }
        //生成JWT

    }

    @Override
    public String getLoginTokenInfo(String username, String clientType) {
        Jedis jedis = jedisPool.getResource();
        try {

            String key = LOGIN_USER_HASH + username;
            String token = jedis.hget(key, TOKEN_TYPE + clientType);
            if (token == null) {
                return null;
            } else {
                return token;
            }
        } finally {
            jedis.close();
        }

    }

    @Override
    public String refreshLoginTokenInfo(String username, String clientType) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = LOGIN_USER_HASH + username;
            String token = this.getJwtTokenUtil().generateToken(username, clientType);
            jedis.hset(key, TOKEN_TYPE + clientType, token);
            return token;
        } finally {
            jedis.close();
        }

    }

    @Override
    public void clearLoginToken(String token) {
        Jedis jedis = jedisPool.getResource();
        try {
            String username = this.getJwtTokenUtil().getUsernameFromToken(token);
            String audience = this.getJwtTokenUtil().getAudienceFromToken(token);
            String type = TOKEN_TYPE + audience;
            String key = LOGIN_USER_HASH + username;
            jedis.hdel(key,type);
        }finally {
            jedis.close();
        }

    }
/*

    @Override
    public void setUserInfo(String username, String token, String clientType, String deviceId, String webId, String openId) throws InterruptedException {
        String key = LOGIN_TOKEN_HASH + token;
        jedis.hset(key, USER_ID, username);
        jedis.hset(key, CLIENT_TYPE, clientType);
        jedis.hset(key, "DeviceID", deviceId);
        jedis.hset(key, "WebID", webId);
        jedis.hset(key, "OpenID", openId);
        jedis.expire(key, TOKEN_TIMEOUT);
    }

    @Override
    public String[] getUserInfo(String token, String clientType) {
        String key = LOGIN_TOKEN_HASH + token;
        String uid = jedis.hget(key, USER_ID);
        if (uid == null) {
            return null;
        } else {
            String[] result = new String[]{uid, jedis.hget(key, CLIENT_TYPE)};
            return result;
        }
    }

    @Override
    public String getLoginUserIdByToken(String token, String clientType) {
        String key = LOGIN_TOKEN_HASH + token;
        String uid = jedis.hget(key, USER_ID);

        return uid;
    }

    @Override
    public void clearLoginUserByToken(String token) {

    }

    @Override
    public void clearLoginByUserID(String userId) {

    }
*/
}
