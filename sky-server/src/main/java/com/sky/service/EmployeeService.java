package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDto
     */
    void save(EmployeeDTO employeeDto);

    /**
     * 分页查询
     * @param pageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO pageQueryDTO);

    /**
     * 启用停用员工账号
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    Employee queryEmployee(Long id);

    /**
     * 编辑用户信息
     * @param employee
     * @return
     */
    void modifyEmployee(EmployeeDTO employeeDTO);
}
