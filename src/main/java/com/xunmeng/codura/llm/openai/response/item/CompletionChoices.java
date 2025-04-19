package com.xunmeng.codura.llm.openai.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompletionChoices {
    @JsonProperty("text")
    private String text;
    @JsonProperty("finish_reason")
    private String finish_reason;
    @JsonProperty("index")
    private Integer index;
}
