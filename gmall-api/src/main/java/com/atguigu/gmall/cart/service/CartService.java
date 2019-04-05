package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.entity.CartItem;
import com.atguigu.gmall.cart.entity.SkuResponse;

import java.util.List;

public interface CartService {
    SkuResponse addToCart(Long skuId, Integer num, String cartKey);

    boolean updateCount(Long skuId, Integer num, String cartKey);

    boolean deleteCart(Long skuId, String cartKey);

    boolean checkCart(Long skuId, Integer flag, String cartKey);

    Cart cartItemsList(String cartKey);

    List<CartItem> cartItemsForJieSuan(String token);
}

