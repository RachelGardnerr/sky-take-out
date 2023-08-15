package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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
     *
     * @param ids
     */
    void removeBatch(List<Long> ids);


    /**
     * 菜品起售停售
     *
     * @param status
     */
    void startOrStop(Integer status, Long id);


    /**
     * 根据id查询菜品和口味
     *
     * @param id
     * @return
     */
    DishVO queryDishWithFlavorsById(Long id);

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    void modify(DishDTO dishDTO);


    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

}
