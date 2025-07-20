package com.xunmeng.system.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "llmgate")
public class LlmGateConfig {
    private String host;
    private String port;
    private String prefix;
    private String protocol;

    private String redisPassword;

    private int redisPort;
}
