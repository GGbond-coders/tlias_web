package com.tlias.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tlias.mapper.EmpMapper;
import com.tlias.pojo.EmpQueryParam;
import com.tlias.pojo.PageBean;
import com.tlias.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 员工管理 Service 实现类
 */
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    
    @Autowired
    private EmpMapper empMapper;
    
    @Override
    public PageBean page(EmpQueryParam param) {
        // 1. 设置分页参数（PageHelper会自动拦截SQL并添加LIMIT）
        PageHelper.startPage(param.getPage(), param.getPageSize());
        
        // 2. 执行查询（PageHelper会自动在SQL后添加LIMIT子句）
        java.util.List<com.tlias.pojo.Emp> list = empMapper.list(param);
        
        // 3. 封装PageInfo对象（包含分页信息和数据列表）
        PageInfo<com.tlias.pojo.Emp> pageInfo = new PageInfo<>(list);
        
        log.info("分页查询结果 - 总记录数: {}, 当前页: {}, 每页条数: {}", 
                pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
        
        // 4. 返回封装好的分页数据
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }
}
