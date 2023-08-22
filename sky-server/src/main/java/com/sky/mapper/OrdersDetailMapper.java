package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersDetailMapper {

    /**
     * 批量添加订单详细数据
     *
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> selectByOrderId(Long orderId);
}
