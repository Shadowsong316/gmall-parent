package com.atguigu.gmall.admin.cms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.cms.entity.Subject;
import com.atguigu.gmall.cms.service.SubjectService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 商品专题
 */
@CrossOrigin
@RestController
@Api(tags = "CmsSubjectController", description = "商品专题管理")
@RequestMapping("/subject")
public class CmsSubjectController {
    @Reference
    private SubjectService subjectService;

    @ApiOperation("获取全部商品专题")
    @GetMapping(value = "/listAll")
    public Object listAll() {
        // 获取全部商品专题
        List<Subject> items=subjectService.listAll();
        return new CommonResult().success(items);
    }

    @ApiOperation(value = "根据专题名称分页获取专题")
    @GetMapping(value = "/list")
    public Object getList(@RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        //根据专题名称分页获取专题
        Map<String,Object> map=subjectService.getList(keyword,pageNum,pageSize);
        return new CommonResult().success(map);
    }
}
