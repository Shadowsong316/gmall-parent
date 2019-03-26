package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.CouponHistory;
import com.atguigu.gmall.sms.mapper.CouponHistoryMapper;
import com.atguigu.gmall.sms.service.CouponHistoryService;
import com.atguigu.gmall.util.SelectPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 优惠券使用、领取历史表 服务实现类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
@Service
@Component
public class CouponHistoryServiceImpl extends ServiceImpl<CouponHistoryMapper, CouponHistory> implements CouponHistoryService {

    @Override
    public Map<String, Object> pageList(Long couponId, Integer useStatus, String orderSn, Integer pageNum, Integer pageSize) {
        QueryWrapper<CouponHistory> queryWrapper = new QueryWrapper<CouponHistory>().eq("coupon_id", couponId);
        if (!StringUtils.isEmpty(useStatus)){
            queryWrapper.eq("use_status",useStatus);
        }
        if (!StringUtils.isEmpty(orderSn)){
            queryWrapper.like("order_sn",orderSn);
        }
        IPage<CouponHistory> selectPage = baseMapper.selectPage(new Page<CouponHistory>(pageNum, pageSize), queryWrapper);
        return SelectPageUtil.getStringObjectMap(pageSize,selectPage);
    }
}
