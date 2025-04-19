package com.xunmeng.codura.llm.openai.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StreamRequestBodyCompletionOpenAI extends RequestBodyBaseOpenAI{
    
    @JsonProperty(value = "prompt",required = true)
    private String prompt;
    
}
