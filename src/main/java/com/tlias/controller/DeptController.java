package com.tlias.controller;

import com.tlias.pojo.Dept;
import com.tlias.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 部门管理 Controller
 */
@Slf4j
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
        log.info("查询所有部门");
        List<Dept> depts = deptService.list();
        log.info("查询到{}个部门", depts.size());
        return Result.success(depts);
    }
    
    /**
     * 根据ID删除部门
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除部门，ID: {}", id);
        deptService.deleteById(id);
        log.info("删除部门成功，ID: {}", id);
        return Result.success();
    }
    
    /**
     * 新增部门
     */
    @PostMapping
    public Result add(@RequestBody Dept dept) {
        log.info("新增部门: {}", dept);
        deptService.add(dept);
        log.info("新增部门成功");
        return Result.success();
    }
    
    /**
     * 根据ID查询部门
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询部门，ID: {}", id);
        Dept dept = deptService.getById(id);
        log.info("查询结果: {}", dept);
        return Result.success(dept);
    }
    
    /**
     * 修改部门
     */
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门: {}", dept);
        deptService.update(dept);
        log.info("修改部门成功");
        return Result.success();
    }
}
