package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.atguigu.gmall.cart.entity.CartItem;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.atguigu.gmall.ums.service.MemberService;
import com.atguigu.gmall.vo.OrderConfirmPageVo;
import com.atguigu.gmall.vo.OrderResponseVo;
import com.atguigu.gmall.vo.OrderSubmitVo;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
@Api(tags = "OrderController", description = "订单服务模块")
public class OrderController {
    @Reference
    OrderAndPayService orderAndPayService;
    @Reference
    CartService cartService;
    @Reference
    MemberService memberService;
    /*订单确认页需要的所有数据*/
    @ResponseBody
    @PostMapping("/orderconfirm")
    public CommonResult jiesuan(@RequestParam("token") String token) {
        //去结算确认页，返回结算页的数据

        //1需要结算的商品信息,目前时获取到的购物车里面的商品的信息
        List<CartItem> cartItems = cartService.cartItemsForJieSuan(token);

        //2用户可选的地址列表
        List<MemberReceiveAddress> memberReceiveAddresses =orderAndPayService.getMemberReceiveAddresses(token);
        //3一个临时令牌，方便下次进行防重验证
        //JVM dubbo认为getXxx不带参数，时获取他里面的属性
        RpcContext.getContext().setAttachment("gmallusertoken",token);
        String tradeToken=orderAndPayService.getTradeToken();

        return new CommonResult().success(new OrderConfirmPageVo(cartItems,memberReceiveAddresses,tradeToken));
    }
    /*下订单并跳转到支付页*/
    @ResponseBody
    @PostMapping("/submit")
    public OrderResponseVo payOrder(OrderSubmitVo orderSubmitVo){
        //1创建订单
        OrderResponseVo orderResponse=orderAndPayService.createOrder(orderSubmitVo);
        //2再给一个交易token

        //3给前端返回订单号等信息 展示在确认支付页面 选择支付方式进行支付

        return orderResponse;
    }
}
