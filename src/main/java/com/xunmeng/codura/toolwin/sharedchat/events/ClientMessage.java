package com.xunmeng.codura.toolwin.sharedchat.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ClientMessage<T> {
    @JsonProperty("conversationId")
    private String conversationId;
    @JsonProperty("data")
    private T data;
    @JsonProperty("type")
    private String type;
    @JsonProperty("key")
    private String key;
}
