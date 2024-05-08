package com.fuze.bcp.redis;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by Lily on 2017/5/26.
 */
public class KryoRedisTemplate extends RedisTemplate<String, Object> {
    public KryoRedisTemplate() {
        KryoRedisSerializer Serializer = new KryoRedisSerializer();
        this.setKeySerializer(Serializer);
        this.setValueSerializer(Serializer);
        this.setHashKeySerializer(Serializer);
        this.setHashValueSerializer(Serializer);
        this.setDefaultSerializer(Serializer);
    }

    public KryoRedisTemplate(RedisConnectionFactory connectionFactory) {
        this();
        this.setConnectionFactory(connectionFactory);
        this.afterPropertiesSet();
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }
}
