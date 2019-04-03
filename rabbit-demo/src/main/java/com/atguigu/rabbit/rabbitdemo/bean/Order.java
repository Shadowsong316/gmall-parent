package com.atguigu.rabbit.rabbitdemo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable{
    private String orderId;
    private String status;
    private BigDecimal price;
}
