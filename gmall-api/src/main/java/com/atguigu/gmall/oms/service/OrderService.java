package com.atguigu.gmall.oms.service;

import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.vo.OmsOrderAllItem;
import com.atguigu.gmall.oms.vo.OmsOrderQueryParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-25
 */
public interface OrderService extends IService<Order> {

    Map<String,Object> pageList(OmsOrderQueryParam omsOrderQueryParam, Integer pageNum, Integer pageSize);

    OmsOrderAllItem getAllInfoById(Long id);
}
