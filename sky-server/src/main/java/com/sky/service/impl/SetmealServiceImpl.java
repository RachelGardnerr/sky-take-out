package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/15 9:37
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.getPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 新增套餐
        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes.size() > 0 && setmealDishes != null) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmeal.getId());
            });
        }
        //新增套餐与菜品关系数据
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 删除套餐
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeBatch(List<Long> ids) {

        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.selectById(id);
            if (StatusConstant.ENABLE == setmeal.getStatus()) {
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            setmealMapper.deleteById(setmealId);
            setmealDishMapper.deleteById(setmealId);
        });
    }

    /**
     * 套餐起售停售
     *
     * @param status
     */
    @Override
    public void startOrStop(Integer status, Long id) {

        // 判断是起售停售
        if (StatusConstant.ENABLE == status) {
            // 起售:判断套餐内是否有停售餐品
            List<Dish> dishes = setmealDishMapper.selectBySetmealId(id);
            if (dishes != null && dishes.size() != 0) {
                dishes.forEach(dish -> {
                    // 有停售菜品不能起售套餐
                    if (StatusConstant.DISABLE == dish.getStatus()) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal
                .builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.modify(setmeal);
    }
}
