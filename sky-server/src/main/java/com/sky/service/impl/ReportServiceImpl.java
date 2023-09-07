package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/30 15:38
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrdersMapper ordersMapper;


    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 获取日期集合
        List<LocalDate> dateList = getLocalDates(begin, end);

        // 获取营业额
        List<Double> turnoverList = dateList.stream().map(date -> {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);
            Double turnover = reportMapper.selectTurnover(map);
            turnover = turnover == null ? 0.0 : turnover;
            return turnover;
        }).collect(Collectors.toList());

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户数量统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 日期集合
        List<LocalDate> dateList = getLocalDates(begin, end);

        //新增用户数量集合
        List<Integer> newUserList = dateList.stream().map(date -> {
            Map<String, LocalDateTime> map = new HashMap();
            map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer newUserCount = userMapper.selectCountByMap(map);
            newUserCount = newUserCount == null ? 0 : newUserCount;
            return newUserCount;
        }).collect(Collectors.toList());

        // 总用户数数量集合
        List<Integer> totalUserList = dateList.stream().map(date -> {
            Map<String, LocalDateTime> map = new HashMap();
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            return userMapper.selectCountByMap(map);
        }).collect(Collectors.toList());

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        // 日期集合
        List<LocalDate> dateList = getLocalDates(begin, end);
        // 每日有效订单数
        List<Integer> effectiveCountList = new ArrayList<>();
        // 每日订单总数
        List<Integer> totalCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            Map map = new HashMap();
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.put("begin", beginTime);
            map.put("end", endTime);
            totalCountList.add(ordersMapper.selectCountByMap(map));// 查询每日订单数
            map.put("status", Orders.COMPLETED);
            effectiveCountList.add(ordersMapper.selectCountByMap(map));// 查询每日有效订单数
        }
        // 订单总数
        Integer totalOrderCount = effectiveCountList.stream().reduce(Integer::sum).get();
        // 有效订单数
        Integer validOrderCount = effectiveCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = 0.0;

        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        return OrderReportVO.builder()
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(totalCountList, ","))
                .validOrderCountList(StringUtils.join(effectiveCountList, ","))
                .build();
    }

    /**
     * 销量排名统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> goodsSalesDTOList = reportMapper.selectSalesTop10(beginTime, endTime);
        List<String> nameList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        String nameVOList = StringUtils.join(nameList, ",");
        String numberVOList = StringUtils.join(numberList, ",");

        return SalesTop10ReportVO
                .builder()
                .nameList(nameVOList)
                .numberList(numberVOList)
                .build();
    }

    /**
     * 获取日期集合
     *
     * @param begin
     * @param end
     * @return
     */
    private static List<LocalDate> getLocalDates(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }


}
