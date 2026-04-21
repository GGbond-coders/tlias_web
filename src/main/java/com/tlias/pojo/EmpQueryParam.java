package com.tlias.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工查询条件封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpQueryParam {
    private String name;
    private Integer gender;
    private Integer deptId;
    private Integer page;
    private Integer pageSize;
}
