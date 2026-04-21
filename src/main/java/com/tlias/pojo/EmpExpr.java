package com.tlias.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * 员工工作经历实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpExpr {
    private Integer id;
    private Integer empId;
    private String company;
    private String profession;
    private LocalDate begin;
    private LocalDate end;
}
