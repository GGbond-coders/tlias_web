package com.tlias.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tlias.mapper.EmpMapper;
import com.tlias.pojo.*;
import com.tlias.service.EmpService;
import com.tlias.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工管理 Service 实现类
 */
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PageBean page(EmpQueryParam param) {
        if (param.getPage() == null || param.getPage() < 1) {
            param.setPage(1);
        }
        if (param.getPageSize() == null || param.getPageSize() < 1) {
            param.setPageSize(10);
        }

        PageHelper.startPage(param.getPage(), param.getPageSize());

        List<Emp> list = empMapper.list(param);

        PageInfo<Emp> pageInfo = new PageInfo<>(list);

        log.info("分页查询结果 - 总记录数: {}, 当前页: {}, 每页条数: {}",
                pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());

        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增员工（事务控制）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Emp emp, List<EmpExpr> exprList) {

        long startTime = System.currentTimeMillis();
//        log.info("新增员工开始，参数: {}", emp);
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(this.getClass().getName());
        operateLog.setMethodName("add");

        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());

            empMapper.insert(emp);
            log.info("新增员工基本信息成功，员工ID: {}", emp.getId());

            if (exprList != null && !exprList.isEmpty()) {
                empMapper.insertBatchExpr(emp.getId(), exprList);
                log.info("新增员工工作经历成功，员工ID: {}, 经历数量: {}", emp.getId(), exprList.size());
            }

            // 设置操作日志信息
            operateLog.setMethodParams(toJson(emp, exprList));
            operateLog.setReturnValue("success");
            operateLog.setCostTime(System.currentTimeMillis() - startTime);
            operateLog.setState(1);

        } catch (Exception e) {

            log.error("新增员工失败", e);

            operateLog.setMethodParams(toJson(emp, exprList));
            operateLog.setReturnValue("error: " + e.getMessage());
            operateLog.setCostTime(System.currentTimeMillis() - startTime);
            operateLog.setState(0);

            throw new RuntimeException("新增员工失败", e);

        } finally {
            operateLogService.saveOperateLog(operateLog);
        }
    }

    /**
     * 批量删除员工（事务控制）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> ids) {
        long startTime = System.currentTimeMillis();
        log.info("批量删除员工开始，员工IDs: {}", ids);
        
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(this.getClass().getName());
        operateLog.setMethodName("delete");
        
        try {
            // 遍历删除每个员工及其工作经历
            for (Integer id : ids) {
                // 先删除工作经历
                empMapper.deleteExprByEmpId(id);
                log.info("删除员工工作经历成功，员工ID: {}", id);
                
                // 再删除员工基本信息
                empMapper.deleteById(id);
                log.info("删除员工基本信息成功，员工ID: {}", id);
            }
            
            // 设置操作日志信息
            operateLog.setMethodParams(toJson(ids));
            operateLog.setReturnValue("success");
            operateLog.setCostTime(System.currentTimeMillis() - startTime);
            operateLog.setState(1);
            
        } catch (Exception e) {
            log.error("批量删除员工失败", e);
            
            operateLog.setMethodParams(toJson(ids));
            operateLog.setReturnValue("error: " + e.getMessage());
            operateLog.setCostTime(System.currentTimeMillis() - startTime);
            operateLog.setState(0);
            
            throw new RuntimeException("批量删除员工失败", e);
            
        } finally {
            operateLogService.saveOperateLog(operateLog);
        }
    }

    /**
     * 根据ID获取员工信息
     */
    @Override
    public Emp getById(Integer id) {
        Emp emp = empMapper.getById(id);

        // 查询工作经历
        List<EmpExpr> exprList = empMapper.getExprByEmpId(id);
        emp.setExprList(exprList);

        return emp;
    }

    @Transactional
    @Override
    public void update(Emp emp) {
        //1. 根据ID更新员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        //2. 根据员工ID删除员工的工作经历信息 【删除老的】
        empMapper.deleteExprByEmpId(emp.getId());

        //3. 新增员工的工作经历数据 【新增新的】
        List<EmpExpr> exprList = emp.getExprList();
        if (exprList != null && !exprList.isEmpty()) {
            empMapper.insertBatchExpr(emp.getId(), exprList);
        }
    }










    /**
     * ----------------------------------------------------------------------------------------------------
     * JSON转换工具方法
     * objectMapper: Jackson的ObjectMapper对象，用于JSON转换
     */
    private String toJson(Object... objs) {
        try {
            return objectMapper.writeValueAsString(objs);
        } catch (JsonProcessingException e) {
            return "JSON序列化失败";
        }
    }


}
