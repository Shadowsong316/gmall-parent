package com.atguigu.gmall.admin.oms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.oms.service.OrderService;
import com.atguigu.gmall.oms.vo.OmsOrderAllItem;
import com.atguigu.gmall.oms.vo.OmsOrderQueryParam;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "OmsOrderController",description = "订单管理")
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OmsOrderController {
    @Reference
    private OrderService orderService;
    @RequestMapping("/list")
    @ApiOperation("分页查询订单")
    public Object list(OmsOrderQueryParam omsOrderQueryParam,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        Map<String,Object> map=orderService.pageList(omsOrderQueryParam,pageNum,pageSize);
        return new CommonResult().success(map);
    }
    @ApiOperation("获取订单详情:订单信息、商品信息、操作记录")
    @GetMapping(value = "/{id}")
    public Object getAllInfo(@PathVariable Long id) {
        // 根据商品id获取商品编辑信息
        OmsOrderAllItem orderAllItem = orderService.getAllInfoById(id);
        return new CommonResult().success(orderAllItem);
    }
}
