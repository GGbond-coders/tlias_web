package com.tlias.mapper;

import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpExprDTO;
import com.tlias.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    
    /**
     * 新增员工
     * @param emp 员工信息
     */
    void insert(Emp emp);
    
    /**
     * 批量插入员工工作经历
     * @param empId 员工ID
     * @param exprList 工作经历列表
     */
    void insertBatchExpr(@Param("empId") Integer empId, @Param("exprList") List<EmpExprDTO> exprList);
}
