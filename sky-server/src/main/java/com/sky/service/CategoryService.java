package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/11 9:16
 */
public interface CategoryService {


    /**
     * 分页查询
     * @param pageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO pageQueryDTO);

    /**
     * 修改分类
     * @param categoryDTO
     */
    void modifyCategory(CategoryDTO categoryDTO);

    /**
     * 启动停用分类
     * @param status
     */
    void modifyCategory(Integer status,Long id);

    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 根据id删除分类
     * @param id
     */
    void remove(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
