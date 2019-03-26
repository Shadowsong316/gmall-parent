package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.atguigu.gmall.sms.service.CouponService;
import com.atguigu.gmall.sms.service.FlashPromotionProductRelationService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "SmsFlashPromotionProductRelationController",description ="限时购和商品关系管理" )
@RequestMapping("/flashProductRelation")
@RestController
@CrossOrigin
public class SmsFlashPromotionProductRelationController {
    @Reference
    private FlashPromotionProductRelationService flashPromotionProductRelationService;
    @ApiOperation("批量选择商品添加关联")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody List<FlashPromotionProductRelation> list, BindingResult result){
        //TODO
        flashPromotionProductRelationService.saveBatch(list);
        return new CommonResult().success("批量选择商品添加关联成功");
    }
    @ApiOperation("修改关联相关信息")
    @PostMapping("/update/{id}")
    public Object update(@Valid @RequestBody FlashPromotionProductRelation flashPromotionProductRelation,@PathVariable("id") Long id, BindingResult result){
        flashPromotionProductRelation.setId(id);
        flashPromotionProductRelationService.updateById(flashPromotionProductRelation);
        return new CommonResult().success("修改关联相关信息成功");
    }
    @ApiOperation("删除关联")
    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable("id") Long id){
        flashPromotionProductRelationService.removeById(id);
        return new CommonResult().success("删除关联成功");
    }
    @ApiOperation("获取管理商品促销信息")
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id){
        FlashPromotionProductRelation flashPromotionProductRelation = flashPromotionProductRelationService.getById(id);
        return new CommonResult().success(flashPromotionProductRelation);
    }
    @ApiOperation("分页查询不同场次关联及商品信息")
    @GetMapping("/list")
    public Object getPage(@RequestParam(value = "flashPromotionId")Long flashPromotionId,
                          @RequestParam(value = "flashPromotionSessionId")Long flashPromotionSessionId,
                          @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                          @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        Map<String,Object> map=flashPromotionProductRelationService.pageList(flashPromotionId,flashPromotionSessionId,pageNum,pageSize);
        return new CommonResult().success(map);
    }

}
