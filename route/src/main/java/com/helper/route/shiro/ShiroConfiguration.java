package com.helper.route.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.servlet.Filter;
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

    @Autowired
    HashedCredentialsMatcher hashedCredentialsMatcher;

    @Autowired
    DefaultWebSessionManager sessionManager;

    @Autowired
    CacheManager cacheManager;


    @Bean
    @Primary
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration[shirFilter]");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        filtersMap.put("authz", new MyAuthzFilter());

        shiroFilterFactoryBean.setFilters(filtersMap);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/logout", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/eureka/**", "anon");
//        filterChainDefinitionMap.put("/ajaxLogin", "anon");
        filterChainDefinitionMap.put("/login/in", "anon");
//        filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/**", "authc,authz");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/webapp/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/webapp/index");

        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    @Primary
    public MyShiroRealm myShiroRealm() {
        System.out.println("ShiroConfiguration[myShiroRealm]");

        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return myShiroRealm;
    }

    @Bean
    @Primary
    public SecurityManager securityManager() {
        System.out.println("ShiroConfiguration[securityManager]");

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager);
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

}
