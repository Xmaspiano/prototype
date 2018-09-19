package com.helper.system.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 * @author XmasPiano
 * @date 2018/3/1 上午10:16
 * @param
 * @return
 */
@Configuration
public class ShiroConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put(contextPath+"/logout", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put(contextPath+"/static/**", "anon");
        filterChainDefinitionMap.put(contextPath+"/eureka/**", "anon");
        filterChainDefinitionMap.put(contextPath+"/ajaxLogin", "anon");
        filterChainDefinitionMap.put(contextPath+"/login", "anon");
//        filterChainDefinitionMap.put("/**", "authc");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /***
     * 授权所用配置
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }


    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器
     *
     */
    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
//
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        // 设置realm.
//        securityManager.setRealm(myShiroRealm());
//
//        // 注入缓存管理器;
//        // 这个如果执行多次，也是同样的一个对象;
////        securityManager.setCacheManager(cacheManager);
//        // session管理器
//        securityManager.setSessionManager(webSessionManager);
//        //注入记住我管理器;
////        securityManager.setRememberMeManager(rememberMeManager());
//        return securityManager;
    }
//
//    @Bean
//    public DefaultWebSessionManager configWebSessionManager(RedisSessionDao redisSessionDao){
//        DefaultWebSessionManager manager = new DefaultWebSessionManager();
//        // 加入缓存管理器
////        manager.setCacheManager(cacheManager);
//        // 设置SessionDao
//        manager.setSessionDAO(redisSessionDao);
//        // 删除过期的session
//        manager.setDeleteInvalidSessions(true);
//        // 设置全局session超时时间
//        manager.setGlobalSessionTimeout(1800000L);
//        // 是否定时检查session
//        manager.setSessionValidationSchedulerEnabled(true);
//
//        return manager;
//    }

//    /**
//     * RedisSessionDAO shiro sessionDao层的实现 通过redis
//     * <p>
//     * 使用的是shiro-redis开源插件
//     */
//    @Bean
//    public RedisSessionDao redisSessionDAO(RedisTemplate redisTemplate) {
//        RedisSessionDao redisSessionDAO = new RedisSessionDao();
//        redisSessionDAO.setRedisTemplate(redisTemplate);
//        return redisSessionDAO;
//    }
//
//
//    @Bean
//    @Primary
//    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory){
//        StringRedisTemplate redisTemplate = new StringRedisTemplate();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//        return redisTemplate;
//    }
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory(){
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisConnectionFactory.setpoolConfig();
//        jedisConnectionFactory.setusePool(true);
//        jedisConnectionFactory.sethostName(${redis.host});
//        jedisConnectionFactory.setport(${redis.port});
//        jedisConnectionFactory.setpassword(${redis.password});
//        jedisConnectionFactory.settimeout(${redis.timeout});
//        jedisConnectionFactory.setdatabase(${redis.database});
//        return jedisConnectionFactory;
//    }


    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("localhost");
        redisManager.setPort(6379);
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(0);
        // redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
}
