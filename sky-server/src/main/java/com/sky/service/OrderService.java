package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     *
     * @param orderSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO orderSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult list(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单详情
     *
     * @param orderId
     * @return
     */
    OrderVO getDetailById(Long orderId);

    /**
     * 取消订单
     *
     * @param orderId
     */
    void modify(Long orderId);

    /**
     * 再来一单
     *
     * @param id
     */
    void againOrder(Long id);
}
