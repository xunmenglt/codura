package com.xunmeng.codura.llm.vllm.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.net.resquest.RequestBodyBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
/**
 * 实现的请求体为粗略版
 */
public class StreamGenerateVllm extends RequestBodyBase {
    @JsonProperty(value = "model",required = true)
    private String prompt;
    @JsonProperty(value = "model",required = true)
    private String model;
    @JsonProperty(value = "max_tokens")
    private int max_tokens=256;
    @JsonProperty(value = "stream")
    private boolean stream=true;
    @JsonProperty(value = "temperature")
    private double temperature=0.2;
    @JsonProperty(value = "presence_penalty")
    private double presence_penalty=0.0;
    
    @JsonProperty(value = "repetition_penalty")
    private double repetition_penalty=1.0;

    @JsonProperty(value = "frequency_penalty")
    private double frequency_penalty=0.0;

    @JsonProperty(value = "top_k")
    private double top_k=0;

    @JsonProperty(value = "top_p")
    private double top_p=1.0;
}
