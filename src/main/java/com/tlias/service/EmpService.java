package com.tlias.service;

import com.tlias.pojo.*;

import java.util.List;

/**
 * 员工管理 Service 接口
 */
public interface EmpService {
    
    /**
     * 分页条件查询员工
     */
    PageBean page(EmpQueryParam param);
    
    /**
     * 新增员工（包含工作经历）
     * @param emp 员工基本信息
     * @param exprList 工作经历列表
     */
    void add(Emp emp, List<EmpExpr> exprList);
    
    /**
     * 批量删除员工（包含工作经历）
     * @param ids 员工ID列表
     */
    void delete(List<Integer> ids);

    /**
     * 根据ID查询员工
     */
    Emp getById(Integer id);

    /**
     * 更新员工信息
     * @param emp
     */
    void update(Emp emp);
}
