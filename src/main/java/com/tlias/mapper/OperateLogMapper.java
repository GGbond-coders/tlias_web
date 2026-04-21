package com.tlias.mapper;

import com.tlias.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper 接口
 */
@Mapper
public interface OperateLogMapper {
    
    /**
     * 插入操作日志
     * @param log 日志信息
     */
    void insert(OperateLog log);
}
