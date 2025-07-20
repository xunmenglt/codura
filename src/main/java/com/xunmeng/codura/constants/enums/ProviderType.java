package com.xunmeng.codura.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProviderType implements Type {
    CHAT(0),
    FIM(1);
    
    private Integer type;
    ProviderType(Integer type){
        this.type=type;
    }

    public Object V() {
        return type;
    }
    @JsonValue
    public Integer getType() {
            return type;
    }
    @JsonCreator
    public static ProviderType fromValue(Integer type) {
        for (ProviderType item : ProviderType.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
