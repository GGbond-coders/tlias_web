package com.tlias.service;

import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpExprDTO;
import com.tlias.pojo.EmpQueryParam;
import com.tlias.pojo.PageBean;

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
    void add(Emp emp, List<EmpExprDTO> exprList);
}
