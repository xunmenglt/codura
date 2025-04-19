package com.xunmeng.codura.llm.openai.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Usage {
     @JsonProperty("prompt_tokens")
     private int prompt_tokens;
     @JsonProperty("completion_tokens")
     private int completion_tokens;
     @JsonProperty("total_tokens")
     private int total_tokens;
}
