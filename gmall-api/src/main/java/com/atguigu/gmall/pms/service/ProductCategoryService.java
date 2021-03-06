package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.vo.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    List<PmsProductCategoryWithChildrenItem> listWithChildren();

    Map<String,Object> pageProductCategory(Integer pageNum, Integer pageSize, Long parentId);

    void updateNavStatus(List<Long> ids, Integer navStatus);

    void updateShowStatus(List<Long> ids, Integer showStatus);
}
