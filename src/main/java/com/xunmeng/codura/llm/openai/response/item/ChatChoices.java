package com.xunmeng.codura.llm.openai.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatChoices {
    @JsonProperty("delta")
    private Delta delta;
    @JsonProperty("finish_reason")
    private String finish_reason;
    @JsonProperty("index")
    private Integer index;
    
    @Data
    public class Delta{
        @JsonProperty("content")
        private String content;
        @JsonProperty("role")
        private String role;
    } 
}
