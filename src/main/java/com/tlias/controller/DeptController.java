package com.tlias.controller;

import com.tlias.pojo.Dept;
import com.tlias.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 部门管理 Controller
 */
@RestController
@RequestMapping("/depts")
public class DeptController {
    
    @Autowired
    private DeptService deptService;
    
    /**
     * 查询所有部门
     */
    @GetMapping
    public Result list() {
        List<Dept> depts = deptService.list();
        return Result.success(depts);
    }
    
    /**
     * 根据ID删除部门
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        deptService.deleteById(id);
        return Result.success();
    }
    
    /**
     * 新增部门
     */
    @PostMapping
    public Result add(@RequestBody Dept dept) {
        deptService.add(dept);
        return Result.success();
    }
    
    /**
     * 根据ID查询部门
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }
    
    /**
     * 修改部门
     */
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        deptService.update(dept);
        return Result.success();
    }
}
