package com.tlias.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog {
    private Integer id;
    private Integer operateUser;
    private LocalDateTime operateTime;
    private String className;
    private String methodName;
    private String methodParams;
    private String returnValue;
    private Long costTime;
    private Integer state;
}
