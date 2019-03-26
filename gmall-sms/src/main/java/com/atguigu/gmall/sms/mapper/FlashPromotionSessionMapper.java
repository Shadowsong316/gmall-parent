package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.atguigu.gmall.sms.vo.FlashPromotionSessionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 限时购场次表 Mapper 接口
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
public interface FlashPromotionSessionMapper extends BaseMapper<FlashPromotionSession> {

    List<FlashPromotionSessionVo> selectListAndCount();

}
