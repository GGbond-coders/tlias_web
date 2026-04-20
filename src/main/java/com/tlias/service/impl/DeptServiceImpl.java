package com.tlias.service.impl;

import com.tlias.mapper.DeptMapper;
import com.tlias.pojo.Dept;
import com.tlias.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门管理 Service 实现类
 */
@Service
public class DeptServiceImpl implements DeptService {
    
    @Autowired
    private DeptMapper deptMapper;
    
    @Override
    public List<Dept> list() {
        return deptMapper.list();
    }
    
    @Override
    public void deleteById(Integer id) {
        deptMapper.deleteById(id);
    }
    
    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }
    
    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }
    
    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }
}
