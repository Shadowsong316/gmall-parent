package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.service.CouponService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Api(tags = "SmsCouponController",description ="优惠券管理" )
@RequestMapping("/coupon")
@RestController
@CrossOrigin
public class SmsCouponController {
    @Reference
    private CouponService couponService;
    @ApiOperation("添加优惠券")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody Coupon coupon, BindingResult result){
        couponService.save(coupon);
        return new CommonResult().success("添加优惠券成功");
    }
    @ApiOperation("修改优惠券")
    @PostMapping("/update/{id}")
    public Object update(@Valid @RequestBody Coupon coupon,@PathVariable("id") Long id, BindingResult result){
        coupon.setId(id);
        couponService.updateById(coupon);
        return new CommonResult().success("修改优惠券成功");
    }
    @ApiOperation("删除优惠券")
    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable("id") Long id){
        couponService.removeById(id);
        return new CommonResult().success("删除优惠券成功");
    }
    @ApiOperation("获取单个优惠券的详细信息")
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id){
        Coupon coupon = couponService.getById(id);
        return new CommonResult().success(coupon);
    }
    @ApiOperation("根据优惠券名称和类型分页获取优惠券列表")
    @GetMapping("/list")
    public Object getPage(@RequestParam(value = "name",required = false)String name,
                          @RequestParam(value = "type",required = false)Integer type,
                          @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                          @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        Map<String,Object> map=couponService.pageCoupon(name,type,pageNum,pageSize);
        return new CommonResult().success(map);
    }

}
