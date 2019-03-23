package com.atguigu.gmall.admin.pms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.admin.pms.vo.PmsProductCategoryParam;
import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.service.ProductCategoryService;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.pms.vo.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品分类模块Controller
 */
@CrossOrigin
@RestController
@Api(tags = "PmsProductCategoryController", description = "商品分类管理")
@RequestMapping("/productCategory")
public class PmsProductCategoryController {
    @Reference
    private ProductCategoryService productCategoryService;

    @ApiOperation("添加产品分类")
    @PostMapping(value = "/create")
    public Object create(@Validated @RequestBody PmsProductCategoryParam productCategoryParam,
                         BindingResult result) {
        // 添加产品分类
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryParam, productCategory);
        productCategoryService.save(productCategory);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改商品分类")
    @PostMapping(value = "/update/{id}")
    public Object update(@PathVariable Long id,
                         @Validated
                         @RequestBody PmsProductCategoryParam productCategoryParam,
                         BindingResult result) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryParam, productCategory);
        productCategory.setId(id);
        productCategoryService.updateById(productCategory);
        // 修改商品分类
        return new CommonResult().success(null);
    }

    @ApiOperation("分页查询商品分类")
    @GetMapping(value = "/list/{parentId}")
    public Object getList(@PathVariable Long parentId,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        // 分页查询商品分类
        Map<String, Object> pageInfo = productCategoryService.pageProductCategory(pageNum, pageSize, parentId);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("根据id获取商品分类")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable Long id) {
        // 根据id获取商品分类
        ProductCategory productCategory = productCategoryService.getById(id);
        return new CommonResult().success(productCategory);
    }

    @ApiOperation("删除商品分类")
    @PostMapping(value = "/delete/{id}")
    public Object delete(@PathVariable Long id) {
        // 删除商品分类
        productCategoryService.removeById(id);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改导航栏显示状态")
    @PostMapping(value = "/update/navStatus")
    public Object updateNavStatus(@RequestParam("ids") List<Long> ids, @RequestParam("navStatus") Integer navStatus) {
        // 修改导航栏显示状态
        productCategoryService.updateNavStatus(ids, navStatus);
        return new CommonResult().success("修改导航栏显示状态成功");
    }

    @ApiOperation("修改显示状态")
    @PostMapping(value = "/update/showStatus")
    public Object updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        // 修改显示状态
        productCategoryService.updateShowStatus(ids, showStatus);

        return new CommonResult().success("修改显示状态成功");
    }

    @ApiOperation("查询所有一级分类及子分类[有难度]")
    @GetMapping(value = "/list/withChildren")
    public Object listWithChildren() {
        // 查询所有一级分类及子分类
        //这个数据加缓存
        List<PmsProductCategoryWithChildrenItem> items = productCategoryService.listWithChildren();
        return new CommonResult().success(items);
    }
}
