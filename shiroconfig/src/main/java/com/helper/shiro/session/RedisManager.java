package com.helper.shiro.session;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/29 - 下午2:22
 * Created by IntelliJ IDEA.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Set;

public class RedisManager implements ObejctManager {
    private RedisConnectionFactory redisConnectionFactory;

    public RedisManager(RedisConnectionFactory redisConnectionFactory){
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public byte[] get(byte[] key) {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        byte[] r = redisConnection.get(key);
        redisConnection.close();
        return r;
    }

    @Override
    public byte[] set(byte[] key, byte[] value) {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.set(key,value);
        redisConnection.close();
        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.set(key,value);
        if (expire != 0) {
            redisConnection.expire(key, expire);
        }
        redisConnection.close();
        return value;
    }

    @Override
    public void del(byte[] key) {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.del(key);
        redisConnection.close();
    }

    @Override
    public void flushDB() {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.flushDb();
        redisConnection.close();
    }

    @Override
    public Long dbSize() {
        Long dbSize = 0L;
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        dbSize = redisConnection.dbSize();
        redisConnection.close();
        return dbSize;
    }

    @Override
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        keys = redisConnection.keys(pattern.getBytes());
        redisConnection.close();;
        return keys;
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
//        return null;
//    }

//    @Override
//    public String getHost() {
//        return this.host;
//    }
//
//
//    @Override
//    public int getPort() {
//        return this.port;
//    }
//
//
//    @Override
//    public int getExpire() {
//        return this.expire;
//    }
//
//
//    @Override
//    public int getTimeout() {
//        return this.timeout;
//    }
//
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }

}
