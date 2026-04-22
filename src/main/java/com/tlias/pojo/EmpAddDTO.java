package com.tlias.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmpAddDTO {

    private String username;
    private String password;
    private String name;
    private Integer gender;
    private String phone;
    private Integer deptId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;

    private List<EmpExpr> exprList;
}