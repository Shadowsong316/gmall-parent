package com.atguigu.gmall.oms.vo;

import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.oms.entity.OrderOperateHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品查询参数
 */
@Data
public class OmsOrderAllItem extends Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单中所包含的商品集合")
    private List<OrderItem> orderItemList;
   @ApiModelProperty("订单中所包含的商品集合")
    private List<OrderOperateHistory> historyList;

}
