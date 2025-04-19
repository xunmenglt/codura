package com.xunmeng.codura.llm.openai.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.net.response.ResponseDataBaseItem;
import com.xunmeng.codura.llm.openai.response.item.ChatChoices;
import com.xunmeng.codura.llm.openai.response.item.Usage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class StreamResponseChatDataItem extends ResponseDataBaseItem {
    @JsonProperty("id")
    private String id;
    @JsonProperty("model")
    private String model;
    @JsonProperty("system_fingerprint")
    private String system_fingerprint;
    @JsonProperty("created")
    private Integer created;
    @JsonProperty("usage")
    private Usage usage;
    @JsonProperty("object")
    private String object;
    @JsonProperty("choices")
    private List<ChatChoices> choices;
    
    public String contentValue(){
        String content=null;
        if (choices!=null && choices.size()>0){
            ChatChoices c = choices.get(0);
            if (c.getDelta()!=null){
                content = c.getDelta().getContent();
            }
        }
        return content;
    }
    
}
