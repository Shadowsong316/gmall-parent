package com.atguigu.gmall.admin.ums.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.admin.ums.vo.UmsAdminLoginParam;
import com.atguigu.gmall.admin.ums.vo.UmsAdminParam;
import com.atguigu.gmall.admin.utils.JwtTokenUtil;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.ums.entity.Admin;
import com.atguigu.gmall.ums.entity.MemberLevel;
import com.atguigu.gmall.ums.service.AdminService;
import com.atguigu.gmall.ums.service.MemberLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 后台用户管理
 */
@Slf4j
@CrossOrigin
@RestController
@Api(tags = "MemberLevelController", description = "会员等级管理")
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {
    @Reference
    private MemberLevelService memberLevelService;

    @ApiOperation("获取会员等级列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "defaultStatus", defaultValue = "0") Integer defaultStatus) {
        //TODO 获取会员等级列表
        List<MemberLevel> memberLevelList = memberLevelService.listMemberLevel(defaultStatus);
        return new CommonResult().success(memberLevelList);
    }
}

