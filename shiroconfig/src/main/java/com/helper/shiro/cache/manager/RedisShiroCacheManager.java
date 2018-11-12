package com.helper.shiro.cache.manager;

import com.helper.shiro.cache.RedisShiroCache;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
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

    /**
     * Acquires the cache with the specified <code>name</code>.  If a cache does not yet exist with that name, a new one
     * will be created with that name and returned.
     *
     * @param name the name of the cache to acquire.
     * @return the Cache with the given name
     * @throws CacheException if there is an error acquiring the Cache instance.
     */
//    @Override
//    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
//        logger.debug("获取名称为: " + name + " 的RedisCache实例");
//        return new RedisShiroCache(redisCacheManager, name);
//    }

    /**
     * Creates a new {@code Cache} instance associated with the specified {@code name}.
     *
     * @param name the name of the cache to create
     * @return a new {@code Cache} instance associated with the specified {@code name}.
     * @throws CacheException if the {@code Cache} instance cannot be created.
     */
    @Override
    protected Cache createCache(String name) throws CacheException {
        logger.debug("获取名称为: " + name + " 的RedisCache实例");
        return new RedisShiroCache(redisCacheManager, name);
    }
}
