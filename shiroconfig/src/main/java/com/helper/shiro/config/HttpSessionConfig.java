package com.helper.shiro.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/29 - 上午9:58
 * Created by IntelliJ IDEA.
 */
@Configuration
@EnableCaching
@EnableRedisHttpSession
public class HttpSessionConfig {

    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration());
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        // 设置缓存有效期一小时
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory()))
                .cacheDefaults(redisCacheConfiguration).build();
    }

}