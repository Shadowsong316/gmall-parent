package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.to.CommonResult;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sec")
public class SecKillController {
    @Autowired
    RedissonClient redissonClient;
    @GetMapping("/kill")
    public CommonResult secKill(String token,Long skuId){
        RSemaphore semaphore = redissonClient.getSemaphore(RedisCacheConstant.SEC_KILL + skuId + "");
        boolean b = semaphore.tryAcquire();
        if (b){
            //给mq发送消息
            //setnx(token,skuId) 防止用户秒多个
            System.out.println("用户："+token+",秒杀完成了商品："+skuId);
        }
        return b?new CommonResult().success("秒杀成功"):new CommonResult().failed();
    }
}
