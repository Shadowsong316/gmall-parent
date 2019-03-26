package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.CouponHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 优惠券使用、领取历史表 服务类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
public interface CouponHistoryService extends IService<CouponHistory> {

    Map<String,Object> pageList(Long couponId, Integer useStatus, String orderSn, Integer pageNum, Integer pageSize);
}
