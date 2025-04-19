package com.xunmeng.codura.toolwin.sharedchat.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServerMessage<T> {
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Value value;

    public Value getValue() {
        if (value==null){
            value=new Value();
        }
        return value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public class Value{
        @JsonProperty("completion")
        
        private String completion;
        @JsonProperty("data")
        
        private T data;
        @JsonProperty("error")
        
        private boolean error;
        @JsonProperty("errorMessage")
        
        private String errorMessage;
        @JsonProperty("type")
        
        private String type;
    }
}
