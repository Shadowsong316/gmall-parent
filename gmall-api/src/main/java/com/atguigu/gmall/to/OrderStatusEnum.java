package com.atguigu.gmall.to;

public enum OrderStatusEnum {
    /*订单状态 0-待付款，1-待发货，2-已发货，3已完成，4已关闭，5无效订单*/
    UNPAY(0,"待付款"),PAYED(1,"已支付，待发货"),SENDED(2,"已发货"),
    FINISHED(3,"已完成"),CLOSED(4,"已关闭"),UNVAILED(5,"无效订单");

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
