package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 商品限时购与商品关系表 服务类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
public interface FlashPromotionProductRelationService extends IService<FlashPromotionProductRelation> {

    Map<String,Object> pageList(Long flashPromotionId, Long flashPromotionSessionId, Integer pageNum, Integer pageSize);
}
