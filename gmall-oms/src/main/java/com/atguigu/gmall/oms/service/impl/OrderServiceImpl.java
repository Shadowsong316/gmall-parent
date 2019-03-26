package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.oms.entity.OrderOperateHistory;
import com.atguigu.gmall.oms.mapper.OrderItemMapper;
import com.atguigu.gmall.oms.mapper.OrderMapper;
import com.atguigu.gmall.oms.mapper.OrderOperateHistoryMapper;
import com.atguigu.gmall.oms.service.OrderService;
import com.atguigu.gmall.oms.vo.OmsOrderAllItem;
import com.atguigu.gmall.oms.vo.OmsOrderQueryParam;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.util.SelectPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-25
 */
@Service
@Component
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderOperateHistoryMapper orderOperateHistoryMapper;

    @Override
    public Map<String, Object> pageList(OmsOrderQueryParam omsOrderQueryParam, Integer pageNum, Integer pageSize) {
        String orderSn = omsOrderQueryParam.getOrderSn();
        String receiverKeyword = omsOrderQueryParam.getReceiverKeyword();
        Integer status = omsOrderQueryParam.getStatus();
        Integer orderType = omsOrderQueryParam.getOrderType();
        Integer sourceType = omsOrderQueryParam.getSourceType();
        String createTime = omsOrderQueryParam.getCreateTime();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(orderSn)){
            queryWrapper.like("order_sn",orderSn);
        }
        if (!StringUtils.isEmpty(receiverKeyword)){
            queryWrapper.like("receiver_name",receiverKeyword)
            .or().like("receiver_phone",receiverKeyword);
        }
        if (!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }
        if (!StringUtils.isEmpty(orderType)){
            queryWrapper.eq("order_type",orderType);
        }
        if (!StringUtils.isEmpty(sourceType)){
            queryWrapper.eq("source_type",sourceType);
        }
        if (!StringUtils.isEmpty(createTime)){
            queryWrapper.like("create_time",createTime);
        }
        IPage<Order> selectPage = baseMapper.selectPage(new Page<Order>(pageNum, pageSize), queryWrapper);

        Map<String, Object> map = SelectPageUtil.getStringObjectMap(pageSize, selectPage);
        return map;
    }
    /*SELECT * FROM oms_order o
    LEFT JOIN oms_order_item oi ON o.`id`=oi.`order_id`
    LEFT JOIN oms_order_operate_history ooh ON o.`id`=ooh.`order_id`
    WHERE o.`id`=12*/
    @Override
    public OmsOrderAllItem getAllInfoById(Long id) {
        OmsOrderAllItem orderAllItem = new OmsOrderAllItem();
        Order order = baseMapper.selectById(id);
        BeanUtils.copyProperties(order,orderAllItem);
        List<OrderItem> orderItemList = orderItemMapper.selectList(new QueryWrapper<OrderItem>().eq("order_id", id));
        List<OrderOperateHistory> orderOperateHistoryList = orderOperateHistoryMapper.selectList(new QueryWrapper<OrderOperateHistory>().eq("order_id", id));
        orderAllItem.setOrderItemList(orderItemList);
        orderAllItem.setHistoryList(orderOperateHistoryList);
        return orderAllItem;
    }
}
