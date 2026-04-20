package com.tlias.mapper;

import com.tlias.pojo.Dept;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 部门管理 Mapper 接口
 */
@Mapper
public interface DeptMapper {
    
    /**
     * 查询所有部门
     */
    @Select("SELECT * FROM dept")
    List<Dept> list();
    
    /**
     * 根据ID删除部门
     */
    @Delete("DELETE FROM dept WHERE id = #{id}")
    void deleteById(Integer id);
    
    /**
     * 根据ID查询部门
     */
    @Select("SELECT * FROM dept WHERE id = #{id}")
    Dept getById(Integer id);
    
    /**
     * 修改部门
     */
    @Update("UPDATE dept SET name = #{name}, update_time = #{updateTime} WHERE id = #{id}")
    void update(Dept dept);
    
    /**
     * 新增部门
     */
    @Insert("INSERT INTO dept(name, create_time, update_time) VALUES(#{name}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dept dept);
}
