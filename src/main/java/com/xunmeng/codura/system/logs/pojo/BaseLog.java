package com.xunmeng.codura.system.logs.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.system.logs.constans.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class BaseLog {
    @JsonProperty(value = "type")
    private LogType type;
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "timestamp")
    private long timestamp; // Time as a timestamp
    @JsonProperty(value = "userId")
    private String userId; // CODE_ALIEN_USER[MAC address]
    @JsonProperty(value = "ipAddress")
    private String ipAddress; // User's current IP address

    @JsonProperty(value = "ideVersion")
    private String ideVersion; // IDE version

    @JsonProperty(value = "value")
    private String value;
    
}
