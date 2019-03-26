package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.atguigu.gmall.sms.entity.HomeBrand;
import com.atguigu.gmall.sms.service.HomeBrandService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 首页品牌功能Controller
 */
@CrossOrigin
@RestController
@Api(tags = "SmsHomeBrandController", description = "首页品牌管理")
@RequestMapping("/home/brand")
public class SmsHomeBrandController {
    @Reference
    private HomeBrandService homeBrandService;

    @ApiOperation(value = "添加首页推荐品牌")
    @PostMapping(value = "/create")
    public Object create(@Valid@RequestBody HomeBrand homeBrand, BindingResult result) {
        //TODO 添加首页推荐品牌 前端好像有问题
        homeBrandService.save(homeBrand);
        return new CommonResult().success("添加成功");
    }

    @ApiOperation(value = "修改品牌排序")
    @PostMapping(value = "/update/sort/{id}")
    public Object updateSortById(@PathVariable("id") Long id,
                         @RequestParam Integer sort,
                         BindingResult result) {
        //修改品牌排序
        homeBrandService.updateSortById(id,sort);
        return new CommonResult().success("修改品牌排序");
    }

    @ApiOperation(value = "分页查询推荐品牌")
    @GetMapping(value = "/list")
    public Object getList(@RequestParam(value = "brandName", required = false) String brandName,
                          @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        // 根据品牌名称分页获取品牌列表
        Map<String, Object> brandPageInfo = homeBrandService.pageBrand(brandName,recommendStatus, pageNum, pageSize);
        return new CommonResult().success(brandPageInfo);
    }

    @ApiOperation(value = "根据编号查询首页推荐品牌")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable("id") Long id) {
        CommonResult commonResult = new CommonResult();
        //根据编号查询首页推荐品牌
        HomeBrand homeBrand = homeBrandService.getById(id);
        return commonResult.success(homeBrand);
    }

    @ApiOperation(value = "批量删除推荐品牌")
    @PostMapping(value = "/delete")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        CommonResult commonResult = new CommonResult();
        // 批量删除品牌
        homeBrandService.removeByIds(ids);
        return commonResult.success("批量删除完成");
    }

    @ApiOperation(value = "批量更新显示状态")
    @PostMapping(value = "/update/recommendStatus")
    public Object updateShowStatus(@RequestParam("ids") List<Long> ids,
                                   @RequestParam("recommendStatus") Integer recommendStatus) {
        // 批量更新显示状态
        List<HomeBrand> list = new ArrayList<>();
        for (Long id : ids) {
            HomeBrand homeBrand = new HomeBrand().setId(id).setRecommendStatus(recommendStatus);
            list.add(homeBrand);
        }
        homeBrandService.updateBatchById(list);
        return new CommonResult().success("批量更新显示状态");
    }


}
