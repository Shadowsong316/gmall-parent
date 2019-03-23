package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.mapper.ProductMapper;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.util.SelectPageUtil;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Map<String, Object> pageProduct(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        Integer publishStatus =productQueryParam.getPublishStatus();
        Integer verifyStatus = productQueryParam.getVerifyStatus();
        String keyword = productQueryParam.getKeyword();
        String productSn = productQueryParam.getProductSn();
        Long productCategoryId = productQueryParam.getProductCategoryId();
        Long brandId = productQueryParam.getBrandId();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(publishStatus)){
            queryWrapper.eq("publish_status",publishStatus);
        }
        if (!StringUtils.isEmpty(verifyStatus)){
            queryWrapper.eq("verify_status",verifyStatus);
        }
        if (!StringUtils.isEmpty(keyword)){
            queryWrapper.like("keywords",keyword);
        }
        if (!StringUtils.isEmpty(productSn)){
            queryWrapper.eq("product_sn",productSn);
        }
        if (!StringUtils.isEmpty(productCategoryId)){
            queryWrapper.eq("product_category_id",productCategoryId);
        }
        if (!StringUtils.isEmpty(brandId)){
            queryWrapper.eq("brand_id",brandId);
        }
        IPage<Product> selectPage = baseMapper.selectPage(new Page<Product>(pageNum, pageSize), queryWrapper);

        Map<String, Object> map = SelectPageUtil.getStringObjectMap(pageSize, selectPage);
        return map;
    }

    @Override
    public List<Product> getList(String keyword) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>().like("keywords", keyword);
        queryWrapper.or().like("brand_id", keyword);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateNewStatus(List<Long> ids, Integer newStatus) {
        baseMapper.updateNewStatus(ids, newStatus);
    }

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) {
        baseMapper.updatePublishStatus(ids,publishStatus);
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        baseMapper.updateRecommendStatus(ids,recommendStatus);
    }

    @Override
    public void updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        baseMapper.updateDeleteStatus(ids,deleteStatus);
    }
}
