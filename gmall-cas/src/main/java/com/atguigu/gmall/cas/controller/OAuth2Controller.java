package com.atguigu.gmall.cas.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cas.config.WeiboOAuthConfig;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.to.social.WeiboAccessTokenVo;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.service.MemberSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class OAuth2Controller {
    @Autowired
    WeiboOAuthConfig config;
    @Reference(version = "1.0")
    MemberSocialService memberSocialService;
    RestTemplate restTemplate=new RestTemplate();//boot提供发get post请求
    @Autowired
    StringRedisTemplate redisTemplate;
    @GetMapping("/register/authorization")
    public String  registerAuthorization(@RequestParam("authType") String authType,
                                         @RequestParam("url") String url, HttpSession session){
        session.setAttribute("url",url);
        if("weibo".equals(authType)){
            return "redirect:"+config.getAuthPage();
        }
        return "redirect:"+config.getAuthPage();
    }

    /**
     * 用户授权通过，会返回code码
     * @return
     */
    @GetMapping("/auth/success")
    public String codeGetToken(@RequestParam("code") String code,HttpSession session){
        //或取到code码
        System.out.println("获取到的code码"+code);
        //1、根据这个code码。我们去weibo换取access_token
        //2、换取access_token
        String authPage =  config.getAccessTokenPage()+"&code="+code;
        WeiboAccessTokenVo tokenVo = restTemplate.postForObject(authPage, null, WeiboAccessTokenVo.class);
        //3、加access_token变为UUID，存储到redis中;
        //4、用户第一次进来？ 接下来将这个用户注册进系统

        Member memberInfo = memberSocialService.getMemberInfo(tokenVo);

        //1）、判断这个社交登陆进来的用户是否之前注册过。如果没有自动注册进来
        //2）、如果以前是登陆过的，将用户在我系统里面的信息返回给用户
        //3）、access_token获取到用户的初始信息，然后注册用户的时候使用这些作为初始信息
        //4）、以后社交登陆登陆进来。利用社交的uid查询本系统的用户，返回这个信息
        //数据库也要保存一下access_token，如果过期，重新引导授权，没有过期，直接使用access_token一键分享社交平台

        String url = (String) session.getAttribute("url");
        //session.setAttribute("loginUser",memberInfo);

        //此次响应命令浏览器保存一个cookie；jsessionid=xxxxxadasdada；仅在访问 www.gmallshop.com有效
        //

        //分布式中全部使用令牌机制
        String token = UUID.randomUUID().toString();
        String memberInfoJson = JSON.toJSONString(memberInfo);
        redisTemplate.opsForValue().set(RedisCacheConstant.USER_INFO_CACHE_KEY+token,memberInfoJson);

//        return tokenVo.getAccess_token();
        return "redirect:"+url+"?token="+token;
    }
}
//https://api.weibo.com/oauth2/authorize?client_id=244598679&response_type=code&redirect_uri=http://www.gmallshop316.com/auth/success###