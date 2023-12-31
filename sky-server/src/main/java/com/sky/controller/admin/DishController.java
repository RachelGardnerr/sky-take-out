package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/14 9:26
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增菜品和口味
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品和口味")
    public Result save(@RequestBody DishDTO dto) {
        log.info("新增菜品：{}", dto);
        dishService.saveWithFlavor(dto);
        String key = "dish_" + dto.getCategoryId();
        clearCache(key);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO pageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result removeBatch(@RequestParam List<Long> ids) {
        dishService.removeBatch(ids);
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * 菜品起售停售
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("菜品状态：{}", status + " 菜品分类id:{}", id);
        dishService.startOrStop(status, id);
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> queryDishWithFlavors(@PathVariable Long id) {
        DishVO dishVO = dishService.queryDishWithFlavorsById(id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param dishDTO
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(DishDTO dishDTO) {
        List<Dish> dishes = dishService.list(dishDTO);
        return Result.success(dishes);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result modify(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);
        dishService.modify(dishDTO);
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * 清理缓存数据
     *
     * @param patten
     */
    private void clearCache(String patten) {
        Set keys = redisTemplate.keys(patten);
        redisTemplate.delete(keys);
    }
}
