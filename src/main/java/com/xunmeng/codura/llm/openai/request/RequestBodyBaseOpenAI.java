package com.xunmeng.codura.llm.openai.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.net.resquest.RequestBodyBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RequestBodyBaseOpenAI extends RequestBodyBase {
    @JsonProperty(value = "model",required = true)
    private String model;
    @JsonProperty(value = "max_tokens")
    private int max_tokens=2048;
    @JsonProperty(value = "stream")
    private boolean stream=true;
    @JsonProperty(value = "stop",required = false)
    private String stop;
    @JsonProperty(value = "temperature")
    private double temperature=0.2;
}
