package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.entity.SkuResponse;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "CartController",description = "购物车模块")
@RequestMapping("/cart")
@RestController
@CrossOrigin
public class CartController {
    @Reference
    CartService cartService;

    /**
     * 前端必须携带一个cartKey才能购物，如果没有我们会给她们新建一个
     * @param skuId
     * @param num
     * @param token
     * @param cartKey
     * @return
     */
    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("/add")
    public CommonResult addToCart(@RequestParam("skuId") Long skuId,
                                  @RequestParam("num") Integer num,
                                  @RequestParam("token") String token,
                                  @RequestParam("cartKey") String cartKey){
        RpcContext.getContext().setAttachment("gmallusertoken",token);
        //给用户都返回一个购物车标志
        SkuResponse skuResponse=cartService.addToCart(skuId,num,cartKey);
        return new CommonResult().success(skuResponse);
    }
    @ApiOperation(value = "修改商品数量")
    @PostMapping("/update")
    public CommonResult updateCart(@RequestParam("skuId") Long skuId,
                                  @RequestParam("num") Integer num,
                                  @RequestParam("token") String token,
                                  @RequestParam("cartKey") String cartKey){
        RpcContext.getContext().setAttachment("gmallusertoken",token);
        //给用户都返回一个购物车标志
        boolean update=cartService.updateCount(skuId,num,cartKey);
        return new CommonResult().success(update);
    }
    @ApiOperation(value = "删除商品")
    @PostMapping("/delete")
    public CommonResult removeToCart(@RequestParam("skuId") Long skuId,
                                  @RequestParam("token") String token,
                                  @RequestParam("cartKey") String cartKey){
        RpcContext.getContext().setAttachment("gmallusertoken",token);
        //给用户都返回一个购物车标志
        boolean delete=cartService.deleteCart(skuId,cartKey);
        return new CommonResult().success(delete);
    }
    @ApiOperation(value = "选中商品")
    @PostMapping("/check")
    public CommonResult checkToCart(@RequestParam("skuId") Long skuId,
                                  @ApiParam(value = "需要选中的商品，0不选中，1选中")
                                  @RequestParam("flag") Integer flag,
                                  @RequestParam("token") String token,
                                  @RequestParam("cartKey") String cartKey){
        RpcContext.getContext().setAttachment("gmallusertoken",token);
        //给用户都返回一个购物车标志
        boolean check=cartService.checkCart(skuId,flag,cartKey);
        return new CommonResult().success(check);
    }
    @ApiOperation(value = "添加商品到购物车")
    @GetMapping("/list")
    public CommonResult list(@RequestParam("token") String token,
                                  @RequestParam("cartKey") String cartKey){
        RpcContext.getContext().setAttachment("gmallusertoken",token);
        //给用户都返回一个购物车标志
        Cart cart=cartService.cartItemsList(cartKey);
        return new CommonResult().success(cart);
    }
}
