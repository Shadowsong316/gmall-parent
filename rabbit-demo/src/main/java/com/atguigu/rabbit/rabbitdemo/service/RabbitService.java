package com.atguigu.rabbit.rabbitdemo.service;

import com.atguigu.rabbit.rabbitdemo.bean.Order;
import com.atguigu.rabbit.rabbitdemo.bean.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class RabbitService {

    @RabbitListener(queues = "deadQueue")
    public void orderCancel(Order order,Channel channel,Message message) throws IOException {
        System.out.println("收到了超时订单。。" + order);
        if (order.getStatus().equals("PAYED")) {
            System.out.println("此订单已经支付，不用操作");
        } else {
            System.out.println("此订单将被关闭");
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


    @RabbitListener(queues = "atguigu.emps")
    public void vc(Message message, Channel channel) throws IOException {
        System.out.println("收到消息" + message.getMessageProperties().getDeliveryTag());
        //只要某个消息没回复，这个消息时unacked状态而且不会继续发过来
        if (message.getMessageProperties().getDeliveryTag() % 2 == 0) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }


    }

    //    @RabbitListener(queues = "atguigu")
    public void consumer(User user, Channel channel, Message message) throws IOException {
//        System.out.println("收到了消息"+message.getBody());
        System.out.println("收到了消息" + user);

//        channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        try {
            if (user.getAge() % 2 == 0) {
                System.out.println("1号服务器收到了消息，并处理完成" + user);
            }
            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println("1号服务器故障，消息拒绝");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

        }
    }

    //    @RabbitListener(queues = "atguigu")
    public void consumer2(User user, Channel channel, Message message) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        System.out.println("【2号服务器...收到了消息】..." + user);
        try {
            if (user.getAge() % 2 != 0) {
                System.out.println("【2号服务器...收到了消息，并处理完成】..." + user);
            }
            throw new RuntimeException();
        } catch (Exception e) {
            //拒绝了消息，并重新入队  ack确认收到   nack：不给回复确认收到 == reject
            System.out.println("2号服务器故障，消息拒绝");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
