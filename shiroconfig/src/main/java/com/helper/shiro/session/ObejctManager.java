package com.helper.shiro.session;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/30 - 上午10:26
 * Created by IntelliJ IDEA.
 */
public interface ObejctManager{
    byte[] get(byte[] key);

    byte[] set(byte[] key, byte[] value);

    byte[] set(byte[] key, byte[] value, int expire);

    void del(byte[] key);

    void flushDB();

    Long dbSize();

    Set<byte[]> keys(String pattern);

//    String getHost();
//
//    int getPort();
//
//    int getExpire();
//
//    int getTimeout();
//
//    String getPassword();

}
