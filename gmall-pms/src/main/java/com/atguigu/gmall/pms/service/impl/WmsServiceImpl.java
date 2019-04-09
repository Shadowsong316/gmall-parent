package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.mapper.SkuStockMapper;
import com.atguigu.gmall.to.OrderMQTo;
import com.atguigu.gmall.to.OrderStatusEnum;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 库存的最终方案
 * 1 缓存为王
 *      1 缓存的使用模式之 cache-Aside [1双写2写数据库清缓存]
 *      2 高并发读写特别多，保存实时同步
 *      ABA：改库存
 *              1 1
 *              2 2
 *              3 1
 *           1 读写锁（改动特别多）
 *              实时数据 加读写锁
 */
@Service
@Slf4j
public class WmsServiceImpl {
    @Autowired
    SkuStockMapper skuStockMapper;
    @Reference
    OrderAndPayService orderAndPayService;
    @RabbitListener(queues = "stockOrderQueue")
    public void lockStock(OrderMQTo orderMQTo, Channel channel, Message message) throws IOException {
        try {
            log.debug("订单开始锁库存："+orderMQTo.getOrder().getOrderSn());
            List<OrderItem> items = orderMQTo.getOrderItems();
            items.forEach(item->{
                Long skuId = item.getProductSkuId();
                Integer quantity = item.getProductQuantity();
                //并发锁库存，库存可以一直锁，预警系统

                //锁库存
                //直接去锁库存
                SkuStock skuStock = new SkuStock();
                skuStock.setId(skuId);
                skuStockMapper.updateStock(skuId,quantity);
                log.debug("sku:"+skuId+"库存锁定成功.."+quantity);
                try {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                } catch (IOException e) {
                }
            });
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
    //释放库存
    @RabbitListener(queues = "releaseStockQueue")
    public void releaseStock(OrderMQTo orderMQTo, Channel channel, Message message) throws IOException {
        //解锁库存
        try{
            String orderSn = orderMQTo.getOrder().getOrderSn();
            Order order = orderAndPayService.getOrderByOrderSn(orderSn);
            if (order.getStatus()== OrderStatusEnum.CLOSED.getCode()){
                List<OrderItem> items = orderMQTo.getOrderItems();
                items.forEach((item)->{
                    Long productSkuId = item.getProductSkuId();
                    Integer productQuantity = item.getProductQuantity();
                    skuStockMapper.releaseStock(productSkuId,productQuantity);
                });
            }
            //消息的重复消费,忘了手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }


    }
}
