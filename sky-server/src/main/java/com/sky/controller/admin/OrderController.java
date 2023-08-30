package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/25 11:07
 */
@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("/订单分页查询")
    public Result<PageResult> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("管理端 订单条件查询：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查看订单详情
     *
     * @param id
     * @return
     */
    @ApiOperation("/查看订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> detailsQuery(@PathVariable Long id) {
        OrderVO orderVO = orderService.getDetailById(id);
        return Result.success(orderVO);
    }

    /**
     * 接单
     *
     * @return
     */
    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单：{}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     *
     * @return
     */
    @PutMapping("/rejection")
    public Result rejectionOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单：{}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 派送订单
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }

    /**
     * 各状态订单数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 取消订单
     *
     * @return
     */
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单:{}",ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }
}
