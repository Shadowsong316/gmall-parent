package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.item.to.ProductAllInfos;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {
    @Reference(version = "1.0")
    private ItemService itemService;
    @GetMapping(value = "/{skuId}.html",produces = "application/json")
    public ProductAllInfos productInfo(@PathVariable("skuId")Long skuId){
        //
        ProductAllInfos allInfos=itemService.getInfo(skuId);
        return allInfos;
    }
}
