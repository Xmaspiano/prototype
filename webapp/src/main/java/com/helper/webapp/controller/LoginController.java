package com.helper.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/15 - 上午10:42
 * Created by IntelliJ IDEA.
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView login(){
        System.out.println("123");
        return new ModelAndView("h_login");
    }

    @RequestMapping("/index")
    public ModelAndView index(){
        System.out.println("3333");
        return new ModelAndView("h_index");
    }
}
