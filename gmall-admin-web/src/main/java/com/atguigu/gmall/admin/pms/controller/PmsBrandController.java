package com.atguigu.gmall.admin.pms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.pms.entity.Brand;
import com.atguigu.gmall.pms.service.BrandService;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.pms.vo.PmsBrandParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 品牌功能Controller
 */
@CrossOrigin
@RestController
@Api(tags = "PmsBrandController", description = "商品品牌管理")
@RequestMapping("/brand")
public class PmsBrandController {
    @Reference
    private BrandService brandService;

    @ApiOperation(value = "获取全部品牌列表")
    @GetMapping(value = "/listAll")
    public Object getList() {

        // 获取全部品牌列表
        List<Brand> brandList = brandService.listAll();
        return new CommonResult().success(brandList);
    }

    @ApiOperation(value = "添加品牌")
    @PostMapping(value = "/create")
    public Object create(@Validated @RequestBody PmsBrandParam pmsBrand, BindingResult result) {
        // 添加品牌
        Brand brand = new Brand();
        BeanUtils.copyProperties(pmsBrand, brand);
        brandService.save(brand);
        return new CommonResult().success("添加成功");
    }

    @ApiOperation(value = "更新品牌")
    @PostMapping(value = "/update/{id}")
    public Object update(@PathVariable("id") Long id,
                         @Validated @RequestBody PmsBrandParam pmsBrandParam,
                         BindingResult result) {
        CommonResult commonResult = new CommonResult();
        // 更新品牌
        Brand brand = new Brand();
        BeanUtils.copyProperties(pmsBrandParam, brand);
        brand.setId(id);
        brandService.updateById(brand);
        return commonResult.success("修改完成");
    }

    @ApiOperation(value = "删除品牌")
    @GetMapping(value = "/delete/{id}")
    public Object delete(@PathVariable("id") Long id) {
        CommonResult commonResult = new CommonResult();
        // 删除品牌
        brandService.removeById(id);
        return commonResult.success("删除完成");
    }

    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @GetMapping(value = "/list")
    public Object getList(@RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        // 根据品牌名称分页获取品牌列表
        Map<String, Object> brandPageInfo = brandService.pageBrand(keyword, pageNum, pageSize);
        return new CommonResult().success(brandPageInfo);
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable("id") Long id) {
        CommonResult commonResult = new CommonResult();
        // 根据编号查询品牌信息
        Brand brand = brandService.getById(id);
        return commonResult.success(brand);
    }

    @ApiOperation(value = "批量删除品牌")
    @PostMapping(value = "/delete/batch")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        CommonResult commonResult = new CommonResult();
        // 批量删除品牌
        brandService.removeByIds(ids);
        return commonResult.success("批量删除完成");
    }

    @ApiOperation(value = "批量更新显示状态")
    @PostMapping(value = "/update/showStatus")
    public Object updateShowStatus(@RequestParam("ids") List<Long> ids,
                                   @RequestParam("showStatus") Integer showStatus) {
        CommonResult commonResult = new CommonResult();
        // 批量更新显示状态
        brandService.updateShowStatus(ids, showStatus);
        return commonResult.success("批量更新显示状态");
    }

    @ApiOperation(value = "批量更新厂家制造商状态")
    @PostMapping(value = "/update/factoryStatus")
    public Object updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                      @RequestParam("factoryStatus") Integer factoryStatus) {
        CommonResult commonResult = new CommonResult();
        // 批量更新厂家制造商状态
        for (Long id : ids) {
            Brand brand = new Brand();
            brand.setId(id);
            brand.setFactoryStatus(factoryStatus);
            brandService.updateById(brand);
        }
        return commonResult.success("批量更新厂家制造商状态完成");
    }
}
