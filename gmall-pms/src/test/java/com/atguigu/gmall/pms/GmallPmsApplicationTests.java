package com.atguigu.gmall.pms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPmsApplicationTests {
    @Autowired
    JedisPool jedisPool;
    @Test
    public void contextLoads() {
//        System.out.println(connectionFactory);
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis);
        String set = jedis.set("hello", "world");
        System.out.println("给redis中保存了数据..."+set);

        String hello = jedis.get("hello");
        System.out.println("从redis中获取hello的值是："+hello);
    }

}
