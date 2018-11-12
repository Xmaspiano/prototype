package com.helper.route.shiro;

import com.helper.route.entity.AccountEntity;
import com.helper.route.service.AccountService;
import com.helper.shiro.Bean.AccountInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.NoSuchElementException;

/**
 *
 *
 * @author XmasPiano
 * @date 2018/3/1 上午10:16
 * @param
 * @return
 */
public class MySqlShiroRealm extends AuthorizingRealm implements CacheManagerAware {
    @Value("${admin.loginid}")
    private String adminName;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.lastname}")
    private String lastname;

    @Autowired
    AccountService accountService;

    /**
     * 认证.登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户输入的token
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        String username = utoken.getUsername();

        AccountEntity accountEntity = new AccountEntity();

        //如果用户名是管理员
        if(this.adminName.equals(username)) {
            accountEntity.setAccount(adminName);
            accountEntity.setPassword(password);
            //验证管理员
            return new SimpleAuthenticationInfo(accountEntity, this.password, getName());
        }

        //验证用户
        try {
            accountEntity = accountService.findByAccount(username);
        }catch (NoSuchElementException nee){
            throw new UnknownAccountException();
        }

        //包装账户信息
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccount(accountEntity.getAccount());
        accountInfo.setUserName("no name");

        return new SimpleAuthenticationInfo(accountInfo, accountEntity.getPassword(), this.getClass().getName());
    }

    /**
     * 授权
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("user");
        AccountEntity accountEntity = (AccountEntity) SecurityUtils.getSubject().getPrincipal();
        if(this.adminName.equals(accountEntity.getAccount())) {
            authorizationInfo.addRole("manager");
        }
        return authorizationInfo;
    }

    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

}