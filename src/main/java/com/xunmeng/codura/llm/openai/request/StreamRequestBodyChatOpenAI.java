package com.xunmeng.codura.llm.openai.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.llm.openai.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class StreamRequestBodyChatOpenAI extends RequestBodyBaseOpenAI {
    @JsonProperty(value = "messages",required = true)
    private List<Message> messages;
    
    public StreamRequestBodyChatOpenAI addMessage(Message message){
        if (messages==null){
            messages=new ArrayList<>();
        }
        messages.add(message);
        return this;
    }
}
