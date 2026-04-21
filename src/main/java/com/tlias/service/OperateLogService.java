package com.tlias.service;

import com.tlias.pojo.OperateLog;

/**
 * 操作日志 Service 接口
 */
public interface OperateLogService {
    
    /**
     * 保存操作日志
     * @param log 日志信息
     */
    void saveOperateLog(OperateLog log);
}
