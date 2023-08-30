package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION: 订单自动任务
 * @AUTHOR: djl
 * @DATE: 2023/8/29 11:02
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrdersMapper ordersMapper;


    /**
     * 已取消订单定时任务
     */
    @Scheduled(cron = "0 * * * * ? ") // 每分钟自动执行
//    @Scheduled(cron = "0/5 * * * * ? ")
    public void cancelledOrdersTask() {
        log.info("超时未支付订单自动取消:{}", LocalDateTime.now());
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = ordersMapper.selectByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, dateTime);
        if (!CollectionUtils.isEmpty(ordersList)) {
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("超时未支付，自动取消");
                ordersMapper.update(orders);
            });
        }

    }

    /**
     * 已完成订单状态派送中修改为已完成
     */
    @Scheduled(cron = "0 0 1 * * ? ")// 每天凌晨一点执行
//        @Scheduled(cron = "0/5 * * * * ? ")
    public void s() {
        log.info("已完成订单状态由派送中修改为已完成:{}", LocalDateTime.now());
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = ordersMapper.selectByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, dateTime);
        if (!CollectionUtils.isEmpty(ordersList)) {
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETED);
                ordersMapper.update(orders);
            });
        }
    }

}
