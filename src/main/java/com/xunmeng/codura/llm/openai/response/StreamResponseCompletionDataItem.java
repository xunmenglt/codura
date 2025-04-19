package com.xunmeng.codura.llm.openai.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.net.response.ResponseDataBaseItem;
import com.xunmeng.codura.llm.openai.response.item.CompletionChoices;
import com.xunmeng.codura.llm.openai.response.item.Usage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class StreamResponseCompletionDataItem extends ResponseDataBaseItem {
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
    private List<CompletionChoices> choices;
    
    public String contentValue(){
        String content=null;
        if (choices!=null && choices.size()>0){
            CompletionChoices c = choices.get(0);
            content=c.getText();
        }
        return content;
    }
    
}
