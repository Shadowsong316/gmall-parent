package com.atguigu.gmall.to.es;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class SearchResponse implements Serializable{
    private SearchResponseAttrVo brand;//品牌
    private SearchResponseAttrVo catelog;//分类
    //所有商品的顶头显示的筛选属性
    private List<SearchResponseAttrVo> attrVos=new ArrayList<>();
    //检索出来的商品信息
    private List<EsProduct> products=new ArrayList<>();
    //分页信息
    private Long total;//总记录数
    private Integer pageSize;//每页显示的内容
    private Integer pageNum;//当前页码
}