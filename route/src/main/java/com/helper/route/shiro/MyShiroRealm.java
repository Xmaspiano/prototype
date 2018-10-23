package com.helper.route.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/13 - 下午4:46
 * Created by IntelliJ IDEA.
 */
public class MyShiroRealm extends AuthorizingRealm {
//    @Resource
//    private UserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
//        for (SysRole role : userInfo.getRoleList()) {
//            authorizationInfo.addRole(role.getRole());
//            for (SysPermission p : role.getPermissions()) {
//                authorizationInfo.addStringPermission(p.getPermission());
//            }
//        }
        return authorizationInfo;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                //用户名
                username,
                //密码
                getPassword(),
                //salt=username+salt
                ByteSource.Util.bytes("adminadmin"),
                //realm name
                getName()
        );
        return authenticationInfo;
    }

    public String getPassword(){
        String algorithmName = "md5";
        String username = "admin";
        String password = "admin";
        String salt1 = username;
        String salt2 = "admin";
        int hashIterations = 2;

        SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
        return hash.toHex();
    }
}

