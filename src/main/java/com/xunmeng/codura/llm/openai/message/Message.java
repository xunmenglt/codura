package com.xunmeng.codura.llm.openai.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @JsonProperty(value = "role",required = true)
    private String role;
    @JsonProperty(value = "content",required = true)
    private String content;
}
