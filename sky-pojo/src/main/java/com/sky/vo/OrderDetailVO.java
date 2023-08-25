package com.sky.vo;

import com.sky.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/23 10:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVO {

    //订单号
    private String number;

    //实收金额
    private BigDecimal amount;

    //预计送达时间
    private LocalDateTime estimatedDeliveryTime;

    //餐具数量
    private int tablewareNumber;

    //打包费
    private int packAmount;

    //地址
    private String address;

    private List<OrderDetail> orderDetailList;

}
