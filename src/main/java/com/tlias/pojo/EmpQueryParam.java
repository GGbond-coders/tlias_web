package com.tlias.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
    private Integer page;
    private Integer pageSize;
}
