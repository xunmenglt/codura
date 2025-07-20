package com.xunmeng.codura.setting.provider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LlmGateLogProvider {
    private String logId;
    private String providerId;
    private String providerName;
    private String modelName;
    private String apiKey;
    private String userName;
    private Long inputTokens;
    private Long outputTokens;
    private Long inputLen;
    private Long outputLen;
    private Double quota;
    private Integer error;
    private String errorType;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime createTime;
}

