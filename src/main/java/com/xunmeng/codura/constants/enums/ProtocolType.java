package com.xunmeng.codura.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProtocolType implements Type {
    HTTP("http"),
    HTTPS("https");
    
    private String type;
    ProtocolType(String type){
        this.type=type;
    }

    public Object V() {
        return type;
    }
    @JsonValue
    public String getType() {
        return type;
    }


    @JsonCreator
    public static ProtocolType fromString(String value) {
        for (ProtocolType item : ProtocolType.values()) {
            if (item.type.equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown ProtocolType: " + value);
    }


}
