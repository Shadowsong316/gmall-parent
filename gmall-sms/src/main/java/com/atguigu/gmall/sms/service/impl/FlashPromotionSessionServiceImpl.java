package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.atguigu.gmall.sms.mapper.FlashPromotionSessionMapper;
import com.atguigu.gmall.sms.service.FlashPromotionSessionService;
import com.atguigu.gmall.sms.vo.FlashPromotionSessionVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 限时购场次表 服务实现类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
@Service
@Component
public class FlashPromotionSessionServiceImpl extends ServiceImpl<FlashPromotionSessionMapper, FlashPromotionSession> implements FlashPromotionSessionService {

    @Override
    public List<FlashPromotionSessionVo> selectList() {
        return baseMapper.selectListAndCount();
    }
}
