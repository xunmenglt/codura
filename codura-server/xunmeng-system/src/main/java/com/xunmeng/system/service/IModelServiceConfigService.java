package com.xunmeng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmeng.system.pojo.ModelServiceConfig;

import java.util.List;

public interface IModelServiceConfigService extends IService<ModelServiceConfig> {
    List<ModelServiceConfig> getByUserName(String userName);
    boolean updateByUserName(ModelServiceConfig config);
}
