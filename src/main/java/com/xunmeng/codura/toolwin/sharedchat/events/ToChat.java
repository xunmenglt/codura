package com.xunmeng.codura.toolwin.sharedchat.events;

import com.xunmeng.codura.toolwin.sharedchat.events.eventnames.ToChatEvent;
import com.xunmeng.codura.toolwin.sharedchat.events.payload.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ToChat implements Serializable {
    private ToChatEvent type;
    private Payload payload;
    
    // 设置剪贴信息
    public static ToChat createSnippetToChat(Payload payload){
        return new ToChat(ToChatEvent.SET_SELECTED_SNIPPET,payload);
    }
}