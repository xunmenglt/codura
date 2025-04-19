package com.xunmeng.codura.toolwin.sharedchat;

import com.xunmeng.codura.pojo.ConversationMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Thread {
    private String id;
    private List<ConversationMessage> messages;
    private String title;
    private boolean attachFile;
}
