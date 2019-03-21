package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.mapper.ProductCategoryMapper;
import com.atguigu.gmall.pms.service.ProductCategoryService;
import com.atguigu.gmall.vo.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> ppcwciList=new ArrayList<>();

        QueryWrapper<ProductCategory> queryWrapper1 = new QueryWrapper<ProductCategory>().eq("level", 0);
        QueryWrapper<ProductCategory> queryWrapper2 = new QueryWrapper<ProductCategory>().eq("level", 1);
        QueryWrapper<ProductCategory> queryWrapper3 = new QueryWrapper<ProductCategory>().eq("level", 2);
        List<ProductCategory> categoryList1 = baseMapper.selectList(queryWrapper1);//一级分类集合
        List<ProductCategory> categoryList2 = baseMapper.selectList(queryWrapper2);
        List<ProductCategory> categoryList3 = baseMapper.selectList(queryWrapper3);
        for (ProductCategory category1 : categoryList1) {//遍历一级分类 并添加子集合
            PmsProductCategoryWithChildrenItem ppcwci1 = new PmsProductCategoryWithChildrenItem();//新建vo对象
            BeanUtils.copyProperties(category1,ppcwci1);//属性填充
            ppcwciList.add(ppcwci1);//加入集合
            List<PmsProductCategoryWithChildrenItem> children2=new ArrayList<>();//新建一级分类的子分类=二级分类vo
            for (ProductCategory category2 : categoryList2) {//遍历二级分类
                if (category1.getId()==category2.getParentId()){
                    PmsProductCategoryWithChildrenItem ppcwci2 = new PmsProductCategoryWithChildrenItem();//新建vo对象 二级分类
                    BeanUtils.copyProperties(category2,ppcwci2);
                    children2.add(ppcwci2);
                    List<PmsProductCategoryWithChildrenItem> children3=new ArrayList<>();//新建二级分类的子分类=三级分类vo
                    for (ProductCategory category3 : categoryList3) {//遍历三级分类
                        if (category2.getId()==category3.getParentId()){
                            PmsProductCategoryWithChildrenItem ppcwci3 = new PmsProductCategoryWithChildrenItem();//新建vo对象 三级分类
                            BeanUtils.copyProperties(category3,ppcwci3);
                            children3.add(ppcwci3);
                        }
                    }
                    ppcwci2.setChildren(children3);
                }
            }
            ppcwci1.setChildren(children2);
        }
        return ppcwciList;
    }
}
