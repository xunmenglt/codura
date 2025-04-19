package com.xunmeng.codura.setting.provider;

import com.xunmeng.codura.pojo.Conversation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConversationHistoryProvider {
    private List<Conversation> conversationList;
    private String currentConversationId;
    
    public ConversationHistoryProvider() {
        initConversationList();
    }


    public List<Conversation> getConversationList() {
        conversationList.sort((a,b)->Long.compare(b.getCreateTime(),a.getCreateTime()));
        return conversationList;
    }

    private void initConversationList() {
        this.conversationList = new ArrayList<>();
    }
    
    // 获取当前对话信息
    public Conversation getCurrentConversation() {
        return conversationList.stream().filter(conversation -> conversation.getId().equals(currentConversationId)).findFirst().orElse(null);
    }
    
    // 删除对话信息
    public void deleteConversation(String conversationId) {
        if (conversationId!=null){
            conversationList.removeIf(conversation -> conversation.getId().equals(conversationId));
        }
    }
    // 根据对话id清空对话信息
    public void clearConversation(String conversationId) {
        conversationList.stream().filter(conversation -> conversation.getId().equals(conversationId)).findFirst().ifPresent(conversation -> conversation.clear());
    }
    
    public void addConversation(Conversation conversation){
        conversationList.add(conversation);
    }
}
