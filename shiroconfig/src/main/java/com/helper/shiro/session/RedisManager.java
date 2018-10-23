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
        return redisConnection.get(key);
    }

    @Override
    public byte[] set(byte[] key, byte[] value) {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.set(key,value);
        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.set(key,value);
        if (expire != 0) {
            redisConnection.expire(key, expire);
        }
        return value;
    }

    @Override
    public void del(byte[] key) {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.del(key);
    }

    @Override
    public void flushDB() {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.flushDb();
    }

    @Override
    public Long dbSize() {
        Long dbSize = 0L;
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        dbSize = redisConnection.dbSize();
        return dbSize;
    }

    @Override
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        keys = redisConnection.keys(pattern.getBytes());
        return keys;
    }

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
