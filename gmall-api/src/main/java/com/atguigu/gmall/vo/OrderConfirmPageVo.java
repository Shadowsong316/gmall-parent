package com.atguigu.gmall.vo;

import com.atguigu.gmall.cart.entity.CartItem;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmPageVo implements Serializable{
    private List<CartItem> cartItem;
    private List<MemberReceiveAddress> memberReceiveAddresses;
    //用户可选优惠卷
    private String tradeToken;//交易令牌
}
