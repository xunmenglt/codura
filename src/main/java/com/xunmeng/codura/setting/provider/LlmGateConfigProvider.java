package com.xunmeng.codura.setting.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LlmGateConfigProvider {

    @JsonProperty("server")
    private ServerConfig server;

    @JsonProperty("mianThreadNum")
    private int mainThreadNum;

    @JsonProperty("workThreadNum")
    private int workThreadNum;

    @JsonProperty("timeOut")
    private int timeOut;

    @JsonProperty("maxConnections")
    private int maxConnections;

    @Data
    public static class ServerConfig {
        @JsonProperty("port")
        private int port;

        @JsonProperty("prefix")
        private String prefix;
    }
}
