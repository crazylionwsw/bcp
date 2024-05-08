package com.fuze.bcp.msg.starter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Bean(name= "jedisPool")
    @Autowired
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                               @Value("${spring.redis.host}")String host,
                               @Value("${spring.redis.port}")int port,
                               @Value("${spring.redis.timeout}")int timeout,
                               @Value("${spring.redis.password}")String password,
                               @Value("${spring.redis.database}")int database) {
        return new JedisPool(config, host, port, timeout);
    }

    @Bean(name= "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig (@Value("${spring.redis.pool.max-total}")int maxTotal,
                                            @Value("${spring.redis.pool.max-idle}")int maxIdle,
                                            @Value("${spring.redis.pool.max-wait-millis}")int maxWaitMillis ) {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数, 默认8个
        config.setMaxTotal(maxTotal);
        //最大空闲连接数, 默认8个
        config.setMaxIdle(maxIdle);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(maxWaitMillis);
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        //config.setMinEvictableIdleTimeMillis(1800000);
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        //config.setNumTestsPerEvictionRun(3);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
        //config.setSoftMinEvictableIdleTimeMillis(1800000);
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        //config.setTimeBetweenEvictionRunsMillis(-1);
        return config;
    }

}