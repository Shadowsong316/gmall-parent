package com.atguigu.gmall.oms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品查询参数
 */
@Data
public class OmsOrderQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private String orderSn;
    @ApiModelProperty("收货人姓名/号码")
    private String receiverKeyword;
    @ApiModelProperty("订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;
    @ApiModelProperty("订单类型：0->正常订单；1->秒杀订单")
    private Integer orderType;
    @ApiModelProperty("订单来源：0->PC订单；1->app订单")
    private Integer sourceType;
    @ApiModelProperty("订单提交时间")
    private String createTime;

}
