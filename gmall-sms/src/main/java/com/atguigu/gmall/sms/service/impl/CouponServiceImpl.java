package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.mapper.CouponMapper;
import com.atguigu.gmall.sms.service.CouponService;
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
 * 优惠卷表 服务实现类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
@Service
@Component
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Override
    public Map<String, Object> pageCoupon(String name, Integer type, Integer pageNum, Integer pageSize) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<Coupon>();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(type)){
            queryWrapper.eq("type", type);
        }
        IPage<Coupon> selectPage = baseMapper.selectPage(new Page<Coupon>(pageNum, pageSize),queryWrapper);
        return SelectPageUtil.getStringObjectMap(pageSize,selectPage);
    }
}
