package com.atguigu.gmall.to.es;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResponseAttrVo implements Serializable{
    private static final long serialVersionUID = 1L;
    //属性id
    private Long productAttributeId;
    //当前属性值的所有值
    private List<String> value=new ArrayList<>();//3G

    // private Integer searchType;
    //属性名称
    private String name;
}
