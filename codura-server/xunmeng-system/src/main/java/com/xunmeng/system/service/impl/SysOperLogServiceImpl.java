package com.xunmeng.system.service.impl;

import com.xunmeng.system.pojo.SysOperLog;
import com.xunmeng.system.mapper.SysOperLogMapper;
import com.xunmeng.system.service.ISysOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-08
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {

    @Autowired
    private SysOperLogMapper operLogMapper;
    
    
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        
        return  operLogMapper.selectOperLogList(operLog);
    }

    @Override
    public void clearOperLogs() {
        operLogMapper.cleanOperLog();
    }
}
