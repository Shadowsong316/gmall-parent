package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.atguigu.gmall.sms.vo.FlashPromotionSessionVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 限时购场次表 服务类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
public interface FlashPromotionSessionService extends IService<FlashPromotionSession> {

    List<FlashPromotionSessionVo> selectList();
}
