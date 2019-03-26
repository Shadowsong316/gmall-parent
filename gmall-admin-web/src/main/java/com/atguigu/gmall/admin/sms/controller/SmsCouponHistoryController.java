package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.service.CouponHistoryService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@Api(tags = "SmsCouponHistoryController",description = "优惠券领取记录管理")
@RequestMapping("/couponHistory")
public class SmsCouponHistoryController {
    @Reference
    private CouponHistoryService couponHistoryService;
    @ApiOperation("根据优惠券id，使用状态，订单编号分页获取领取记录")
    @GetMapping("/list")
    public Object getPage(@RequestParam(value = "couponId")Long couponId,
                       @RequestParam(value = "useStatus",required = false)Integer useStatus,
                       @RequestParam(value = "orderSn",required = false)String orderSn,
                       @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                       @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        Map<String,Object> map=couponHistoryService.pageList(couponId,useStatus,orderSn,pageNum,pageSize);
        return new CommonResult().success(map);
    }
}
