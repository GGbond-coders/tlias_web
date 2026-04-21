package com.tlias.service;

import com.tlias.pojo.EmpQueryParam;
import com.tlias.pojo.PageBean;

/**
 * 员工管理 Service 接口
 */
public interface EmpService {
    
    /**
     * 分页条件查询员工
     */
    PageBean page(EmpQueryParam param);
}
