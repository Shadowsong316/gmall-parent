package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Brand;
import com.atguigu.gmall.sms.entity.FlashPromotion;
import com.atguigu.gmall.sms.mapper.FlashPromotionMapper;
import com.atguigu.gmall.sms.service.FlashPromotionService;
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
 * 限时购表 服务实现类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
@Service
@Component
public class FlashPromotionServiceImpl extends ServiceImpl<FlashPromotionMapper, FlashPromotion> implements FlashPromotionService {

    @Override
    public Map<String, Object> pageFlashPromotion(String keyword, Integer pageNum, Integer pageSize) {
        QueryWrapper<FlashPromotion> queryWrapper=null;
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper = new QueryWrapper<FlashPromotion>().like("title", keyword);
        }
        IPage<FlashPromotion> selectPage = baseMapper.selectPage(new Page<FlashPromotion>(pageNum, pageSize), queryWrapper);
        Map<String, Object> map = SelectPageUtil.getStringObjectMap(pageSize, selectPage);
        return map;
    }
}
