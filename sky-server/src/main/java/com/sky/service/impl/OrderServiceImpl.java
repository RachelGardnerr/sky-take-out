package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/21 17:12
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrdersDetailMapper ordersDetailMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 用户下单
     *
     * @param orderSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO orderSubmitDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO, orders);

        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = addressBookMapper.getById(orderSubmitDTO.getAddressBookId());

        // 用户地址簿为空，抛出业务异常
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 用户购物车为空，抛出业务异常
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(addressBook.getConsignee());
        orders.setConsignee(addressBook.getConsignee());
        orders.setEstimatedDeliveryTime(orderSubmitDTO.getEstimatedDeliveryTime());

        // 生成订单
        ordersMapper.insert(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        shoppingCartList.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setSetmealId(cart.getSetmealId());
            orderDetail.setName(cart.getName());
            orderDetail.setImage(cart.getImage());
            orderDetail.setDishFlavor(cart.getDishFlavor());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            orderDetailList.add(orderDetail);
        });
        // 生成订单详细数据
        ordersDetailMapper.insertBatch(orderDetailList);
        // 删除购物车数据
        shoppingCartMapper.deleteByUserId(userId);
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder().id(orders.getId()).orderNumber(orders.getNumber()).orderAmount(orders.getAmount()).orderTime(orders.getOrderTime()).build();
        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        /**
         //调用微信支付接口，生成预支付交易单
         JSONObject jsonObject = weChatPayUtil.pay(
         ordersPaymentDTO.getOrderNumber(), //商户订单号
         new BigDecimal(0.01), //支付金额，单位 元
         "苍穹外卖订单", //商品描述
         user.getOpenid() //微信用户的openid
         );
         **/
        JSONObject jsonObject = new JSONObject();


        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        ordersMapper.update(orders);
    }


    /**
     * 历史订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult list(OrdersPageQueryDTO ordersPageQueryDTO) {

        // 设置分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        // 分页条件查询
        Page<Orders> page = ordersMapper.selectByUserId(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = ordersDetailMapper.selectByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }

        return new PageResult(page.getTotal(), list);
    }
}
