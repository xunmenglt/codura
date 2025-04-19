package com.xunmeng.system.service;

import com.xunmeng.system.pojo.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-08
 */
public interface ISysOperLogService extends IService<SysOperLog> {

    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    void clearOperLogs();
}
