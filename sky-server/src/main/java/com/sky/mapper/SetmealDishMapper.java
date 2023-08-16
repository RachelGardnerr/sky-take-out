package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 删除套餐和菜品关系
     *
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteById(Long id);

    /**
     * 根据套餐id查询菜品
     *
     * @param id
     * @return
     */
    List<Dish> selectBySetmealId(Long id);
}
