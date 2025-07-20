package com.xunmeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmeng.system.mapper.ChatConfigMapper;
import com.xunmeng.system.pojo.ChatConfig;
import com.xunmeng.system.service.IChatConfigService;
import org.springframework.stereotype.Service;

@Service
public class ChatConfigServiceImpl extends ServiceImpl<ChatConfigMapper, ChatConfig> implements IChatConfigService {
    @Override
    public ChatConfig getByUserName(String userName) {
        return this.getOne(new LambdaQueryWrapper<ChatConfig>()
                .eq(ChatConfig::getUserName, userName),false);
    }

    @Override
    public boolean updateByUserName(ChatConfig config) {
        return this.update(config, new LambdaQueryWrapper<ChatConfig>()
                .eq(ChatConfig::getUserName, config.getUserName()));
    }
}