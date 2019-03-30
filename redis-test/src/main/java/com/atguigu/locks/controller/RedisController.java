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
    public String redisIncr() {
        redisService.incr();
        return "ok";
    }

    @GetMapping("/lock")
    public String lock() {
        redisService.getLockUnlock();
        return "ok";
    }

    @GetMapping("/getlock/{ops}")
    public String getLock(@PathVariable("ops") String ops) {
        redisService.getLock(ops);
        return "ok";
    }

    /**
     * 读写的目的，锁细化
     *
     * @return
     */
    @GetMapping("/read")
    public String readValue() {
        return redisService.readValue();
    }

    @GetMapping("/write")
    public String writeValue() {
        return redisService.writeValue();
    }

    @GetMapping("/park")//停车
    public String park() {
        return redisService.park();
    }

    @GetMapping("/out")
    public String out() {

        return redisService.out();
    }

    @GetMapping("/lockdoor")
    public String lockdoor(){
        System.out.println("我想要锁门");
        redisService.lockdoor();
        return "班长锁门了";
    }
    @GetMapping("/go")
    public String go(){
        System.out.println("溜了");
        redisService.go();
        return "溜了";
    }
}
