package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/11 9:07
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO pageQueryDTO) {
        PageResult pageResult = categoryService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result modifyCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.modifyCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 启动停用分类
     *
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启动停用分类")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        categoryService.modifyCategory(status, id);
        return Result.success();
    }

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return Result.success();
    }


    /**
     * 删除分类
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result remove(Long id) {
        categoryService.remove(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("分类查询")
    public Result<List<Category>> list(Integer type) {
        List<Category> categories = categoryService.list(type);
        return Result.success(categories);
    }


}
