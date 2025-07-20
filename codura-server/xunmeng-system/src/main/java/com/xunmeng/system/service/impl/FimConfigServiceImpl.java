package com.xunmeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmeng.system.mapper.FimConfigMapper;
import com.xunmeng.system.pojo.FimConfig;
import com.xunmeng.system.service.IFimConfigService;
import org.springframework.stereotype.Service;

@Service
public class FimConfigServiceImpl extends ServiceImpl<FimConfigMapper, FimConfig> implements IFimConfigService {
    @Override
    public FimConfig getByUserName(String userName) {
        return this.getOne(new LambdaQueryWrapper<FimConfig>()
                .eq(FimConfig::getUserName, userName),false);
    }

    @Override
    public boolean updateByUserName(FimConfig config) {
        return this.update(config, new LambdaQueryWrapper<FimConfig>()
                .eq(FimConfig::getUserName, config.getUserName()));
    }
}
