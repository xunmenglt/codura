package com.xunmeng.codura.setting.provider;

import com.xunmeng.codura.constants.Constants;
import com.xunmeng.codura.constants.enums.ModeType;
import com.xunmeng.codura.constants.enums.OpenApiType;
import com.xunmeng.codura.constants.enums.ProtocolType;
import com.xunmeng.codura.constants.enums.ProviderType;
import com.xunmeng.codura.utils.CodeBundle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ChatModelProvider extends ModelProvider{
    public static ChatModelProvider defaultProvider() {
        ChatModelProvider chatModelProvider = new ChatModelProvider();
        chatModelProvider.setLabel("聊天模型");
        chatModelProvider.setType(ProviderType.CHAT);
        chatModelProvider.setOpenApiType(OpenApiType.OPENAI);
        chatModelProvider.setModelName(ModeType.GPT_4_O_MINI);
        chatModelProvider.setHostName(Constants.HOST);
        chatModelProvider.setProtocol(ProtocolType.HTTP);
        chatModelProvider.setPort(Constants.PORT);
        chatModelProvider.setPath(Constants.CHAT_SERVER_PATH);
        return chatModelProvider;
    }

    @Override
    public String ID() {
        return CodeBundle.message("code.configurable.chat.provider.name");
    }
}
