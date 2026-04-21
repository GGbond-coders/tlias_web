package com.tlias.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tlias.mapper.EmpMapper;
import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpExprDTO;
import com.tlias.pojo.EmpQueryParam;
import com.tlias.pojo.OperateLog;
import com.tlias.pojo.PageBean;
import com.tlias.service.EmpService;
import com.tlias.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void add(Emp emp, List<EmpExprDTO> exprList) {

        long startTime = System.currentTimeMillis();

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
     * JSON转换工具方法
     */
    private String toJson(Object... objs) {
        try {
            return objectMapper.writeValueAsString(objs);
        } catch (JsonProcessingException e) {
            return "JSON序列化失败";
        }
    }
}
