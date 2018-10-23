package com.helper.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/30 - 上午9:47
 * Created by IntelliJ IDEA.
 */
public class RedisShiroCache implements Cache{
    private final static Map<String,Map> CACHE_MAP_INFO = new HashMap();
    private final static int KEYS =0;
    private final static int VALUES =1;
    private final static int SIZE =2;

    private RedisCacheManager redisCacheManager;
    private String name;

    private LinkedList keys;
    private Collection values;
    private int size;

    public RedisShiroCache(RedisCacheManager redisCacheManager, String name) {
        this.redisCacheManager = redisCacheManager;
        this.name = name;

        if(CACHE_MAP_INFO.get(name) != null){
            Map mp = CACHE_MAP_INFO.get(name);
            this.keys = (LinkedList) mp.get(KEYS);
            this.values = (Collection) mp.get(VALUES);
            this.size = (int) mp.get(SIZE);
        }else{
            Map mp = new HashMap();
            this.keys = (LinkedList) new LinkedList();
            this.values = new LinkedList();
            this.size = 0;

            mp.put(KEYS,this.keys);
            mp.put(VALUES,this.values);
            mp.put(SIZE,this.size);
            CACHE_MAP_INFO.put(name,mp);
        }
    }

    @Override
    public Object get(Object o) throws CacheException {
        return redisCacheManager.getCache(name).get(o);
    }

    @Override
    public Object put(Object o, Object o2) throws CacheException {
        redisCacheManager.getCache(name).put(o,o2);
        keys.add(o);
        values.add(o2);
        return o2;
    }

    @Override
    public Object remove(Object o) throws CacheException {
        Object value = redisCacheManager.getCache(name).get(o);
        redisCacheManager.getCache(name).put(o,null);
        keys.remove(o);
        values.remove(value);
        return value;
    }

    @Override
    public void clear() throws CacheException {
        keys.clear();
        values.clear();
        redisCacheManager.getCache(name).clear();
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public Set keys() {
        return new HashSet(Arrays.asList(keys));
    }

    @Override
    public Collection values() {
        return values;
    }
}
