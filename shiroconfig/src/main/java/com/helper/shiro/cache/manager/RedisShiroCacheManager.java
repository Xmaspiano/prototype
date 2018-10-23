package com.helper.shiro.cache.manager;

import com.helper.shiro.cache.RedisShiroCache;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/30 - 上午9:23
 * Created by IntelliJ IDEA.
 */
public class RedisShiroCacheManager extends AbstractCacheManager {
    private static Logger logger = LoggerFactory.getLogger(RedisShiroCacheManager.class);

    private RedisCacheManager redisCacheManager;

    public RedisShiroCacheManager(RedisCacheManager redisCacheManager){
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    protected Cache createCache(String name) throws CacheException {
        logger.debug("获取名称为: " + name + " 的RedisCache实例");
        return new RedisShiroCache(redisCacheManager, name);
    }
}
