package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.entity.ProductAttributeCategory;
import com.atguigu.gmall.pms.mapper.ProductAttributeCategoryMapper;
import com.atguigu.gmall.pms.service.ProductAttributeCategoryService;
import com.atguigu.gmall.pms.service.ProductAttributeService;
import com.atguigu.gmall.pms.util.SelectPageUtil;
import com.atguigu.gmall.pms.vo.PmsProductAttributeCategoryItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {
    @Autowired
    private ProductAttributeService productAttributeService;
    @Override
    public Map<String, Object> pageProductAttributeCategory(Integer pageSize, Integer pageNum) {
        IPage<ProductAttributeCategory> selectPage = baseMapper.selectPage(new Page<ProductAttributeCategory>(pageNum, pageSize), null);
        return SelectPageUtil.getStringObjectMap(pageSize, selectPage);

    }

    @Override
    public List<PmsProductAttributeCategoryItem> listWithAttr() {
        List<PmsProductAttributeCategoryItem> list=new ArrayList<>();

        List<ProductAttributeCategory> productAttributeCategories = baseMapper.selectList(null);
        for (ProductAttributeCategory productAttributeCategory : productAttributeCategories) {
            PmsProductAttributeCategoryItem pmsProductAttributeCategoryItem = new PmsProductAttributeCategoryItem();
            BeanUtils.copyProperties(productAttributeCategory,pmsProductAttributeCategoryItem);
            list.add(pmsProductAttributeCategoryItem);
            QueryWrapper<ProductAttribute> queryWrapper = new QueryWrapper<ProductAttribute>()
                    .eq("product_attribute_category_id", productAttributeCategory.getId());
            List<ProductAttribute> productAttributeList = productAttributeService.list(queryWrapper);
            pmsProductAttributeCategoryItem.setProductAttributeList(productAttributeList);
        }
        return list;
    }
}
