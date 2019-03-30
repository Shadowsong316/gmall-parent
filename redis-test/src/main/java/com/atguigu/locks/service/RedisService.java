package com.atguigu.locks.service;

import org.redisson.api.*;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    public String readValue() {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("hello-lock");
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        String hello = redisTemplate.opsForValue().get("hello");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rLock.unlock();
        return hello;
    }

    public String writeValue() {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("hello-lock");
        RLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        redisTemplate.opsForValue().set("hello", UUID.randomUUID().toString());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
        return "ok";
    }

    public String park() {
        RSemaphore semaphore = redissonClient.getSemaphore("parklength");
        try {
            semaphore.acquire();
            System.out.println("找到车位了");
            return "ok";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public String out() {
        RSemaphore semaphore = redissonClient.getSemaphore("parklength");
        semaphore.release();
        return "释放车位完成";
    }

    public void lockdoor() {
        RCountDownLatch latch = redissonClient.getCountDownLatch("students");

        //等待别人都走完
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("锁门了。。。bye");
    }

    public void go() {
        RCountDownLatch latch = redissonClient.getCountDownLatch("students");
        latch.countDown();
    }
}
