package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.pms.constant.RedisCacheConstant;
import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.mapper.ProductCategoryMapper;
import com.atguigu.gmall.pms.service.ProductCategoryService;
import com.atguigu.gmall.util.SelectPageUtil;
import com.atguigu.gmall.pms.vo.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Slf4j
@Service
@Component
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cache = ops.get(RedisCacheConstant.PRODUCT_CATEGORY_CACHE_KEY);
        if (!StringUtils.isEmpty(cache)){
            log.debug("PRODUCT_CATEGORY_CACHE_KEY缓存命中");
            List<PmsProductCategoryWithChildrenItem> items = JSON.parseArray(cache, PmsProductCategoryWithChildrenItem.class);
            return items;
        }
        //这个数据加缓存
        log.debug("PRODUCT_CATEGORY_CACHE_KEY缓存未命中，去查询数据库");
        List<PmsProductCategoryWithChildrenItem> items = baseMapper.listWithChildren(0);
        String jsonString = JSON.toJSONString(items);
        ops.set(RedisCacheConstant.PRODUCT_CATEGORY_CACHE_KEY,jsonString,3, TimeUnit.DAYS);
        //查某个菜单的所有子菜单
        return items;
    }

    @Override
    public Map<String, Object> pageProductCategory(Integer pageNum, Integer pageSize, Long parentId) {
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<ProductCategory>().eq("parent_id", parentId);
        IPage<ProductCategory> selectPage = baseMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        Map<String, Object> map = SelectPageUtil.getStringObjectMap(pageSize, selectPage);
        return map;
    }

    @Override
    public void updateNavStatus(List<Long> ids, Integer navStatus) {
        baseMapper.updateNavStatus(ids,navStatus);
    }

    @Override
    public void updateShowStatus(List<Long> ids, Integer showStatus) {
        baseMapper.updateShowStatus(ids,showStatus);
    }
}
