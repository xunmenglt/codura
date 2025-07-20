package com.xunmeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmeng.system.mapper.ModelServiceConfigMapper;
import com.xunmeng.system.pojo.ModelServiceConfig;
import com.xunmeng.system.service.IModelServiceConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelServiceConfigServiceImpl
        extends ServiceImpl<ModelServiceConfigMapper, ModelServiceConfig>
        implements IModelServiceConfigService {

    @Override
    public List<ModelServiceConfig> getByUserName(String userName) {
        return this.list(new LambdaQueryWrapper<ModelServiceConfig>()
                .eq(ModelServiceConfig::getUserName, userName));
    }

    @Override
    public boolean updateByUserName(ModelServiceConfig config) {
        return this.update(config, new LambdaQueryWrapper<ModelServiceConfig>()
                .eq(ModelServiceConfig::getUserName, config.getUserName())
                .eq(ModelServiceConfig::getBusinessType, config.getBusinessType()));
    }
}
