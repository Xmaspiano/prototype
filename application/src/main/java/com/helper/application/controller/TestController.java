package com.helper.application.controller;

import com.helper.application.APIservice.ServiceAFeignClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/25 - 下午3:00
 * Created by IntelliJ IDEA.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    DefaultWebSessionManager defaultWebSessionManager;

    @Autowired
    ServiceAFeignClient serviceAFeignClient;

    @RequestMapping("/hi")
    public String hi(@RequestParam String id){

        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.getPrincipal());

        System.out.println(subject.getSession().getId());
        System.out.println(new DefaultSessionKey(subject.getSession().getId()));

        org.apache.shiro.session.Session kickoutSession
                = defaultWebSessionManager.getSession(
                new DefaultSessionKey(subject.getSession().getId())
        );

        kickoutSession.getAttributeKeys().forEach(obj-> System.out.println(obj));
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

        subject.getSession().getAttributeKeys().forEach(
                obj -> System.out.println(subject.getSession().getAttribute(obj))
        );

        return serviceAFeignClient.hi(id);
    }

    @RequestMapping
    public String index(HttpSession httpSession){

        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.getPrincipal());

        System.out.println(httpSession.getId());
        System.out.println(subject.getSession().getId());
        System.out.println(new DefaultSessionKey(subject.getSession().getId()));

        org.apache.shiro.session.Session kickoutSession
                = defaultWebSessionManager.getSession(
                new DefaultSessionKey(subject.getSession().getId())
        );

        kickoutSession.getAttributeKeys().forEach(obj-> System.out.println(obj));
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

        subject.getSession().getAttributeKeys().forEach(
                obj -> System.out.println(subject.getSession().getAttribute(obj))
        );

        return subject.getPrincipal().toString();
    }
}
