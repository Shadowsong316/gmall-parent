package com.atguigu.locks.controller;

import com.atguigu.locks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    RedisService redisService;
    @GetMapping("/incr")
    public String redisIncr(){
        redisService.incr();
        return "ok";
    }
    @GetMapping("/lock")
    public String lock(){
        redisService.getLockUnlock();
        return "ok";
    }
    @GetMapping("/getlock/{ops}")
    public String getLock(@PathVariable("ops")String ops){
        redisService.getLock(ops);
        return "ok";
    }
}
