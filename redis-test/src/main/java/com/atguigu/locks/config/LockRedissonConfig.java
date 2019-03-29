package com.atguigu.locks.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockRedissonConfig {
    @Value("${spring.redis.host}")
    private String host;

    //    spring.redis.host=192.168.13.100
    @Bean
    public RedissonClient config() {
        // 默认连接地址 127.0.0.1:6379
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
