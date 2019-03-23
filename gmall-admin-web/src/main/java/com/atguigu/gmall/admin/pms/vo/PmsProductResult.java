package com.atguigu.gmall.admin.pms.vo;

import com.atguigu.gmall.pms.vo.PmsProductParam;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询单个产品进行修改时返回的结果
 */
@Data
public class PmsProductResult extends PmsProductParam implements Serializable {
    private static final long serialVersionUID = 1L;
    //商品所选分类的父id
    private Long cateParentId;

}
