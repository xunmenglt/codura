package com.xunmeng.codura.setting.state;

import com.xunmeng.codura.setting.provider.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CodeState {
    private ChatModelProvider chatModelProvider;
    private FimModelProvider fimModelProvider;
    // 代码补全配置提供者
    private CompletionConfigProvider completionConfigProvider;
    
    private ChatConfigProvider chatConfigProvider;
    
    private ConversationHistoryProvider conversationHistoryProvider;
    
    
    public CodeState(){
        initModelProviders();
        initCompletionConfigProvider();
        initChatConfigProvider();
        initConversationHistoryProvider();
    }
    
    
    public void resetModerProviders(){
        chatModelProvider=ChatModelProvider.defaultProvider();
        fimModelProvider=FimModelProvider.defaultProvider();
    }
    
    public void resetCompletionProvider(){
        completionConfigProvider=CompletionConfigProvider.defaultProvider();
    }
    
    private void initModelProviders() {
        if (chatModelProvider==null){
            chatModelProvider=ChatModelProvider.defaultProvider();
        }
        if (fimModelProvider==null){
            fimModelProvider=FimModelProvider.defaultProvider();
        }

    }
    
    private void initCompletionConfigProvider(){
        if (completionConfigProvider==null){
            completionConfigProvider=CompletionConfigProvider.defaultProvider();
        }
    }
    private void initChatConfigProvider(){
        if (chatConfigProvider==null){
            chatConfigProvider=ChatConfigProvider.defaultProvider();
        }
    }

    private void initConversationHistoryProvider(){
        if (conversationHistoryProvider==null){
            conversationHistoryProvider=new ConversationHistoryProvider();
        }
    }
}
