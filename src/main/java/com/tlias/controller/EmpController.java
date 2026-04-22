package com.tlias.controller;

import com.tlias.pojo.*;
import com.tlias.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理 Controller
 */
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 分页条件查询员工
     */
    @GetMapping
    public Result page(EmpQueryParam param) {
        log.info("分页条件查询员工，参数：{}", param);
        PageBean pageBean = empService.page(param);
        return Result.success(pageBean);
    }

    /**
     * 新增员工（包含工作经历）
     * 请求参数格式：
     * {
     * "username": "zhangsan",
     * "password": "123456",
     * "name": "张三",
     * "gender": 1,
     * "phone": "13800138000",
     * "deptId": 1,
     * "entryDate": "2024-01-15",
     * "exprList": [
     * {
     * "company": "某某公司",
     * "profession": "Java开发",
     * "begin": "2020-01-01",
     * "end": "2023-12-31"
     * }
     * ]
     * }
     */
    @PostMapping
    //@RequestBody修饰的形参将自动将JSON格式的请求参数转换为Java对象
    public Result add(@RequestBody EmpAddDTO dto) {
        log.info("新增员工，参数：{}", dto);

        // DTO → 实体
        Emp emp = new Emp();
        emp.setUsername(dto.getUsername());
        emp.setPassword(dto.getPassword());
        emp.setName(dto.getName());
        emp.setGender(dto.getGender());
        emp.setPhone(dto.getPhone());
        emp.setDeptId(dto.getDeptId());
        emp.setEntryDate(dto.getEntryDate());

        empService.add(emp, dto.getExprList());// 调用服务层方法，新增员工ExprList若没有则为null

        log.info("新增员工成功");
        return Result.success();
    }

    /**
     * 批量删除员工
     * 请求参数格式：[1, 2, 3] （员工ID数组）
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("批量删除员工，参数：{}", ids);
        empService.delete(ids);
        log.info("批量删除员工成功");
        return Result.success();
    }


    /**
     * 根据ID查询员工
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询员工，参数：{}", id);
        Emp emp = empService.getById(id);
        log.info("根据ID查询员工成功，结果：{}", emp);
        return Result.success(emp);
    }


    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工信息, {}", emp);
        empService.update(emp);
        return Result.success();
    }


}



