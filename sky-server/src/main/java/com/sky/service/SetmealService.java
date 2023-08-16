package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/15 9:36
 */
public interface SetmealService {


    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 删除套餐
     *
     * @param ids
     */
    void removeBatch(List<Long> ids);

    /**
     * 套餐起售停售
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    SetmealVO list(Long id);

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void modifySetmeal(SetmealDTO setmealDTO);
}
