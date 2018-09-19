package com.helper.system.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
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
    @PostMapping(value="/in", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> loginPost(String username, String password, boolean rememberMe) {
        Map<String, Object> map = new HashMap<>();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(username,password,rememberMe);
        Subject subject = SecurityUtils.getSubject();

        String error = "";
        try {
            usernamePasswordToken.setRememberMe(rememberMe);
            //完成登录
            subject.login(usernamePasswordToken);
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
