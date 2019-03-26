package com.atguigu.gmall.sms.vo;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FlashPromotionProductRelationVo extends FlashPromotionProductRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品全部信息")
    @TableField("product")
    private Product product;
}
