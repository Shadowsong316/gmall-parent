package com.atguigu.gmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HelloController {
    @GetMapping("shuai")
    public String shuaige(HttpSession session){
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser!=null){
            return "protected";
        }else {
            //去cas登录
            return "redirect:http://www.gmallshop316.com/login.html?url=http://www.java-sys.com/meinv";
        }
    }
}
