package com.tlias.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * 员工工作经历DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpExprDTO {
    private Integer id;
    private String company;
    private String profession;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
}
