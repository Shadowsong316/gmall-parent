package com.atguigu.gmall.admin.sms.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.FlashPromotion;
import com.atguigu.gmall.sms.service.FlashPromotionService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin
@RestController
@Api(tags = "SmsFlashPromotionController",description = "限时购活动管理")
@RequestMapping("/flash")
public class SmsFlashPromotionController {
    @Reference
    private FlashPromotionService flashPromotionService;
    @ApiOperation("添加活动")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody FlashPromotion flashPromotion){
        flashPromotionService.save(flashPromotion);
        return new CommonResult().success("添加活动成功");
    }
    @ApiOperation(value = "删除活动")
    @GetMapping(value = "/delete/{id}")
    public Object delete(@PathVariable("id") Long id) {
        // 删除品牌
        flashPromotionService.removeById(id);
        return new CommonResult().success("删除活动完成");
    }
    @ApiOperation(value = "更新活动")
    @PostMapping(value = "/update/{id}")
    public Object update(@PathVariable("id") Long id,
                         @Validated @RequestBody FlashPromotion flashPromotion,
                         BindingResult result) {
        // 更新活动
        flashPromotion.setId(id);
        flashPromotionService.updateById(flashPromotion);
        return new CommonResult().success("修改完成");
    }
    @ApiOperation(value = "根据编号查询活动信息")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable("id") Long id) {
        // 根据编号查询活动信息
        FlashPromotion flashPromotion = flashPromotionService.getById(id);
        return new CommonResult().success(flashPromotion);
    }
    @ApiOperation(value = "修改活动上下线状态")
    @PostMapping(value = "/update/status/{id}")
    public Object update(@PathVariable("id") Long id,
                          @RequestParam Integer status) {
        // 修改活动上下线状态
        FlashPromotion flashPromotion = new FlashPromotion().setId(id).setStatus(status);
        flashPromotionService.updateById(flashPromotion);
        return new CommonResult().success("修改活动上下线状态");
    }
    @ApiOperation(value = "根据活动名称分页查询")
    @GetMapping(value = "/list")
    public Object getList(@RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        // 根据品牌名称分页获取品牌列表
        Map<String, Object> map = flashPromotionService.pageFlashPromotion(keyword, pageNum, pageSize);
        return new CommonResult().success(map);
    }
}
