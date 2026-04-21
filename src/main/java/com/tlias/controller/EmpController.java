package com.tlias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlias.pojo.*;
import com.tlias.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     *   "username": "zhangsan",
     *   "password": "123456",
     *   "name": "张三",
     *   "gender": 1,
     *   "phone": "13800138000",
     *   "deptId": 1,
     *   "entryDate": "2024-01-15",
     *   "exprList": [
     *     {
     *       "company": "某某公司",
     *       "profession": "Java开发",
     *       "begin": "2020-01-01",
     *       "end": "2023-12-31"
     *     }
     *   ]
     * }
     */
    @PostMapping
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

        empService.add(emp, dto.getExprList());

        log.info("新增员工成功");
        return Result.success();
    }
}
