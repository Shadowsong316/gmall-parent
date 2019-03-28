package com.atguigu.gmall.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.item.to.ProductAllInfos;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(version = "1.0")
public class ItemServiceImpl implements ItemService {
    @Reference
    private SkuStockService skuStockService;
    @Reference
    private ProductService productService;
    @Override
    public ProductAllInfos getInfo(Long skuId) {
        ProductAllInfos infos=new ProductAllInfos();
        //1当前sku的详细信息查出来 包括销售属性的组合 库存 价格
        SkuStock skuStock = skuStockService.getById(skuId);
        //2当前商品的详细信息
        Long productId = skuStock.getProductId();
        //引入缓存机制
        //1.查询商品，直接去缓存中查询
        //2.如果缓存中没有，去数据库查询，查来的数据再放入缓存，下一个人就不用查了
        Product product = productService.getProductByIdFromCache(productId);
        //3所有sku的组合选法
        List<SkuStock> skuStockList=skuStockService.getAllSkuInfoByProductId(productId);
        //4查询这个商品所有的属性可选值销售
        List<EsProductAttributeValue> saleAttrList=productService.getProductSaleAttr(productId);

        //5商品的其他属性可选值基础
        List<EsProductAttributeValue> baseAttrList=productService.getProductBaseAttr(productId);

        //6当前商品涉及到的服务
        infos.setBaseAttr(baseAttrList);
        infos.setSaleAttr(saleAttrList);
        infos.setProduct(product);
        infos.setSkuStock(skuStock);
        infos.setSkuStockList(skuStockList);
        return infos;
    }
}
/*select pav.*,pa.`name`,pa.`type` from pms_product_attribute_value pav
	left join pms_product_attribute pa
	on pa.`id`=pav.`product_attribute_id`
 where product_id=27 and pa.`type`=0 */