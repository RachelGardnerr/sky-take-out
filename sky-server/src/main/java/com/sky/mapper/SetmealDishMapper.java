package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);


    /**
     * 根据id批量查询
     *
     * @param ids
     * @return
     */
    List<SetmealDish> selectByIds(List<Long> ids);

    /**
     * 批量添加
     *
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
