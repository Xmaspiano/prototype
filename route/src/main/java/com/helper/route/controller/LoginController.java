package com.helper.route.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/15 - 上午10:42
 * Created by IntelliJ IDEA.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    RedisCacheManager redisCacheManager;

    @Autowired
    DefaultWebSessionManager defaultWebSessionManager;

    @PostMapping(value="/in")
    public Map<String, Object> loginPost(String username, String password, boolean rememberMe,HttpSession httpSession) {
        Map<String, Object> map = new HashMap<>();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(username,password,rememberMe);
        Subject subject = SecurityUtils.getSubject();

        String error = "";
        try {
            usernamePasswordToken.setRememberMe(rememberMe);
            //完成登录
            subject.login(usernamePasswordToken);

            System.out.println(subject.getPrincipal());

            System.out.println(subject.getPrincipal());

            System.out.println(httpSession.getId());
            System.out.println(subject.getSession().getId());
            System.out.println(new DefaultSessionKey(subject.getSession().getId()));

            org.apache.shiro.session.Session kickoutSession
                    = defaultWebSessionManager.getSession(
                    new DefaultSessionKey(subject.getSession().getId())
            );

            kickoutSession.getAttributeKeys().forEach(obj-> System.out.println(kickoutSession.getAttribute(obj)));
            System.out.println("==1==");
            SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(subject.getSession().getId()))
                    .getAttributeKeys().forEach(obj->
                    System.out.println(SecurityUtils.getSecurityManager().getSession(
                            new DefaultSessionKey(subject.getSession().getId())).getAttribute(obj)));
            System.out.println("==2==");
            System.out.println(defaultWebSessionManager.getSessionId(new DefaultSessionKey(subject.getSession().getId())));

            System.out.println("==3==");
            defaultWebSessionManager.getSession(new DefaultSessionKey(subject.getSession().getId()))
                    .getAttributeKeys().forEach(obj->
                    System.out.println(defaultWebSessionManager.getSession(
                            new DefaultSessionKey(subject.getSession().getId())).getAttribute(obj)));
            System.out.println("==4==");
            System.out.println(defaultWebSessionManager.getSessionIdCookie().getName());
            System.out.println(defaultWebSessionManager.getSessionIdCookie().getValue());

            subject.getSession().getAttributeKeys().forEach(
                    obj -> System.out.println(subject.getSession().getAttribute(obj))
            );


            map.put("status", true);
            map.put("error", "登录成功！");
            //此处会被Shiro重定向至success页面
            return map;

        } catch (UnknownAccountException uae) {
            error = "用户不存在,请申请用户!!!";
        } catch (IncorrectCredentialsException ice) {
            error = "用户名密码错误,请确认后重新登陆!!!";
        } catch (LockedAccountException lae) {
            error = "账户已被锁定，无法登陆!!!";
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            error = "unexpected condition...";
        } catch(Exception e) {
            error = "登录异常：" + e.getMessage();
        }

        map.put("status", false);
        map.put("error", error);
        return map;
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login";
    }
}
