package com.xunmeng.system.service;

import com.xunmeng.system.pojo.AiUseLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
public interface IAiUseLogService extends IService<AiUseLog> {

    boolean addAiUseLog(AiUseLog aiUseLog);
    
}
