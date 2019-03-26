package com.atguigu.gmall.sms.vo;

import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FlashPromotionSessionVo extends FlashPromotionSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品数量")
    @TableField("productCount")
    private Integer productCount;
}
