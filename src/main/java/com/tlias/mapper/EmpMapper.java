package com.tlias.mapper;

import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpExpr;
import com.tlias.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    void insertBatchExpr(@Param("empId") Integer empId, @Param("exprList") List<EmpExpr> exprList);
    
    /**
     * 根据ID删除员工
     * @param id 员工ID
     */
    void deleteById(Integer id);
    
    /**
     * 根据员工ID删除工作经历
     * @param empId 员工ID
     */
    void deleteExprByEmpId(Integer empId);

    /**
     * 根据ID查询员工
     * @param id 员工ID
     * @return 员工信息
     */
    // EmpMapper
    @Select("select * from emp where id = #{id}")
    Emp getById(Integer id);

    // EmpExprMapper
    @Select("select * from emp_expr where emp_id = #{empId}")
    List<EmpExpr> getExprByEmpId(Integer empId);


    /**
     * 更新员工基本信息
     */
    void updateById(Emp emp);


}
