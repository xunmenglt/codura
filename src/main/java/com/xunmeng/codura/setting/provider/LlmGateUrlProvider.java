package com.xunmeng.codura.setting.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LlmGateUrlProvider {
    @JsonProperty("host")
    private String host;
    @JsonProperty("port")
    private String port;
    @JsonProperty("prefix")
    private String prefix;
    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("redisPassword")
    private String redisPassword;

    @JsonProperty("redisPort")
    private int redisPort;
    public String getBaseUrl() {
        if (protocol != null && host != null && port != null && prefix != null
                && !protocol.isEmpty() && !host.isEmpty() && !port.isEmpty() && !prefix.isEmpty()) {
            // 注意 prefix 本身最好不要带开头的 /，这里加上
            String cleanPrefix = prefix.startsWith("/") ? prefix : "/" + prefix;
            return String.format("%s://%s:%s%s", protocol, host, port, cleanPrefix);
        }
        return null;
    }
}
