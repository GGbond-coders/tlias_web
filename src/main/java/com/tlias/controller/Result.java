package com.tlias.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回结果类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;
    
    /**
     * 成功返回
     */
    public static Result success() {
        return new Result(1, "success", null);
    }
    
    /**
     * 成功返回（带数据）
     */
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }
    
    /**
     * 失败返回
     */
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
