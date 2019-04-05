package com.atguigu.gmall.order;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDubbo
@MapperScan("com.atguigu.gmall.order.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class GmallOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallOrderApplication.class, args);
	}

}
