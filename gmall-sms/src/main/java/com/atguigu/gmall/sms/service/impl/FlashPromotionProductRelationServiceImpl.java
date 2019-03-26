package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.atguigu.gmall.sms.mapper.FlashPromotionProductRelationMapper;
import com.atguigu.gmall.sms.service.FlashPromotionProductRelationService;
import com.atguigu.gmall.sms.vo.FlashPromotionProductRelationVo;
import com.atguigu.gmall.util.SelectPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品限时购与商品关系表 服务实现类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
@Service
@Component
public class FlashPromotionProductRelationServiceImpl extends ServiceImpl<FlashPromotionProductRelationMapper, FlashPromotionProductRelation> implements FlashPromotionProductRelationService {
    @Reference
    private ProductService productService;
    @Override//做点有点复杂 把从原始对象得到商品ID 查询到商品 放入新VO对象中 再把VO集合放入分页map中
    public Map<String, Object> pageList(Long flashPromotionId, Long flashPromotionSessionId, Integer pageNum, Integer pageSize) {
        QueryWrapper<FlashPromotionProductRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flash_promotion_id",flashPromotionId)
                .eq("flash_promotion_session_id",flashPromotionSessionId);
        IPage<FlashPromotionProductRelation> selectPage = baseMapper.selectPage(new Page<FlashPromotionProductRelation>(pageNum, pageSize),queryWrapper);
        Map<String, Object> map=new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("totalPage",selectPage.getPages());
        map.put("total",selectPage.getTotal());
        map.put("pageNum",selectPage.getCurrent());

        List<FlashPromotionProductRelation> records = selectPage.getRecords();
        List<FlashPromotionProductRelationVo> list=new ArrayList<>();
        for (FlashPromotionProductRelation fppr : records) {
            Long productId = fppr.getProductId();
            Product product = productService.getById(productId);
            FlashPromotionProductRelationVo fpprv = new FlashPromotionProductRelationVo();
            BeanUtils.copyProperties(fppr,fpprv);
            fpprv.setProduct(product);
            list.add(fpprv);
        }
        map.put("list",list);
        return map;
    }
}
