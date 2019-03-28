package com.atguigu.gmall.to.es;

import lombok.Data;

import java.io.Serializable;

/**
 * 检索前端传来的数据
 */
@Data
public class SearchParam implements Serializable{

    private Long[] catelog3Id;//三级分类ID
    private Long[] brandId;//品牌ID
    private String keyword;//检索关键字
    private String order;//0:综合,1:销量，2：价格，3价格区间
    private Integer pageNum=1;//当前页
    private Integer pageSize=12;//每页记录数
    private String[] props;//页码提交的数组
    private Integer priceFrom;//价格区间开始
    private Integer priceTo;//价格区间结束
}
