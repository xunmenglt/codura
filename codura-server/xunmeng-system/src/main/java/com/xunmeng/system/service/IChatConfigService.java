package com.xunmeng.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmeng.system.pojo.ChatConfig;

public interface IChatConfigService extends IService<ChatConfig> {
    ChatConfig getByUserName(String userName);
    boolean updateByUserName(ChatConfig config);
}
