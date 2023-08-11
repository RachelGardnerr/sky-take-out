package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @AUTHOR: djl
 * @DATE: 2023/8/11 9:18
 */
@Mapper
public interface CategoryMapper {

    /**
     * 分页查询
     *
     * @param pageQueryDTO
     * @return
     */
    Page<Category> getPage(CategoryPageQueryDTO pageQueryDTO);

    /**
     * 修改分类信息
     *
     * @param category
     */
    void update(Category category);

    /**
     * 新增分类
     *
     * @param category
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            "values " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Category category);

    /**
     * 删除分类
     *
     * @param id
     */
    @Delete("delete from category where id =#{id}")
    void deleteByID(Long id);

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @Select("select * from category where type = #{type}")
    List<Category> selectByType(Integer type);
}
