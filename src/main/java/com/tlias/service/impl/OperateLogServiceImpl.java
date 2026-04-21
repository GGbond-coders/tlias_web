package com.tlias.service.impl;

import com.tlias.mapper.OperateLogMapper;
import com.tlias.pojo.OperateLog;
import com.tlias.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作日志 Service 实现类
 */
@Slf4j
@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    /**
     * 保存操作日志（使用独立事务，确保即使主事务回滚，日志也会被记录）
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOperateLog(OperateLog operateLog) {
        operateLogMapper.insert(operateLog);
        log.info("操作日志记录成功");
    }
}
