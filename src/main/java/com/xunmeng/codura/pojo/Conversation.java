package com.xunmeng.codura.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class Conversation {
    private String id;
    private String title;
    private List<ConversationMessage> messages;
    private long createTime;
    private long updateTime;
    
    public Conversation(){
        this("");
    }
    
    
    public Conversation(String title) {
        createTime=new Date().getTime();
        id= UUID.randomUUID().toString();
        this.title = title;
        this.messages=new ArrayList<>();
    }
    
    public Conversation(String id, String title) {
        createTime=new Date().getTime();
        this.id = id;
        this.title = title;
        this.messages=new ArrayList<>();
    }
    public Conversation(String id, String title, List<ConversationMessage> messages) {
        createTime=new Date().getTime();
        this.id = id;
        this.title = title;
        this.messages=new ArrayList<>();
    }
    
    public void clear(){
        if (messages != null) {
            messages=new ArrayList<>();
        }
    }

    public void setMessages(List<ConversationMessage> messages) {
        this.messages = messages;
        this.updateTime=new Date().getTime();
    }
}
