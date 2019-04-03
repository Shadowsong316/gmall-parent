package com.atguigu.rabbit.rabbitdemo;

import com.atguigu.rabbit.rabbitdemo.bean.Order;
import com.atguigu.rabbit.rabbitdemo.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitDemoApplicationTests {
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	AmqpAdmin amqpAdmin;
	/**
	 * 用代码创建出 exchange queue bindings
	 */
	@Test
	public void createOrder() throws InterruptedException {
		Order payed = new Order(UUID.randomUUID().toString(), "PAYED", new BigDecimal("199.99"));
		System.out.println("创建订单...");
		rabbitTemplate.convertAndSend("delayExchange","order",payed);

		Order payed1 = new Order(UUID.randomUUID().toString(), "sss", new BigDecimal("199.99"));
		TimeUnit.SECONDS.sleep(20);
		System.out.println("创建订单...");
		rabbitTemplate.convertAndSend("delayExchange","order",payed1);

		Order payed2 = new Order(UUID.randomUUID().toString(), "PAaaaYED", new BigDecimal("199.99"));
		TimeUnit.SECONDS.sleep(20);
		System.out.println("创建订单...");
		rabbitTemplate.convertAndSend("delayExchange","order",payed2);

	}
	@Test
	public void createQueue(){
		amqpAdmin.declareQueue(new Queue("haha",true,false,false));
		System.out.println("创建queue");
	}
	@Test
	public void createExchange(){
		amqpAdmin.declareExchange(new DirectExchange("direct-exchange-666",true,false));

	}

	/**
	 * String destination,
	 * Binding.DestinationType destinationType,
	 * String exchange,
	 * String routingKey,
	 * Map<String, Object> arguments
	 */
	@Test
	public void createBingding(){
		amqpAdmin.declareBinding(new Binding("haha",Binding.DestinationType.QUEUE,"direct-exchange-666","hahaha",null));
	}
	@Test
	public void sendMsg() {
//		Map<String, Object> map = new HashMap<>();
//		map.put("hello3","12334");
		User user = new User("zhangsan", 14, "北京");
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.convertAndSend("exchange.topic","atguigu",user);
		System.out.println("消息发送完成");


	}

}
