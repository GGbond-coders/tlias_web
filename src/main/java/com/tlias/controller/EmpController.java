package com.tlias.controller;

import com.tlias.pojo.EmpQueryParam;
import com.tlias.pojo.PageBean;
import com.tlias.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
