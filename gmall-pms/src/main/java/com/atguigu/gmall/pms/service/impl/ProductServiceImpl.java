package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.PrefrenceAreaProductRelation;
import com.atguigu.gmall.cms.entity.SubjectProductRelation;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.mapper.*;
import com.atguigu.gmall.pms.service.*;
import com.atguigu.gmall.pms.vo.PmsProductParam;
import com.atguigu.gmall.util.SelectPageUtil;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Slf4j
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private SkuStockMapper skuStockMapper;
    @Autowired
    private ProductLadderMapper productLadderMapper;
    @Autowired
    private ProductFullReductionMapper productFullReductionMapper;
    @Autowired
    private MemberPriceMapper memberPriceMapper;
    @Autowired
    private ProductAttributeValueMapper productAttributeValueMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    ThreadLocal<Product> productThreadLocal = new ThreadLocal<>();
    @Autowired
    private SkuStockService skuStockService;
    @Autowired
    private ProductLadderService productLadderService;
    @Autowired
    private ProductFullReductionService productFullReductionService;
    @Autowired
    private MemberPriceService memberPriceService;
    @Autowired
    private ProductAttributeValueService productAttributeValueService;
//    @Autowired
//    private ProductCategoryService productCategoryService;

    @Override
    public Map<String, Object> pageProduct(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        Integer publishStatus = productQueryParam.getPublishStatus();
        Integer verifyStatus = productQueryParam.getVerifyStatus();
        String keyword = productQueryParam.getKeyword();
        String productSn = productQueryParam.getProductSn();
        Long productCategoryId = productQueryParam.getProductCategoryId();
        Long brandId = productQueryParam.getBrandId();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(publishStatus)) {
            queryWrapper.eq("publish_status", publishStatus);
        }
        if (!StringUtils.isEmpty(verifyStatus)) {
            queryWrapper.eq("verify_status", verifyStatus);
        }
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("keywords", keyword);
        }
        if (!StringUtils.isEmpty(productSn)) {
            queryWrapper.eq("product_sn", productSn);
        }
        if (!StringUtils.isEmpty(productCategoryId)) {
            queryWrapper.eq("product_category_id", productCategoryId);
        }
        if (!StringUtils.isEmpty(brandId)) {
            queryWrapper.eq("brand_id", brandId);
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
        baseMapper.updatePublishStatus(ids, publishStatus);
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        baseMapper.updateRecommendStatus(ids, recommendStatus);
    }

    @Override
    public void updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        baseMapper.updateDeleteStatus(ids, deleteStatus);
    }



    @Override//保存商品全部信息，调用下面的方法
    @Transactional(propagation = Propagation.REQUIRED)
    public void create(PmsProductParam productParam) {
        ProductServiceImpl proxy = (ProductServiceImpl) AopContext.currentProxy();
        proxy.saveBaseProductInfo(productParam);
        proxy.saveProductLadder(productParam.getProductLadderList());
        proxy.saveProductFullReduction(productParam.getProductFullReductionList());
        proxy.saveMemberPrice(productParam.getMemberPriceList());
        proxy.saveProductAttributeValue(productParam.getProductAttributeValueList());
        proxy.updateProductCategoryCount();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBaseProductInfo(PmsProductParam productParam) {
        ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
        psProxy.saveProduct(productParam);
        psProxy.saveSkuStock(productParam.getSkuStockList());
    }

    //     1保存商品基本信息
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveProduct(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        baseMapper.insert(product);
        productThreadLocal.set(product);
    }

    //    5商品的sku库存信息
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveSkuStock(List<SkuStock> list) {
        Product product = productThreadLocal.get();
        AtomicReference<Integer> i = new AtomicReference<>(0);
        list.forEach(skuStock -> {
            skuStock.setProductId(product.getId());
            String format = String.format("%02d", i.get());
            String code = "K_" + product.getId() + "_" + format;
            skuStock.setSkuCode(code);
            i.set(i.get() + 1);
//            skuStockMapper.insert(skuStock);
        });
        skuStockService.saveBatch(list);
    }

    //    2商品阶梯价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveProductLadder(List<ProductLadder> list) {
        Product product = productThreadLocal.get();
        System.out.println("product" + product);
        System.out.println("id" + product.getId());
        list.forEach(productLadder -> {
            productLadder.setProductId(product.getId());
//            productLadderMapper.insert(productLadder);
        });
        productLadderService.saveBatch(list);
    }

    //    3商品满减价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveProductFullReduction(List<ProductFullReduction> list) {
        Product product = productThreadLocal.get();
        list.forEach(productFullReduction -> {
            productFullReduction.setProductId(product.getId());
//            productFullReductionMapper.insert(productFullReduction);
        });
        productFullReductionService.saveBatch(list);
    }

    //    4("商品会员价格设置")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveMemberPrice(List<MemberPrice> list) {
        Product product = productThreadLocal.get();
        list.forEach(memberPrice -> {
            memberPrice.setProductId(product.getId());
//            memberPriceMapper.insert(memberPrice);
        });
        memberPriceService.saveBatch(list);
    }

    //    6("商品参数及自定义规格属性")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveProductAttributeValue(List<ProductAttributeValue> list) {
        Product product = productThreadLocal.get();
        list.forEach(productAttributeValue -> {
            productAttributeValue.setProductId(product.getId());
//            productAttributeValueMapper.insert(productAttributeValue);
        });
        productAttributeValueService.saveBatch(list);
    }

    //7、更新商品分类数目 【REQUIRES_NEW】
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductCategoryCount() {
        Product product = productThreadLocal.get();
        Long id = product.getProductCategoryId();
        productCategoryMapper.updateCountById(id);
    }

    @Override//查询所有信息
    public PmsProductParam getupdateInfoById(Long id) {
        Product product = baseMapper.selectById(id);
        PmsProductParam productParam = new PmsProductParam();
        BeanUtils.copyProperties(product, productParam);
        List<SkuStock> skuStockList = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", id));
        List<ProductLadder> productLadderList = productLadderMapper.selectList(new QueryWrapper<ProductLadder>().eq("product_id", id));
        List<ProductFullReduction> productFullReductionList = productFullReductionMapper.selectList(new QueryWrapper<ProductFullReduction>().eq("product_id", id));
        List<MemberPrice> memberPriceList = memberPriceMapper.selectList(new QueryWrapper<MemberPrice>().eq("product_id", id));
        List<ProductAttributeValue> productAttributeValue = productAttributeValueMapper.selectList(new QueryWrapper<ProductAttributeValue>().eq("product_id", id));
        productParam.setSkuStockList(skuStockList);
        productParam.setProductLadderList(productLadderList);
        productParam.setProductFullReductionList(productFullReductionList);
        productParam.setMemberPriceList(memberPriceList);
        productParam.setProductAttributeValueList(productAttributeValue);
        return productParam;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override//大更新所有信息
    public void updateAllInfo(PmsProductParam productParam) {
        ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
        psProxy.updateBaseProductInfo(productParam);
        psProxy.updateProductLadder(productParam.getProductLadderList());
        psProxy.updateProductFullReduction(productParam.getProductFullReductionList());
        ////
        psProxy.updateMemberPrice(productParam.getMemberPriceList());
        psProxy.updateProductAttributeValue(productParam.getProductAttributeValueList());
    }

    //1修改商品基本信息和SKU
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBaseProductInfo(PmsProductParam productParam) {
        ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
        psProxy.updateProduct(productParam);
        psProxy.updateSkuStock(productParam.getSkuStockList());

    }

    @Override//1修改商品基本信息
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateProduct(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        baseMapper.updateById(product);
    }

    @Override//5 修改商品的sku库存信息
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSkuStock(List<SkuStock> list) {
        skuStockService.updateBatchById(list);
    }

    @Override//2修改商品阶梯价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductLadder(List<ProductLadder> list) {
        productLadderService.updateBatchById(list);
    }

    @Override//3修改商品满减价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductFullReduction(List<ProductFullReduction> list) {
        productFullReductionService.updateBatchById(list);
    }


    @Override//4修改商品会员价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMemberPrice(List<MemberPrice> list) {
        memberPriceService.updateBatchById(list);
    }

    @Override//5修改商品参数及自定义规格属性
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductAttributeValue(List<ProductAttributeValue> list) {
        productAttributeValueService.updateBatchById(list);
    }


}
