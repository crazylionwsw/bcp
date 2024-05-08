package com.fuze.bcp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by CJ on 2017/10/20.
 */
@Service
@Scope("singleton")
public class MutexService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * @param businessKey
     * @param userId
     * @param operation
     */
    public void setMutex(String businessKey, String userId, String operation) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = "O:U:" + userId;
            String field = "Business-" + businessKey;
            String value = userId + operation;
            Long l1 = jedis.hset(key, field, value);
            Long l2 = jedis.expire(key, 300);
        } finally {
            jedis.close();
        }
    }

    public String getMutex(String businessKey, String userId) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = "O:U:" + userId;
            String field = "Business-" + businessKey;
            return jedis.hget(key, field);
        } finally {
            jedis.close();
        }
    }

    public Long delMutex(String businessKey, String userId) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = "O:U:" + userId;
            String field = "Business-" + businessKey;
            return jedis.hdel(key, field);
        } finally {
            jedis.close();
        }
    }

    public synchronized boolean lockSaveObject(String businessKey, String userId, String operation) {
        String value = this.getMutex(businessKey, userId);
        if (value == null || !value.equals(userId + operation)) {
            this.setMutex(businessKey, userId, operation);
            return true;
        }
        return false;
    }

    public synchronized Long unLockSaveObject(String businessKey, String userId) {
        Long value = this.delMutex(businessKey, userId);
        return value;
    }

}
