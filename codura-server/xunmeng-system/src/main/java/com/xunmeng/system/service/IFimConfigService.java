package com.xunmeng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmeng.system.pojo.FimConfig;
import java.util.List;

public interface IFimConfigService extends IService<FimConfig> {
    FimConfig getByUserName(String userName);
    boolean updateByUserName(FimConfig config);
}