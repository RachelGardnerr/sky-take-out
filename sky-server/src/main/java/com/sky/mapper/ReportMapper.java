package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {
    Double selectTurnover(Map map);

    /**
     * 查询销量排名前十的商品
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> selectSalesTop10(LocalDateTime beginTime, LocalDateTime endTime);
}
