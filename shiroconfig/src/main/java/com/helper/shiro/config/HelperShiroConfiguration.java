package com.helper.shiro.config;

import com.helper.shiro.cache.manager.RedisShiroCacheManager;
import com.helper.shiro.session.RedisManager;
import com.helper.shiro.session.ShiroSessionDAO;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@EnableCaching
@EnableRedisHttpSession
public class HelperShiroConfiguration{
    private static final Logger LOGGER = LoggerFactory.getLogger(HelperShiroConfiguration.class);

//    @Autowired
//    RedisConnectionFactory redisConnectionFactory;
//
//    @Autowired
//    RedisCacheManager redisCacheManager;

    @Bean
    @ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("HelperShiroConfiguration[shirFilter]");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //当请求的URL端口不是8081时，跳转到schemal://serverName:8081?queryString
//        filterChainDefinitionMap.put("/**","port[8888]");

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
    @ConditionalOnMissingBean(HashedCredentialsMatcher.class)
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        System.out.println("HelperShiroConfiguration[hashedCredentialsMatcher]");
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }


    /***
     * 授权所用配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DefaultAdvisorAutoProxyCreator.class)
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        System.out.println("HelperShiroConfiguration[getDefaultAdvisorAutoProxyCreator]");
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
    @ConditionalOnMissingBean(AuthorizationAttributeSourceAdvisor.class)
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        System.out.println("HelperShiroConfiguration[authorizationAttributeSourceAdvisor]");
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器
     *
     */
    @Bean
    @ConditionalOnMissingBean(LifecycleBeanPostProcessor.class)
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        System.out.println("HelperShiroConfiguration[getLifecycleBeanPostProcessor]");
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    @ConditionalOnMissingBean(SecurityManager.class)
    public SecurityManager securityManager() {
        System.out.println("HelperShiroConfiguration[securityManager]");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(shiroCacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager shiroCacheManager() {
        System.out.println("HelperShiroConfiguration[shiroCacheManager]");
        RedisShiroCacheManager shiroCacheManager = new RedisShiroCacheManager(redisCacheManager());
        return shiroCacheManager;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    @ConditionalOnMissingBean(DefaultWebSessionManager.class)
    public DefaultWebSessionManager sessionManager() {
        System.out.println("HelperShiroConfiguration[sessionManager]");
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(shiroSessionDAO());
        return sessionManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    @ConditionalOnMissingBean(AbstractSessionDAO.class)
    public ShiroSessionDAO shiroSessionDAO() {
        System.out.println("HelperShiroConfiguration[shiroSessionDAO]");
        ShiroSessionDAO shiroSessionDAO = new ShiroSessionDAO(redisManager());
        return shiroSessionDAO;
    }

    @Bean
    @ConditionalOnMissingBean(RedisManager.class)
    public RedisManager redisManager(){
        System.out.println("HelperShiroConfiguration[redisManager]");
        return new RedisManager(connectionFactory());
    }

    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedisConnectionFactory connectionFactory() {
        System.out.println("HelperShiroConfiguration[connectionFactory]");
        return new JedisConnectionFactory(new RedisStandaloneConfiguration());
    }

    @Bean
    @ConditionalOnMissingBean(RedisCacheManager.class)
    public RedisCacheManager redisCacheManager() {
        System.out.println("HelperShiroConfiguration[redisCacheManager]");
        // 设置缓存有效期一小时
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory()))
                .cacheDefaults(redisCacheConfiguration).build();
    }
}
