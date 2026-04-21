package com.tlias.mapper;

import com.tlias.pojo.Emp;
import com.tlias.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 员工管理 Mapper 接口
 */
@Mapper
public interface EmpMapper {
    
    /**
     * 查询员工列表（关联部门表）
     * PageHelper会自动拦截此SQL并添加LIMIT实现分页
     * 同时会自动执行COUNT查询获取总记录数
     */
    @Select("<script>" +
            "SELECT e.id, e.username, e.password, e.name, e.gender, e.phone, " +
            "e.job_id, e.dept_id, e.entry_date, e.create_time, e.update_time, " +
            "d.name AS deptName " +
            "FROM emp e LEFT JOIN dept d ON e.dept_id = d.id " +
            "<where>" +
            "  <if test='name != null and name != \"\"'>" +
            "    AND e.name LIKE CONCAT('%', #{name}, '%')" +
            "  </if>" +
            "  <if test='gender != null'>" +
            "    AND e.gender = #{gender}" +
            "  </if>" +
            "  <if test='deptId != null'>" +
            "    AND e.dept_id = #{deptId}" +
            "  </if>" +
            "</where>" +
            "ORDER BY e.update_time DESC" +
            "</script>")
    List<Emp> list(EmpQueryParam param);
}
