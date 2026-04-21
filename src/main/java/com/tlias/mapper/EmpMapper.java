package com.tlias.mapper;

import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 员工管理 Mapper 接口
 * SQL语句配置在 resources/mapper/EmpMapper.xml 中
 */
@Mapper
public interface EmpMapper {
    
    /**
     * 查询员工列表（关联部门表）
     * PageHelper会自动拦截此SQL并添加LIMIT实现分页
     * 同时会自动执行COUNT查询获取总记录数
     * 
     * @param param 查询条件（姓名、性别、部门ID、入职日期范围）
     * @return 员工列表
     */
    List<Emp> list(EmpQueryParam param);
}
