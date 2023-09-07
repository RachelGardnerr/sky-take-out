package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {

    /**
     * 新增订单
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据用户id查询订单
     *
     * @param orderPageQueryDTO
     * @return
     */

    Page<Orders> selectByUserId(OrdersPageQueryDTO orderPageQueryDTO);

    @Select("select * from orders where id = #{orderId}")
    Orders selectById(Long orderId);

    /**
     * 订单分页查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> selectPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单数量统计
     *
     * @param toBeConfirmed
     * @return
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer toBeConfirmed);

    /**
     * 查询订单
     *
     * @param status
     * @param dateTime
     * @return
     */
    @Select("select * from orders where status = #{status}  and order_time < #{dateTime}")
    List<Orders> selectByStatusAndOrderTimeLT(Integer status, LocalDateTime dateTime);

    List<Orders> selectMap(Map map);

    Integer selectCountByMap(Map map);

    Double sumByMap(Map map);
}
