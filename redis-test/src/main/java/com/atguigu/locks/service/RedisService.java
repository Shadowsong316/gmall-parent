package com.atguigu.locks.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    public void incr() {
        RLock lock = redissonClient.getLock("redis-lock");
        lock.lock();//
        try {
            String hello = redisTemplate.opsForValue().get("hello");
            int i = Integer.parseInt(hello);
            redisTemplate.opsForValue().set("hello", i + 1 + "");
        } finally {
            lock.unlock();
        }
    }
    public void getLockUnlock(){
        RLock lock = redissonClient.getLock("getlock");
        lock.lock();//不放锁
    }

    public void getLock(String ops) {
        RLock lock = redissonClient.getLock("getlock");
        if ("trylock".equals(ops)){
            boolean b = lock.tryLock();//尝试获取
            System.out.println("trylock()获取到锁"+b);
        }else {
            lock.lock();
            System.out.println("lock()获取到锁");
        }
    }
}
