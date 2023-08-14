package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/14 9:30
 */
public interface DishService {
    /**
     * 新增菜品和口味
     *
     * @param dto
     */
    void saveWithFlavor(DishDTO dto);


    /**
     * 菜品分页查询
     *
     * @param pageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO pageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void removeBatch(List<Long> ids);


}
