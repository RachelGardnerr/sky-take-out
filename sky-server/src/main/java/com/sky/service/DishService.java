package com.sky.service;

import com.sky.dto.DishDTO;

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
}
