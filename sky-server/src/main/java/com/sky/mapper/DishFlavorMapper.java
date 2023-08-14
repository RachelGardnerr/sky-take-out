package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/14 9:56
 */
@Mapper
public interface DishFlavorMapper {


    /**
     * 新增口味
     *
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);
}
