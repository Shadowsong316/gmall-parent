package com.atguigu.gmall.item.to;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductAllInfos implements Serializable{
    private Product product;//当前商品详情
    private SkuStock skuStock;//当前sku详情
    private List<SkuStock> skuStockList;//当前所有sku组合
    private List<EsProductAttributeValue> saleAttr;//商品的筛选属性SKU销售的属性;
    private List<EsProductAttributeValue> baseAttr;//商品的筛选属性SPU的属性;

}
