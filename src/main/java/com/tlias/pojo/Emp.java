package com.tlias.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Integer gender;
    private String phone;
    private Integer jobId;
    private Integer deptId;
    private LocalDate entryDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    /**
     * 部门名称（关联查询字段）
     */
    private String deptName;
}
