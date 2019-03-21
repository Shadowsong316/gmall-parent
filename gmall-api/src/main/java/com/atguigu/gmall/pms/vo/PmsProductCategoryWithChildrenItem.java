package com.atguigu.gmall.pms.vo;


import com.atguigu.gmall.pms.entity.ProductCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 */
@Data
public class PmsProductCategoryWithChildrenItem extends ProductCategory implements Serializable {
    private static final long serialVersionUID = 1L;
//    private List<ProductCategory> children;
    private List<PmsProductCategoryWithChildrenItem> children;

}
