package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.atguigu.gmall.sms.service.FlashPromotionSessionService;
import com.atguigu.gmall.sms.vo.FlashPromotionSessionVo;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@Api(tags = "SmsFlashPromotionSessionController",description = "限时购场次管理")
@RequestMapping("/flashSession")
@RestController
public class SmsFlashPromotionSessionController {
    @Reference
    private FlashPromotionSessionService flashPromotionSessionService;
    @ApiOperation(value = "获取全部场次")
    @GetMapping(value = "/list")
    public Object getList() {
        // 获取全部场次
        List<FlashPromotionSession> list = flashPromotionSessionService.list();
        return new CommonResult().success(list);
    }
    @ApiOperation("添加场次")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody FlashPromotionSession flashPromotionSession){
        flashPromotionSessionService.save(flashPromotionSession);
        return new CommonResult().success("添加场次成功");
    }
    @ApiOperation(value = "删除场次")
    @GetMapping(value = "/delete/{id}")
    public Object delete(@PathVariable("id") Long id) {
        // 删除品牌
        flashPromotionSessionService.removeById(id);
        return new CommonResult().success("删除场次完成");
    }
    @ApiOperation(value = "更新场次")
    @PostMapping(value = "/update/{id}")
    public Object update(@PathVariable("id") Long id,
                         @Validated @RequestBody FlashPromotionSession flashPromotionSession,
                         BindingResult result) {
        // 更新活动
        flashPromotionSession.setId(id);
        flashPromotionSessionService.updateById(flashPromotionSession);
        return new CommonResult().success("修改场次完成");
    }
    @ApiOperation(value = "获取场次详情")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable("id") Long id) {
        // 根据编号查询活动信息
        FlashPromotionSession flashPromotionSession = flashPromotionSessionService.getById(id);
        return new CommonResult().success(flashPromotionSession);
    }
    @ApiOperation(value = "修改启用状态状态")
    @PostMapping(value = "/update/status/{id}")
    public Object update(@PathVariable("id") Long id,
                         @RequestParam Integer status) {
        // 修改活动上下线状态
        FlashPromotionSession flashPromotionSession = new FlashPromotionSession().setId(id).setStatus(status);
        flashPromotionSessionService.updateById(flashPromotionSession);
        return new CommonResult().success("修改活动上下线状态");
    }
    @ApiOperation(value = "获取全部场次")
    @GetMapping(value = "/selectList")
    public Object selectList() {
        // 获取全部场次
        List<FlashPromotionSessionVo> list = flashPromotionSessionService.selectList();
        return new CommonResult().success(list);
    }
}
