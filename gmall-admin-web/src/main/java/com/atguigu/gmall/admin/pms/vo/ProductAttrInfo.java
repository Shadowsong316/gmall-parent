package com.atguigu.gmall.admin.pms.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * 商品分类对应属性信息
 */
@Data
public class ProductAttrInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long attributeId;
    private Long attributeCategoryId;

}
