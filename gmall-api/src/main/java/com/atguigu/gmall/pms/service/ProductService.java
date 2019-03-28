package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.PmsProductParam;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductService extends IService<Product> {

    Map<String,Object> pageProduct(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    List<Product> getList(String keyword);

    void updateNewStatus(List<Long> ids, Integer newStatus);

    void updatePublishStatus(List<Long> ids, Integer publishStatus);

    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    void updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    void create(PmsProductParam productParam);

    void saveProduct(PmsProductParam productParam);
//    "商品的sku库存信息"
    void saveSkuStock(List<SkuStock> list);
//    商品阶梯价格设置
    void saveProductLadder(List<ProductLadder> list);
//    商品满减价格设置
    void saveProductFullReduction(List<ProductFullReduction> list);
//    ("商品会员价格设置")
    void saveMemberPrice(List<MemberPrice> list);
//    ("商品参数及自定义规格属性")
    void saveProductAttributeValue(List<ProductAttributeValue> list);

    PmsProductParam getupdateInfoById(Long id);

    void updateAllInfo(PmsProductParam productParam);
    //    修改商品的基本信息
    void updateProduct(PmsProductParam productParam);
    //    修改商品的sku库存信息
    void updateSkuStock(List<SkuStock> list);
    //    修改商品阶梯价格设置
    void updateProductLadder(List<ProductLadder> list);
    //    修改商品满减价格设置
    void updateProductFullReduction(List<ProductFullReduction> list);
    //    修改商品会员价格设置
    void updateMemberPrice(List<MemberPrice> list);
    //    修改商品参数及自定义规格属性
    void updateProductAttributeValue(List<ProductAttributeValue> list);


    List<EsProductAttributeValue> getProductSaleAttr(Long productId);

    List<EsProductAttributeValue> getProductBaseAttr(Long productId);

    Product getProductByIdFromCache(Long productId);
}
