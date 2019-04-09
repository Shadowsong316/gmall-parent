package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.order.mapper.OrderMapper;
import com.atguigu.gmall.to.OrderMQTo;
import com.atguigu.gmall.to.OrderStatusEnum;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderCloseService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    //    @Scheduled(cron = "0 * * 7 4 *")
    //秒分时日月周年
//    public void timer(){
//        System.out.println("扫描数据所有订单...");
//    }
    @Schedules({
            @Scheduled(cron = "0 0 3 * * ?")
    })
    public void clearCount(){
        //删除掉，下次没有会自建
        stringRedisTemplate.delete("orderCountId");
    }
    /**
     * 死信关单
     * @param order
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = "deadOrderQueue")
    public void closeOrder(OrderMQTo order, Channel channel, Message message) throws IOException {
        //1、获取到order的状态
        try {
            Order order1 = orderMapper.selectById(order.getOrder().getId());
            if(order1.getStatus()== OrderStatusEnum.UNPAY.getCode()){
                Order saveOrder = new Order();
                saveOrder.setId(order1.getId());
                saveOrder.setStatus(OrderStatusEnum.CLOSED.getCode());
                orderMapper.updateById(saveOrder);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                //关单完成，将关闭的订单解锁库存
                rabbitTemplate.convertAndSend("orderDeadExchange","release.stock",order);

            }
        }catch (Exception e){
            //重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            //channel.basicNack();
        }
    }
}