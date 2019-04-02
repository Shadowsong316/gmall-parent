package com.atguigu.gmall.cas.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class SSOController {
    @Reference
    MemberService memberService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/login.html")
    public String loginPage(String url, @CookieValue(value = "gmallsso", required = false) String gmallsso) {

//        //登录成功去url
//        Object jwt = users.get(gmallsso);
//        if (jwt!=null){
//             return "redirect:"+url+"?token="+gmallsso;
//        }
//        return "login";
        return null;
    }

    @PostMapping("/logintosys")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("url") String url,
                        HttpSession session, HttpServletResponse response,
                        HttpServletRequest request) {
        //账号密码登录。查出放在redis
        String token = UUID.randomUUID().toString().replace("-", "");
        Member member = memberService.login(username, password);
        if (member != null) {

            String memberJson = JSON.toJSONString(member);
            redisTemplate.opsForValue().set(RedisCacheConstant.USER_INFO_CACHE_KEY + token, memberJson,RedisCacheConstant.USER_INFO_TIMEOUT, TimeUnit.DAYS);

            return "redirect:" + url + "?token=" + token;
        } else {
            //返回json字符串比较好
            //前端登录失败 获取上次从哪里来的
            String referer = request.getHeader("Referer");
            return "redirect:" + referer + "?token=";

        }

    }
}
