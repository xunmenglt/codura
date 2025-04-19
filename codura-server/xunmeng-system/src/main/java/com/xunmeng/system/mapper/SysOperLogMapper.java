package com.xunmeng.system.mapper;

import com.xunmeng.system.pojo.SysOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-08
 */
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    void cleanOperLog();
    
}
